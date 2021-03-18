package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class PlayerComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk;

    public PlayerComponent() {
        animIdle = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9, 40, 55, Duration.seconds(0.5), 0, 3);
        animWalk = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9, 40, 55, Duration.seconds(0.5), 4, 7);

        texture = new AnimatedTexture(animIdle);

        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(20, 25));
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        if (physics.isMoving()) {
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
        }
    }

    public void left() {
        getEntity().setScaleX(-1);
        physics.setVelocityX(-170);
    }

    public void right() {
        getEntity().setScaleX(1);
        physics.setVelocityX(170);
    }

    public void up() {
        physics.setVelocityY(-170);
    }

    public void down() {
        physics.setVelocityY(170);
    }

    public void stop() {
        physics.setVelocityX(0);
        physics.setVelocityY(0);
    }
}
