package uwu.openjfx;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

import javafx.scene.paint.Color;
import uwu.openjfx.MapGeneration.Coordinate;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.collision.PlayerSkeletCollisionHandler;
import uwu.openjfx.components.PlayerComponent;

import java.util.EnumSet;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

public class MainApp extends GameApplication {

    private Entity player;
    private GameMap gameMap;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(960);
        settings.setHeight(640);
        settings.setTitle("Royal Demons");
        settings.setVersion("0.1");
        settings.setAppIcon("lizard_m_idle_anim_f0.png");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
//        settings.setDeveloperMenuEnabled(true);
//        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalMusicVolume(0.25);
        // loopBGM("some_music.wav");
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }

        }, KeyCode.A, VirtualButton.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.D, VirtualButton.RIGHT);

        getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).up();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }

        }, KeyCode.W, VirtualButton.UP);

        getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).down();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.S, VirtualButton.DOWN);

    }

    @Override
    protected void initGame() {
        gameMap = new GameMap(40);

        getGameWorld().addEntityFactory(new StructureFactory());
        getGameWorld().addEntityFactory(new CreatureFactory());
        getGameScene().setBackgroundColor(Color.BLACK);

        loadRoom(new Coordinate(0,0));

        player = spawn("player", 300, 300);
        set("player", player);

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-32*5 , -getAppHeight(), 32*50, 32 * 50);
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerSkeletCollisionHandler());

        FXGL.onCollisionOneTimeOnly(RoyalType.PLAYER, RoyalType.DOOR, (player, door) -> {
            getInput().setProcessInput(false);
            player.getComponent(PlayerComponent.class).stop();

            Room curRoom = FXGL.geto("curRoom");
            Coordinate newCoordinate;
            switch (door.getString("direction")) {
                case "north":
                    newCoordinate = curRoom.getNorthRoom().getCoordinate();
                    break;
                case "east":
                    newCoordinate = curRoom.getEastRoom().getCoordinate();
                    break;
                case "south":
                    newCoordinate = curRoom.getSouthRoom().getCoordinate();
                    break;
                case "west":
                    newCoordinate = curRoom.getWestRoom().getCoordinate();
                    break;
                default:
                    newCoordinate = curRoom.getCoordinate();
                    System.err.println("Error getting new coordinate!");
            }
            getGameScene().getViewport().fade(() -> {
                loadRoom(newCoordinate);
                getInput().setProcessInput(true);
            });
        });
    }

    @Override
    protected void initUI() {

    }


    private void loadRoom(Coordinate coordinate) {
        Room room = gameMap.getRoom(coordinate);
        Level curLevel = setLevelFromMap("tmx/" + room.getRoomType() + ".tmx");

        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(300, 300));
            player.setZIndex(Integer.MAX_VALUE);
        }

        for (Entity entity : curLevel.getEntities()) {
            if (entity.isType(RoyalType.ENEMY)) {
                IDComponent idComponent = entity.getComponent(IDComponent.class);
                if (!room.visited()) {
                    room.setEntityData(idComponent.getId(), "isAlive", 1);
                } else {
                    if (room.getEntityData(idComponent.getId(), "isAlive") == 0) {
                        entity.removeFromWorld();
                    }
                }
            }
        }

        if (!room.visited()) {
            room.setVisited(true);
        }

        set("curRoom", room);
        System.out.println(room.getCoordinate());
    }

    private void changeRoom() {
        if (((Room) geto("curRoom")).getCoordinate().equals(new Coordinate(0, 0))) {
            loadRoom(new Coordinate(1, 0));
        } else {
            loadRoom(new Coordinate(0, 0));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
