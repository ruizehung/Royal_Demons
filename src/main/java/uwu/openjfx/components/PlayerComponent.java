package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.Timer;

import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGL.text;

public class PlayerComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk, animAutoAttack, animSwordUltimate1, animSwordUltimate2;

    private Entity meleeSword1; // Player's sword

    private boolean attacking = false; // Player has initiated attack charge/channel
    private boolean startAttacking = false; // Player does the actual attack
    private boolean ultimateActivated = false; // Player is using ultimate
    private boolean ultimateCD = false; // When Player can activate Ultimate again
    private int attackDuration = 500; // Milliseconds
    private int ultimateChargeDuration = 1000; // Milliseconds

    //TODO: remove temp vars and put in char state class
    public static String playerName;
    public static String playerWeapon;
    public static String gameDifficulty;
    public static int gold;
    private boolean removeThisLaterPlease = false;

    public PlayerComponent() {
        animIdle = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(0.5), 0, 3);
        animWalk = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(0.5), 4, 7);
        animAutoAttack = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(attackDuration / 1000), 8, 8);
        animSwordUltimate1 = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(ultimateChargeDuration / 1000), 7, 7);
        animSwordUltimate2 = new AnimationChannel(FXGL.image("lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(attackDuration / 1000), 8, 8);

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(20, 25));
        entity.getViewComponent().addChild(texture);
        // meleeSword1 = spawn("meleeSword1", getEntity().getX(), getEntity().getY() + 50);
    }

    @Override
    public void onUpdate(double tpf) {
        // region REMOVE THIS LATER
        if (!removeThisLaterPlease) {
            meleeSword1 = spawn("meleeSword1", getEntity().getX(), getEntity().getY() + 50);
            removeThisLaterPlease = true;
        }
        // endregion
        updateSwordPosition();
        // region Movement
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
        //endregion

        //region Attacking
        if (startAttacking) {
            if (ultimateActivated) {
                texture.playAnimationChannel(animSwordUltimate2);
                final Entity meleeUltimateHitBox = spawn("meleeUltimateHitBox",
                        getEntity().getX() -  67.5, getEntity().getY() - 60.0);
                FXGL.getGameTimer().runAtInterval(() -> {
                    meleeUltimateHitBox.removeFromWorld();
                }, Duration.seconds(.01));
                ultimateCD = true;
            } else {
                final Entity meleeSword1HitBox = spawn("meleeSword1HitBox", getEntity().getScaleX() > 0 ?
                        getEntity().getX(): getEntity().getX() - 40, getEntity().getY() - 15);
                FXGL.getGameTimer().runAtInterval(() -> {
                    meleeSword1HitBox.removeFromWorld();
                }, Duration.seconds(.01));
            }
            startAttacking = false;
            attacking = false;
            ultimateActivated = false;
        }
        // Player ultimate is on cooldown from activation recently
        if (ultimateCD) {
            FXGL.getGameTimer().runAtInterval(() -> {
                ultimateCD = false;
                FXGL.getGameTimer().clear();
            }, Duration.seconds(2));
        }
        // endregion
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

    private void updateSwordPosition() {
        if (getEntity().getScaleX() > 0) {
            meleeSword1.setPosition(getEntity().getX() - 32, getEntity().getY() + 61);
            meleeSword1.setRotation(-100);
        } else {
            meleeSword1.setPosition(getEntity().getX() + 75, getEntity().getY() + 41);
            meleeSword1.setRotation(100);
        }
    }

    public void autoAttack() {
        // Perform autoattack based on weapon (SWORD, BOW, WAND)
        attacking = true;
        stop();
        texture.playAnimationChannel(animAutoAttack);
        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        startAttacking = true;
                        t.cancel();
                    }
                }, attackDuration
        );
    }

    public void ultimateAttack() {
        // Perform ultimate based on weapon (SWORD, BOW, WAND)
        if (!ultimateCD) {
            ultimateActivated = true;
            attacking = true;
            stop();
            texture.playAnimationChannel(animSwordUltimate1);
            Timer t = new java.util.Timer();
            t.schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            startAttacking = true;
                            t.cancel();
                        }
                    }, ultimateChargeDuration
            );
        }
    }

    public boolean isAttacking() {
        return attacking;
    }
}
