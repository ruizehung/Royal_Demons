import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.collision.EnemyAttackPlayerCollisionHandler;
import uwu.openjfx.components.PlayerComponent;

public class TestEnemyAttackPlayerCollisionHandler {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    //alice 2
    @Test
    void testPlayerGetsHurt() {
        int origHealth = 10;

        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(origHealth);
        player.addComponent(playerComponent);

        Entity monster = new Entity();

        EnemyAttackPlayerCollisionHandler handler = new EnemyAttackPlayerCollisionHandler();
        handler.onCollisionBegin(monster, player);

        assert playerComponent.getHealthPoints() < origHealth;
    }
    // jason 6
    @Test
    void testEnemyKillsPlayer() {
        int playerCurrHealth = 1;
        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(playerCurrHealth);
        player.addComponent(playerComponent);

        Entity monsterWeapon = new Entity();

        EnemyAttackPlayerCollisionHandler handler = new EnemyAttackPlayerCollisionHandler();
        handler.onCollisionBegin(monsterWeapon, player);

        assert playerComponent.dead();
    }
}
