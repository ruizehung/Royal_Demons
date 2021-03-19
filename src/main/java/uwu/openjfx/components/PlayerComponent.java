package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.Timer;

public class PlayerComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk, animAutoAttack;

    private boolean attacking = false;
    private int attackDuration = 500; // Milliseconds

    public PlayerComponent() {
        animIdle = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(0.5), 0, 3);
        animWalk = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(0.5), 4, 7);
        animAutoAttack = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(attackDuration / 1000), 8, 8);

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
        if (!attacking) {
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
    }

    public void left() {
        if (!attacking) {
            getEntity().setScaleX(-1);
            physics.setVelocityX(-170);
        }
    }

    public void right() {
        if (!attacking) {
            getEntity().setScaleX(1);
            physics.setVelocityX(170);
        }
    }

    public void up() {
        if (!attacking) {
            physics.setVelocityY(-170);
        }
    }

    public void down() {
        if (!attacking) {
            physics.setVelocityY(170);
        }
    }

    public void stop() {
        physics.setVelocityX(0);
        physics.setVelocityY(0);
    }

    public void autoAttack() {
        attacking = true;
        stop();
        texture.playAnimationChannel(animAutoAttack);
        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        attacking = false;
                        t.cancel();
                    }
                }, attackDuration
        );
    }

    public boolean isAttacking() {
        return attacking;
    }
}
