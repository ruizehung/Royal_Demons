package uwu.openjfx.weapons;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.PlayerComponent;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for the following:
    - Spawning the magic spell at a specific location relative to player
    - Calculate the angle relative to mouse-pressed location and fire the magic spell
    towards this location.
    - Spawn the player-staff(wand) in the prepAttack() method, whose animation
    will be handled in the WeaponAnimationComponent class.
 */
public class MagicStaff2 implements Weapon, AngleBehavior {
    private final double playerHitBoxOffsetX = 3; // player's hitbox own offset from top left
    private final double playerHitBoxOffsetY = 15; // player's hitbox own offset from top left
    private final double playerHitBoxWidth = 35; // width of player's hitbox from 3 to 38
    private final double playerHitBoxHeight = 40; // height of player's hitbox from 15 to 55
    private Vec2 dir; // the direction with respect to mouse-pressed location
    private boolean ultimateActivated;
    private final Image sprite = new Image("assets/textures/ui/weapons/staff2_ui.png"); // sprite

    @Override
    public void prepAttack(Entity player) {
        int width = 50; // width of bow
        int height = 65; // height of bow
        double staffOffsetX = 8; // spawn bow offset with respect to x player location
        double staffOffsetY = 25; // spawn bow offset with respect to y player location

        Entity b = spawn("weapon",
            new SpawnData(
                player.getX(), player.getY()).
                put("weaponFile", "red_wand_50x65").
                put("duration", getDuration(ultimateActivated)).
                put("frameWidth", width).
                put("frameHeight", height).
                put("fpr", 4).
                put("weaponSprite", sprite));
        // Spawn bow at player's "hands"
        b.getTransformComponent().setAnchoredPosition(
            new Point2D(
                (player.getX() + playerHitBoxOffsetX + (playerHitBoxWidth / 2))
                    - ((double) width / 2) + staffOffsetX,
                (player.getY() + staffOffsetY) - ((double) height / 2)));
        b.setZIndex(2000); // put staff on top of player (z = 1000)
        if (player.getScaleX() == 1) {
            b.setScaleX(1);
        } else {
            // smooth reflection over middle axis rel. to player
            b.translateX(width - 2 * staffOffsetX);
            b.setScaleX(-1);
        }
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        calculateAnglePlayerRelative(player, mouseCurrX, mouseCurrY);

        // top offset used to shrink the top/bot edges of hitbox
        int topBottomOffset = 10;
        // left offset used to shrink the left edge of hitbox
        int leftOffset = 30;
        // right offset used to shrink the right edge of hitbox
        int rightOffset = 20;
        // width of the original frame (64 / 100)
        int frameWidth = !ultimateActivated ? 64 : 100;
        // height of original frame (64 / 100)
        int frameHeight = !ultimateActivated ? 64 : 100;

        // the center of the NEW and MODIFIED hitbox
        double centerX = ((double) (leftOffset + (frameWidth - rightOffset)) / 2);
        double centerY = ((double) (topBottomOffset + (frameHeight - topBottomOffset)) / 2);

        if (!ultimateActivated) {
            int speed = 300; // speed at which magic spell goes
            double attackDamage = 80;

            /*
                Instantiate a brand new magic spell that will hold the
                corresponding dimensions, components, and speed. It will temporarily
                spawn the magic spell at the players ORIGINAL getX() and getY() excluding
                its modified hitbox done in CreatureFactory.
             */

            Entity rangedHitBox = spawn("rangedMagicHitBox",
                new SpawnData(
                    player.getX(), player.getY()).
                    put("dir", dir.toPoint2D()).
                    put("speed", speed).
                    put("weapon", "fireball_64x64").
                    put("duration", 500).
                    put("fpr", 60).
                    put("ultimateActive", ultimateActivated).
                    put("topBotOffset", topBottomOffset).
                    put("leftOffset", leftOffset).
                    put("rightOffset", rightOffset).
                    put("frameWidth", frameWidth).
                    put("frameHeight", frameHeight).
                    put("isArrow", false).
                    put("isMagic", true).
                    put("damage", attackDamage).
                    put("royalType", RoyalType.PLAYERATTACK));
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
            rangedHitBox.setAnchoredPosition(
                (player.getX() + playerHitBoxOffsetX
                    + (player.getScaleX() > 0 ? playerHitBoxWidth : 0)), // right-left side
                (player.getY() + playerHitBoxOffsetY
                    + (playerHitBoxHeight / 2))); // midpoint player hitbox
            rangedHitBox.getTransformComponent().setRotationOrigin(
                new Point2D(centerX, ((double) (frameHeight)) / 2));
            rangedHitBox.setZIndex(5);
        } else {
            int hitBoxWidth = 175; // width of the hitbox
            int hitBoxHeight = 110; // height of the hitbox
            double breathHitBoxOffset = 50; // distance from player the hitbox should spawn
            double breathSpriteOffset = 20; // distance from player the sprite should spawn

            Entity breathSprite = spawn("weapon",
                new SpawnData(
                    player.getX() - ((double) frameWidth / 2)
                        + (player.getScaleX() > 0 ? breathSpriteOffset : 6 * breathSpriteOffset),
                    player.getY() - ((double) frameHeight / 2) - 40).
                    put("weaponFile", "firebreath_100x100").
                    put("duration", 2000).
                    put("frameWidth", frameWidth).
                    put("frameHeight", frameHeight).
                    put("fpr", 60).
                    put("weaponSprite", sprite));
            breathSprite.setScaleX(2.5);
            breathSprite.setScaleY(2.5);
            breathSprite.setZIndex(5);

            if (player.getScaleX() == 1) {
                breathSprite.setScaleX(2.5);
            } else {
                breathSprite.setScaleX(-2.5);
            }

            Entity breathHitbox = spawn("breathOfFire",
                new SpawnData(player.getX(), player.getY()).
                    put("width", hitBoxWidth).put("height", hitBoxHeight).
                    put("damage", 0.000000000001));
            // Spawn hitbox on top of player and apply offset
            PlayerComponent.channelAttack();

            Runnable runnable = () -> {
                FXGL.getGameTimer().runAtInterval(() -> {
                    if (!PlayerComponent.isChanneling()) {
                        breathHitbox.removeFromWorld();
                        FXGL.getGameTimer().clear();
                    }
                    breathHitbox.getTransformComponent().setAnchoredPosition(
                        new Point2D(
                            player.getX() - ((double) hitBoxWidth / 2) + player.getWidth() / 2
                                + (player.getScaleX() > 0
                                ? breathHitBoxOffset : -breathHitBoxOffset),
                            player.getY() - ((double) hitBoxHeight / 2)
                                + player.getHeight() / 2));
                    breathSprite.getTransformComponent().setAnchoredPosition(
                        new Point2D(
                            player.getX() - ((double) frameWidth / 2)
                                + (player.getScaleX() > 0
                                ? breathSpriteOffset : 6 * breathSpriteOffset),
                            player.getY() - ((double) frameHeight / 2) - 40));
                }, Duration.seconds(.01));
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    @Override
    public void calculateAnglePlayerRelative(Entity player, double mouseCurrX, double mouseCurrY) {
        // adjacent is distance (x) from mouse to player's "hands"
        // opposite is distance (y) from mouse to player's "hands"
        // angle calculated with tangent
        double adjacent = mouseCurrX
            - (player.getX() + playerHitBoxOffsetX + (player.getScaleX() > 0
            ? playerHitBoxWidth : 0));
        double opposite = mouseCurrY
            - (player.getY() + playerHitBoxOffsetY + (playerHitBoxHeight / 2));
        double angle = Math.atan2(opposite, adjacent);
        angle = Math.toDegrees(angle);
        dir = Vec2.fromAngle(angle);
    }

    @Override
    public int getDuration(boolean ultimateActivated) {
        int attackDuration = 800; // charge-up time of attacking in milliseconds
        int ultimateChargeDuration = 350; // charge-up time of attacking in milliseconds
        this.ultimateActivated = ultimateActivated;
        return ultimateActivated ? ultimateChargeDuration : attackDuration;
    }

    @Override
    public Image getWeaponSprite() {
        return sprite;
    }

    @Override
    public String getWeaponIconPath() {
        return "ui/inventory/staff2.png";
    }

    @Override
    public String getName() {
        return "Dragon's Staff";
    }

    @Override
    public String getDescription() {
        return "A Dragon's Staff's primary element is Fire, shooting fireballs in the "
            + "direction of target. When casting the ultimate, the wielder shall momentarily "
            + "release a dragon's breath of fire, damaging any enemies within its range.";
    }
    @Override
    public boolean isMeleeAttack() {
        return false;
    }

    @Override
    public int getUltimateCD() {
        return 5;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MagicStaff2;
    }
}
