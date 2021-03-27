import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.collision.EnemyAttackPlayerCollisionHandler;
import uwu.openjfx.components.HealthComponent;
import uwu.openjfx.components.PlayerComponent;

public class TestEnemyAttackPlayerCollisionHandler {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    //alice 2
    @Test
    void testPlayerGetsHurt() {
        PlayerComponent.setPlayerWeapon("Sword");
        Entity player = new Entity();
        HealthComponent myHealth = new PlayerComponent(10);
        player.addComponent(myHealth);

        Entity monster = new Entity();

        EnemyAttackPlayerCollisionHandler handler = new EnemyAttackPlayerCollisionHandler();
        handler.onCollisionBegin(monster, player);

        int origHealth = 10;
        assert (myHealth.getHealthPoints() < origHealth);
    }
}
