package uwu.openjfx.events;

import com.almasb.fxgl.entity.Entity;
import javafx.event.Event;
import javafx.event.EventType;

public class InteractEvent extends Event {

    public static final EventType<InteractEvent> ANY
            = new EventType<>(Event.ANY, "Interact_Event");
    public static final EventType<InteractEvent> NPC
            = new EventType<>(ANY, "NPC");

    private Entity entity;

    public InteractEvent(EventType<? extends Event> eventType, Entity entity) {
        super(eventType);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
