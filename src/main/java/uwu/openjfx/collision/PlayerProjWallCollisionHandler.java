package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;
import uwu.openjfx.MainApp;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.ExplosionAtDistComponent;
import uwu.openjfx.components.ProjectileAnimationComponent;

/*
    This class is responsible for when a projectile touches a wall.
 */
public class PlayerProjWallCollisionHandler extends CollisionHandler {

    public PlayerProjWallCollisionHandler() {
        super(RoyalType.PLAYERATTACK, RoyalType.WALL);
    }

    public void onCollisionBegin(Entity weapon, Entity wall) {
        if (weapon.hasComponent(ProjectileAnimationComponent.class)) {
            if (weapon.getComponent(ProjectileAnimationComponent.class).getIsArrow()) {
                weapon.getComponent(ProjectileComponent.class).pause();
                if (!weapon.hasComponent(ExplosionAtDistComponent.class)) {
                    if (!MainApp.isIsTesting()) {
                        FXGL.play("skills/arrow_dagger_stuck.wav");
                    }
                }
            }
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

    protected void onCollision(Entity weapon, Entity wall) {
        if (weapon != null) {
            FXGL.getGameTimer().runAtInterval(() -> {
                if (weapon.hasComponent(IrremovableComponent.class)) {
                    weapon.removeComponent(IrremovableComponent.class);
                }
                if (weapon.isActive()) {
                    weapon.removeFromWorld();
                }
            }, Duration.seconds(3));
        }
    }
}
