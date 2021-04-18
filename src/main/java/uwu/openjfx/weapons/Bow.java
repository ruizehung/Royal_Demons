package uwu.openjfx.weapons;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for the following:
    - Spawning the arrow at a specific location relative to player.
    - Calculate the angle relative to mouse-pressed location and shoot the arrow
    towards this location.
    - Spawn the player-bow in the prepAttack() method, whose animation
    will be handled in the WeaponAnimationComponent class.
 */
public abstract class Bow implements Weapon, AngleBehavior {

    protected final double playerHitBoxOffsetX = 3; // player's hitbox own offset from top left
    protected final double playerHitBoxOffsetY = 15; // player's hitbox own offset from top left
    protected final double playerHitBoxWidth = 35; // width of player's hitbox from 3 to 38
    protected final double playerHitBoxHeight = 40; // height of player's hitbox from 15 to 55
    protected Vec2 dir; // the direction with respect to mouse-pressed location

    protected boolean ultimateActivated;
    private final Image sprite; // weapon sprite
    protected String inventoryIconPath;
    private final String arrow;
    protected String name;
    protected String description;
    private final double attackDamage;
    private final String regularAttackPath;
    private final String ultimateAttackPath;
    private final int attackDuration;
    private final int ultimateChargeDuration;

    private int topBottomOffset;
    private int leftOffset;
    private int rightOffset;
    private int frameWidth;
    private int frameHeight;
    private double centerX;
    private double centerY;
    private int speed;

    public Bow(Image sprite, String arrow, double attackDamage,
               String regularAttackPath, String ultimateAttackPath,
               int attackDuration, int ultimateChargeDuration) {
        this.sprite = sprite;
        this.arrow = arrow;
        this.attackDamage = attackDamage;
        this.regularAttackPath = regularAttackPath;
        this.ultimateAttackPath = ultimateAttackPath;
        this.attackDuration = attackDuration;
        this.ultimateChargeDuration = ultimateChargeDuration;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bow;
    }

    @Override
    public void prepAttack(Entity player) {
        int width = 44; // width of bow
        int height = 43; // height of bow
        double bowOffsetX = 8; // spawn bow offset with respect to x player location
        double bowOffsetY = 5; // spawn bow offset with respect to y player location

        Entity b = spawn("weapon",
            new SpawnData(
                player.getX(), player.getY()).
                put("weaponFile", !ultimateActivated ? regularAttackPath : ultimateAttackPath).
                put("duration", getDuration(ultimateActivated)).
                put("frameWidth", width).
                put("frameHeight", height).
                put("fpr", 5).
                put("weaponSprite", sprite));
        // Spawn bow at player's "hands"
        b.getTransformComponent().setAnchoredPosition(
            new Point2D(
                (player.getX() + playerHitBoxOffsetX + (playerHitBoxWidth / 2)) + bowOffsetX,
                (player.getY() + bowOffsetY)));
        b.setZIndex(2000); // put bow on top of player (z = 1000)
        if (player.getScaleX() == 1) {
            b.setScaleX(1);
        } else {
            b.setScaleX(-1);
            b.translateX(-(2 * bowOffsetX)); // smooth reflection over middle axis rel. to player
        }
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        calculateAnglePlayerRelative(player, mouseCurrX, mouseCurrY);

        // top offset used to shrink the top/bot edges of hitbox
        topBottomOffset = 5;
        // left offset used to shrink the left edge of hitbox
        leftOffset = 20;
        // right offset used to shrink the right edge of hitbox
        rightOffset = 12;
        // width of the original frame (32 / 64)
        frameWidth = 48;
        // height of original frame (32 / 64)
        frameHeight = 16;

        // the center of the NEW and MODIFIED hitbox
        centerX = ((double) (leftOffset + (frameWidth - rightOffset)) / 2);
        centerY = ((double) (topBottomOffset + (frameHeight - topBottomOffset)) / 2);

        speed = 300; // speed at which magic spell goes
        /*
            Instantiate a brand new arrow that will hold the
            corresponding dimensions, components, and speed. It will temporarily
            spawn the magic spell at the players ORIGINAL getX() and getY() excluding
            its modified hitbox done in CreatureFactory.
         */
        if (!ultimateActivated) {
            Entity rangedHitBox = spawn("rangedArrowHitBox",
                new SpawnData(
                    player.getX(), player.getY()).
                    put("dir", dir.toPoint2D()).
                    put("speed", speed).
                    put("weapon", arrow).
                    put("duration", 500).
                    put("fpr", 1).
                    put("ultimateActive", ultimateActivated).
                    put("topBotOffset", topBottomOffset).
                    put("leftOffset", leftOffset).
                    put("rightOffset", rightOffset).
                    put("frameWidth", frameWidth).
                    put("frameHeight", frameHeight).
                    put("isArrow", true).
                    put("isMagic", false).
                    put("damage", attackDamage));
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
            rangedHitBox.setAnchoredPosition(
                (player.getX() + playerHitBoxOffsetX
                    + (player.getScaleX() > 0 ? playerHitBoxWidth : 0)), // right-left side
                (player.getY() + playerHitBoxOffsetY
                    + (playerHitBoxHeight / 2))); // midpoint player hitbox
            rangedHitBox.getTransformComponent().setRotationOrigin(
                new Point2D(centerX, ((double) (frameHeight)) / 2));
            rangedHitBox.setZIndex(5);
        } else {
            ultimateAttack(player, dir.angle());
        }
    }

