import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.test.RunWithFX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uwu.openjfx.MainApp;
import uwu.openjfx.UI;
import uwu.openjfx.collision.PlayerDroppedItemCollisionHandler;
import uwu.openjfx.components.PlayerComponent;

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

}