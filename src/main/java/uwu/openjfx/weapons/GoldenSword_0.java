package uwu.openjfx.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.entity.Entity;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class GoldenSword_0 implements Weapon {
    private AnimatedTexture texture;
    private AnimationChannel animAttack;
    private Entity meleeHitBox;
    private int attackDuration = 500; // Milliseconds
    private int ultimateChargeDuration = 1000; // Milliseconds

    public GoldenSword_0() {
        animAttack = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(.5), 8, 8);
        texture = new AnimatedTexture(animAttack);
        texture.loop();
    }

    @Override
    public void attack(Entity player, boolean ultimateActivated, double mouseCurrX, double mouseCurrY) {
        texture.playAnimationChannel(animAttack);
        if (!ultimateActivated) {
            meleeHitBox = spawn("meleeSword1HitBox", player.getScaleX() > 0
                    ? player.getX() : player.getX() - 40, player.getY() - 15);
        } else {
            meleeHitBox = spawn("meleeUltimateHitBox",
                    player.getX() -  67.5, player.getY() - 60.0);
        }
        FXGL.getGameTimer().runAtInterval(() -> {
            meleeHitBox.removeFromWorld();
        }, Duration.seconds(.01));
    }

    @Override
    public int getDuration(boolean ultimateActivated) {
        return ultimateActivated ? ultimateChargeDuration : attackDuration;
    }
}
