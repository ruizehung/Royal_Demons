package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
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

        Room curRoom = FXGL.geto("curRoom");
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
        GameMap gameMap = FXGL.geto("gameMap");

        if (newRoom != null) {
            if (!newRoom.visited() && !curRoom.enemiesCleared()) {
                pushNotification("Clear enemies before exploring new rooms!");
            } else {
                final String spawnPosition_ = spawnPosition;
                getGameScene().getViewport().fade(() -> {
                    gameMap.loadRoom(newRoom, spawnPosition_);
                    getInput().setProcessInput(true);
                });
                return;
            }
        } else if (curRoom.enemiesCleared()) {
            pushNotification("Oops! No room there....");
        }
        getInput().setProcessInput(true);
    }

    private void pushNotification(String message) {
        FXGL.getNotificationService().pushNotification(message);
    }
}