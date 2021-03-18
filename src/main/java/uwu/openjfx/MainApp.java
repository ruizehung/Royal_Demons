package uwu.openjfx;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.collection.PropertyMap;
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
import uwu.openjfx.collision.PlayerSkeletCollisionHandler;
import uwu.openjfx.components.PlayerComponent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

public class MainApp extends GameApplication {

    private Entity player;
    private Map<String, RoomData> gameMap = new HashMap<>();

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
        getGameWorld().addEntityFactory(new StructureFactory());
        getGameWorld().addEntityFactory(new CreatureFactory());
        getGameScene().setBackgroundColor(Color.BLACK);

        Level curLevel = setLevelFromMap("tmx/initialRoom.tmx");

        player = spawn("player", 300, 300);

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-32*5 , -getAppHeight(), 32*50, 32 * 50);
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);

//        for (Entity entity : curLevel.getEntities()) {
//            if (entity.isType(RoyalType.SKELET)) {
//                System.out.println(entity);
//                System.out.println(entity.getComponent(IDComponent.class));
//                System.out.println(entity.getType());
//                System.out.println(entity.getProperties());
//            }
//        }
        RoomData roomData = new RoomData("initialRoom", curLevel.getEntities());
        gameMap.put("initialRoom", roomData);
        set("curRoom", roomData);
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

            getGameScene().getViewport().fade(() -> {
                changeRoom();
                getInput().setProcessInput(true);
            });
        });
    }

    @Override
    protected void initUI() {

    }

    private void loadRoom(String roomName) {
        RoomData roomData = gameMap.get(roomName);
        Level curLevel = setLevelFromMap("tmx/" + roomName + ".tmx");
        if (roomData != null) {
            for (Entity entity : curLevel.getEntities()) {
                if (entity.isType(RoyalType.SKELET)) {
                    IDComponent idComponent = entity.getComponent(IDComponent.class);
                    if (roomData.getEntityData(idComponent.getId(), "isAlive") == 0) {
                        entity.removeFromWorld();
                    }
                }
            }
            set("curRoom", roomData);
        } else {
            set("curRoom", new RoomData(roomName, curLevel.getEntities()));
        }
    }

    private void changeRoom() {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(300, 300));
            player.setZIndex(Integer.MAX_VALUE);
        }

        if (((RoomData) geto("curRoom")).getName() == "initialRoom") {
            loadRoom("testRoom");

        } else {
            loadRoom("initialRoom");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
