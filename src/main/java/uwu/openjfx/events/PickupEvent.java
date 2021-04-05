package uwu.openjfx.events;

import com.almasb.fxgl.entity.Entity;
import javafx.event.Event;
import javafx.event.EventType;

public class PickupEvent extends Event {
    public static final EventType<PickupEvent> ANY
            = new EventType<>(Event.ANY, "PICKUP_EVENT");

    public static final EventType<PickupEvent> COIN
            = new EventType<>(ANY, "COIN_PICKUP");

    private Entity pickup;

    public PickupEvent(EventType<? extends Event> eventType, Entity pickup) {
        super(eventType);
        this.pickup = pickup;
    }

    public Entity getPickup() {
        return pickup;
    }
}
