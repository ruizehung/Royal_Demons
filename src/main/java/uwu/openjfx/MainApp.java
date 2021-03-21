package uwu.openjfx;

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
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.collision.*;
import uwu.openjfx.input.KillAllEnemy;
import uwu.openjfx.input.ShowMapAction;
import uwu.openjfx.components.HealthComponent;
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.components.TrapComponent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getSettings;
import static com.almasb.fxgl.dsl.FXGLForKtKt.loopBGM;
import static com.almasb.fxgl.dsl.FXGLForKtKt.set;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;
import static com.almasb.fxgl.dsl.FXGL.setLevelFromMap;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class MainApp extends GameApplication {

    private Entity player;
    private GameMap gameMap;
    private List<String> minionList;
    final private Boolean developerCheat = true;
    // TODO: be able to beat the boss and show win screen

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(960);
        settings.setHeight(640);
        settings.setTitle("Royal Demons");
        settings.setVersion("0.1");
        settings.setAppIcon("skelet_idle_anim_f0_32x32.png");
        settings.setFontUI("ThaleahFat.ttf");
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new MainMenuSceneFactory());
        settings.setGameMenuEnabled(true);
        settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
        // TODO: give credits to all sources that we use
        settings.getCredits().addAll(Arrays.asList(
                "Asset by 0x72 from itch.io",
                "0x72.itch.io/dungeontileset-ii"
        ));
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
        if (developerCheat) {
            getInput().addAction(new KillAllEnemy("KillAll"), KeyCode.K);
        }
        // TODO: refactor all these to input package
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

        // Player Input
        getInput().addAction(new UserAction("LMB") {
            @Override
            protected void onActionBegin() {
                if (!player.getComponent(PlayerComponent.class).isAttacking()) {
                    player.getComponent(PlayerComponent.class).autoAttack();
                    if (FXGL.getInput().getMousePositionWorld().getX() > player.getX() + 20) {
                        player.setScaleX(1);
                    } else {
                        player.setScaleX(-1);
                    }
                }
            }
        }, MouseButton.PRIMARY);
        getInput().addAction(new UserAction("SPACE") {
            @Override
            protected void onActionBegin() {
                if (!player.getComponent(PlayerComponent.class).isAttacking()) {
                    player.getComponent(PlayerComponent.class).ultimateAttack();
                    if (FXGL.getInput().getMousePositionWorld().getX() > player.getX() + 20) {
                        player.setScaleX(1);
                    } else {
                        player.setScaleX(-1);
                    }
                }
            }
        }, KeyCode.SPACE);
        // endregion

        getInput().addAction(new ShowMapAction("showMap"), KeyCode.M);
    }

    @Override
    protected void initGame() {
        loadEnemies();
        gameMap = new GameMap(40);
        set("gameMap", gameMap);

        getGameWorld().addEntityFactory(new StructureFactory());
        getGameWorld().addEntityFactory(new CreatureFactory());
        getGameWorld().addEntityFactory(new WeaponFactory());
        getGameScene().setBackgroundColor(Color.BLACK);

        getAudioPlayer().stopMusic(FXGL.getAssetLoader().loadMusic("MainMenu.mp3"));
        loopBGM("evil4.mp3");
        player = spawn("player", 0, 0);
        set("player", player);

        gameMap.loadRoom(gameMap.getInitialRoom(), "center");

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-32 * 5, -getAppHeight(), 32 * 70, 32 * 70);
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setLazy(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
        vars.put("developerCheat", developerCheat);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerEnemyCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerAttackEnemyCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new EnemyAttackPlayerCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerTriggerCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerDoorCollisionHandler());
    }

    @Override
    protected void initUI() {
        Text textPixels = getUIFactoryService().newText("", 75);
        textPixels.setTranslateX(25);
        textPixels.setTranslateY(75);
        textPixels.setStroke(Color.WHITE);

        textPixels.textProperty().bind(
                player.getComponent(PlayerComponent.class).getPlayerHealth().asString());
        getGameScene().addUINode(textPixels); // add to the scene graph
    }

    public void loadEnemies() {
        minionList = new ArrayList<>();
        File dir = new File("src/main/resources/assets/textures/creatures/minions");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".png")) {
                minionList.add(file.getName());
            }
        }
        set("minionList", minionList);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
