import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.collision.EnemyAttackPlayerCollisionHandler;
import uwu.openjfx.collision.PlayerSmashedGroundCollisionHandler;
import uwu.openjfx.components.PlayerComponent;

public class TestPlayerSmashedGroundCollisionHandler {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    //james 6
    @Test
    void testSmashedGroundDoesDamage() {
        int origHealth = 10;

        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(origHealth);
        player.addComponent(playerComponent);

        Entity smashedGround = new Entity();

        PlayerSmashedGroundCollisionHandler handler = new PlayerSmashedGroundCollisionHandler();
        handler.onCollisionBegin(smashedGround, player);

        assert playerComponent.getHealthPoints() < origHealth;
    }
}
