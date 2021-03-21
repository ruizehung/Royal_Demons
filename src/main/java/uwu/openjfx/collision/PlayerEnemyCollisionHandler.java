package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.HealthComponent;

public class PlayerEnemyCollisionHandler extends CollisionHandler {

    public PlayerEnemyCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.ENEMY);
    }

    protected void onCollision(Entity player, Entity enemy) {
        HealthComponent healthComponent = player.getComponent(HealthComponent.class);
        if (!healthComponent.getIsInvulnerable()) {
            healthComponent.deductHealth();
        }
    }
}
