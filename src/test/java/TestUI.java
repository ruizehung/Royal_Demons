import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.test.RunWithFX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uwu.openjfx.MainApp;
import uwu.openjfx.UI;
import uwu.openjfx.components.PlayerComponent;

@ExtendWith(RunWithFX.class)
public class TestUI {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    // devan 3
    @Test
    void testHealthPotionUseHealsHP() {
        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        playerComponent.setHealthPoints(6);
        player.addComponent(playerComponent);

        UI.init(player);
        UI.addHealthPot();
        UI.useHealthPot();

        assert player.getComponent(PlayerComponent.class).getHealthPoints() == 9;
    }

    // devan 4
    @Test
    void testRagePotionCounterAccuracy() {
        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(10);
        player.addComponent(playerComponent);

        UI.init(player);
        UI.setRagePotProperty(0);
        UI.addRagePot();
        assert UI.ragePotProperty().get() == 1;
        UI.useRagePot();
        assert UI.ragePotProperty().get() == 0;
    }
}