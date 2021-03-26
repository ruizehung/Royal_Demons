package uwu.openjfx;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.collision.*;
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.input.KillAllEnemy;
import uwu.openjfx.input.ShowMapAction;
import uwu.openjfx.input.TeleportToBossRoom;

import java.io.File;
import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class MainApp extends GameApplication {

    private Entity player;
    private GameMap gameMap;
    private List<String> minionList;
    private List<String> miniBossList;
    private List<String> roomTypeList;
    private final Boolean developerCheat = true;

    // by Friday
    // Meeting Friday 5pm! Don't forget
    // Todo: display gold!!! We must have all the features from M2, M3
    // Todo: fix name bugs in configuration screen
    // Todo: show win screen
    // Todo: Clarify robustness diagram
    // Todo: 2 junit tests each per person

    // Tier 2 priority
    // Todo: Jason refactors interfaces
    // Todo: Clarify robustness diagram
    // Todo: Boss special attack??
    // Todo: Fix notification in boss room (in boss fight)
    // Todo: James weapons animation
    // Todo: Alice try making some rooms
    // Todo: Devan UI - heart/health

    // Tier 3 priority
    // Todo: Jason makes more weapons / special effects
    // Todo: Devan more sound effects
    // Todo: Ray more traps, lever
    // Todo: Ray open and close door
    // Todo: Ray tile animation for waterfall
    // Todo: make more rooms

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
        // TODO_: give credits to all sources that we use
        settings.getCredits().addAll(Arrays.asList(
                "Asset by 0x72, aekae13 from itch.io",
                "0x72.itch.io/dungeontileset-ii",
                "aekae13.itch.io/16x16-dungeon-walls-reconfig"
        ));
        // settings.setDeveloperMenuEnabled(true);
        // settings.setApplicationMode(ApplicationMode.DEVELOPER);
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
            getInput().addAction(new TeleportToBossRoom("TeleportToBossRoom"), KeyCode.B);
        }
        // TODO_: refactor all these to input package
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
                    player.getComponent(PlayerComponent.class).setMousePosition(
                            FXGL.getInput().getMousePositionWorld().getX(),
                            FXGL.getInput().getMousePositionWorld().getY());
                    player.getComponent(PlayerComponent.class).autoAttack(false);
                }
            }
        }, MouseButton.PRIMARY);
        getInput().addAction(new UserAction("SPACE") {
            @Override
            protected void onActionBegin() {
                if (!player.getComponent(PlayerComponent.class).isAttacking()) {
                    player.getComponent(PlayerComponent.class).setMousePosition(
                            FXGL.getInput().getMousePositionWorld().getX(),
                            FXGL.getInput().getMousePositionWorld().getY());
                    player.getComponent(PlayerComponent.class).autoAttack(true);
                }
            }
        }, KeyCode.SPACE);
        // endregion

        getInput().addAction(new ShowMapAction("showMap"), KeyCode.M);
    }

    @Override
    protected void initGame() {
        loadRoomAsset();
        loadEnemiesAsset();
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
        FXGL.getPhysicsWorld().addCollisionHandler(new ProjectileWallCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerTriggerCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerDoorCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerCoinCollisionHandler());
    }

    @Override
    protected void initUI() {
        Text textHealth = getUIFactoryService().newText("", 50);
        textHealth.setTranslateX(100);
        textHealth.setTranslateY(50);
        textHealth.setStroke(Color.WHITE);

        textHealth.textProperty().bind(
                player.getComponent(PlayerComponent.class).getPlayerHealth().asString());
        getGameScene().addUINode(textHealth); // add to the scene graph

        Text textHealthPrefix = getUIFactoryService().newText("HP:", 50);
        textHealthPrefix.setTranslateX(25);
        textHealthPrefix.setTranslateY(50);
        textHealthPrefix.setStroke(Color.RED);
        getGameScene().addUINode(textHealthPrefix);

        Text textGoldPrefix = getUIFactoryService().newText("GOLD:", 50);
        textGoldPrefix.setTranslateX(getAppWidth() - 225);
        textGoldPrefix.setTranslateY(50);
        textGoldPrefix.setStroke(Color.GOLD);
        getGameScene().addUINode(textGoldPrefix);

        Text textGold = getUIFactoryService().newText("", 50);
        textGold.setTranslateX(getAppWidth() - 100);
        textGold.setTranslateY(50);
        textGold.setStroke(Color.WHITE);

        textGold.textProperty().bind(
                player.getComponent(PlayerComponent.class).getGold().asString());
        getGameScene().addUINode(textGold); // add to the scene graph
    }

    public void loadEnemiesAsset() {
        minionList = new ArrayList<>();
        File dir = new File("src/main/resources/assets/textures/creatures/minions");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".png")) {
                minionList.add(file.getName());
            }
        }
        set("minionList", minionList);

        miniBossList = new ArrayList<>();
        dir = new File("src/main/resources/assets/textures/creatures/miniBoss");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".png")) {
                miniBossList.add(file.getName());
            }
        }
        set("miniBossList", miniBossList);

    }

    public void loadRoomAsset() {
        roomTypeList = new ArrayList<>();
        File dir = new File("src/main/resources/assets/levels/tmx");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".tmx") && !file.getName().equals("initialRoom.tmx")
                    && !file.getName().equals("bossRoom.tmx")) {
                roomTypeList.add(file.getName().replaceAll(".tmx", ""));
            }
        }
        set("roomTypeList", roomTypeList);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
