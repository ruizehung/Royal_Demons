package uwu.openjfx.components;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
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
    private String fighterClass; // melee/ranged/magic
    private boolean massEffect = true; // if enemy has ability to reach equilibrium against player
    private double blockProbability; // chance of blocking
    private double armorStat; // how much damage to soak
    /*
        Every Enemy Aspects
     */
    private int kiteTimer;
    private String assetName;
    private int width;
    private int height;
    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private AnimationChannel animWalk;
    private AnimationChannel animMeleeAttack;
    private double enemyX; // the center of the enemy X
    private double enemyY; // the center of the enemy Y
    private boolean playerLeavesRadius = false; // if player has stopped interacting with enemy
    private boolean collidingWithPlayer = false;
    private int attackDuration; // how long in MS the actual attack/animation takes
    private final double velocityDecrementer = 10; // how much velocity will be deducted on update
    private double speed; // movement speed of the enemy
    private double dist;
    private boolean kiteBack;
    private boolean kiteCircular;
    private boolean clockwise;
    private boolean kiting = false;
    private boolean overDrive = false; // if enemy needs to slow to a stop if involuntarily moving
    private boolean isStunned = false;
    protected boolean attackCD = false; // when the enemy can attack again
    protected boolean prepAttack = false; // enemy begins attack charge
    protected boolean startAttacking = false; // enemy does the attack animation/instantiate hitbox
    protected boolean startShrink = false; // for growing enemies
    private double scaler = 1.0; // to keep track of scale when shrinking/enlarging
    private LocalTimer moveTimer; // time to check player's location

    /*
        Boss Attributes
     */
    private int attackBreaktime; // how long before can cast ultimate again
    private int hammerUltimateDuration = 2000; // how long smash charge is
    private int hammerChargeCounter = 0; // count up to hammerUltimateDuration then smash
    private boolean isHammerSmashing = false;
    private int novaCounter; // track of how many times nova has been done by chance
    private int ricochetCounter; // track of how many times ricochet has been done by chance

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
        int[] nameOfArray = new int[100];
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
            armorStat = 12.5;
            break;
        default:
        }
        speed = !fighterClass.equals("finalboss") ? 70 : 100;
        attackDuration = fighterClass.equals("melee") ? 900 : 1100;
        if (type.equals("finalboss")) {
            attackBreaktime = 1500;
        } else {
            attackBreaktime = fighterClass.equals("melee") ? 2000 : 2500;
        }
        // endregion

        // region Initializations
        if (!MainApp.isIsTesting()) {
            entity.getTransformComponent().setScaleOrigin(
                new Point2D(width / 2.0, height / 2.0));
            entity.getViewComponent().addChild(texture);

            moveTimer = FXGL.newLocalTimer();
            moveTimer.capture();
        } else {
            entity.setProperty("isDead", false);
        }
        // endregion
    }

    @Override
    public void onUpdate(double tpf) {
        if (type.equals("finalboss")) {
            if (getFighterClass().equals("melee") && getHealthPoints() <= 50) {
                transformBoss("creatures/boss/HighElf_M_48x48.png", 48, 48,
                    4, "ranged");
            }
        }

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
            attackCD = true;
            normalizeVelocityX();
            normalizeVelocityY();
            // Grow in size
            enlarge();
        }
        // Enemy does the actual attack, spawns hitbox and then shrinks
        if (startAttacking) {
            if (fighterClass.equals("melee")) {
                if (!type.equals("finalboss")) {
                    meleePunch();
                } else {
                    if (!isHammerSmashing) {
                        hammerAttack();
                    } else {
                        hammerUltimateSmash();
                    }
                }
            } else {
                magicAutoAttack();
            }
            startShrink = true;
            startAttacking = false;
            prepAttack = false;
            isHammerSmashing = false;
            Runnable runnable = () -> {
                try {
                    Thread.sleep(attackBreaktime);
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

        if (isHammerSmashing) {
            hammerChargeCounter++;
            if (hammerChargeCounter >= (hammerUltimateDuration / 1000) * 60) {
                hammerChargeCounter = 0;
                startAttacking = true;
            }
        }
        // endregion
    }

    // region Movement
    private void moveToPlayer() {
        int attackDist;
        int moveDist;
        if (type.equals("finalboss")) {
            attackDist = fighterClass.equals("melee") ? 150 : 300;
            moveDist = 2000;
        } else {
            attackDist = fighterClass.equals("melee") ? 120 : 300;
            moveDist = fighterClass.equals("melee") ? 300 : 500;
        }
        double reactionTime = type.equals("finalboss") ? 0.25 : 1;
        if (moveTimer.elapsed(Duration.seconds(reactionTime)) && !isStunned) {
            if (fighterClass.equals("melee")) {
                if (dist < attackDist && !attackCD) {
                    playerLeavesRadius = false;
                    // ultimate < 50 and regular autoattack >= 50
                    int chooseAttack = (int) (Math.random() * 101);
                    if (chooseAttack < 50) {
                        prepAttack = true;
                        stop();
                        texture.playAnimationChannel(animMeleeAttack);
                        hammerUltimatePrepSprite();
                        isHammerSmashing = true;
                    } else {
                        autoAttack();
                    }
                } else if (dist < moveDist && !prepAttack) {
                    double xDir = playerX - enemyX > 0 ? 1 : -1;
                    double yDir = playerY - enemyY > 0 ? 1 : -1;
                    physics.setVelocityX(speed * xDir);
                    physics.setVelocityY(speed * yDir);
                    entity.setScaleX(xDir);
                } else {
                    stop();
                }
            } else {
                if (dist < attackDist) {
                    if (!attackCD) {
                        entity.setScaleX(playerX - enemyX > 0 ? 1 : -1);
                        // ultimate < 50 and regular autoattack >= 50
                        int chooseAttack = 40;//(int) (Math.random() * 101);
                        if (chooseAttack < 50) {
                            int chooseUltimate;
                            do {
                                chooseUltimate = (int) (Math.random() * 101);
                            } while ((novaCounter >= 2 && chooseUltimate < 50)
                                || (ricochetCounter >= 2 && chooseUltimate >= 50));
                            if (chooseUltimate < 50) {
                                ricochetCounter = 0;
                                // nova
                                System.out.println("nova");
                                autoAttack(); // temp
                                novaCounter++;
                            } else {
                                novaCounter = 0;
                                // ricochet
                                System.out.println("rico");
                                autoAttack(); // temp
                                ricochetCounter++;
                            }
                        } else {
                            System.out.println("auto");
                            autoAttack();
                        }
                    }
                    if (!prepAttack && !kiting) {
                        kiting = true;
                    }
                } else if (dist < moveDist && !prepAttack) {
                    double xDir = playerX - enemyX > 0 ? 1 : -1;
                    double yDir = playerY - enemyY > 0 ? 1 : -1;
                    physics.setVelocityX(speed * xDir);
                    physics.setVelocityY(speed * yDir);
                    entity.setScaleX(xDir);
                } else {
                    stop();
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
        if (type.equals("finalboss") && fighterClass.equals("melee")) {
            bossPrepAttack();
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
                put("weapon", !type.equals("finalboss")
                    ? "fireball_enemy_64x64" : "fireball_boss_64x64").
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
        rangedHitBox.setZIndex(5);
    }

    private void meleePunch() {
        int widthBox = width;
        int heightBox = height;
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
    }

    private void bossPrepAttack() {
        if (fighterClass.equals("melee")) {
            int width = 175; // width of the frame
            int height = 180; // height of the frame
            Entity gs = spawn("weapon",
                new SpawnData(
                    getEntity().getX(), getEntity().getY()).
                    put("weaponFile", "legend_sword_175x180").
                    put("duration", attackDuration).
                    put("frameWidth", width).
                    put("frameHeight", height).
                    put("fpr", 6));
            // Spawn the sword at boss's "hands"
            gs.getTransformComponent().setAnchoredPosition(
                new Point2D(entity.getX() - ((double) width / 2) + entity.getWidth() / 2,
                    entity.getY() - ((double) height / 2) + entity.getHeight() / 2));
            gs.setZIndex(5);
            if (entity.getScaleX() == 1) {
                gs.setScaleX(1);
            } else {
                gs.translateX(width); // smooth reflection over middle axis of player
                gs.setScaleX(-1);
            }
        }
    }

    private void hammerAttack() {
        int hitBoxWidth = 160; // width of the hitbox
        int hitBoxHeight = 160; // height of the hitbox
        double swordOffset = 55; // distance from player the hitbox should spawn

        Entity hammerHitBox = spawn("meleeEnemyPunch",
            new SpawnData(getEntity().getX(), getEntity().getY()).
                put("widthBox", hitBoxWidth).put("heightBox", hitBoxHeight));
        // Spawn hitbox on top of player and apply offset
        hammerHitBox.getTransformComponent().setAnchoredPosition(
            new Point2D(
                getEntity().getX() - ((double) hitBoxWidth / 2) + getEntity().getWidth() / 2
                    + (getEntity().getScaleX() > 0 ? swordOffset : -swordOffset),
                getEntity().getY() - ((double) hitBoxHeight / 2) + getEntity().getHeight() / 2));

        MainApp.addToHitBoxDestruction(hammerHitBox);
    }

    private void hammerUltimatePrepSprite() {
        int width = 175; // width of the frame
        int height = 180; // height of the frame
        Entity gs = spawn("weapon",
            new SpawnData(
                getEntity().getX(), getEntity().getY()).
                put("weaponFile", "legend_sword_175x180").
                put("duration", hammerUltimateDuration).
                put("frameWidth", width).
                put("frameHeight", height).
                put("fpr", 6));
        // Spawn the sword at boss's "hands"
        gs.getTransformComponent().setAnchoredPosition(
            new Point2D(entity.getX() - ((double) width / 2) + entity.getWidth() / 2,
                entity.getY() - ((double) height / 2) + entity.getHeight() / 2));
        gs.setZIndex(5);
        if (entity.getScaleX() == 1) {
            gs.setScaleX(1);
        } else {
            gs.translateX(width); // smooth reflection over middle axis of player
            gs.setScaleX(-1);
        }
    }

    private void hammerUltimateSmash() {
        int hitBoxWidth = 450; // width of the hitbox
        int hitBoxHeight = 450; // height of the hitbox
        double swordOffset = 0; // distance from player the hitbox should spawn

        Entity hammerHitBox = spawn("meleeEnemyPunch",
            new SpawnData(getEntity().getX(), getEntity().getY()).
                put("widthBox", hitBoxWidth).put("heightBox", hitBoxHeight));
        // Spawn hitbox on top of player and apply offset
        hammerHitBox.getTransformComponent().setAnchoredPosition(
            new Point2D(
                getEntity().getX() - ((double) hitBoxWidth / 2) + getEntity().getWidth() / 2
                    + (getEntity().getScaleX() > 0 ? swordOffset : -swordOffset),
                getEntity().getY() - ((double) hitBoxHeight / 2) + getEntity().getHeight() / 2));

        //MainApp.addToHitBoxDestruction(hammerHitBox);
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

    public String getFighterClass() {
        return fighterClass;
    }

    public void transformBoss(String assetName, int width, int height, int frames,
                              String fighterClass) {
        this.assetName = assetName;
        this.width = width;
        this.height = height;
        this.fighterClass = fighterClass;

        if (!MainApp.isIsTesting()) {
            animIdle = new AnimationChannel(FXGL.image(assetName), frames,
                width, height, Duration.seconds(0.5), 0, frames / 2 - 1);
            animWalk = new AnimationChannel(FXGL.image(assetName), frames,
                width, height, Duration.seconds(0.5), frames / 2, frames - 1);
            animMeleeAttack = new AnimationChannel(FXGL.image(assetName), frames,
                width, height, Duration.seconds((double) attackDuration / 1000),
                frames / 2, frames / 2);
            getEntity().getBoundingBoxComponent().clearHitBoxes();
            getEntity().getBoundingBoxComponent().addHitBox(new HitBox(
                new Point2D(
                    5,
                    5),
                BoundingShape.box(width - 5, height - 5)));
            entity.getTransformComponent().setScaleOrigin(
                new Point2D(width / 2.0, height / 2.0));
            texture.set(new AnimatedTexture(animIdle));
            texture.loop();
            speed = 100;
            attackDuration = 1100;
            attackBreaktime = 2000;
        }
    }
    // endregion
}