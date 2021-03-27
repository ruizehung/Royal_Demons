package uwu.openjfx.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for spawning the slash hitbox at a specific location relative to player.
    This class will spawn the player-sword in the prepAttack() method, whose animation
    will be handled in the SwordComponent class.
 */
public class GoldenSword0 implements Weapon {
    private Entity meleeHitBox; // slash hitbox to be spawned (ultimate / nonultimate)
    private int attackDuration = 500; // charge-up time of attacking in milliseconds
    private int ultimateChargeDuration = 1000; // charge-up time of attacking in milliseconds
    private int hitBoxWidth; // width of the hitbox
    private int hitBoxHeight; // height of the hitbox
    private double swordOffset; // distance from player the hitbox should spawn
    private boolean ultimateActivated;

    @Override
    public void prepAttack(Entity player) {
        int width = !ultimateActivated ? 140 : 175; // width of the frame
        int height = !ultimateActivated ? 140 : 175; // height of the frame
        Entity gs = spawn("meleeSword",
                new SpawnData(
                        player.getX(), player.getY()).
                        put("weapon", !ultimateActivated
                                ? "gold_sword0_slash_140x140" : "gold_sword0_ult_175x175").
                        put("duration", getDuration(ultimateActivated)).
                        put("frameWidth", width).
                        put("frameHeight", height).
                        put("fpr", !ultimateActivated ? 6 : 7));
        // Spawn the sword at player's "hands"
        gs.getTransformComponent().setAnchoredPosition(
                new Point2D(player.getX() - (width / 2) + player.getWidth() / 2,
                        player.getY() - (height / 2) + player.getHeight() / 2));
        if (player.getScaleX() == 1) {
            gs.setScaleX(1);
        } else {
            gs.translateX(width); // smooth reflection over middle axis of player
            gs.setScaleX(-1);
        }
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        hitBoxWidth = !ultimateActivated ? 82 : 175;
        hitBoxHeight = !ultimateActivated ? 130 : 175;
        swordOffset = !ultimateActivated ? 22 : 0;
        meleeHitBox = spawn("meleeSwordHitBox",
                new SpawnData(player.getX(), player.getY())
                        .put("width", hitBoxWidth).put("height", hitBoxHeight));
        // Spawn hitbox on top of player and apply offset
        meleeHitBox.getTransformComponent().setAnchoredPosition(
                new Point2D(
                        player.getX() - (hitBoxWidth / 2) + player.getWidth() / 2
                                + (player.getScaleX() > 0 ? swordOffset : -swordOffset),
                        player.getY() - (hitBoxHeight / 2) + player.getHeight() / 2));
        // Delete the hitbox after attacking
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
