import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.collision.PlayerCoinCollisionHandler;
import uwu.openjfx.components.CoinComponent;
import uwu.openjfx.components.PlayerComponent;

public class TestPlayerCoinCollisionHandler {

    @BeforeEach
    public void init() {
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
}