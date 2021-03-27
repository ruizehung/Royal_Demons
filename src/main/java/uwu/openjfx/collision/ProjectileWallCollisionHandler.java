package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.ProjectileAnimationComponent;

/*
    This class is responsible for when a projectile touches a wall.
 */
public class ProjectileWallCollisionHandler extends CollisionHandler {

    public ProjectileWallCollisionHandler() {
        super(RoyalType.PLAYERATTACK, RoyalType.WALL);
    }

    public void onCollisionBegin(Entity weapon, Entity wall) {
        if (weapon.hasComponent(ProjectileAnimationComponent.class)) {
            ProjectileAnimationComponent projectileAnimationComponent =
                    weapon.getComponent(ProjectileAnimationComponent.class);
            if (projectileAnimationComponent.getIsArrow()) {
                weapon.getComponent(ProjectileComponent.class).pause();
            } else if (projectileAnimationComponent.getIsMagic()) {
                weapon.removeFromWorld();
            }
        }
    }

    protected void onCollision(Entity weapon, Entity wall) {
        if (weapon != null) {
            FXGL.getGameTimer().runAtInterval(() -> {
                weapon.removeFromWorld();
            }, Duration.seconds(5));
        }
    }
}
