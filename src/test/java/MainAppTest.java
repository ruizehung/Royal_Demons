import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.concurrent.Async;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.test.RunWithFX;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import uwu.openjfx.MainApp;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.collision.PlayerCoinCollisionHandler;
import uwu.openjfx.components.CoinComponent;
import uwu.openjfx.components.PlayerComponent;

import java.util.Map;

//@ExtendWith(ApplicationExtension.class)
@ExtendWith(RunWithFX.class)
public class MainAppTest {

    FxRobot robot = new FxRobot();
    /**
     * Starts the test.
     *
     * @param stage passes in the initial stage of the game
     * @throws Exception throws an exception if there is an issue
     *                    when running the tests
     */
    @Start
    private void start(Stage stage) throws Exception {
        //MainApp.main(new String[0]);
        // GameApplication.launch(MainApp.class, new String[0]);
    }


    @BeforeEach
    public void init(){
        // GameApplication.launch(MainApp.class, new String[0]);
        MainApp.setIsTesting(true);
    }

    /**
     * A test that ensures the welcome screen contains a start button.
     */
//    @Test
//    void testWelcomeScreenContainsExitButton() {
//        Async.INSTANCE.startAsyncFX((Runnable) robot.clickOn("Exit"));
//        robot.clickOn("Yes");
//        //FxAssert.verifyThat(".Label", TextMatchers.hasText("Royal Demons"));
//    }
    // ray 1
    @Test
    void testPlayerPickGold() {
        int initialGold = 1000;
        int coinValue = 5;
        PlayerComponent.playerWeapon = "Sword";
        PlayerComponent.gold = initialGold;
        Entity player = new Entity();
        player.addComponent(new PlayerComponent(1));

        Entity coin = new Entity();
        coin.addComponent(new CoinComponent(coinValue));

        PlayerCoinCollisionHandler handler = new PlayerCoinCollisionHandler();
        handler.onCollisionBegin(player, coin);

        assert (PlayerComponent.gold == initialGold + coinValue);
    }

}