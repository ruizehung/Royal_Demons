package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.behaviors.HasLife;
import uwu.openjfx.components.PlayerComponent;

/*
    This class is responsible for the hitbox of the enemy's attack touching the player.
 */
public class EnemyAttackPlayerCollisionHandler extends CollisionHandler  {
    public EnemyAttackPlayerCollisionHandler() {
        super(RoyalType.ENEMYATTACK, RoyalType.PLAYER);
    }

    @Override
    public void onCollisionBegin(Entity enemyWeapon, Entity player) {
        HasLife playerComponent = player.getComponent(PlayerComponent.class);
        if (enemyWeapon != null && !playerComponent.isInvulnerable()) {
            enemyWeapon.removeFromWorld();
        }
        if (!playerComponent.isInvulnerable()) {
            playerComponent.deductHealth(1, 1, 0, 1, 0);
        }
    }
}
