package uwu.openjfx.components;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import uwu.openjfx.MainApp;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;

import java.util.Timer;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for the following:
    - Enemy moves in the direction of player so long as player is within certain radius
    - Enemy attacks player when player is within certain radius
    - Enemy has an animation to go with whatever attack it's performing.
 */
public class EnemyComponent extends CreatureComponent {
    private PhysicsComponent physics;

    /*
        Player Numbers
     */
    private double playerX; // the center of the player X
    private double playerY; // the center of the player Y

    /*
        Abstract Enemy
     */
    private final String type;
    private final String fighterClass; // melee/ranged/magic
    private boolean massEffect = true; // if enemy has ability to reach equilibrium against player
    private double blockProbability; // chance of blocking
    private double armorStat; // how much damage to soak

    /*
        Every Enemy Aspects
     */
    private int kiteTimer;
    private final String assetName;
    private final int width;
    private final int height;
    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private AnimationChannel animWalk;
    private AnimationChannel animMeleeAttack;
    private double enemyX; // the center of the enemy X
    private double enemyY; // the center of the enemy Y
    private boolean playerLeavesRadius = false; // if player has stopped interacting with enemy
    private boolean collidingWithPlayer = false;
    private final int attackDuration = 900; // how long in MS the actual attack/animation takes
    private final double velocityDecrementer = 10; // how much velocity will be deducted on update
    private double speed; // movement speed of the enemy
    private double dist;
    private boolean kiteBack;
    private boolean kiteCircular;
    private boolean clockwise;
    private boolean kiting = false;
    private boolean overDrive = false; // if enemy needs to slow to a stop if involuntarily moving
    private boolean isStunned = false;
    private boolean attackCD = false; // when the enemy can attack again
    private boolean prepAttack = false; // enemy begins attack charge
    private boolean startAttacking = false; // enemy does the attack animation / instantiate hitbox
    private boolean startShrink = false; // for growing enemies
    private double scaler = 1.0; // to keep track of scale when shrinking/enlarging
    private LocalTimer moveTimer; // time to check player's location

    public EnemyComponent(int maxHP, String assetName, int width, int height, int frames,
                          String type, String fighterClass) {
        super(maxHP, maxHP);

        this.assetName = assetName;
        this.width = width;
        this.height = height;
        this.type = type;
        this.fighterClass = fighterClass;

        if (!MainApp.isIsTesting()) {
            animIdle = new AnimationChannel(FXGL.image(assetName), frames,
                width, height, Duration.seconds(0.5), 0, frames / 2 - 1);
            animWalk = new AnimationChannel(FXGL.image(assetName), frames,
                width, height, Duration.seconds(0.5), frames / 2, frames - 1);
            animMeleeAttack = new AnimationChannel(FXGL.image(assetName), frames,
                width, height, Duration.seconds((double) attackDuration / 1000),
                frames / 2, frames / 2);

            texture = new AnimatedTexture(animIdle);
            texture.loop();
        }
    }

    public EnemyComponent(int healthPoints, String assetName, int width, int height) {
        this(healthPoints, assetName, width, height, 8, "small", "melee");
    }

