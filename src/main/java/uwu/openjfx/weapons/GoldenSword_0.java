package uwu.openjfx.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class GoldenSword_0 implements Weapon {
    private Entity meleeHitBox;
    private int attackDuration = 500; // Milliseconds
    private int ultimateChargeDuration = 1000; // Milliseconds
    private int hitBoxWidthReg = 80;
    private int hitBoxHeightReg = 110;
    private int hitBoxWidthUlt = 175;
    private int hitBoxHeightUlt = 175;
    private boolean ultimateActivated;

    @Override
    public void prepAttack(Entity player) {
        int width = !ultimateActivated ? 140 : 175;
        int height = !ultimateActivated ? 140 : 175;
        Entity gs = spawn("meleeSword",
                new SpawnData(player.getX(),
                        player.getY()).
                        put("weapon", !ultimateActivated ? "gold_sword0_slash_140x140" : "gold_sword0_ult_175x175").
                        put("duration", getDuration(ultimateActivated)).
                        put("frameWidth", width).
                        put("frameHeight", height).put("fpr", !ultimateActivated ? 6 : 7));

        gs.getTransformComponent().setAnchoredPosition(
                new Point2D(player.getX() - (width / 2) + player.getWidth() / 2,
                        player.getY() - (height / 2) + player.getHeight() / 2));
        if (player.getScaleX() == 1) {
            gs.setScaleX(1);
        } else {
            gs.translateX(width);
            gs.setScaleX(-1);
        }
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        if (!ultimateActivated) {
            meleeHitBox = spawn("meleeSword1HitBox", player.getScaleX() > 0
                    ? player.getX() : player.getX() - 40, player.getY() - 15);
        } else {
            meleeHitBox = spawn("meleeUltimateHitBox",
                    player.getX() -  67.5, player.getY() - 60.0);
        }
        FXGL.getGameTimer().runAtInterval(() -> {
            if (meleeHitBox.isActive()) {
                meleeHitBox.removeFromWorld();
            }
        }, Duration.seconds(.1));
    }

    @Override
    public int getDuration(boolean ultimateActivated) {
        this.ultimateActivated = ultimateActivated;
        return ultimateActivated ? ultimateChargeDuration : attackDuration;
    }
}
