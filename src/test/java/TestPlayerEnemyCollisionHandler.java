import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.collision.PlayerEnemyCollisionHandler;
import uwu.openjfx.components.EnemyComponent;
import uwu.openjfx.components.HealthComponent;
import uwu.openjfx.components.PlayerComponent;

public class TestPlayerEnemyCollisionHandler {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    //james 1
    @Test
    void testEnemyRunsIntoPlayer() {
        PlayerComponent.setPlayerWeapon("Sword");
        Entity player = new Entity();
        HealthComponent myHealth = new PlayerComponent(10);
        player.addComponent(myHealth);

        Entity monster = new Entity();
        monster.addComponent(new EnemyComponent(1, "", 10, 20));

        PlayerEnemyCollisionHandler handler = new PlayerEnemyCollisionHandler();
        handler.onCollision(player, monster);

        int origHealth = 10;
        assert (myHealth.getHealthPoints() < origHealth);
    }
}
