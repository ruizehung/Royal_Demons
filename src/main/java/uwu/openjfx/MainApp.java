package uwu.openjfx;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.EnumSet;
import java.util.Map;

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

        FXGL.onKey(KeyCode.W, () -> {
            player.translateY(-5); // move up 5 pixels
            FXGL.inc("pixelsMoved", +5);
        });

        FXGL.onKey(KeyCode.S, () -> {
            player.translateY(5); // move down 5 pixels
            FXGL.inc("pixelsMoved", +5);
        });

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(AnimationComponent.class).moveRight();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(AnimationComponent.class).moveLeft();
            }
        }, KeyCode.A);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new SimpleFactory());

        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(300, 300)
                .with(new AnimationComponent())
                .viewWithBBox(new Rectangle(30, 30, Color.TRANSPARENT))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        FXGL.entityBuilder()
                .type(EntityType.COIN)
                .at(500, 200)
                .viewWithBBox(new Circle(15, 15, 15, Color.YELLOW))
                .with(new CollidableComponent(true))
                .buildAndAttach();
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
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

        textPixels.textProperty().bind(FXGL.getWorldProperties().intProperty("pixelsMoved").asString());

        FXGL.getGameScene().addUINode(textPixels); // add to the scene graph

        var skeletTexture = FXGL.getAssetLoader().loadTexture("skelet_idle_anim_f0_32x32.png");
        skeletTexture.setTranslateX(50);
        skeletTexture.setTranslateY(450);

        FXGL.getGameScene().addUINode(skeletTexture);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
