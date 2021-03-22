package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.HealthComponent;
import uwu.openjfx.components.PlayerComponent;

public class EnemyAttackPlayerCollisionHandler extends CollisionHandler  {
    public EnemyAttackPlayerCollisionHandler() {
        super(RoyalType.ENEMYATTACK, RoyalType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity enemy, Entity player) {
        HealthComponent playerHealth = player.getComponent(PlayerComponent.class);
        if (!playerHealth.getIsInvulnerable()) {
            playerHealth.deductHealth();
        }
    }
}
