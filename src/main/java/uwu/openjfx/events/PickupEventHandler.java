package uwu.openjfx.events;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.event.EventHandler;
import uwu.openjfx.components.CoinComponent;
import uwu.openjfx.components.PlayerComponent;

public class PickupEventHandler implements EventHandler<PickupEvent> {
    @Override
    public void handle(PickupEvent event) {
        Entity item = event.getPickup();
        PlayerComponent player = ((Entity) FXGL.geto("player"))
                .getComponent(PlayerComponent.class);

        if (event.getEventType().getName().equals("COIN_PICKUP")) {
            CoinComponent coinComponent = item.getComponent(CoinComponent.class);
            player.addGold(coinComponent.getValue());
        }

        item.removeFromWorld();
    }
}
