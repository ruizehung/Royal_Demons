import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.collision.ProjectileWallCollisionHandler;
import uwu.openjfx.components.ProjectileAnimationComponent;

public class TestProjectileWallCollisionHandler {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    //jason 1
    @Test
    void testArrowGetsStuckInAWall() {
        // Create new arrow
        // Add its animation component so that its detected by collision
        // Add its projectile component so that its able to be paused
        Entity arrow = new Entity();
        arrow.addComponent(new ProjectileAnimationComponent(
                "", 0, 0, 0, 0, true, false));
        arrow.addComponent(new ProjectileComponent(new Point2D(0, 0), 0));

        // Create a wall
        Entity wall = new Entity();
        ProjectileWallCollisionHandler handler = new ProjectileWallCollisionHandler();
        handler.onCollisionBegin(arrow, wall);
        assert (arrow.getComponent(ProjectileComponent.class).isPaused());
    }

    //jason 2
    @Test
    void testMagicSpellCollidesWithWall() {
        // Create a fireball
        // Add its animation component so that its detected by collision
        Entity fireball = new Entity();
        fireball.addComponent(new ProjectileAnimationComponent(
                "", 0, 0, 0, 0, false, true));

        // Create a wall
        Entity wall = new Entity();
        ProjectileWallCollisionHandler handler = new ProjectileWallCollisionHandler();
        handler.onCollisionBegin(fireball, wall);
        assert (!fireball.isActive());
    }
}