    @Override
    public void onAdded() {
        // region Define Enemy Variables based on type
        switch (type) {
        case "small":
            massEffect = false;
            blockProbability = 10;
            armorStat = 1;
            break;
        case "large":
            massEffect = true;
            blockProbability = 20;
            armorStat = 2.5;
            break;
        case "miniboss":
            massEffect = true;
            blockProbability = 25;
            armorStat = 4.5;
            break;
        case "finalboss":
            massEffect = true;
            blockProbability = 30;
            armorStat = 5.5;
            break;
        default:
        }
        speed = fighterClass.equals("melee") ? 70 : 120;
        // endregion

        if (!MainApp.isIsTesting()) {
            entity.getTransformComponent().setScaleOrigin(
                new Point2D(width / 2.0, height / 2.0));
            entity.getViewComponent().addChild(texture);

            moveTimer = FXGL.newLocalTimer();
            moveTimer.capture();
        } else {
            entity.setProperty("isDead", false);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        /*
        Player Numbers
        */
        final double playerHitBoxOffsetX = 3; // player's hitbox own offset from top left
        final double playerHitBoxOffsetY = 15; // player's hitbox own offset from top left
        final double playerHitBoxWidth = 35; // width of player's hitbox from 3 to 38
        final double playerHitBoxHeight = 40; // height of player's hitbox from 15 to 55

        // region Calculate Player Center and Enemy Center
        Entity player = FXGL.geto("player");
        playerX = player.getX() + playerHitBoxOffsetX + (playerHitBoxWidth / 2);
        playerY = player.getY() + playerHitBoxOffsetY + (playerHitBoxHeight / 2);
        enemyX = entity.getX() + ((double) width / 2);
        enemyY = entity.getY() + ((double) height / 2);
        // endregion

        // region Player to Enemy Distance Relationship
        double xPythag = Math.abs(playerX - enemyX);
        double yPythag = Math.abs(playerY - enemyY);
        dist = Math.sqrt(xPythag * xPythag + yPythag * yPythag);
        if (dist > 150) {
            playerLeavesRadius = true;
        }
        moveToPlayer();
        if (kiting) {
            kitePlayer();
        }
        if (isStunned) {
            normalizeVelocityX();
            normalizeVelocityY();
            Runnable runnable = () -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isStunned = false;
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }

        if (prepAttack && (physics.getVelocityX() != 0 || physics.getVelocityY() != 0)) {
            normalizeVelocityX();
            normalizeVelocityY();
        }
        // endregion

        // region Overdrive
        // if enemy has been pushed by player to a velocity greater than its limit
        if (Math.abs(physics.getVelocityX()) - Math.abs(speed) > 5.0
            || Math.abs(physics.getVelocityY()) - Math.abs(speed) > 5.0) {
            overDrive = true;
        }

        // if enemy is in overdrive
        if (overDrive) {
            normalizeVelocityX();
            normalizeVelocityY();
        }
        // endregion

        // region Enemy Attack
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
            if (fighterClass.equals("melee")) {
                int widthBox = width * 2;
                int heightBox = height * 2;
                int sideOffset = widthBox / 2;
                Entity meleePunchHitBox = spawn("meleeEnemyPunch",
                    new SpawnData(enemyX, enemyY).
                        put("widthBox", widthBox).
                        put("heightBox", heightBox));
                // Spawn hitbox on top of enemy and apply offset
                meleePunchHitBox.getTransformComponent().setAnchoredPosition(
                    new Point2D(
                        enemyX - ((double) widthBox / 2)
                            + (getEntity().getScaleX() > 0 ? sideOffset : -sideOffset),
                        enemyY - ((double) heightBox / 2)));
                MainApp.addToHitBoxDestruction(meleePunchHitBox);
            } else {
                magicAutoAttack();
            }
            startShrink = true;
            startAttacking = false;
            prepAttack = false;
            attackCD = true;
            Runnable runnable = () -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                attackCD = false;
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }

        // Enemy shrinks
        if (startShrink) {
            shrink();
        }
        // endregion
    }

    // region Movement
    private void moveToPlayer() {
        if (moveTimer.elapsed(Duration.seconds(1))) {
            if (!isStunned) {
                if (fighterClass.equals("melee")) {
                    if (dist < 120 && !attackCD) {
                        playerLeavesRadius = false;
                        autoAttack();
                    } else if (dist < 300) {
                        if (!prepAttack) {
                            double xDir = playerX - enemyX > 0 ? 1 : -1;
                            double yDir = playerY - enemyY > 0 ? 1 : -1;
                            physics.setVelocityX(speed * xDir);
                            physics.setVelocityY(speed * yDir);
                            entity.setScaleX(xDir);
                        }
                    } else {
                        stop();
                    }
                } else {
                    if (dist < 400) {
                        if (!attackCD) {
                            entity.setScaleX(playerX - enemyX > 0 ? 1 : -1);
                            autoAttack();
                        }
                        if (!prepAttack && !kiting) {
                            kiting = true;
                        }
                    } else if (dist < 500) {
                        if (!prepAttack) {
                            double xDir = playerX - enemyX > 0 ? 1 : -1;
                            double yDir = playerY - enemyY > 0 ? 1 : -1;
                            physics.setVelocityX(speed * xDir);
                            physics.setVelocityY(speed * yDir);
                            entity.setScaleX(xDir);
                        }
                    } else {
                        stop();
                    }
                }
            }
            moveTimer.capture();
        }
    }

