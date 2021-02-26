# coding=utf-8
"""
Checkstyle script to automatically run Checkstyle on every java source file within
the current working directory (using class-specific options)
"""

__author__ = "CS 2340 TAs"
__version__ = "1.0"

# pylint: disable=wrong-import-position
# Python version check
import sys
if sys.version_info[0] < 3:
    print(
        """This script requires Python 3 to run:
https://www.python.org/downloads/
""")
    sys.exit(-1)

import os
import ssl
import argparse
import urllib.request
import re
import datetime
import platform
import warnings
import functools
import traceback
import subprocess
from subprocess import PIPE
from shutil import which

# Make requests with SSL verification disabled - misconfiguration of Python3 on OSX causes crash
# https://stackoverflow.com/questions/52805115/certificate-verify-failed-unable-to-get-local-issuer-certificate
# pylint: disable=protected-access
ssl._create_default_https_context = ssl._create_unverified_context

DESCRIPTION = "Checkstyle script to run checkstyle on every .java file in the CWD"
JAVA_EXTENSION = ".java"
BASE_PROCESS = ["java", "-jar"]
CHECKSTYLE_XML_NAME = "cs2340_checks.xml"
CHECKSTYLE_XML_URL = "https://raw.githubusercontent.com/Georgia-Tech-CS2340/cs2340-codestyle/master/cs2340_checks.xml" # pylint: disable=line-too-long
CHECKSTYLE_JAR_NAME = "checkstyle-8.24-all.jar"
CHECKSTYLE_JAR_URL = "https://github.com/checkstyle/checkstyle/releases/download/checkstyle-8.24/checkstyle-8.24-all.jar" # pylint: disable=line-too-long
CHECKSTYLE_JAR_GITIGNORE = "checkstyle-*.jar"
CHECKSTYLE_JAR_PATTERN = r"checkstyle-.*\.jar"
SENTINEL = object()

# Scoring
MULTILINE_COMMENT_REGEX = r"\/\*([\S\s]+?)\*\/"
SINGLELINE_COMMENT_REGEX = r"\/{2,}.+"
STATEMENT_REGEX = r"[^;]+?;"
AFTER_BRACE_REGEX = r"{\s*;"
EMPTY_STATEMENT_REGEX = r"^\s*;"
CHECKSTYLE_OUTPUT_REGEX = r"^\[[A-Z]+\] (.+?):\d+(?::\d+?)?:"
ERROR_TEXT_REGEX = r"CheckstyleException: Exception was thrown while processing .+\.java"
MAX_SCORE=10
SCORE_FORMAT = """Your code has been rated at {:.2f}/{} [raw score: {:.2f}/{}]"""

# Gitignore analysis
GIT_REPO_COMMAND = ["git", "rev-parse", "--show-toplevel"]
GIT_REPO_FAILED_SUBSTRING = "fatal: not a git repository"
GITIGNORE = ".gitignore"
IGNORED_FILE_COMMAND = ["git", "status", "--ignored", "--short"]
IGNORED_FILE_REGEX = "^!! (.+)$"

# Patterns
NO_JAVA_TEXT = """
Java is required to run Checkstyle. Make sure it is installed
and that it exists on your PATH. More instructions are available here:

https://www.java.com/en/download/help/path.xml"""
ADDED_GITIGNORE_TEXT = """
> Note: The checkstyle jar has been downloaded and a gitignore has automatically been created
        for you at the project root. This file should be checked into version control.
""".lstrip()
MODIFIED_GITIGNORE_TEXT = """
> Note: The checkstyle jar has been downloaded and automatically added to your gitignore.
        This change should be checked into version control.
""".lstrip()


def crash_reporter(func=None, fallback=SENTINEL):
    """
    Prints system/error information in the case of an unexpected crash during
    the execution of func
    """

    def _decorate(function):
        # closes over func/fallback
        @functools.wraps(function)
        def crash_handler(*args, **kwargs):
            try:
                return function(*args, **kwargs)
            # pylint: disable=broad-except
            except Exception:
                # Disable platform.dist() deprecation warning
                warnings.filterwarnings("ignore", category=DeprecationWarning)

                print(" ┌─────────────────────────────────────────────────────────────────────┐")
                print(" │ An unexpected error has occurred in the checkstyle script           │")
                print(" │ Please make a private post on Piazza with the following information │")
                print(" └─────────────────────────────────────────────────────────────────────┘")
                print()
                print("Error during {}".format(function.__name__))
                print(traceback.format_exc())
                print("Time: {}".format(str(datetime.datetime.now())))
                print("Script: run_checkstyle.py")
                print("Python version: {} => {}".format(sys.version, sys.version_info))
                print("Platform: {}".format(sys.platform))
                print("Architecture: {}".format(platform.architecture()))
                try:
                    # pylint: disable=deprecated-method
                    print("Distribution: {}".format(platform.dist()))
                except AttributeError:
                    # no-op
                    pass

                print("Processor: {}".format(platform.processor()))
                print("System: {}".format(platform.system()))
                print("uname: {}".format(platform.uname()))
                print("CPU Count: {}".format(os.cpu_count()))
                print()
                print("════════════════════════════════════════════════════════════════════════")

                if fallback is SENTINEL:
                    # Unrecoverable error
                    sys.exit(-1)

                return fallback
        return crash_handler

    if func:
        return _decorate(func)

    return _decorate


