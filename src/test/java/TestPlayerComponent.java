import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.components.HealthComponent;
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
        HealthComponent myHealth = new PlayerComponent(1);
        player.addComponent(myHealth);
        player.getComponent(PlayerComponent.class).deductHealth();
        assert (player.getComponent(PlayerComponent.class).isDeadTest());
    }
}
