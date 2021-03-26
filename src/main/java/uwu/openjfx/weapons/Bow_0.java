package uwu.openjfx.weapons;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class Bow_0 implements Weapon, AngleBehavior {

    private Entity rangedHitBox;
    private int leftOffset;
    private int rightOffset;
    private int frameWidth;
    private int frameHeight;
    private Vec2 dir;
    private int speed = 400;
    private int attackDuration = 800; // Milliseconds
    private int ultimateChargeDuration = 1000; // Milliseconds
    private boolean ultimateActivated;
    private double bowOffset = 10;

    @Override
    public void prepAttack(Entity player) {
        int width = 16;
        int height = 32;
        Entity b = spawn("rangedBow",
                new SpawnData(player.getX(),
                        player.getY()).
                        put("weapon", !ultimateActivated ? "bow0_reg_16x32" : "bow0_reg_16x32").
                        put("duration", getDuration(ultimateActivated)).
                        put("frameWidth", width).
                        put("frameHeight", height).
                        put("fpr", !ultimateActivated ? 1 : 1));

        b.getTransformComponent().setAnchoredPosition(
                new Point2D(player.getX() - (width / 2) + player.getWidth() / 2 + bowOffset,
                        player.getY() - (height / 2) + player.getHeight() / 2));
        b.setZIndex(2000);
        if (player.getScaleX() == 1) {
            b.setScaleX(2);
        } else {
            b.setScaleX(-2);
            b.translateX(-bowOffset);
        }
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        leftOffset = !ultimateActivated ? 10 : 10;
        rightOffset = !ultimateActivated ? 10 : 10;
        frameWidth = !ultimateActivated ? 48 : 48;
        frameHeight = !ultimateActivated ? 16 : 16;
        if (this instanceof AngleBehavior) {
            ((AngleBehavior) this).calculateAnglePlayerRelative(player, mouseCurrX, mouseCurrY);
        }
        rangedHitBox = spawn("rangedArrowHitBox",
                new SpawnData(
                        player.getScaleX() > 0 ? player.getX() + 20.0 : player.getX() - 15.0,
                        player.getY() + 27.5).
                        put("dir", dir.toPoint2D()).
                        put("speed", speed).
                        put("ultimateActive", ultimateActivated).
                        put("leftOffset", leftOffset).
                        put("rightOffset", rightOffset).
                        put("frameWidth", frameWidth).
                        put("frameHeight", frameHeight));
        if (ultimateActivated) {
            rangedHitBox.setScaleX(2);
            rangedHitBox.setScaleY(2);
        }
    }

    @Override
    public void calculateAnglePlayerRelative(Entity player, double mouseCurrX, double mouseCurrY) {
        double opposite = mouseCurrY - (player.getY() + 27.5);
        double adjacent = mouseCurrX - (player.getScaleX() > 0 ? player.getX() + 20.0 : player.getX() - 15.0);
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
