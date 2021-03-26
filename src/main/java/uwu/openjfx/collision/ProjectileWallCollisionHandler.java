package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.ArrowComponent;
import uwu.openjfx.components.MagicComponent;

public class ProjectileWallCollisionHandler extends CollisionHandler {

    public ProjectileWallCollisionHandler() {
        super(RoyalType.PLAYERATTACK, RoyalType.WALL);
    }

    protected void onCollisionBegin(Entity weapon, Entity wall) {
        if (weapon.hasComponent(ArrowComponent.class)) {
            weapon.getComponent(ProjectileComponent.class).pause();
        } else if (weapon.hasComponent(MagicComponent.class)) {
            weapon.removeFromWorld();
        }
    }

    protected void onCollision(Entity weapon, Entity wall) {
        if (weapon.isActive()) {
            FXGL.getGameTimer().runAtInterval(() -> {
                weapon.removeFromWorld();
            }, Duration.seconds(5));
        }
    }
}
