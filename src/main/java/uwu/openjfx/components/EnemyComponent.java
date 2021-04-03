package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uwu.openjfx.MainApp;
import uwu.openjfx.MapGeneration.Room;

import java.util.ArrayList;
import java.util.List;
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

    private final double playerHitBoxOffsetX = 3; // player's hitbox own offset from top left
    private final double playerHitBoxOffsetY = 15; // player's hitbox own offset from top left
    private final double playerHitBoxWidth = 35; // width of player's hitbox from 3 to 38
    private final double playerHitBoxHeight = 40; // height of player's hitbox from 15 to 55

    private double playerX;
    private double playerY;

    private double enemyX;
    private double enemyY;

    private AnimationChannel animIdle;
    private AnimationChannel animWalk;
    private AnimationChannel animMeleeAttack;

    private boolean massEffect = true;
    private boolean isStunned = false;
    private boolean playerLeavesRadius = false;
    private boolean collidingWithPlayer = false;
    private boolean prepAttack = false; // enemy begins attack charge
    private boolean startAttacking = false; // enemy does the attack animation / instantiate hitbox
    private boolean startShrink = false; // for growing enemies
    private boolean attackCD = false;
    private int attackDuration = 900;
    private boolean overDrive = false;
    private double velocityDecrementer = 10;
    private double speed = 70;

    private double scaler = 1.0;

    private LocalTimer moveTimer;
    private List<String> itemsDropList = new ArrayList<>();

    public EnemyComponent(int healthPoints, String assetName, int width, int height, int frames) {
        super(healthPoints);
        this.type = assetName;
        this.width = width;
        this.height = height;

        if (!MainApp.isIsTesting()) {
            animIdle = new AnimationChannel(FXGL.image(assetName), frames,
                    width, height, Duration.seconds(0.5), 0, frames / 2 - 1);
            animWalk = new AnimationChannel(FXGL.image(assetName), frames,
                    width, height, Duration.seconds(0.5), frames / 2, frames - 1);
            animMeleeAttack = new AnimationChannel(FXGL.image(assetName), frames,
                    width, height, Duration.seconds(attackDuration / 1000), frames / 2, frames / 2);

            texture = new AnimatedTexture(animIdle);

            texture.loop();
        }
    }

    public EnemyComponent(int healthPoints, String assetName, int width, int height) {
        this(healthPoints, assetName, width, height, 8);
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
        playerX = player.getX() + playerHitBoxOffsetX + (playerHitBoxWidth / 2);
        playerY = player.getY() + playerHitBoxOffsetY + (playerHitBoxHeight / 2);
        enemyX = entity.getX() + (width / 2);
        enemyY = entity.getY() + (height / 2);

        double dist = Math.max(
                        Math.abs(playerX - enemyX),
                        Math.abs(playerY - enemyY));
        if (dist > 150) {
            playerLeavesRadius = true;
        }

        if (moveTimer.elapsed(Duration.seconds(1))) {
            if (!isStunned) {
                if (dist < 100 && !attackCD) {
                    playerLeavesRadius = false;
                    autoAttack();
                } else if (dist < 300 && !prepAttack) {
                    moveToPlayer();
                } else {
                    stop();
                }
            }
            moveTimer.capture();
        }

        // if enemy has been pushed by player to a velocity greater than its limit
        if (Math.abs(physics.getVelocityX()) > speed || Math.abs(physics.getVelocityY()) > speed) {
            overDrive = true;
        }

        // if enemy is in overdrive
        if (overDrive) {
            normalizeVelocityX();
            normalizeVelocityY();
        }

        // if enemy is not charging up
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
            normalizeVelocityX();
            normalizeVelocityY();
            // Grow in size
            enlarge();
        }

        // Enemy does the actual attack, spawns hitbox and then shrinks
        if (startAttacking) {
            final Entity meleeHitBox = spawn("meleeEnemyAttack", getEntity().getScaleX() > 0
                    ? enemyX
                    : enemyX - 40, enemyY - 5);
            FXGL.getGameTimer().runAtInterval(meleeHitBox::removeFromWorld, Duration.seconds(.01));
            startShrink = true;
            startAttacking = false;
            prepAttack = false;
            attackCD = true;
        }

        // Enemy shrinks
        if (startShrink) {
            shrink();
        }

        // Enemy is on cooldown from attacking recently
        if (attackCD) {
            FXGL.getGameTimer().runAtInterval(() -> {
                attackCD = false;
                FXGL.getGameTimer().clear();
            }, Duration.seconds(2));
        }

        if (isStunned) {
            normalizeVelocityX();
            normalizeVelocityY();
            FXGL.getGameTimer().runAtInterval(() -> {
                isStunned = false;
                FXGL.getGameTimer().clear();
            }, Duration.seconds(.5));
        }
    }

    private void normalizeVelocityX() {
        if (physics.getVelocityX() != 0) {
            if (physics.getVelocityX() > 0) {
                physics.setVelocityX(physics.getVelocityX() - velocityDecrementer);
                if (physics.getVelocityX() < 0) {
                    physics.setVelocityX(0);
                    overDrive = false;
                }
            } else if (physics.getVelocityX() < 0) {
                physics.setVelocityX(physics.getVelocityX() + velocityDecrementer);
                if (physics.getVelocityX() > 0) {
                    physics.setVelocityX(0);
                    overDrive = false;
                }
            }
        }
    }

    private void normalizeVelocityY() {
        if (physics.getVelocityY() != 0) {
            if (physics.getVelocityY() > 0) {
                physics.setVelocityY(physics.getVelocityY() - velocityDecrementer);
                if (physics.getVelocityY() < 0) {
                    physics.setVelocityY(0);
                    overDrive = false;
                }
            } else if (physics.getVelocityY() < 0) {
                physics.setVelocityY(physics.getVelocityY() + velocityDecrementer);
                if (physics.getVelocityY() > 0) {
                    physics.setVelocityY(0);
                    overDrive = false;
                }
            }
        }
    }

    public boolean getPlayerLeavesRadius() {
        return playerLeavesRadius;
    }

    public void setCollidingWithPlayer(boolean collidingWithPlayer) {
        this.collidingWithPlayer = collidingWithPlayer;
    }

    public boolean getMassEffect() {
        return massEffect;
    }

    public void knockBackFromPlayer() {
        if (physics != null) {
            double knockBackPower = 4.5;
            isStunned = true;
            double xDir = playerX - enemyX > 0 ? -1 : 1;
            double yDir = playerY - enemyY > 0 ? -1 : 1;
            physics.setVelocityX(speed * knockBackPower * xDir);
            physics.setVelocityY(speed * knockBackPower * yDir);
        }
    }

    private void moveToPlayer() {
        double xDir = playerX - enemyX > 0 ? 1 : -1;
        double yDir = playerY - enemyY > 0 ? 1 : -1;
        physics.setVelocityX(speed * xDir);
        physics.setVelocityY(speed * yDir);
        entity.setScaleX(xDir);
    }

    private void enlarge() {
        entity.setScaleX(scaler * Math.signum(entity.getScaleX()));
        entity.setScaleY(scaler * Math.signum(entity.getScaleY()));
        if (scaler < 1.2) {
            scaler += 0.005;
        }
    }

    private void shrink() {
        entity.setScaleX(scaler * Math.signum(entity.getScaleX()));
        entity.setScaleY(scaler * Math.signum(entity.getScaleY()));
        if (scaler > 1) {
            scaler -= 0.01;
        } else {
            scaler = 1;
            startShrink = false;
        }
    }

    private void stop() {
        if (!collidingWithPlayer) {
            physics.setVelocityX(0);
            physics.setVelocityY(0);
        }
    }

    private void autoAttack() {
        prepAttack = true;
        stop();
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

    public void addDroppedItem(String itemName) {
        itemsDropList.add(itemName);
    }

    @Override
    public void die() {
        if (MainApp.isIsTesting()) {
            entity.setProperty("isDead", true);
            return;
        }

        if (FXGL.random() < 0.5) {
            FXGL.spawn("coin", getEntity().getX() + width / 2, getEntity().getY() + height / 2);
        }

        if (itemsDropList.size() > 0) {
            for (String itemName : itemsDropList) {
                spawn("itemOnFloor",
                        new SpawnData(getEntity().getX() + width / 2, getEntity().getY() + height / 2)
                                        .put("name", itemName).put("isWeapon", false));
            }
        }

        getEntity().removeFromWorld();
        IDComponent idComponent = getEntity().getComponent(IDComponent.class);
        Room curRoom = FXGL.geto("curRoom");
        curRoom.setEntityData(idComponent.getId(), "isAlive", 0);
    }
}