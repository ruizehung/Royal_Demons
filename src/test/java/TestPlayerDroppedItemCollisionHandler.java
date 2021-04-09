import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.test.RunWithFX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uwu.openjfx.MainApp;
import uwu.openjfx.UI;
import uwu.openjfx.behaviors.DropItemComponent;
import uwu.openjfx.collision.PlayerDroppedItemCollisionHandler;
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.weapons.Bow0;
import uwu.openjfx.weapons.GoldenSword0;

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

        PlayerComponent.setPlayerWeapon("Sword");
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

        PlayerComponent.setPlayerWeapon("Sword");
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

        PlayerComponent.setPlayerWeapon("bow");
        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        player.addComponent(playerComponent);

        PlayerDroppedItemCollisionHandler handler = new PlayerDroppedItemCollisionHandler();
        handler.onCollision(player, sword);

        assert PlayerComponent.getWeaponInventoryList().contains(new GoldenSword0());
    }

    //alice 4
    @Test
    void testBowPickUp() {
        Entity bow = new Entity();
        bow.setProperty("name", "bow");

        PlayerComponent.setPlayerWeapon("golden_sword");
        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        player.addComponent(playerComponent);

        PlayerDroppedItemCollisionHandler handler = new PlayerDroppedItemCollisionHandler();
        handler.onCollision(player, bow);

        assert PlayerComponent.getWeaponInventoryList().contains(new Bow0());
    }
}