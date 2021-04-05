package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.CoinComponent;
import uwu.openjfx.components.PlayerComponent;

public class PlayerCoinCollisionHandler extends CollisionHandler {
    public PlayerCoinCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.COIN);
    }

    @Override
    public void onCollisionBegin(Entity player, Entity coin) {
        PlayerComponent playerComponent = FXGL.geto("playerComponent");
        CoinComponent coinComponent = coin.getComponent(CoinComponent.class);
        playerComponent.addGold(coinComponent.getValue());
        coin.removeFromWorld();
    }
}
