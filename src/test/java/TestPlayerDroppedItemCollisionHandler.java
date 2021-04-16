import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.test.RunWithFX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uwu.openjfx.MainApp;
import uwu.openjfx.UI;
import uwu.openjfx.collision.PlayerDroppedItemCollisionHandler;
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.weapons.Bow0;
import uwu.openjfx.weapons.GoldenSword1;
import uwu.openjfx.items.Heart;
import uwu.openjfx.items.Item;

@ExtendWith(RunWithFX.class)
public class TestPlayerDroppedItemCollisionHandler {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    // james 3
    @Test
    void testHealthPotionPickUp() {
        Entity potion = new Entity();
        potion.setProperty("name", "HealthPotion");

        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        player.addComponent(playerComponent);

        PlayerDroppedItemCollisionHandler handler = new PlayerDroppedItemCollisionHandler();
        handler.onCollision(player, potion);

        assert UI.getHealthPotProperty().get() == 1;
    }

    // james 4
    @Test
    void testRagePotionPickUp() {
        Entity potion = new Entity();
        potion.setProperty("name", "RagePotion");


        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        player.addComponent(playerComponent);

        PlayerDroppedItemCollisionHandler handler = new PlayerDroppedItemCollisionHandler();
        handler.onCollision(player, potion);

        assert UI.ragePotProperty().get() == 1;
    }

    // alice 3
    @Test
    void testSwordPickUp() {
        Entity sword = new Entity();
        sword.setProperty("name", "golden_sword");

        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        player.addComponent(playerComponent);

        PlayerDroppedItemCollisionHandler handler = new PlayerDroppedItemCollisionHandler();
        handler.onCollision(player, sword);

        assert PlayerComponent.getWeaponInventoryList().contains(new GoldenSword1());
    }

    //alice 4
    @Test
    void testBowPickUp() {
        Entity bow = new Entity();
        bow.setProperty("name", "bow");

        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        player.addComponent(playerComponent);

        PlayerDroppedItemCollisionHandler handler = new PlayerDroppedItemCollisionHandler();
        handler.onCollision(player, bow);

        assert PlayerComponent.getWeaponInventoryList().contains(new Bow0());
    }

    // Ray 4
    @Test
    void testHeartPickUp() {
        int initialMaxHealth = 10;

        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(initialMaxHealth);
        player.addComponent(playerComponent);
        player.setProperty("CreatureComponent", playerComponent);

        Item heart = new Heart("Heart", 3);
        heart.onPickUp(player);

        assert playerComponent.getMaxHealthPoints() == initialMaxHealth + 3;
    }
}