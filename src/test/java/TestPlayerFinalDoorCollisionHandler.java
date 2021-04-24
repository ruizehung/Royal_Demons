import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.RoyalType;
import uwu.openjfx.input.TeleportToWinRoom;

public class TestPlayerFinalDoorCollisionHandler {

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    //james 5
    @Test
    void testCannotGoToFinalWinRoomBeforeBeatingFinalBoss() {
        GameWorld gameWorld = new GameWorld();
        Entity finalBoss = new Entity();
        finalBoss.setType(RoyalType.ENEMY);
        gameWorld.addEntity(finalBoss);

        TeleportToWinRoom teleportToWinRoom = new TeleportToWinRoom("");
        assert !teleportToWinRoom.canProceed(gameWorld);

        // beat Final boss
        gameWorld.removeEntity(finalBoss);

        assert teleportToWinRoom.canProceed(gameWorld);
    }
}
