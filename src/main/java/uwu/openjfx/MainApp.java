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
import uwu.openjfx.behaviors.Interactable;
import uwu.openjfx.collision.*;
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.events.InteractEvent;
import uwu.openjfx.input.*;
import uwu.openjfx.items.Heart;
import uwu.openjfx.items.Item;
import uwu.openjfx.weapons.GoldenSword0;

import java.io.File;
import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class MainApp extends GameApplication {

    private Entity player;
    private PlayerComponent playerComponent;
    private GameMap gameMap;
    private List<String> normalMinionList;
    private List<String> forestMinionList;
    private List<String> miniBossList;
    private List<String> roomTypeList;
    private Set<String> weaponsSet;
    private Map<String, String> itemNameAssetMap;
    private Map<String, Item> itemNameObjMap;
    private final Boolean developerCheat = false;
    private static boolean isTesting = false;
    private static Random random = new Random();

    // Top priority : (


    // Tier 2 priority
    // Todo: Boss special attack??
    // Todo: Alice try making some rooms


    // Tier 3 priority : )
    // Todo: Jason makes more weapons / special effects
    // Todo: Devan more sound effects
    // Todo: Ray more traps, lever
    // Todo: Ray open and close door
    // Todo: Ray tile animation for waterfall
    // Todo: make more rooms
    // Todo: Devan UI - heart/health (more polished)

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(960);
        settings.setHeight(640);
        settings.setTitle("Royal Demons");
        settings.setVersion("0.1");
        settings.setAppIcon("skelet_idle_anim_f0_32x32.png");
        settings.setFontUI("ThaleahFat.ttf");
        settings.setMainMenuEnabled(true);
        if (!developerCheat) {
            settings.setSceneFactory(new MainMenuSceneFactory());
        }
        settings.setGameMenuEnabled(true);
        settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
        // settings.setDeveloperMenuEnabled(true);
        // settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }


    @Override
    protected void onPreInit() {
        getSettings().setGlobalMusicVolume(developerCheat ? 0 : 0.25);
        loopBGM("MainMenu.mp3");

        getEventBus().addEventHandler(InteractEvent.ANY, event -> {
            Interactable interactable = event.getEntity().getObject("Interactable");
            interactable.interact();
        });
    }


    @Override
    protected void onUpdate(double tpf) {
        if (PlayerComponent.getCurrentWeapon().isMeleeAttack()) {
            List<Entity> hitboxes = FXGL.getGameWorld().getEntitiesByType(RoyalType.PLAYERATTACK);
            for (Entity hitbox : hitboxes) {
                if (hitbox != null && hitbox.isActive()) {
                    hitbox.removeFromWorld();
                }
            }
        }
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
                player.getComponent(PlayerComponent.class).setPressingMovementKeys(true);
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
                player.getComponent(PlayerComponent.class).setPressingMovementKeys(false);
            }

        }, KeyCode.A, VirtualButton.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
                player.getComponent(PlayerComponent.class).setPressingMovementKeys(true);
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
                player.getComponent(PlayerComponent.class).setPressingMovementKeys(false);
            }
        }, KeyCode.D, VirtualButton.RIGHT);

        getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).up();
                player.getComponent(PlayerComponent.class).setPressingMovementKeys(true);
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
                player.getComponent(PlayerComponent.class).setPressingMovementKeys(false);
            }

        }, KeyCode.W, VirtualButton.UP);

        getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).down();
                player.getComponent(PlayerComponent.class).setPressingMovementKeys(true);
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
                player.getComponent(PlayerComponent.class).setPressingMovementKeys(false);
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

        getInput().addAction(new PickItem("PickItem"), KeyCode.E);
        getInput().addAction(new UseItem("UseItem"), KeyCode.F);
        getInput().addAction(new UseHealthPot("UseHealthPot"), KeyCode.DIGIT1);
        getInput().addAction(new UseRagePot("UseRagePot"), KeyCode.DIGIT2);

        // endregion

        getInput().addAction(new ShowMapAction("showMap"), KeyCode.M);
        getInput().addAction(new ShowInventoryAction("showInventory"), KeyCode.I);

    }

    @Override
    protected void initGame() {
        if (developerCheat) {
            GoldenSword0 goldenSword0 = new GoldenSword0();
            PlayerComponent.setCurrentWeapon(goldenSword0);
            PlayerComponent.getWeaponInventoryList().add(goldenSword0);
            PlayerComponent.setGold(1000);
        }
        // Initialize Epressed to false. During the time player press E, this will
        // become true
        set("Epressed", false);
        set("Fpressed", false);
        set("developerCheat", developerCheat);

        loadRoomAsset();
        loadEnemiesAsset();
        initItemsNameAssetMappingAndWeaponsList();
        gameMap = new GameMap(8);
        gameMap.setRandomSeed(63);
        gameMap.generateRooms();
        set("gameMap", gameMap);

        getGameWorld().addEntityFactory(new StructureFactory());
        getGameWorld().addEntityFactory(new CreatureFactory());
        getGameWorld().addEntityFactory(new WeaponFactory());
        getGameScene().setBackgroundColor(Color.BLACK);

        getAudioPlayer().stopMusic(FXGL.getAssetLoader().loadMusic("MainMenu.mp3"));
        loopBGM("evil4.mp3");
        player = spawn("player", 0, 0);
        set("player", player);
        playerComponent = player.getComponent(PlayerComponent.class);
        set("playerComponent", playerComponent);
        if (developerCheat) {
            player.getComponent(PlayerComponent.class).setHealthPoints(200);
            player.getComponent(PlayerComponent.class).setMaxHealthPoints(999);
            player.getComponent(PlayerComponent.class).setSpeed(300);
        }

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
        FXGL.getPhysicsWorld().addCollisionHandler(new ProjectileDoorCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerTriggerCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerDoorCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerCoinCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerDroppedItemCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerChestCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new PlayerNPCCollisionHandler());
    }

    @Override
    protected void initUI() {
        UI.init(player);

        Text textHealth = getUIFactoryService().newText("", 50);
        textHealth.setTranslateX(100);
        textHealth.setTranslateY(50);
        textHealth.setStroke(Color.WHITE);

        textHealth.textProperty().bind(
                player.getComponent(PlayerComponent.class).getHealthIntegerProperty().asString());
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

        textGold.textProperty().bind(UI.getGoldProperty().asString());
        getGameScene().addUINode(textGold); // add to the scene graph

    }

    public void loadEnemiesAsset() {
        normalMinionList = new ArrayList<>();
        File dir = new File("src/main/resources/assets/textures/creatures/minions/normal");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".png")) {
                normalMinionList.add(file.getName());
            }
        }
        set("normalMinionList", normalMinionList);

        forestMinionList = new ArrayList<>();
        dir = new File("src/main/resources/assets/textures/creatures/minions/forest");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".png")) {
                forestMinionList.add(file.getName());
            }
        }
        set("forestMinionList", forestMinionList);

        miniBossList = new ArrayList<>();
        dir = new File("src/main/resources/assets/textures/creatures/miniBoss");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".png")) {
                miniBossList.add(file.getName());
            }
        }
        set("miniBossList", miniBossList);

    }

    public void initItemsNameAssetMappingAndWeaponsList() {
        itemNameAssetMap = new HashMap<>();
        itemNameAssetMap.put("HealthPotion", "items/healthPotion.png");
        itemNameAssetMap.put("RagePotion", "items/ragePotion.png");
        itemNameAssetMap.put("Heart", "items/ui_heart_full_32x32.png");

        itemNameObjMap = new HashMap<>();
        itemNameObjMap.put("Heart", new Heart("Heart", 3));


        weaponsSet = new HashSet<>();
        File dir = new File("src/main/resources/assets/textures/ui/inventory/");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".png")) {
                weaponsSet.add(file.getName().replace(".png", ""));
                itemNameAssetMap.put(file.getName().replace(".png", ""),
                        "ui/inventory/" + file.getName());
            }
        }

        set("weaponsSet", weaponsSet);
        set("itemsNameAssetMap", itemNameAssetMap);
        set("itemNameObjMap", itemNameObjMap);
    }

    public void loadRoomAsset() {
        roomTypeList = new ArrayList<>();
        File dir = new File("src/main/resources/assets/levels/tmx");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".tmx") && !file.getName().equals("initialRoom.tmx")
                    && !file.getName().equals("bossRoom.tmx")
                    && !file.getName().equals("challengeRoom.tmx")) {
                roomTypeList.add(file.getName().replaceAll(".tmx", ""));
            }
        }
        set("roomTypeList", roomTypeList);
    }

    public static boolean isIsTesting() {
        return isTesting;
    }

    public static void setIsTesting(boolean isTesting) {
        MainApp.isTesting = isTesting;
    }

    public static Random getRandom() {
        return random;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
