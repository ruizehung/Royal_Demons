package uwu.openjfx.collision;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.control.ProgressBar;
import uwu.openjfx.DieScreenMenu;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
import uwu.openjfx.UI;
import uwu.openjfx.components.PlayerComponent;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;

public class PlayerDoorCollisionHandler extends CollisionHandler {
    public PlayerDoorCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.DOOR);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity door) {
        getInput().setProcessInput(false);
        player.getComponent(PlayerComponent.class).stop();

        GameMap gameMap = FXGL.geto("gameMap");
        Room curRoom = FXGL.geto("curRoom");
        if (curRoom.getRoomType().equals("finalWinRoom")) {
            triggerWin();
            return;
        } else if (curRoom.getRoomType().equals("weaponsShowcaseRoom")) {
            getGameScene().getViewport().fade(() -> {
                gameMap.loadRoom(gameMap.getInitialRoom(), "center");
                FXGL.getGameScene().clearUINodes();
                UI.init(FXGL.geto("player"));
                getInput().setProcessInput(true);
            });
            return;
        }

        Room newRoom;
        String spawnPosition = "north";
        switch (door.getString("direction")) {
        case "north":
            newRoom = curRoom.getNorthRoom();
            spawnPosition = "south";
            break;
        case "east":
            newRoom = curRoom.getEastRoom();
            spawnPosition = "west";
            break;
        case "south":
            newRoom = curRoom.getSouthRoom();
            spawnPosition = "north";
            break;
        case "west":
            newRoom = curRoom.getWestRoom();
            spawnPosition = "east";
            break;
        default:
            newRoom = curRoom;
            System.err.println("Error getting new room!");
        }


        if (newRoom != null) {
            if (!newRoom.visited() && !curRoom.enemiesCleared()) {
                pushNotification("Clear enemies before exploring new rooms!");
            } else {
                if (newRoom.getCoordinate().equals(gameMap.getBossRoom().getCoordinate())) {
                    String finalSpawnPosition = spawnPosition;
                    FXGL.getDialogService().showConfirmationBox(
                        "Ready to enter boss room?", answer -> {
                            if (answer) {
                                final String spawnPositionFinal = finalSpawnPosition;
                                getGameScene().getViewport().fade(() -> {
                                    gameMap.loadRoom(newRoom, spawnPositionFinal);
                                    getInput().setProcessInput(true);
                                });
                            }
                        });
                } else {
                    final String spawnPositionFinal = spawnPosition;
                    getGameScene().getViewport().fade(() -> {
                        gameMap.loadRoom(newRoom, spawnPositionFinal);
                        getInput().setProcessInput(true);
                    });
                    return;
                }
            }
        } else {
            if (curRoom.enemiesCleared()) {
                pushNotification("Oops! No room there....");
            } else {
                pushNotification("Clear enemies before exploring new rooms!");
            }
        }
        getInput().setProcessInput(true);
    }

    private void pushNotification(String message) {
        FXGL.getNotificationService().pushNotification(message);
    }

    private void triggerWin() {
        FXGL.getSceneService().pushSubScene(new DieScreenMenu(MenuType.MAIN_MENU));
    }
}
