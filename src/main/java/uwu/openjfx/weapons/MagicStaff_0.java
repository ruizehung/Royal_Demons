package uwu.openjfx.weapons;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class MagicStaff_0 implements Weapon, AngleBehavior {

    private AnimatedTexture texture;
    private AnimationChannel animAttack;
    private Entity rangedHitBox;
    private Vec2 dir;
    private int attackDuration = 1200; // Milliseconds
    private int ultimateChargeDuration = 1500; // Milliseconds
    private boolean ultimateActivated;

    public MagicStaff_0() {
        animAttack = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(.5), 8, 8);
        texture = new AnimatedTexture(animAttack);
        texture.loop();
    }

    @Override
    public void prepAttack(Entity player) {
//        Entity gs = spawn("meleeSword", new SpawnData(0, 0).put("weapon", "gs0"));
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        texture.playAnimationChannel(animAttack);
        if (this instanceof AngleBehavior) {
            ((AngleBehavior) this).calculateAnglePlayerRelative(player, mouseCurrX, mouseCurrY);
        }
        rangedHitBox = spawn(!ultimateActivated ? "rangedMagic1HitBox" : "rangedMagicUltimateHitBox",
                new SpawnData(
                        player.getScaleX() > 0 ? player.getX() + 8.0 : player.getX() - 32,
                        player.getY() + 3.5).put("dir", dir.toPoint2D()));
        if (ultimateActivated) {
            rangedHitBox.setScaleX(2);
            rangedHitBox.setScaleY(2);
        }
    }

    @Override
    public void calculateAnglePlayerRelative(Entity player, double mouseCurrX, double mouseCurrY) {
        double opposite = mouseCurrY - (player.getY() + 3.5);
        double adjacent = mouseCurrX - (player.getScaleX() > 0 ? player.getX() + 8.0 : player.getX() - 32);
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
