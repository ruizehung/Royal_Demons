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

public class MagicStaff0 implements Weapon, AngleBehavior {

    private AnimatedTexture texture;
    private AnimationChannel animAttack;
    private Entity rangedHitBox;
    private int leftOffset;
    private int rightOffset;
    private int frameWidth;
    private int frameHeight;
    private Vec2 dir;
    private int speed = 400;
    private int attackDuration = 1200; // Milliseconds
    private int ultimateChargeDuration = 1500; // Milliseconds
    private boolean ultimateActivated;

    public MagicStaff0() {
        animAttack = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(.5), 8, 8);
        texture = new AnimatedTexture(animAttack);
        texture.loop();
    }

    @Override
    public void prepAttack(Entity player) {
        // Entity gs = spawn("meleeSword", new SpawnData(0, 0).put("weapon", "gs0"));
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        leftOffset = !ultimateActivated ? 20 : 10;
        rightOffset = !ultimateActivated ? 30 : 15;
        frameWidth = !ultimateActivated ? 64 : 32;
        frameHeight = !ultimateActivated ? 64 : 32;
        texture.playAnimationChannel(animAttack);
        // double centerToPath = Math.sqrt(Math.pow(frameWidth / 2, 2)
        //         + Math.pow(frameHeight / 2, 2));
        // mouseCurrY -= centerToPath;
        if (this instanceof AngleBehavior) {
            ((AngleBehavior) this).calculateAnglePlayerRelative(player, mouseCurrX, mouseCurrY);
        }
        rangedHitBox = spawn("rangedMagicHitBox",
                new SpawnData(
                        player.getX() + 3 - (frameWidth / 2) + 38,
                        player.getY() + 15 - (frameHeight / 2) + 20).
                        put("dir", dir.toPoint2D()).
                        put("speed", speed).
                        put("ultimateActive", ultimateActivated).
                        put("spell", !ultimateActivated ? "fireball" : "ultimate").
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
        double opposite = mouseCurrY - (player.getY() + 15 - (frameHeight / 2) + 20);
        double adjacent = mouseCurrX - (player.getX() + 3 - (frameWidth / 2) + 38);
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
