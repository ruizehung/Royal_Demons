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
    private String assetName;
    private int width;
    private int height;
    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private AnimationChannel animWalk;
    private AnimationChannel animMeleeAttack;
    private AnimationChannel animBossHammerAttack;
    private double enemyX; // the center of the enemy X
    private double enemyY; // the center of the enemy Y
    private boolean playerLeavesRadius = false; // if player has stopped interacting with enemy
    private boolean collidingWithPlayer = false;
    private int attackDuration; // how long in MS the actual attack/animation takes
    private final double velocityDecrementer = 10; // how much velocity will be deducted on update
    private double speed; // movement speed of the enemy
    private double dist; // constant distance between player and enemy
    private int kiteTimer; // how long a ranged enemy is stuck before changing kite style
    private boolean kiteBack; // if a ranged enemy is currently kiting directly opposite of player
    private boolean kiteCircular; // if a ranged enemy is kiting circularly around player
    private boolean clockwise; // if a ranged enemy is kiting clockwise if kiting circularly
    private boolean kiting = false; // if a ranged enemy is kiting
    private boolean overDrive = false; // if enemy needs to slow to a stop if involuntarily moving
    private boolean isStunned = false; // if the enemy was hit by player and cannot move or attack
    protected boolean attackCD = false; // when the enemy can attack again
    protected boolean prepAttack = false; // enemy begins attack charge
    protected boolean startAttacking = false; // enemy does the attack animation/instantiate hitbox
    protected boolean startShrink = false; // for growing enemies
    private double scaler = 1.0; // to keep track of scale when shrinking/enlarging
    private LocalTimer moveTimer; // time to check player's location
    private Entity dizzyEffect; // dizzy sprite on top of enemy

    /*
        Boss Attributes
     */
    private int attackBreaktime; // how long before can cast ultimate again
    private final int ultimateDuration = 2000; // how long ultimate charge is
    private boolean isHammerSmashing = false;
    private int novaCounter; // track of how many times nova has been done by chance
    private boolean isMagic360Firing = false;
    private int ricochetCounter; // track of how many times ricochet has been done by chance
    private boolean isRicochetFiring = false;

    public EnemyComponent(int maxHP, String assetName, int width, int height, int frames,
                          String type, String fighterClass) {
        super(maxHP, maxHP);

        // region Constructor
        this.assetName = assetName;
        this.width = width;
        this.height = height;
        this.type = type;
        this.fighterClass = fighterClass;
        // endregion

        // region Initialize Animations
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
        // endregion
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
        case "miniboss":
            massEffect = true;
            blockProbability = 25;
            armorStat = 4.5;
            break;
        case "finalboss":
            massEffect = true;
            blockProbability = 30;
            armorStat = 25;
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
        // region Boss Update Attributes
        if (type.equals("finalboss")) {
            if (getFighterClass().equals("melee") && getHealthPoints() <= 50 && !prepAttack) {
                setHealthPoints(50);
                Entity poof = spawn("weapon",
                    new SpawnData(
                        enemyX - 80, enemyY - 80).
                        put("weaponFile", "poof").
                        put("duration", 1000).
                        put("frameWidth", 32).
                        put("frameHeight", 32).
                        put("fpr", 5));
                poof.setZIndex(2000);
                poof.setScaleX(5);
                poof.setScaleY(5);
                transformBoss("creatures/boss/wizard_40x70.png", 40, 70,
                    9, "ranged");
            }
        }
        // endregion
        // region Player to Enemy Distance Relationship
        final double playerHitBoxOffsetX = 3; // player's hitbox own offset from top left
        final double playerHitBoxOffsetY = 15; // player's hitbox own offset from top left
        final double playerHitBoxWidth = 35; // width of player's hitbox from 3 to 38
        final double playerHitBoxHeight = 40; // height of player's hitbox from 15 to 55

        Entity player = FXGL.geto("player");
        playerX = player.getX() + playerHitBoxOffsetX + (playerHitBoxWidth / 2);
        playerY = player.getY() + playerHitBoxOffsetY + (playerHitBoxHeight / 2);
        enemyX = getEntity().getX() + getEntity().getWidth() / 2;
        enemyY = getEntity().getY() + getEntity().getHeight() / 2;


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

        if(dizzyEffect != null) {
            dizzyEffect.setAnchoredPosition(
                getEntity().getX() + (getEntity().getWidth() / 2) - (55.0 / 2),
                getEntity().getY() + (getEntity().getHeight() / 2) - (30.0 / 2) - 10);
        }

        if (isStunned) {
            normalizeVelocityX();
            normalizeVelocityY();
            Runnable runnable = () -> {
                try {
                    Thread.sleep(1500);
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
            // if enemy is charging up, then on cooldown, disallow movement, and grow in size
            attackCD = true;
            normalizeVelocityX();
            normalizeVelocityY();
            // Grow in size
            if (!type.equals("finalboss")) {
                enlarge();
            }
        }

        // Enemy does the actual attack, spawns hitbox and then shrinks
        if (startAttacking) {
            performAttack();
        }

        // Enemy shrinks
        if (startShrink) {
            shrink();
        }
        // endregion
    }

    // region Movement Interactions Related to Player
    private void moveToPlayer() {
        int attackDist; // how far away player and enemy have to be for enemy to attack
        int moveDist; // how far away player and enemy have to be for enemy to follow player
        if (type.equals("finalboss")) {
            attackDist = fighterClass.equals("melee") ? 150 : 300;
            moveDist = 2000;
        } else {
            attackDist = fighterClass.equals("melee") ? 120 : 300;
            moveDist = fighterClass.equals("melee") ? 300 : 500;
        }
        double reactionTime = type.equals("finalboss") ? 0.25 : 1; // boss tracks player faster
        if (moveTimer.elapsed(Duration.seconds(reactionTime)) && !isStunned) {
            if (fighterClass.equals("melee")) { // if enemy is melee
                if (dist < attackDist && !attackCD) { // if within attack distance and not under cd
                    playerLeavesRadius = false;
                    if (type.equals("finalboss")) {
                        // ultimate < 42 and regular autoattack >= 42
                        int chooseAttack = (int) (Math.random() * 101); // choose an attack
                        if (chooseAttack < 42) {
                            prepAttack = true;
                            isHammerSmashing = true;
                            stop();
                            texture.playAnimationChannel(animMeleeAttack);
                            Runnable runnable = () -> {
                                try {
                                    Thread.sleep(ultimateDuration);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                startAttacking = true;
                            };
                            Thread thread = new Thread(runnable);
                            thread.start();
                            hammerUltimatePrepAttack();
                        } else {
                            initiateAutoAttack();
                        }
                    } else {
                        initiateAutoAttack();
                    }
                } else if (dist < moveDist && !prepAttack) { // if within big radius and no charge
                    double xDir = playerX - enemyX > 0 ? 1 : -1;
                    double yDir = playerY - enemyY > 0 ? 1 : -1;
                    physics.setVelocityX(speed * xDir);
                    physics.setVelocityY(speed * yDir);
                    entity.setScaleX(xDir);
                } else {
                    stop();
                }
            } else { // if ranged enemy
                if (dist < attackDist) {
                    if (!attackCD) {
                        if (type.equals("finalboss")) {
                            // ultimate < 50 and regular autoattack >= 50
                            int chooseAttack = (int) (Math.random() * 101);
                            if (chooseAttack < 58) {
                                int chooseUltimate;
                                do { // ensure ultimate has not already been spammed twice
                                    chooseUltimate = (int) (Math.random() * 101);
                                } while ((novaCounter >= 2 && chooseUltimate < 50)
                                    || (ricochetCounter >= 2 && chooseUltimate >= 50));
                                if (chooseUltimate < 50) {
                                    ricochetCounter = 0;
                                    prepAttack = true;
                                    stop();
                                    texture.playAnimationChannel(animMeleeAttack);
                                    magicUltimate360PrepAttack();
                                    isMagic360Firing = true;
                                    novaCounter++;
                                } else {
                                    novaCounter = 0;
                                    prepAttack = true;
                                    stop();
                                    texture.playAnimationChannel(animMeleeAttack);
                                    magicUltimateRicochetPrepAttack();
                                    isRicochetFiring = true;
                                    ricochetCounter++;
                                }
                                Runnable runnable = () -> {
                                    try {
                                        Thread.sleep(ultimateDuration);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    startAttacking = true;
                                };
                                Thread thread = new Thread(runnable);
                                thread.start();
                            } else {
                                initiateAutoAttack();
                            }
                        } else {
                            initiateAutoAttack();
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
            if (dizzyEffect != null) {
                dizzyEffect.removeFromWorld();
            }
            dizzyEffect = spawn("weapon",
                new SpawnData(
                    enemyX, enemyY).
                    put("weaponFile", "dizzyEffect_55x30").
                    put("duration", 1500).
                    put("frameWidth", 55).
                    put("frameHeight", 30).
                    put("fpr", 6));
            dizzyEffect.setZIndex(5);
        }
    }

    private void kitePlayer() {
        if (physics != null && !prepAttack) {
            getEntity().setScaleX(playerX - enemyX > 0 ? 1 : -1);
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

    private void stop() {
        if (!collidingWithPlayer) {
            physics.setVelocityX(0);
            physics.setVelocityY(0);
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
    // region Regular Attacks
    private void performAttack() {
        if (fighterClass.equals("melee")) { // if enemy is melee
            if (!type.equals("finalboss")) { // if enemy is normal melee
                meleePunch();
                int random = (int) (Math.random() + .5);
                String attSound = random == 1 ? "mob/minion_1.wav" : "mob/minion_2.wav";
                FXGL.play(attSound);
            } else { // if enemy is boss melee
                if (!isHammerSmashing) { // perform attack based on boss's chosen attack
                    hammerAttack();
                    FXGL.play("skills/sword_basic.wav");
                } else {
                    hammerUltimateSmash();
                    FXGL.play("skills/explosion_largest2.wav");
                }
            }
        } else { // if enemy is ranged
            if (!type.equals("finalboss")) {
                magicAutoAttack();
                FXGL.play("skills/fireball 2.wav");
            } else {
                if (isMagic360Firing) {
                    magicUltimate360Fire();
                    FXGL.play("skills/explosion_largest.wav");
                } else if (isRicochetFiring) {
                    magicUltimateRicochetFire();
                    FXGL.play("skills/fireball3.wav");
                } else {
                    magicAutoAttack();
                    FXGL.play("skills/fireball3.wav");
                }
            }
        }
        startShrink = true; // start shrinking
        startAttacking = false; // not attacking anymore
        prepAttack = false; // not charging anymore
        isHammerSmashing = false; // not smashing anymore
        isMagic360Firing = false; // not 360 firing anymore
        isRicochetFiring = false; // not ricochet firing anymore
        Runnable runnable = () -> { // attackBreaktime amount of time before cooldown is done
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

    private void initiateAutoAttack() {
        prepAttack = true;
        stop();
        texture.playAnimationChannel(animMeleeAttack);
        if (type.equals("finalboss")) {
            bossPrepAttack();
        } else if (fighterClass.equals("ranged")) {
            int width = 16; // width of the frame
            int height = 16; // height of the frame
            int handOffset = 7; // offset away from center of entity
            Entity magicHB = spawn("weapon",
                new SpawnData(
                    getEntity().getX(), getEntity().getY()).
                    put("weaponFile", "fireCharge_16x16").
                    put("duration", attackDuration).
                    put("frameWidth", width).
                    put("frameHeight", height).
                    put("fpr", 15));
            // Spawn the sword at boss's "hands"
            magicHB.getTransformComponent().setAnchoredPosition(
                new Point2D(entity.getX() - ((double) width / 2) + entity.getWidth() / 2
                    + handOffset,
                    entity.getY() - ((double) height / 2) + entity.getHeight() / 2 + 15));
            magicHB.setZIndex(5);
            magicHB.setScaleX(1.5);
            magicHB.setScaleY(1.5);
            if (entity.getScaleX() == 1) {
                magicHB.setScaleX(1.5);
            } else {
                // smooth reflection over middle axis rel. to player
                magicHB.translateX(width - 2 * handOffset);
                magicHB.setScaleX(-1.5);
            }
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
        entity.setScaleX(playerX - enemyX > 0 ? 1 : -1);
        Entity magicRegAutoHB = spawn("rangedMagicHitBox",
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
                put("damage", 1.0).
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
        magicRegAutoHB.setLocalAnchor(new Point2D(centerX, centerY));
        magicRegAutoHB.setAnchoredPosition(enemyX, enemyY);
        magicRegAutoHB.getTransformComponent().setRotationOrigin(
            new Point2D(centerX, ((double) (frameHeight)) / 2));
        magicRegAutoHB.setZIndex(5);
    }

    private void meleePunch() {
        int hitBoxWidth = (int) getEntity().getBoundingBoxComponent().getWidth() * 2;
        int hitBoxHeight = (int) getEntity().getBoundingBoxComponent().getHeight() * 2;
        double swordOffset = (double) hitBoxWidth / 2; // distance from player hitbox should spawn

        Entity punch = spawn("meleeEnemyPunch",
            new SpawnData(getEntity().getX(), getEntity().getY()).
                put("widthBox", hitBoxWidth).put("heightBox", hitBoxHeight));
        // Spawn hitbox on top of player and apply offset
        punch.getTransformComponent().setAnchoredPosition(
            new Point2D(
                enemyX - ((double) hitBoxWidth / 2)
                    + (getEntity().getScaleX() > 0 ? swordOffset : -swordOffset),
                enemyY - ((double) hitBoxHeight / 2)));
        MainApp.addToHitBoxDestruction(punch);
    }

    private void bossPrepAttack() {
        if (fighterClass.equals("melee")) {
            int width = 255; // width of the frame
            int height = 130; // height of the frame
            int offsetX = 35; // offset of weapon
            int offsetY = -10; // offset of weapon
            Entity hammerAutoHB = spawn("weapon",
                new SpawnData(
                    getEntity().getX(), getEntity().getY()).
                    put("weaponFile", "bossAttack_255x130_2").
                    put("duration", attackDuration).
                    put("frameWidth", width).
                    put("frameHeight", height).
                    put("fpr", 5));
            // Spawn the sword at boss's "hands"
            hammerAutoHB.getTransformComponent().setAnchoredPosition(new Point2D(
                entity.getX() - ((double) width / 2) + entity.getWidth() / 2 + offsetX,
                entity.getY() - ((double) height / 2) + entity.getHeight() / 2 + offsetY));
            hammerAutoHB.setZIndex(5);
            if (entity.getScaleX() == 1) {
                hammerAutoHB.setScaleX(1);
            } else {
                hammerAutoHB.translateX(width - 2 * offsetX + 20);
                hammerAutoHB.setScaleX(-1);
            }
        } else {
            int width = 16; // width of the frame
            int height = 16; // height of the frame
            int handOffset = 15; // offset away from center of entity
            Entity magicBossAutoHB = spawn("weapon",
                new SpawnData(
                    getEntity().getX(), getEntity().getY()).
                    put("weaponFile", "fireCharge_16x16").
                    put("duration", attackDuration).
                    put("frameWidth", width).
                    put("frameHeight", height).
                    put("fpr", 15));
            // Spawn the sword at boss's "hands"
            magicBossAutoHB.getTransformComponent().setAnchoredPosition(
                new Point2D(entity.getX() - ((double) width / 2) + entity.getWidth() / 2
                    + handOffset,
                    entity.getY() - ((double) height / 2) + entity.getHeight() / 2 + 15));
            magicBossAutoHB.setZIndex(5);
            magicBossAutoHB.setScaleX(1.5);
            magicBossAutoHB.setScaleY(1.5);
            if (entity.getScaleX() == 1) {
                magicBossAutoHB.setScaleX(1.5);
            } else {
                // smooth reflection over middle axis rel. to player
                magicBossAutoHB.translateX(width - 2 * handOffset);
                magicBossAutoHB.setScaleX(-1.5);
            }
        }
    }
    // endregion

    // region Boss Melee Attacks
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

    private void hammerUltimatePrepAttack() {
        int width = 215; // width of the frame
        int height = 175; // height of the frame
        int offsetX = 16;
        int offsetY = -27;
        FXGL.play("skills/charge_hammer.wav");
        Entity hammerUltimateHB = spawn("weapon",
            new SpawnData(
                getEntity().getX(), getEntity().getY()).
                put("weaponFile", "bossUltSpriteSheet_215x175").
                put("duration", ultimateDuration).
                put("frameWidth", width).
                put("frameHeight", height).
                put("fpr", 10));
        // Spawn the sword at boss's "hands"
        hammerUltimateHB.getTransformComponent().setAnchoredPosition(
            new Point2D(entity.getX() - ((double) width / 2) + entity.getWidth() / 2 + offsetX,
                entity.getY() - ((double) height / 2) + entity.getHeight() / 2 + offsetY));
        hammerUltimateHB.setZIndex(5);
        if (entity.getScaleX() == 1) {
            hammerUltimateHB.setScaleX(1);
        } else {
            hammerUltimateHB.translateX(width - 2 * offsetX + 20);
            hammerUltimateHB.setScaleX(-1);
        }
    }

    private void hammerUltimateSmash() {
        int hitBoxWidth = 350; // width of the hitbox
        int hitBoxHeight = 350; // height of the hitbox
        double swordOffset = 0; // distance from player the hitbox should spawn

        Entity hammerHitBox = spawn("meleeEnemyPunch",
            new SpawnData(getEntity().getX(), getEntity().getY()).
                put("widthBox", hitBoxWidth).put("heightBox", hitBoxHeight));
        hammerHitBox.addComponent(new GroundSmashComponent());
        // Spawn hitbox on top of player and apply offset
        hammerHitBox.getTransformComponent().setAnchoredPosition(
            new Point2D(
                getEntity().getX() - ((double) hitBoxWidth / 2) + getEntity().getWidth() / 2
                    + (getEntity().getScaleX() > 0 ? swordOffset : -swordOffset),
                getEntity().getY() - ((double) hitBoxHeight / 2) + getEntity().getHeight() / 2));
        hammerHitBox.setType(RoyalType.SMASHEDGROUND);
        Entity sg = spawn("weapon",
            new SpawnData(
                getEntity().getX(), getEntity().getY()).
                put("weaponFile", "smashedGround").
                put("duration", 4000).
                put("frameWidth", hitBoxWidth).
                put("frameHeight", hitBoxHeight).
                put("fpr", 1));
        // Spawn the sword at boss's "hands"
        sg.getTransformComponent().setAnchoredPosition(
            new Point2D(
                getEntity().getX() - ((double) hitBoxWidth / 2) + getEntity().getWidth() / 2
                    + (getEntity().getScaleX() > 0 ? swordOffset : -swordOffset),
                getEntity().getY() - ((double) hitBoxHeight / 2) + getEntity().getHeight() / 2));
    }
    // endregion

    // region Boss Ranged Attacks
    private void magicUltimate360PrepAttack() {
        int width = 100; // width of magic spell
        int height = 100; // height of magic spell
        FXGL.play("skills/charge_boss.wav");

        Entity magic360AuraHB = spawn("weapon",
            new SpawnData(
                enemyX - ((double) width / 2) + 4, enemyY - ((double) height / 2) + 10).
                put("weaponFile", "fire360_100x100").
                put("duration", ultimateDuration).
                put("frameWidth", width).
                put("frameHeight", height).
                put("fpr", 60));
        magic360AuraHB.setZIndex(5);
    }

    private void magicUltimate360Fire() {
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
            Instantiate a brand new arrow that will hold the
            corresponding dimensions, components, and speed. It will temporarily
            spawn the magic spell at the players ORIGINAL getX() and getY() excluding
            its modified hitbox done in CreatureFactory.
         */
        int amountOfFireballs = 35;
        Vec2[] angles = new Vec2[amountOfFireballs];
        double angleIncrementer = 2 * Math.PI / amountOfFireballs;
        double angle = 0;
        for (int i = 0; i < angles.length; i++) {
            double x = Math.cos(angle);
            double y = Math.sin(angle);
            angles[i] = new Vec2(new Point2D(x, y));
            angle += angleIncrementer;
        }
        for (Vec2 vec : angles) {
            Entity rangedHitBox = spawn("rangedMagicHitBox",
                new SpawnData(
                    enemyX, enemyY).
                    put("dir", vec.toPoint2D()).
                    put("speed", speed).
                    put("weapon", !type.equals("finalboss")
                        ? "fireball_enemy_64x64" : "fireball_boss_64x64").
                    put("duration", 500).
                    put("fpr", 60).
                    put("ultimateActive", false).
                    put("topBotOffset", topBottomOffset).
                    put("leftOffset", leftOffset).
                    put("rightOffset", rightOffset).
                    put("frameWidth", frameWidth).
                    put("frameHeight", frameHeight).
                    put("isArrow", false).
                    put("isMagic", true).
                    put("damage", 1.0).
                    put("royalType", RoyalType.ENEMYATTACK));
            /*
                setLocalAnchor(...) will ensure that the anchor/pivot point of the
                arrow is located at the CENTER of the NEW hitbox.
                setAnchoredPosition(...) will spawn the arrow to the right
                of the player if player is facing right, and left if the player is
                facing left, and located at the player's "hands".
                setRotationOrigin(...) will ensure that the rotation anchor/pivot
                point of the arrow is located at the CENTER of the NEW hitbox.
                The arguments are offsets based off of the top-left point of the
                ORIGINAL frameWidth x frameHeight frame. Therefore, we need to offset
                centerX in the x-direction, and the center of the arrow will
                CONSISTENTLY be at its midpoint in the y-direction.
             */
            rangedHitBox.setLocalAnchor(new Point2D(centerX, centerY));
            rangedHitBox.setAnchoredPosition(enemyX, enemyY);
            rangedHitBox.getTransformComponent().setRotationOrigin(
                new Point2D(centerX, ((double) (frameHeight)) / 2));
            rangedHitBox.setZIndex(5);
        }
    }

    private void magicUltimateRicochetPrepAttack() {
        int width = 32; // width of the frame
        int height = 32; // height of the frame
        int handOffset = 22; // offset away from center of entity
        Entity magicUltimateRicoHB = spawn("weapon",
            new SpawnData(
                getEntity().getX(), getEntity().getY()).
                put("weaponFile", "orangeNovaBall_32x32").
                put("duration", attackDuration).
                put("frameWidth", width).
                put("frameHeight", height).
                put("fpr", 32));
        // Spawn the sword at boss's "hands"
        magicUltimateRicoHB.getTransformComponent().setAnchoredPosition(
            new Point2D(entity.getX() - ((double) width / 2) + entity.getWidth() / 2
                + handOffset,
                entity.getY() - ((double) height / 2) + entity.getHeight() / 2 + 15));
        magicUltimateRicoHB.setZIndex(5);
        if (entity.getScaleX() == 1) {
            magicUltimateRicoHB.setScaleX(1);
        } else {
            // smooth reflection over middle axis rel. to player
            magicUltimateRicoHB.translateX(width - 2 * handOffset);
            magicUltimateRicoHB.setScaleX(-1);
        }
    }

    private void magicUltimateRicochetFire() {
        // adjacent is distance (x) from mouse to player's "hands"
        // opposite is distance (y) from mouse to player's "hands"
        // angle calculated with tangent
        double adjacent = playerX - enemyX;
        double opposite = playerY - enemyY;
        double angle = Math.atan2(opposite, adjacent);
        angle = Math.toDegrees(angle);
        Vec2 dir = Vec2.fromAngle(angle);

        // top offset used to shrink the top/bot edges of hitbox
        int topBottomOffset = 40;
        // left offset used to shrink the left edge of hitbox
        int leftOffset = 50;
        // right offset used to shrink the right edge of hitbox
        int rightOffset = 25;
        // width of the original frame (64 / 32)
        int frameWidth = 100;
        // height of original frame (64 / 32)
        int frameHeight = 100;

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
        entity.setScaleX(playerX - enemyX > 0 ? 1 : -1);
        Entity rangedHitBox = spawn("rangedMagicHitBox",
            new SpawnData(
                enemyX, enemyY).
                put("dir", dir.toPoint2D()).
                put("speed", speed).
                put("weapon", "ricochetBall_100x100").
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
                put("damage", 1.0).
                put("royalType", RoyalType.ENEMYATTACK));
        rangedHitBox.addComponent(new RicochetComponent());
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
    // endregion

    // region Size Manipulation
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

    // endregion

    // region Enemy Misc.
    @Override
    public void die() {
        super.die();
        if (!MainApp.isIsTesting()) {
            getEntity().removeFromWorld();
            if (dizzyEffect != null) {
                dizzyEffect.removeFromWorld();
            }
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

    public String getType() {
        return type;
    }

    public String transformBoss(String assetName, int width, int height, int frames,
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
        } else {
            return "transformed";
        }
        return null;
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

    public void turnSpriteRed() {
        if (texture != null) {
            texture.set(texture.multiplyColor(Color.RED));
        }
    }
    // endregion
}