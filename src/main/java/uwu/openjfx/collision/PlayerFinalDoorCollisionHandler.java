package uwu.openjfx.collision;

import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;
import uwu.openjfx.MapGeneration.Coordinate;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.PlayerComponent;

import java.util.ArrayList;
import java.util.Arrays;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;

public class PlayerFinalDoorCollisionHandler extends CollisionHandler {

    public PlayerFinalDoorCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.FINALDOOR);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity finalDoor) {
        if (FXGL.getGameWorld().getEntitiesByType(RoyalType.ENEMY).size() > 0) {
            System.out.println("Player should kill the boss first");
        } else {
            System.out.println("Teleport Player to final room");
            getInput().setProcessInput(false);
            GameMap gameMap = FXGL.geto("gameMap");
            Room finalWinRoom = new Room(new Coordinate(999, 999));
            finalWinRoom.setRoomType("finalWinRoom");
            getGameScene().getViewport().fade(() -> {
                gameMap.loadRoom(finalWinRoom, "center");
                getInput().setProcessInput(true);
            });
            ArrayList<String> savePrincess = new ArrayList<>(Arrays.asList(
                    "1.name = " + PlayerComponent.getPlayerName(),
                    "2.name = " + PlayerComponent.getPlayerName() + "'s dino lover",
                    "1.image = ui_yoshi.png",
                    "2.image = ui_peach.png",
                    "1: omg is dat u??",
                    "2: ya boo, thnx for saving me",
                    "1: np, lets go home and watch some netflix ^^"
            ));
            FXGL.getSceneService().getTimer().runOnceAfter(() -> FXGL.getCutsceneService()
                    .startCutscene(
                            new Cutscene(savePrincess)), Duration.millis(1500));
        }

    }
}
