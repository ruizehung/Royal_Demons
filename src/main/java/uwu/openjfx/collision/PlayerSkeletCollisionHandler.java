package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.HealthComponent;

public class PlayerSkeletCollisionHandler extends CollisionHandler {

    private LocalTimer invulnerableTimer;

    public PlayerSkeletCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity skelet) {
        HealthComponent healthComponent = player.getComponent(HealthComponent.class);
        healthComponent.deductHealth();
        System.out.println(healthComponent.getHealthPoints());
        invulnerableTimer = FXGL.newLocalTimer();
        invulnerableTimer.capture();
    }

    protected void onCollision(Entity player, Entity skelet) {
        if (invulnerableTimer.elapsed(Duration.seconds(1))) {
            HealthComponent healthComponent = player.getComponent(HealthComponent.class);
            healthComponent.deductHealth();
            System.out.println(healthComponent.getHealthPoints());
            invulnerableTimer.capture();
        }
        Room curRoom = FXGL.geto("curRoom");
    }
}
