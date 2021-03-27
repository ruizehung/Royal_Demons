import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.test.RunWithFX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uwu.openjfx.MainApp;
import uwu.openjfx.collision.PlayerAttackEnemyCollisionHandler;
import uwu.openjfx.components.BossComponent;
import uwu.openjfx.components.EnemyComponent;

@ExtendWith(RunWithFX.class)
public class TestPlayerAttackEnemyCollisionHandler {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    // ray 2
    @Test
    void testBossGetHurt() {
        Entity boss = new Entity();
        BossComponent bossComponent = new BossComponent(1, "", 10, 20);
        boss.addComponent(bossComponent);
        boss.setProperty("enemyComponent", bossComponent);

        Entity weapon = new Entity();

        assert !boss.getBoolean("isDead");

        PlayerAttackEnemyCollisionHandler handler = new PlayerAttackEnemyCollisionHandler();
        handler.onCollisionBegin(weapon, boss);

        assert boss.getBoolean("isDead");
    }

    //alice 1
    @Test
    void testMinionDies() {
        Entity monster = new Entity();
        EnemyComponent enemyComponent = new EnemyComponent(
                1, "", 10, 20);
        monster.addComponent(enemyComponent);
        monster.setProperty("enemyComponent", enemyComponent);

        Entity weapon = new Entity();

        assert !monster.getBoolean("isDead");

        PlayerAttackEnemyCollisionHandler handler = new PlayerAttackEnemyCollisionHandler();
        handler.onCollisionBegin(weapon, monster);

        assert monster.getBoolean("isDead");
    }

    // devan 2
    @Test
    void testMinionGetsHurt() {
        Entity monster = new Entity();
        EnemyComponent enemyComponent = new EnemyComponent(
                2, "", 10, 20);
        monster.addComponent(enemyComponent);
        monster.setProperty("enemyComponent", enemyComponent);

        Entity weapon = new Entity();

        PlayerAttackEnemyCollisionHandler handler = new PlayerAttackEnemyCollisionHandler();
        handler.onCollisionBegin(weapon, monster);

        int origHealth = 2;
        assert (enemyComponent.getHealthPoints() < origHealth);
    }
}