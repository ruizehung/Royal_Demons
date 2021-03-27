import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.test.RunWithFX;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uwu.openjfx.MainApp;
import uwu.openjfx.RoyalType;
import uwu.openjfx.collision.EnemyAttackPlayerCollisionHandler;
import uwu.openjfx.collision.PlayerAttackEnemyCollisionHandler;
import uwu.openjfx.collision.PlayerCoinCollisionHandler;
import uwu.openjfx.collision.ProjectileWallCollisionHandler;
import uwu.openjfx.components.AttackMultipleComponent;
import uwu.openjfx.components.BossComponent;
import uwu.openjfx.components.CoinComponent;
import uwu.openjfx.components.EnemyComponent;
import uwu.openjfx.components.HealthComponent;
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.components.ProjectileAnimationComponent;

@ExtendWith(RunWithFX.class)
public class MainAppTest {

    @BeforeEach
    public void init() {
        // GameApplication.launch(MainApp.class, new String[0]);
        MainApp.setIsTesting(true);
    }

    // ray 1
    @Test
    void testPlayerPickGold() {
        int initialGold = 1000;
        int coinValue = 5;

        PlayerComponent.setPlayerWeapon("Sword");
        PlayerComponent.setGold(initialGold);

        Entity player = new Entity();
        player.addComponent(new PlayerComponent(1));

        Entity coin = new Entity();
        coin.addComponent(new CoinComponent(coinValue));

        PlayerCoinCollisionHandler handler = new PlayerCoinCollisionHandler();
        handler.onCollisionBegin(player, coin);

        assert (PlayerComponent.getGold() == initialGold + coinValue);
    }

    // ray 2
    @Test
    void testBossGetHurt() {
        Entity boss = new Entity();
        BossComponent bossComponent = new BossComponent(1, "", 10, 20);
        boss.addComponent(bossComponent);
        boss.setProperty("enemyComponent", bossComponent);

        Entity weapon = new Entity();
//        weapon.addComponent(new AttackMultipleComponent(true));

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
//        weapon.addComponent(new AttackMultipleComponent(true));

        assert !monster.getBoolean("isDead");

        PlayerAttackEnemyCollisionHandler handler = new PlayerAttackEnemyCollisionHandler();
        handler.onCollisionBegin(weapon, monster);

        assert monster.getBoolean("isDead");
    }

    //alice 2
    @Test
    void testPlayerGetsHurt() {
        PlayerComponent.setPlayerWeapon("Sword");
        Entity player = new Entity();
        HealthComponent myHealth = new PlayerComponent(10);
        player.addComponent(myHealth);

        Entity monster = new Entity();
//        EnemyComponent enemyComponent = new EnemyComponent(10, "", 10, 20);
//        monster.addComponent(enemyComponent);
//        monster.setProperty("enemyComponent", enemyComponent);
        // monster.addComponent(new AttackThroughComponent(true));

        EnemyAttackPlayerCollisionHandler handler = new EnemyAttackPlayerCollisionHandler();
        handler.onCollisionBegin(monster, player);

        int origHealth = 10;
        assert (myHealth.getHealthPoints() < origHealth);
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
        // Set its type to WALL so that its detected by collision
        Entity wall = new Entity();
        wall.setType(RoyalType.WALL);
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
        // Set its type to WALL so that its detected by collision
        Entity wall = new Entity();
        ProjectileWallCollisionHandler handler = new ProjectileWallCollisionHandler();
        handler.onCollisionBegin(fireball, wall);
        assert (!fireball.isActive());
    }
}