    private void stop() {
        if (!collidingWithPlayer) {
            physics.setVelocityX(0);
            physics.setVelocityY(0);
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
        if (physics != null && !type.equals("finalboss")) {
            isStunned = true;
            double knockBackPower = 400;
            double adjacent = (getEntity().getX() + ((double) width) / 2) - playerX;
            double opposite = (getEntity().getY() + ((double) height) / 2) - playerY;
            double angle = Math.atan2(opposite, adjacent);
            angle = Math.toDegrees(angle);
            Vec2 dir = Vec2.fromAngle(angle);
            double xPow = dir.toPoint2D().getX() * knockBackPower;
            double yPow = dir.toPoint2D().getY() * knockBackPower;
            physics.setLinearVelocity(new Point2D(xPow, yPow));
        }
    }

    public void turnSpriteRed() {
        if (texture != null) {
            texture.set(texture.multiplyColor(Color.RED));
        }
    }

    private void kitePlayer() {
        if (physics != null && !prepAttack) {
            if (!kiteBack && !kiteCircular) {
                int random = (int) (Math.random() * 101);
                kiteBack = random < 35;
                kiteCircular = random >= 35;
                if ((physics.getVelocityX() == 0 && physics.getVelocityY() == 0) && !prepAttack) {
                    kiteTimer++;
                    if (kiteTimer >= 60) {
                        if (kiteCircular) {
                            clockwise = !clockwise;
                        }
                        kiteBack = false;
                        kiteCircular = true;
                        kiteTimer = 0;
                    }
                }
            }
            double adjacent = (getEntity().getX()
                + ((double) width) / 2) - playerX;
            double opposite = (getEntity().getY()
                + ((double) height) / 2) - playerY;
            double radians = (Math.atan2(opposite, adjacent));
            if (kiteCircular) {
                radians = radians + (clockwise ? Math.PI / 2 : -(Math.PI / 2));
            }
            Vec2 dir = Vec2.fromAngle(Math.toDegrees(radians));
            double xPow = dir.toPoint2D().getX() * speed;
            double yPow = dir.toPoint2D().getY() * speed;
            physics.setLinearVelocity(xPow, yPow);
            FXGL.getGameTimer().runAtInterval(() -> {
                kiteBack = false;
                kiteCircular = false;
            }, Duration.seconds(1));
            if (dist >= 300) {
                kiting = false;
                stop();
            }
        }
    }
    // endregion

    // region Attack Stats
    public double getBlockProbability() {
        return blockProbability;
    }

    public void setBlockProbability(double blockProbability) {
        this.blockProbability = blockProbability;
    }

    public double getArmorStat() {
        return armorStat;
    }

    public void setArmorStat(double armorStat) {
        this.armorStat = armorStat;
    }
    // endregion

    // region Attack
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

    private void magicAutoAttack() {
        // adjacent is distance (x) from mouse to player's "hands"
        // opposite is distance (y) from mouse to player's "hands"
        // angle calculated with tangent
        double adjacent = playerX - enemyX;
        double opposite = playerY - enemyY;
        double angle = Math.atan2(opposite, adjacent);
        angle = Math.toDegrees(angle);
        Vec2 dir = Vec2.fromAngle(angle);

        // top offset used to shrink the top/bot edges of hitbox
        int topBottomOffset = 20;
        // left offset used to shrink the left edge of hitbox
        int leftOffset = 30;
        // right offset used to shrink the right edge of hitbox
        int rightOffset = 20;
        // width of the original frame (64 / 32)
        int frameWidth = 64;
        // height of original frame (64 / 32)
        int frameHeight = 64;

        // the center of the NEW and MODIFIED hitbox
        double centerX = ((double) (leftOffset + (frameWidth - rightOffset)) / 2);
        double centerY = ((double) (topBottomOffset + (frameHeight - topBottomOffset)) / 2);

        int speed = 300; // speed at which magic spell goes

        /*
            Instantiate a brand new magic spell that will hold the
            corresponding dimensions, components, and speed. It will temporarily
            spawn the magic spell at the players ORIGINAL getX() and getY() excluding
            its modified hitbox done in CreatureFactory.
         */

        Entity rangedHitBox = spawn("rangedMagicHitBox",
            new SpawnData(
                enemyX, enemyY).
                put("dir", dir.toPoint2D()).
                put("speed", speed).
                put("weapon", "fireball_64x64").
                put("duration", 500).
                put("fpr", 60).
                put("ultimateActive", true).
                put("topBotOffset", topBottomOffset).
                put("leftOffset", leftOffset).
                put("rightOffset", rightOffset).
                put("frameWidth", frameWidth).
                put("frameHeight", frameHeight).
                put("isArrow", false).
                put("isMagic", true).
                put("damage", 50.0).
                put("royalType", RoyalType.ENEMYATTACK));
        /*
            setLocalAnchor(...) will ensure that the anchor/pivot point of the
            magic spell is located at the CENTER of the NEW hitbox.
            setAnchoredPosition(...) will spawn the magic spell to the right
            of the player if player is facing right, and left if the player is
            facing left, and located at the player's "hands".
            setRotationOrigin(...) will ensure that the rotation anchor/pivot
            point of the magic spell is located at the CENTER of the NEW hitbox.
            The arguments are offsets based off of the top-left point of the
            ORIGINAL frameWidth x frameHeight frame. Therefore, we need to offset
            centerX in the x-direction, and the center of the magic spell will
            CONSISTENTLY be at its midpoint in the y-direction.
         */
        rangedHitBox.setLocalAnchor(new Point2D(centerX, centerY));
        rangedHitBox.setAnchoredPosition(enemyX, enemyY);
        rangedHitBox.getTransformComponent().setRotationOrigin(
            new Point2D(centerX, ((double) (frameHeight)) / 2));
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
    // endregion

    // region Enemy Misc.
    @Override
    public void die() {
        super.die();
        if (!MainApp.isIsTesting()) {
            getEntity().removeFromWorld();
            IDComponent idComponent = getEntity().getComponent(IDComponent.class);
            Room curRoom = FXGL.geto("curRoom");
            curRoom.setEntityData(idComponent.getId(), "isAlive", 0);

            if (curRoom.getRoomType().equals("challengeRoom") && curRoom.enemiesCleared()) {
                Entity tempChest = FXGL.spawn("chest", 480, 387);
                tempChest.addComponent(new IDComponent("", 999));
            }
        }
    }
    // endregion
}