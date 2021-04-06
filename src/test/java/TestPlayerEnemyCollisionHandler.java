import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.collision.PlayerEnemyCollisionHandler;
import uwu.openjfx.components.EnemyComponent;
import uwu.openjfx.components.PlayerComponent;

public class TestPlayerEnemyCollisionHandler {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    //james 1
    @Test
    void testEnemyRunsIntoPlayer() {
        int origHealth = 10;

        PlayerComponent.setPlayerWeapon("Sword");
        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        player.addComponent(playerComponent);

        Entity monster = new Entity();
        monster.addComponent(new EnemyComponent(origHealth, "", 10, 20));

        PlayerEnemyCollisionHandler handler = new PlayerEnemyCollisionHandler();
        handler.onCollision(player, monster);

        assert (playerComponent.getHealthPoints() < origHealth);
    }
}
