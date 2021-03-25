package uwu.openjfx.weapons;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class Bow_0 implements Weapon, AngleBehavior {

    private AnimatedTexture texture;
    private AnimationChannel animAttack;
    private Entity rangedHitBox;
    private Vec2 dir;
    private int attackDuration = 800; // Milliseconds
    private int ultimateChargeDuration = 1000; // Milliseconds

    public Bow_0() {
        animAttack = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(.5), 8, 8);
        texture = new AnimatedTexture(animAttack);
        texture.loop();
    }

    @Override
    public void attack(Entity player, boolean ultimateActivated, double mouseCurrX, double mouseCurrY) {
        texture.playAnimationChannel(animAttack);
        if (this instanceof AngleBehavior) {
            ((AngleBehavior) this).calculateAnglePlayerRelative(player, mouseCurrX, mouseCurrY);
        }
        if (!ultimateActivated) {
            rangedHitBox = spawn("rangedArrow1HitBox",
                    new SpawnData(
                            player.getScaleX() > 0 ? player.getX() + 20.0 : player.getX() - 15.0,
                            player.getY() + 27.5).put("dir", dir.toPoint2D()));
        } else {
            rangedHitBox = spawn("rangedBowUltimateHitBox",
                    new SpawnData(
                            player.getScaleX() > 0 ? player.getX() + 20.0 : player.getX() - 15.0,
                            player.getY() + 27.5).put("dir", dir.toPoint2D()));
            rangedHitBox.setScaleX(2);
            rangedHitBox.setScaleY(2);
        }
    }

    @Override
    public void calculateAnglePlayerRelative(Entity player, double mouseCurrX, double mouseCurrY) {
        double opposite = mouseCurrY - player.getY();
        double adjacent = mouseCurrX - player.getX();
        double angle = Math.atan2(opposite, adjacent);
        angle = Math.toDegrees(angle);
        dir = Vec2.fromAngle(angle);
    }

    @Override
    public int getDuration(boolean ultimateActivated) {
        return ultimateActivated ? ultimateChargeDuration : attackDuration;
    }
}
