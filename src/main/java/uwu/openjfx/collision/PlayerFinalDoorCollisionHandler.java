package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MapGeneration.Coordinate;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;

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
        }

    }
}
