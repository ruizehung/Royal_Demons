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

import javafx.scene.paint.Color;
import uwu.openjfx.components.PlayerComponent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

public class MainApp extends GameApplication {

    private Entity player;
    private Map<String, Level> rooms = new HashMap<>();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(960);
        settings.setHeight(640);
//        settings.setWidth(1280);
//        settings.setHeight(720);
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

        rooms.put("initialRoom", setLevelFromMap("tmx/initialRoom.tmx"));
        player = spawn("player", 300, 300);


        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-32*5 , -getAppHeight(), 32*50, 32 * 50);
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);

        for (Entity entity : rooms.get("initialRoom").getEntities()) {
            if (entity.isType(RoyalType.WALL) || entity.isType(RoyalType.DOOR)) {
                System.out.println(entity.getComponent(IDComponent.class));
            }
        }
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);

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
//        Text textPixels = new Text();
//        textPixels.setTranslateX(50); // x = 50
//        textPixels.setTranslateY(100); // y = 100
//
//        textPixels.textProperty().bind(FXGL.getWorldProperties().intProperty("pixelsMoved").asString());
//
//        FXGL.getGameScene().addUINode(textPixels); // add to the scene graph
//
//        var skeletTexture = FXGL.getAssetLoader().loadTexture("skelet_idle_anim_f0_32x32.png");
//        skeletTexture.setTranslateX(50);
//        skeletTexture.setTranslateY(450);
//
//        FXGL.getGameScene().addUINode(skeletTexture);

    }

    private void changeRoom() {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(300, 300));
            player.setZIndex(Integer.MAX_VALUE);
        }

        setLevelFromMap("tmx/testRoom.tmx");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
