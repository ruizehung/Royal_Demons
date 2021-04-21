package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.ExplosionAtDistComponent;
import uwu.openjfx.components.ProjectileAnimationComponent;

/*
    This class is responsible for when a projectile touches a wall.
 */
public class EnemyProjWallCollisionHandler extends CollisionHandler {

    public EnemyProjWallCollisionHandler() {
        super(RoyalType.ENEMYATTACK, RoyalType.WALL);
    }

    public void onCollisionBegin(Entity weapon, Entity wall) {
        if (weapon.hasComponent(ProjectileAnimationComponent.class)) {
            if (weapon.getComponent(ProjectileAnimationComponent.class).getIsMagic()
                || weapon.hasComponent(ExplosionAtDistComponent.class)) {
                if (weapon.hasComponent(ExplosionAtDistComponent.class)
                    && weapon.getComponent(ExplosionAtDistComponent.class).getExplodeColl()) {
                    weapon.getComponent(ExplosionAtDistComponent.class).explode();
                }
                weapon.removeFromWorld();
            }
        }
    }
}
