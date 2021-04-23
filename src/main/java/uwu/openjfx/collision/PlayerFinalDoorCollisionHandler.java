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
            PlayerComponent playerComponent = FXGL.geto("playerComponent");
            playerComponent.stop();

            GameMap gameMap = FXGL.geto("gameMap");
            Room finalWinRoom = new Room(new Coordinate(999, 999));
            finalWinRoom.setRoomType("finalWinRoom");
            getGameScene().getViewport().fade(() -> {
                gameMap.loadRoom(finalWinRoom, "center");
                getInput().setProcessInput(true);
                playerComponent.faceRight();
            });

            ArrayList<String> savePrincess = new ArrayList<>(Arrays.asList(
                    "1.name = " + PlayerComponent.getPlayerName(),
                    "2.name = " + PlayerComponent.getPlayerName() + "'s Super Amazing n Cute and "
                            + "Now Un-Kidnapped Princess",
                    "1.image = ui_yoshi.png",
                    "2.image = ui_peach.png",
                    "1: My princess, I was so worried!",
                    "2: Omg, ily, thx for saving me. U the best. Old man was gross af",
                    "1: My love, no problem. Let's go home so you can rest! During my journey, I "
                            + "stole " + FXGL.geti("coin") + " coins on the way here, "
                            + "did " + (int) PlayerComponent.getDamageDealt() + " damage, and "
                            + "killed " + PlayerComponent.getMonstersKilled() + " monsters!"
            ));
            FXGL.getSceneService().getTimer().runOnceAfter(() -> FXGL.getCutsceneService()
                    .startCutscene(
                            new Cutscene(savePrincess)), Duration.millis(1500));
        }

    }
}
