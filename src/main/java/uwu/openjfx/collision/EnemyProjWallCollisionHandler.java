package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.ExplosionAtDistComponent;
import uwu.openjfx.components.ProjectileAnimationComponent;
import uwu.openjfx.components.RicochetComponent;

/*
    This class is responsible for when a projectile touches a wall.
 */
public class EnemyProjWallCollisionHandler extends CollisionHandler {

    public EnemyProjWallCollisionHandler() {
        super(RoyalType.ENEMYATTACK, RoyalType.WALL);
    }

    public void onCollisionBegin(Entity weapon, Entity wall) {
        if (weapon.hasComponent(ProjectileAnimationComponent.class)
            && !weapon.hasComponent(RicochetComponent.class)) {
            if (weapon.getComponent(ProjectileAnimationComponent.class).getIsMagic()
                || weapon.hasComponent(ExplosionAtDistComponent.class)) {
                if (weapon.hasComponent(ExplosionAtDistComponent.class)
                    && weapon.getComponent(ExplosionAtDistComponent.class).getExplodeColl()) {
                    weapon.getComponent(ExplosionAtDistComponent.class).explode();
                }
                weapon.removeFromWorld();
            }
        } else if (weapon.hasComponent(RicochetComponent.class)) {
            System.out.println("hello");
            weapon.getComponent(RicochetComponent.class).incrementWallHit();
            ProjectileComponent pc = weapon.getComponent(ProjectileComponent.class);
            pc.setDirection(new Point2D(
                -pc.getDirection().getX() + .4 * Math.signum(pc.getDirection().getX()),
                -pc.getDirection().getY() + .4 * Math.signum(pc.getDirection().getY())));
        }
    }
}
