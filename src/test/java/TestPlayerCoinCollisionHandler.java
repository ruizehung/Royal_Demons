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

        PlayerComponent.setGold(initialGold);

        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(1);
        player.addComponent(playerComponent);
        player.setProperty("CreatureComponent", playerComponent);

        Entity coin = new Entity();
        coin.addComponent(new CoinComponent(coinValue));

        PlayerCoinCollisionHandler handler = new PlayerCoinCollisionHandler();
        handler.onCollisionBegin(player, coin);

        assert (PlayerComponent.getGold() == initialGold + coinValue);
    }

    // alice 5
    @Test
    void testCoinPickUpAddsToGoldCountStatistic() {

        int coinValue = 5;
        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(1);
        player.addComponent(playerComponent);
        player.setProperty("CreatureComponent", playerComponent);

        Entity coin = new Entity();
        coin.addComponent(new CoinComponent(coinValue));
        int currentGold = PlayerComponent.getGold();

        PlayerCoinCollisionHandler handler = new PlayerCoinCollisionHandler();
        handler.onCollisionBegin(player, coin);

        assert PlayerComponent.getGold() > currentGold;
    }

}