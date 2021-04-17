package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MainApp;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.CoinComponent;
import uwu.openjfx.components.PlayerComponent;

public class PlayerCoinCollisionHandler extends CollisionHandler {
    public PlayerCoinCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.COIN);
    }

    @Override
    public void onCollisionBegin(Entity player, Entity coin) {
        PlayerComponent playerComponent = player.getObject("CreatureComponent");
        CoinComponent coinComponent = coin.getComponent(CoinComponent.class);
        playerComponent.addGold(coinComponent.getValue());
        if (!MainApp.isIsTesting()) {
            int coins = FXGL.geti("coin") + coinComponent.getValue();
            FXGL.set("coin", coins);
        }
        coin.removeFromWorld();
    }
}
