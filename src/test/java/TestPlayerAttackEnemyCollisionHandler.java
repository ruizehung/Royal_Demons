import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.test.RunWithFX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uwu.openjfx.MainApp;
import uwu.openjfx.collision.PlayerAttackEnemyCollisionHandler;
import uwu.openjfx.components.AttackDamageComponent;
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
    void testBossDies() {
        Entity boss = new Entity();
        BossComponent bossComponent = new BossComponent(1, "", 0, 0, 0);
        boss.addComponent(bossComponent);
        boss.setProperty("CreatureComponent", bossComponent);

        Entity weapon = new Entity();
        weapon.addComponent(new AttackDamageComponent(false, Double.MAX_VALUE));
        boss.getComponent(bossComponent.getClass()).setBlockProbability(0);
        boss.getComponent(bossComponent.getClass()).setArmorStat(1);
        assert !bossComponent.dead();

        PlayerAttackEnemyCollisionHandler handler = new PlayerAttackEnemyCollisionHandler();
        handler.onCollisionBegin(weapon, boss);

        assert bossComponent.dead();
    }

    // james 2
    @Test
    void testBossGetHurt() {
        int origHealth = 5;
        Entity boss = new Entity();
        BossComponent bossComponent = new BossComponent(origHealth, "", 0, 0, 0);
        boss.addComponent(bossComponent);
        boss.setProperty("CreatureComponent", bossComponent);

        Entity weapon = new Entity();
        weapon.addComponent(new AttackDamageComponent(false, Double.MAX_VALUE));
        boss.getComponent(bossComponent.getClass()).setBlockProbability(0);
        boss.getComponent(bossComponent.getClass()).setArmorStat(1);

        PlayerAttackEnemyCollisionHandler handler = new PlayerAttackEnemyCollisionHandler();
        handler.onCollisionBegin(weapon, boss);

        assert (bossComponent.getHealthPoints() < origHealth);
    }

    //alice 1
    @Test
    void testMinionDies() {
        Entity monster = new Entity();
        EnemyComponent enemyComponent = new EnemyComponent(
                1, "", 10, 20);
        monster.addComponent(enemyComponent);
        monster.setProperty("CreatureComponent", enemyComponent);

        Entity weapon = new Entity();
        weapon.addComponent(new AttackDamageComponent(false, Double.MAX_VALUE));
        enemyComponent.setBlockProbability(0);
        enemyComponent.setArmorStat(1);

        assert !monster.getBoolean("isDead");

        PlayerAttackEnemyCollisionHandler handler = new PlayerAttackEnemyCollisionHandler();
        handler.onCollisionBegin(weapon, monster);

        assert enemyComponent.dead();
    }

    // devan 2
    @Test
    void testMinionGetsHurt() {
        int origHealth = 5;
        Entity monster = new Entity();
        EnemyComponent enemyComponent = new EnemyComponent(
                origHealth, "", 10, 20);
        monster.addComponent(enemyComponent);
        monster.setProperty("CreatureComponent", enemyComponent);

        Entity weapon = new Entity();
        weapon.addComponent(new AttackDamageComponent(false, Double.MAX_VALUE));
        enemyComponent.setBlockProbability(0);
        enemyComponent.setArmorStat(1);

        PlayerAttackEnemyCollisionHandler handler = new PlayerAttackEnemyCollisionHandler();
        handler.onCollisionBegin(weapon, monster);

        assert enemyComponent.getHealthPoints() < origHealth;
    }
}