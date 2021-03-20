package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.HealthComponent;

public class PlayerSkeletCollisionHandler extends CollisionHandler {

    public PlayerSkeletCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.ENEMY);
    }

    protected void onCollision(Entity player, Entity skelet) {
        HealthComponent healthComponent = player.getComponent(HealthComponent.class);
        if (!healthComponent.getIsInvulnerable()) {
            healthComponent.deductHealth();
        } else {
            System.out.println("Player is currently invulnerable. " +
                    "Player is currently at " + healthComponent.getHealthPoints() + " hearts");
        }
    }
}
