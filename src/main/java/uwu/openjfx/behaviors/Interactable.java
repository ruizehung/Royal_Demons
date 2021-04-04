package uwu.openjfx.behaviors;

public interface Interactable {
    void interact();

    boolean hasInteractedBefore();

    void setInteractedBefore(boolean interactedBefore);
}
