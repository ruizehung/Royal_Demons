package uwu.openjfx.weapons;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for spawning the magic spell at a specific location relative to player.
    This class will calculate the angle relative to mouse-pressed location and fire the magic spell
    towards this location.
    This class will spawn the player-staff(wand) in the prepAttack() method, whose animation
    will be handled in the WandComponent class.
 */
public class MagicStaff0 implements Weapon, AngleBehavior {

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
    private int attackDuration = 1200; // charge-up time of attacking in milliseconds
    private int ultimateChargeDuration = 1500; // charge-up time of attacking in milliseconds
    private boolean ultimateActivated;

    @Override
    public void prepAttack(Entity player) {
        // Entity gs = spawn("meleeSword", new SpawnData(0, 0).put("weapon", "gs0"));
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        // Confirm goal is to shoot a projectile, then calculate angle
        if (this instanceof AngleBehavior) {
            ((AngleBehavior) this).calculateAnglePlayerRelative(player, mouseCurrX, mouseCurrY);
        }
        leftOffset = !ultimateActivated ? 20 : 10;
        rightOffset = !ultimateActivated ? 20 : 10;
        frameWidth = !ultimateActivated ? 64 : 32;
        frameHeight = !ultimateActivated ? 64 : 32;
        centerX = ((double) (leftOffset + (frameWidth - rightOffset)) / 2);
        centerY = ((double) (leftOffset + (frameHeight - leftOffset)) / 2);
        if (player.getScaleX() == -1) {
            centerX -= leftOffset;
            centerY -= leftOffset;
        }
        rangedHitBox = spawn("rangedMagicHitBox",
                new SpawnData(
                        player.getX(), player.getY()).
                        put("dir", dir.toPoint2D()).
                        put("speed", speed).
                        put("weapon", !ultimateActivated ? "fireball_64x64" : "magic1_32x32").
                        put("duration", 500).
                        put("fpr", !ultimateActivated ? 60 : 30).
                        put("ultimateActive", ultimateActivated).
                        put("leftOffset", leftOffset).
                        put("rightOffset", rightOffset).
                        put("frameWidth", frameWidth).
                        put("frameHeight", frameHeight).
                        put("isArrow", false).
                        put("isMagic", true));

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
}
