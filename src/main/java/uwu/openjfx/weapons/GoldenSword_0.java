package uwu.openjfx.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class GoldenSword_0 implements Weapon {
    private Entity meleeHitBox;
    private int attackDuration = 500; // Milliseconds
    private int ultimateChargeDuration = 1000; // Milliseconds
    private int hitBoxWidth;
    private int hitBoxHeight;
    private boolean ultimateActivated;
    private double swordOffset;

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
                        put("frameHeight", height).
                        put("fpr", !ultimateActivated ? 6 : 7));

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
        hitBoxWidth = !ultimateActivated ? 75 : 175;
        hitBoxHeight = !ultimateActivated ? 130 : 175;
        swordOffset = !ultimateActivated ? 25 : 0;
        meleeHitBox = spawn("meleeSwordHitBox",
                new SpawnData(player.getX(), player.getY()).put("width", hitBoxWidth).put("height", hitBoxHeight));
        meleeHitBox.getTransformComponent().setAnchoredPosition(
                new Point2D(
                        player.getX() - (hitBoxWidth / 2) + player.getWidth() / 2
                                + (player.getScaleX() > 0 ? swordOffset : -swordOffset),
                        player.getY() - (hitBoxHeight / 2) + player.getHeight() / 2));
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
