package uwu.openjfx.components;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.Timer;

import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.spawn;

public class PlayerComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk, animAutoAttack, animSwordUltimate1, animSwordUltimate2;

    private Entity meleeSword1; // Player's sword

    private double currMouseX;
    private double currMouseY;

    private boolean attacking = false; // Player has initiated attack charge/channel
    private boolean startAttacking = false; // Player does the actual attack
    private boolean ultimateActivated = false; // Player is using ultimate
    private boolean ultimateCD = false; // When Player can activate Ultimate again
    private int attackDuration = 500; // Milliseconds (500 is default for melee)
    private int ultimateChargeDuration = 1000; // Milliseconds

    //TODO: remove temp vars and put in char state class
    public static String playerName;
    public static String playerWeapon;
    public static String gameDifficulty;
    public static int gold;
    private boolean removeThisLaterPlease = false;

    public PlayerComponent() {
        animIdle = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(0.5), 0, 3);
        animWalk = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(0.5), 4, 7);
        animAutoAttack = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(attackDuration / 1000), 8, 8);
        animSwordUltimate1 = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(ultimateChargeDuration / 1000), 7, 7);
        animSwordUltimate2 = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
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
                if (playerWeapon.equals("Sword")) {
                    texture.playAnimationChannel(animSwordUltimate2);
                    final Entity meleeUltimateHitBox = spawn("meleeUltimateHitBox",
                            getEntity().getX() -  67.5, getEntity().getY() - 60.0);
                    FXGL.getGameTimer().runAtInterval(() -> {
                        meleeUltimateHitBox.removeFromWorld();
                    }, Duration.seconds(.01));
                } else if (playerWeapon.equals("Bow")) {
                    double opposite = currMouseY - (entity.getY() + 27.5);
                    double adjacent = currMouseX - (entity.getScaleX() > 0 ?
                            entity.getX() + 20.0 : entity.getX() - 15.0);
                    double angle = Math.atan2(opposite, adjacent);
                    angle = Math.toDegrees(angle);
                    Vec2 dir = Vec2.fromAngle(angle);
                    final Entity rangedUltimateHitBox = spawn("rangedUltimateHitBox",
                            new SpawnData(
                                    entity.getScaleX() > 0 ? entity.getX() + 20.0 : entity.getX() - 15.0,
                                    entity.getY() + 27.5).put("dir", dir.toPoint2D()));
                    rangedUltimateHitBox.setScaleX(2);
                    rangedUltimateHitBox.setScaleY(2);
                } else if (playerWeapon.equals("Wand")) {

                }
                ultimateCD = true;
            } else {
                if (playerWeapon.equals("Sword")) {
                    final Entity meleeSword1HitBox = spawn("meleeSword1HitBox", getEntity().getScaleX() > 0 ?
                            getEntity().getX() : getEntity().getX() - 40, getEntity().getY() - 15);
                    FXGL.getGameTimer().runAtInterval(() -> {
                        meleeSword1HitBox.removeFromWorld();
                    }, Duration.seconds(.01));
                } else if (playerWeapon.equals("Bow")) {
                    double opposite = currMouseY - (entity.getY() + 27.5);
                    double adjacent = currMouseX - (entity.getScaleX() > 0 ?
                            entity.getX() + 20.0 : entity.getX() - 15.0);
                    double angle = Math.atan2(opposite, adjacent);
                    angle = Math.toDegrees(angle);
                    Vec2 dir = Vec2.fromAngle(angle);
                    final Entity rangedArrow1HitBox = spawn("rangedArrow1HitBox",
                            new SpawnData(
                                    entity.getScaleX() > 0 ? entity.getX() + 20.0 : entity.getX() - 15.0,
                                    entity.getY() + 27.5).put("dir", dir.toPoint2D()));
                } else if (playerWeapon.equals("Wand")) {

                }
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

    // region Player Movement
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
    // endregion

    private void updateSwordPosition() {
        if (getEntity().getScaleX() > 0) {
            meleeSword1.setPosition(getEntity().getX() - 25, getEntity().getY() + 25);
            meleeSword1.setRotation(-100);
        } else {
            meleeSword1.setPosition(getEntity().getX() + 45, getEntity().getY() + 25);
            meleeSword1.setRotation(100);
        }
    }

    public void autoAttack() {
        // Perform autoattack based on weapon (SWORD, BOW, WAND)
        if (currMouseX > entity.getX() + 20) {
            entity.setScaleX(1);
        } else {
            entity.setScaleX(-1);
        }
        attacking = true;
        stop();
        switch (playerWeapon) {
            case "Sword":
                texture.playAnimationChannel(animAutoAttack);
                attackDuration = 500;
                break;
            case "Bow":
                texture.playAnimationChannel(animAutoAttack);
                attackDuration = 800;
                break;
            case "Wand":
                texture.playAnimationChannel(animAutoAttack);
                attackDuration = 800;
                break;
            default:
        }
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
        if (currMouseX > entity.getX() + 20) {
            entity.setScaleX(1);
        } else {
            entity.setScaleX(-1);
        }
        if (!ultimateCD) {
            ultimateActivated = true;
            attacking = true;
            stop();
            switch (playerWeapon) {
                case "Sword":
                    texture.playAnimationChannel(animSwordUltimate1);
                    ultimateChargeDuration = 1000;
                    break;
                case "Bow":
                    texture.playAnimationChannel(animSwordUltimate1);
                    ultimateChargeDuration = 1000;
                    break;
                case "Wand":
                    texture.playAnimationChannel(animSwordUltimate1);
                    ultimateChargeDuration = 1200;
                    break;
                default:
            }
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

    public void setMousePosition(double mouseXPos, double mouseYPos) {
        currMouseX = mouseXPos;
        currMouseY = mouseYPos;
    }
}
