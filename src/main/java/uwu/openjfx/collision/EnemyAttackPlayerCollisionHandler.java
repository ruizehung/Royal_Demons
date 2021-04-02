package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.HealthComponent;
import uwu.openjfx.components.PlayerComponent;

/*
    This class is responsible for the hitbox of the enemy's attack touching the player.
 */
public class EnemyAttackPlayerCollisionHandler extends CollisionHandler  {
    public EnemyAttackPlayerCollisionHandler() {
        super(RoyalType.ENEMYATTACK, RoyalType.PLAYER);
    }

    @Override
    public void onCollisionBegin(Entity enemy, Entity player) {
        if (!FXGL.getb("developerCheat")) {
            HealthComponent playerHealth = player.getComponent(PlayerComponent.class);
            if (!playerHealth.getIsInvulnerable()) {
                playerHealth.deductHealth();
            }
        }
    }
}
