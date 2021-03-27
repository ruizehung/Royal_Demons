package uwu.openjfx.weapons;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for spawning the arrow at a specific location relative to player.
    This class will calculate the angle relative to mouse-pressed location and shoot the arrow
    towards this location.
    This class will spawn the player-bow in the prepAttack() method, whose animation
    will be handled in the BowComponent class.
 */
public class Bow0 implements Weapon, AngleBehavior {

    private Entity rangedHitBox; // the magic spell (ultimate / nonultimate)
    private int leftOffset; // side offset used to shrink the top/left/bot edges of hitbox
    private int rightOffset; // right offset used to shrink the right edge of hitbox
    private int frameWidth; // width of the original frame (32 / 64)
    private int frameHeight; // height of original frame (32 / 64)
    double playerHitBoxOffsetX = 3; // player's hitbox own offset from top left
    double playerHitBoxOffsetY = 15; // player's hitbox own offset from top left
    double playerHitBoxWidth = 35; // width of player's hitbox from 3 to 38
    double playerHitBoxHeight = 40; // height of player's hitbox from 15 to 55
    private double centerX; // the center of the hitbox
    private double centerY; // the center of the hitbox
    private Vec2 dir; // the direction with respect to mouse-pressed location
    private int speed = 400; // speed at which magic spell goes
    private int attackDuration = 800; // charge-up time of attacking in milliseconds
    private int ultimateChargeDuration = 1000; // charge-up time of attacking in milliseconds
    private boolean ultimateActivated;
    private double bowOffset = 10; // spawn bow offset with respect to player location

    @Override
    public void prepAttack(Entity player) {
        int width = 16; // width of bow
        int height = 32; // height of bow
        Entity b = spawn("rangedBow",
                new SpawnData(
                        player.getX(), player.getY()).
                        put("weapon", !ultimateActivated ? "bow0_reg_16x32" : "bow0_reg_16x32").
                        put("duration", getDuration(ultimateActivated)).
                        put("frameWidth", width).
                        put("frameHeight", height).
                        put("fpr", !ultimateActivated ? 1 : 1));
        // Spawn bow at player's "hands"
        b.getTransformComponent().setAnchoredPosition(
                new Point2D(player.getX() - (width / 2) + player.getWidth() / 2 + bowOffset,
                        player.getY() - (height / 2) + player.getHeight() / 2));
        b.setZIndex(2000); // put bow on top of player (z = 1000)
        if (player.getScaleX() == 1) {
            b.setScaleX(2);
        } else {
            b.setScaleX(-2);
            b.translateX(-bowOffset); // smooth reflection over middle axis relative to player
        }
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        // Confirm goal is to shoot a projectile, then calculate angle
        if (this instanceof AngleBehavior) {
            ((AngleBehavior) this).calculateAnglePlayerRelative(player, mouseCurrX, mouseCurrY);
        }
        leftOffset = !ultimateActivated ? 0 : 20;
        rightOffset = !ultimateActivated ? 0 : 10;
        frameWidth = !ultimateActivated ? 48 : 48;
        frameHeight = !ultimateActivated ? 16 : 16;
        centerX = ((double) (leftOffset + (frameWidth - rightOffset)) / 2);
        centerY = ((double) (leftOffset + (frameHeight - leftOffset)) / 2);
        if (player.getScaleX() == -1) {
            centerX -= leftOffset;
            centerY -= leftOffset;
        }
        rangedHitBox = spawn("rangedArrowHitBox",
                new SpawnData(
                        player.getX(), player.getY()).
                        put("dir", dir.toPoint2D()).
                        put("speed", speed).
                        put("weapon", "arrow").
                        put("duration", 500).
                        put("fpr", 1).
                        put("ultimateActive", ultimateActivated).
                        put("leftOffset", leftOffset).
                        put("rightOffset", rightOffset).
                        put("frameWidth", frameWidth).
                        put("frameHeight", frameHeight).
                        put("isArrow", true).
                        put("isMagic", false));
        // Set the center of the hitbox to be at the player's "hands"
        rangedHitBox.setAnchoredPosition(
                (player.getX() + playerHitBoxOffsetX + (player.getScaleX() > 0 ? playerHitBoxWidth : 0)),
                (player.getY() + playerHitBoxOffsetY + (playerHitBoxHeight / 2)),
                new Point2D(centerX, centerY));
        if (ultimateActivated) {
            rangedHitBox.setScaleX(2);
            rangedHitBox.setScaleY(2);
        }
    }

    @Override
    public void calculateAnglePlayerRelative(Entity player, double mouseCurrX, double mouseCurrY) {
        // adjacent is distance (x) from mouse to player's "hands"
        // opposite is distance (y) from mouse to player's "hands"
        // angle calculated with tangent
        double adjacent = mouseCurrX -
                (player.getX() + playerHitBoxOffsetX
                        + (player.getScaleX() > 0 ? playerHitBoxWidth : 0));
        double opposite = mouseCurrY -
                (player.getY() + playerHitBoxOffsetY
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
}
