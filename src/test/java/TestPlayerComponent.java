import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.components.PlayerComponent;

public class TestPlayerComponent {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    // devan 1
    @Test
    void testPlayerDies() {

        Entity player = new Entity();
        PlayerComponent playerComponent = new PlayerComponent(1);
        player.addComponent(playerComponent);
        playerComponent.deductHealth(Double.MAX_VALUE, 1, 0, 1);
        assert playerComponent.dead();
    }
}