@crash_reporter
def main(root=None, verbose=False):
    """
    Runs the main checkstyle script and parses/redirects output
    """

    # Verify java is installed
    if which("java") is None:
        print(NO_JAVA_TEXT)
        return

    # Assemble dependencies
    print()
    xml_path, _ = find_or_download(CHECKSTYLE_XML_NAME, CHECKSTYLE_XML_URL)
    jar_path, jar_downloaded = find_or_download(CHECKSTYLE_JAR_NAME, CHECKSTYLE_JAR_URL)
    if jar_downloaded:
        result, mode = add_to_gitignore(jar_path)
        if result:
            print(ADDED_GITIGNORE_TEXT if mode == "add" else MODIFIED_GITIGNORE_TEXT)

    path = os.path.abspath(root) if root is not None else os.getcwd()
    files = find_files(path, JAVA_EXTENSION)

    verbose_tip = " (run with -v to view files)" if not verbose else ""
    print("Running Checkstyle on {} files{}:".format(len(files), verbose_tip))
    # Print each file in verbose mode
    if verbose:
        for file in files:
            print(" - {}".format(file))

    output = run_checkstyle(files, jar_path=jar_path, xml_path=xml_path)
    print()
    print(output)
    # Print score
    score = 0 if re.search(ERROR_TEXT_REGEX, output) else assemble_score(files, output)
    score_output = SCORE_FORMAT.format(max(score, 0), MAX_SCORE, score, MAX_SCORE)
    print(len(score_output) * "-")
    print(score_output)
    print()

    # Exit with a proper exit code (epsilon equality)
    if score >= (MAX_SCORE - 0.0000001):
        sys.exit(0)
    else:
        sys.exit(1)


@crash_reporter(fallback=(False, ""))
def add_to_gitignore(filename):
    """
    Adds the given file to the gitignore if applicable/necessary
    """

    if which("git") is None:
        return False, ""

    repo_relative_folder = os.path.dirname(filename)
    result = subprocess.run(GIT_REPO_COMMAND, stdout=PIPE, stderr=PIPE,
                            cwd=repo_relative_folder, check=False)
    repo_root = result.stdout.decode(sys.stdout.encoding).strip()
    error = result.stderr.decode(sys.stderr.encoding).strip()

    if GIT_REPO_FAILED_SUBSTRING in repo_root or error:
        return False, ""

    if not os.path.exists(repo_root):
        return False, ""

    # Clean path
    repo_root = os.path.normpath(repo_root)

    # Find .gitignore file if it exists by walking down the tree
    gitignore_path = find_gitignore(repo_root, repo_relative_folder)

    # Do nothing
    if is_ignored(CHECKSTYLE_JAR_PATTERN, cwd=repo_relative_folder):
        return False, ""

    if gitignore_path is None:
        # Create gitignore file at project root
        with open(os.path.join(repo_root, GITIGNORE), "w") as gitignore:
            gitignore.write(CHECKSTYLE_JAR_GITIGNORE + "\n")
        return True, "add"

    # Append to existing
    with open(gitignore_path, "a") as gitignore:
        gitignore.write("\n" + CHECKSTYLE_JAR_GITIGNORE + "\n")
    return True, "modify"


@crash_reporter(fallback=None)
def find_gitignore(repo_root, top_folder):
    """
    Finds the first .gitignore file starting at the top_folder and walking up
    the tree until the repo_root is found, or None if not found
    """

    repo_root = os.path.normpath(repo_root)
    current_path = os.path.normpath(top_folder)
    while len(current_path) >= len(repo_root):
        gitignore_path = os.path.join(current_path, GITIGNORE)
        if os.path.exists(gitignore_path):
            return gitignore_path

        current_path = os.path.dirname(current_path)

    return None


@crash_reporter(fallback=False)
def is_ignored(regex, cwd=None):
    """
    Determines whether the given file is ignored in its containing git repository
    """

    result = subprocess.run(IGNORED_FILE_COMMAND, stdout=PIPE, stderr=PIPE, cwd=cwd, check=False)
    output = result.stdout.decode(sys.stdout.encoding)
    output += result.stderr.decode(sys.stderr.encoding)
    for line in output.splitlines():
        match = re.search(IGNORED_FILE_REGEX, line)
        if match:
            line_filename = match.group(1)
            if re.search(regex, line_filename):
                return True

    return False


