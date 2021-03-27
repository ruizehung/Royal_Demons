package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uwu.openjfx.MainApp;
import uwu.openjfx.MapGeneration.Room;

import java.util.Timer;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for the following:
    - Enemy moves in the direction of player so long as player is within certain radius
    - Enemy attacks player when player is within certain radius
    - Enemy has an animation to go with whatever attack it's performing.
 */
public class EnemyComponent extends HealthComponent {
    private PhysicsComponent physics;

    private String type;
    private AnimatedTexture texture;
    private int width;
    private int height;

    private AnimationChannel animIdle;
    private AnimationChannel animWalk;
    private AnimationChannel animMeleeAttack;
    private AnimationChannel animLunge1;
    private AnimationChannel animLunge2;

    private boolean prepAttack = false; // enemy begins attack charge
    private boolean startAttacking = false; // enemy does the attack animation / instantiate hitbox
    private boolean shrinkDown = false; // for growing enemies
    private boolean attackCD = false;
    private int attackDuration = 900;

    private double scaler = 1.0;

    private LocalTimer moveTimer;

    public EnemyComponent(int healthPoints, String assetName, int width, int height) {
        super(healthPoints);
        this.type = assetName;
        this.width = width;
        this.height = height;

        if (!MainApp.isIsTesting()) {
            animIdle = new AnimationChannel(FXGL.image(assetName), 8,
                    width, height, Duration.seconds(0.5), 0, 3);
            animWalk = new AnimationChannel(FXGL.image(assetName), 8,
                    width, height, Duration.seconds(0.5), 4, 7);
            animMeleeAttack = new AnimationChannel(FXGL.image(assetName), 8,
                    width, height, Duration.seconds(attackDuration / 1000f), 4, 4);

            texture = new AnimatedTexture(animIdle);

            texture.loop();
        }
    }

    @Override
    public void onAdded() {
        if (!MainApp.isIsTesting()) {
            entity.getTransformComponent().setScaleOrigin(new Point2D(width / 2.0, height / 2.0));
            entity.getViewComponent().addChild(texture);

            moveTimer = FXGL.newLocalTimer();
            moveTimer.capture();
        } else {
            entity.setProperty("isDead", false);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        Entity player = FXGL.geto("player");
        if (moveTimer.elapsed(Duration.seconds(1))) {
            if (getEntity().distance(player) < 100 && !attackCD) {
                autoAttack();
                stop();
            } else if (getEntity().distance(player) < 300 && !prepAttack) {
                // constantly signal other AI that player is clos
                double xDir = (player.getX() + 20) - getEntity().getX() > 0 ? 1 : -1;
                double yDir = (player.getY() + 27.5) - getEntity().getY() > 0 ? 1 : -1;
                physics.setVelocityX(70 * xDir);
                physics.setVelocityY(70 * yDir);
                entity.setScaleX(xDir);
            } else {
                stop();
            }
            moveTimer.capture();
        }

        // If enemy is not charging up
        if (!prepAttack) {
            if (physics.isMoving()) {
                if (texture.getAnimationChannel() != animWalk) {
                    texture.loopAnimationChannel(animWalk);
                }
            } else {
                if (texture.getAnimationChannel() != animIdle) {
                    texture.loopAnimationChannel(animIdle);
                }
            }
        } else {
            // Grow in size
            entity.setScaleX(scaler * Math.signum(entity.getScaleX()));
            entity.setScaleY(scaler * Math.signum(entity.getScaleY()));
            if (scaler < 1.2) {
                scaler += 0.005;
            }
        }
        // Enemy does the actual attack, spawns hitbox and then shrinks
        if (startAttacking) {
            final Entity meleeHitBox = spawn("meleeEnemyAttack", getEntity().getScaleX() > 0
                    ? getEntity().getX()
                    : getEntity().getX() - 40, getEntity().getY() - 5);
            FXGL.getGameTimer().runAtInterval(meleeHitBox::removeFromWorld, Duration.seconds(.01));
            shrinkDown = true;
            startAttacking = false;
            prepAttack = false;
            attackCD = true;
        }

        // Enemy shrinks
        if (shrinkDown) {
            entity.setScaleX(scaler * Math.signum(entity.getScaleX()));
            entity.setScaleY(scaler * Math.signum(entity.getScaleY()));
            if (scaler > 1) {
                scaler -= 0.01;
            } else {
                scaler = 1;
                shrinkDown = false;
            }
        }

        // Enemy is on cooldown from attacking recently
        if (attackCD) {
            FXGL.getGameTimer().runAtInterval(() -> {
                attackCD = false;
                FXGL.getGameTimer().clear();
            }, Duration.seconds(2));
        }
    }

    private void stop() {
        physics.setVelocityX(0);
        physics.setVelocityY(0);
    }

    private void autoAttack() {
        prepAttack = true;
        texture.playAnimationChannel(animMeleeAttack);
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

    @Override
    public void die() {
        if (MainApp.isIsTesting()) {
            entity.setProperty("isDead", true);
            return;
        }

        if (FXGL.random() < 0.5) {
            FXGL.spawn("coin", getEntity().getX(), getEntity().getY());
        }
        getEntity().removeFromWorld();
        IDComponent idComponent = getEntity().getComponent(IDComponent.class);
        Room curRoom = FXGL.geto("curRoom");
        curRoom.setEntityData(idComponent.getId(), "isAlive", 0);
    }
}