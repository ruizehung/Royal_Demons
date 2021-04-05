package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.CoinComponent;
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.events.PickupEvent;

public class PlayerCoinCollisionHandler extends CollisionHandler {
    public PlayerCoinCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.COIN);
    }

    @Override
    public void onCollisionBegin(Entity player, Entity coin) {
        FXGL.getEventBus().fireEvent(new PickupEvent(PickupEvent.COIN, coin));
    }
}