@crash_reporter
def find_or_download(filename, url):
    """
    Finds a file with the given filename in the same directory as the current
    script or downloads it from the given url if it doesn't exist
    """

    current_dir = os.path.dirname(os.path.realpath(__file__))
    target_path = os.path.join(current_dir, filename)
    exists = os.path.exists(target_path)

    if exists:
        return target_path, False

    # Download file
    print("Downloading {}".format(filename))
    urllib.request.urlretrieve(url, target_path, report_hook)
    print()
    return target_path, True


@crash_reporter(fallback=10.0)
def assemble_score(files, checkstyle_output):
    """
    Calculates code "score" using class-specific formula
    """

    errors = count_errors(checkstyle_output)
    statements = 0
    for filename in files:
        statements += count_statements(filename)

    if statements == 0:
        return 10.0

    return 10.0 - ((float(5 * errors) / statements) * 10)


@crash_reporter(fallback=0)
def count_errors(checkstyle_output):
    """
    Counts total checkstyle errors given the checkstyle stdout
    """

    count = 0
    for line in checkstyle_output.splitlines():
        if line.startswith("["):
            if re.match(CHECKSTYLE_OUTPUT_REGEX, line):
                count += 1

    return count


@crash_reporter(fallback=0)
def count_statements(filename):
    """
    Counts the number of Java statements included in a file
    each semicolon counts as a semicolon as long as it isn't preceded by only
    whitespace or brackets before the previous semicolon
    """

    if not os.path.exists(filename):
        return 0

    count = 0
    with open(filename, "r+") as java_file:
        contents = remove_comments(java_file.read())
        for match in re.finditer(STATEMENT_REGEX, contents):
            statement_candidate = match.group()
            if not is_invalid_statement(statement_candidate):
                count += 1

    return count


def remove_comments(java_source):
    """
    First removes multiline comments using regex before removing single line
    comments. Might not catch all due to regex-based parsing
    """

    without_multiline = re.sub(MULTILINE_COMMENT_REGEX, "", java_source)
    return re.sub(SINGLELINE_COMMENT_REGEX, "", without_multiline)


def is_invalid_statement(statement):
    """
    Filters out whether the statement candidate is valid or not. Rejects "statements"
    such as: "{ ;" and " ;"
    """

    return (re.search(AFTER_BRACE_REGEX, statement)
            or re.search(EMPTY_STATEMENT_REGEX, statement))

def format_size(size):
    """
    Formats a filesize to use Kb
    """

    kb_int = int(size / 1024)
    return "{:d} KB".format(kb_int)


def report_hook(block_num, block_size, total_size):
    """
    Report hook callback for urllib.request.urlretrieve
    Sourced from:
    https://stackoverflow.com/questions/13881092/download-progressbar-for-python-3
    """

    read_progress = block_num * block_size
    if total_size > 0:
        percent = min(read_progress * 1e2 / total_size, 1e2)
        clamped_progress = min(total_size, read_progress)
        fraction = "{:s} / {:s}".format(format_size(clamped_progress), format_size(total_size))
        progress_string = "\r{:5.1f}% {:s}".format(percent, fraction)
        sys.stdout.write(progress_string)
        if read_progress >= total_size:
            sys.stdout.write("\n")
    else:
        sys.stdout.write("read {:d}\n".format(read_progress))


@crash_reporter
def run_checkstyle(files, jar_path=None, xml_path=None):
    """
    Runs checkstyle on every file specified using the class-specific
    arguments as well as any additional ones specified

    Parameters:
    files (array(string)): filepaths to java source files

    Named:
    jar_path (string): The filepath to the checkstyle jar
    xml_path (string): The filepath to the checkstyle config XML file

    Returns:
    the stdout output from checkstyle
    """

    if not files or jar_path is None or xml_path is None:
        return ""

    args = BASE_PROCESS + [jar_path, "-c", xml_path] + files
    result = subprocess.run(args, stdout=PIPE, stderr=PIPE, check=False)
    return result.stdout.decode(sys.stdout.encoding) + result.stderr.decode(sys.stderr.encoding)


@crash_reporter
def find_files(path, extension):
    """
    Gets a list of every file in the given path that has the given file extension
    """

    file_list = []
    for root, _, files in os.walk(path):
        for file in files:
            if file.endswith(extension):
                file_list.append(os.path.join(root, file))

    return file_list


def bootstrap():
    """
    Runs CLI parsing/execution
    """

    # Argument definitions
    parser = argparse.ArgumentParser(description=DESCRIPTION)
    parser.add_argument("--root", "-r", metavar="path",
                        help="the path to run checkstyle over (defaults to current directory)")
    parser.add_argument("--verbose", "-v", action="store_true",
                        help="whether to display additional output")

    # Parse arguments
    parsed_args = parser.parse_args()
    main(root=parsed_args.root, verbose=parsed_args.verbose)


# Run script
if __name__ == "__main__":
    bootstrap()
