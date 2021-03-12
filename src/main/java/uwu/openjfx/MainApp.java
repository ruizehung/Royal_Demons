package uwu.openjfx;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;

import uwu.openjfx.model.PlayerControl;

import java.util.EnumSet;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.spawn;

public class MainApp extends GameApplication {

    private Entity player;

    public enum EntityType {
        PLAYER, COIN
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(960);
        settings.setHeight(640);
        settings.setTitle("Royal Demons");
        settings.setVersion("0.1");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
    }

    @Override
    protected void initInput() {
//        FXGL.onKeyDown(KeyCode.D, () -> {
//            FXGL.getNotificationService().pushNotification("Hello World!");
//        });

        FXGL.onKeyDown(KeyCode.F, () -> {
            FXGL.play("drop.wav");
        });

//        FXGL.onKey(KeyCode.D, () -> {
//            player.translateX(5); // move right 5 pixels
//            FXGL.inc("pixelsMoved", +5);
//        });
//
//        FXGL.onKey(KeyCode.A, () -> {
//            player.translateX(-5); // move left 5 pixels
//            FXGL.inc("pixelsMoved", -5);
//        });

        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).left();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).stop();
            }

        }, KeyCode.A, VirtualButton.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).stop();
            }
        }, KeyCode.D, VirtualButton.RIGHT);

        getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).up();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).stop();
            }

        }, KeyCode.W, VirtualButton.UP);

        getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).down();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).stop();
            }
        }, KeyCode.S, VirtualButton.DOWN);

    }

    @Override
    protected void initGame() {
        FXGL.getPhysicsWorld().setGravity(0, 0);
        FXGL.getGameWorld().addEntityFactory(new SimpleFactory());
        FXGL.setLevelFromMap("tmx/initialRoom.tmx");
        player = spawn("player", 50, 50);

//        FXGL.entityBuilder()
//                .type(EntityType.COIN)
//                .at(500, 200)
//                .viewWithBBox(new Circle(15, 15, 15, Color.YELLOW))
//                .with(new CollidableComponent(true))
//                .buildAndAttach();
//        FXGL.run(() -> {
//            FXGL.spawn("enemy", FXGLMath.randomPoint(
//                    new Rectangle2D(0, 0, FXGL.getAppWidth(), FXGL.getAppHeight()))
//            );
//            FXGL.spawn("ally", FXGLMath.randomPoint(
//                    new Rectangle2D(0, 0, FXGL.getAppWidth(), FXGL.getAppHeight()))
//            );
//        }, Duration.seconds(1));

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                coin.removeFromWorld();
            }
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

    public static void main(String[] args) {
        launch(args);
    }
}
