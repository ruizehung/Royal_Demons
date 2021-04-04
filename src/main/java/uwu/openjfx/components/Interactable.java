package uwu.openjfx.components;

public interface Interactable {
    void interact();

    boolean hasInteractedBefore();

    void setInteractedBefore(boolean interactedBefore);
}
