package uwu.openjfx.events;

import javafx.event.EventHandler;
import uwu.openjfx.behaviors.Interactable;

public class InteractEventHandler implements EventHandler<InteractEvent> {
    @Override
    public void handle(InteractEvent event) {
        Interactable interactable =  event.getEntity().getObject("Interactable");
        interactable.interact();
    }
}
