import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.behaviors.DropItemComponent;
import uwu.openjfx.collision.PlayerChestCollisionHandler;
import uwu.openjfx.components.ChestComponent;
import uwu.openjfx.components.PlayerComponent;


public class TestPlayerChestCollisionHandler {
    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    // Ray 3
    @Test
    public void testPlayerOpensChest() {
        Entity chest = new Entity();
        chest.addComponent(new DropItemComponent());
        ChestComponent chestComponent = new ChestComponent();
        chest.addComponent(chestComponent);
        chest.setProperty("chestComponent", chestComponent);

        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        player.addComponent(playerComponent);

        assert !chestComponent.hasInteractedBefore();

        PlayerChestCollisionHandler playerChestCollisionHandler = new PlayerChestCollisionHandler();
        playerChestCollisionHandler.onCollision(player, chest);

        assert chestComponent.hasInteractedBefore();
    }
}
