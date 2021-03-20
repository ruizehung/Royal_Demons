package uwu.openjfx;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import uwu.openjfx.MapGeneration.Coordinate;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.collision.MeleeSwordEnemyCollisionHandler;
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
        settings.setFontUI("ThaleahFat.ttf");
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new MainMenuSceneFactory());
        settings.setSoundMenuSelect("drop.wav");
        settings.setGameMenuEnabled(true);
        settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
//        settings.setDeveloperMenuEnabled(true);
//        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalMusicVolume(0.25);
        loopBGM("MainMenu.mp3");
    }

    @Override
    protected void initInput() {
        //region Movement
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
        //endregion

        getInput().addAction(new UserAction("LMB") {
            @Override
            protected void onActionBegin() {
                if (!player.getComponent(PlayerComponent.class).isAttacking()) {
                    player.getComponent(PlayerComponent.class).autoAttack();
//                    meleeAttack();
                }
            }
        }, MouseButton.PRIMARY);

    }

    @Override
    protected void initGame() {
        gameMap = new GameMap(40);

        getGameWorld().addEntityFactory(new StructureFactory());
        getGameWorld().addEntityFactory(new CreatureFactory());
        getGameWorld().addEntityFactory(new WeaponFactory());
        getGameScene().setBackgroundColor(Color.BLACK);

        loadRoom(gameMap.getInitialRoom());

        player = spawn("player", 300, 300);
        set("player", player);

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-32 * 5, -getAppHeight(), 32 * 50, 32 * 50);
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
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
        FXGL.getPhysicsWorld().addCollisionHandler(new MeleeSwordEnemyCollisionHandler());

        FXGL.onCollisionOneTimeOnly(RoyalType.PLAYER, RoyalType.DOOR, (player, door) -> {
            getInput().setProcessInput(false);
            player.getComponent(PlayerComponent.class).stop();

            Room curRoom = FXGL.geto("curRoom");
            Room newRoom;
            switch (door.getString("direction")) {
                case "north":
                    newRoom = curRoom.getNorthRoom();
                    break;
                case "east":
                    newRoom = curRoom.getEastRoom();
                    break;
                case "south":
                    newRoom = curRoom.getSouthRoom();
                    break;
                case "west":
                    newRoom = curRoom.getWestRoom();
                    break;
                default:
                    newRoom = curRoom;
                    System.err.println("Error getting new room!");
            }

            if (newRoom != null) {
                getGameScene().getViewport().fade(() -> {
                    loadRoom(newRoom);
                    getInput().setProcessInput(true);
                });
            } else {
                getInput().setProcessInput(true);
            }
        });
    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

        textPixels.textProperty().bind(FXGL.getWorldProperties()
                .intProperty("pixelsMoved").asString());

        FXGL.getGameScene().addUINode(textPixels); // add to the scene graph

        var skeletTexture = FXGL.getAssetLoader().loadTexture(
                "skelet_idle_anim_f0_32x32.png");
        skeletTexture.setTranslateX(50);
        skeletTexture.setTranslateY(450);

        FXGL.getGameScene().addUINode(skeletTexture);
    }


    private void loadRoom(Room newRoom) {
        Level curLevel = setLevelFromMap("tmx/" + newRoom.getRoomType() + ".tmx");

        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(300, 300));
            player.setZIndex(Integer.MAX_VALUE);
        }

        for (Entity entity : curLevel.getEntities()) {
            if (entity.isType(RoyalType.ENEMY)) {
                IDComponent idComponent = entity.getComponent(IDComponent.class);
                if (!newRoom.visited()) {
                    newRoom.setEntityData(idComponent.getId(), "isAlive", 1);
                } else {
                    if (newRoom.getEntityData(idComponent.getId(), "isAlive") == 0) {
                        entity.removeFromWorld();
                    }
                }
            }
        }

        if (!newRoom.visited()) {
            newRoom.setVisited(true);
        }

        set("curRoom", newRoom);
        System.out.println(newRoom.getCoordinate());
    }

    public void meleeAttack() {
        Entity meleeSword = new Entity();
        meleeSword = spawn("meleeSword", player.getX() + 5, player.getY());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