    @Override
    public void calculateAnglePlayerRelative(Entity player, double mouseCurrX, double mouseCurrY) {
        // adjacent is distance (x) from mouse to player's "hands"
        // opposite is distance (y) from mouse to player's "hands"
        // angle calculated with tangent
        double adjacent = mouseCurrX
            - (player.getX() + playerHitBoxOffsetX
            + (player.getScaleX() > 0 ? playerHitBoxWidth : 0));
        double opposite = mouseCurrY
            - (player.getY() + playerHitBoxOffsetY
            + (playerHitBoxHeight / 2));
        double angle = Math.atan2(opposite, adjacent);
        angle = Math.toDegrees(angle);
        dir = Vec2.fromAngle(angle);
    }

    @Override
    public int getDuration(boolean ultimateActivated) {
        this.ultimateActivated = ultimateActivated;
        return ultimateActivated ? ultimateChargeDuration : attackDuration;
    }

    @Override
    public Image getWeaponSprite() {
        return sprite;
    }

    @Override
    public String getWeaponIconPath() {
        return inventoryIconPath;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isMeleeAttack() {
        return false;
    }

    @Override
    public int getUltimateCD() {
        return 3;
    }

    public void ultimateAttack(Entity player, double angle) {
        int amountOfArrows = 8;
        Vec2[] angles = new Vec2[amountOfArrows];
        double angleIncrementer = 2.0 * (10.0) / amountOfArrows;
        angle -= 10;
        for (int i = 0; i < angles.length; i++) {
            angles[i] = Vec2.fromAngle(angle);
            angle += angleIncrementer;
        }
        for (Vec2 vec : angles) {
            Entity rangedHitBox = spawn("rangedArrowHitBox",
                new SpawnData(
                    player.getX(), player.getY()).
                    put("dir", vec.toPoint2D()).
                    put("speed", speed).
                    put("weapon", arrow).
                    put("duration", 500).
                    put("fpr", 1).
                    put("ultimateActive", ultimateActivated).
                    put("topBotOffset", topBottomOffset).
                    put("leftOffset", leftOffset).
                    put("rightOffset", rightOffset).
                    put("frameWidth", frameWidth).
                    put("frameHeight", frameHeight).
                    put("isArrow", true).
                    put("isMagic", false).
                    put("damage", attackDamage));
            rangedHitBox.setLocalAnchor(new Point2D(centerX, centerY));
            rangedHitBox.setAnchoredPosition(
                (player.getX() + playerHitBoxOffsetX
                    + (player.getScaleX() > 0 ? playerHitBoxWidth : 0)), // right-left side
                (player.getY() + playerHitBoxOffsetY
                    + (playerHitBoxHeight / 2))); // midpoint player hitbox
            rangedHitBox.getTransformComponent().setRotationOrigin(
                new Point2D(centerX, ((double) (frameHeight)) / 2));
            rangedHitBox.setZIndex(5);
        }
    }
}
