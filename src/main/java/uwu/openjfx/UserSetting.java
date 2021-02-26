package uwu.openjfx;

public class UserSetting {
    private static String difficulty = "Easy";

    public static void reset() {
        difficulty = "Easy";
    }

    public static String getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(String difficulty) {
        UserSetting.difficulty = difficulty;
    }
}
