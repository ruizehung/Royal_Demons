package uwu.openjfx.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import uwu.openjfx.MainApp;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for the following:
    - Spawning the slash hitbox at a specific location relative to player.
    - Spawn the player-sword in the prepAttack() method, whose animation
    will be handled in the WeaponAnimationComponent class.
 */
public class GoldenSword1 implements Weapon {
    private boolean ultimateActivated;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GoldenSword1;
    }

    @Override
    public void prepAttack(Entity player) {
        int width = !ultimateActivated ? 140 : 175; // width of the frame
        int height = !ultimateActivated ? 140 : 175; // height of the frame
        Entity gs = spawn("weapon",
                new SpawnData(
                        player.getX(), player.getY()).
                        put("weaponFile", !ultimateActivated
                                ? "gold_sword0_slash_140x140" : "gold_sword0_ult_175x175").
                        put("duration", getDuration(ultimateActivated)).
                        put("frameWidth", width).
                        put("frameHeight", height).
                        put("fpr", !ultimateActivated ? 6 : 6));
        // Spawn the sword at player's "hands"
        gs.getTransformComponent().setAnchoredPosition(
                new Point2D(player.getX() - ((double) width / 2) + player.getWidth() / 2,
                        player.getY() - ((double) height / 2) + player.getHeight() / 2));
        gs.setZIndex(5);
        if (player.getScaleX() == 1) {
            gs.setScaleX(1);
        } else {
            gs.translateX(width); // smooth reflection over middle axis of player
            gs.setScaleX(-1);
        }
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        int hitBoxWidth = !ultimateActivated ? 82 : 175; // width of the hitbox
        int hitBoxHeight = !ultimateActivated ? 130 : 175; // height of the hitbox
        double swordOffset = !ultimateActivated ? 22 : 0; // distance from player hitbox spawns
        double attackDamage = 50;
        String attSound = ultimateActivated ? "skills/sword_ulti.wav" : "skills/sword_basic.wav";
        FXGL.play(attSound);

        Entity meleeHitBox = spawn("meleeSwordHitBox",
                new SpawnData(player.getX(), player.getY()).
                        put("width", hitBoxWidth).put("height", hitBoxHeight).
                        put("damage", attackDamage));
        // Spawn hitbox on top of player and apply offset
        meleeHitBox.getTransformComponent().setAnchoredPosition(
                new Point2D(
                        player.getX() - ((double) hitBoxWidth / 2) + player.getWidth() / 2
                                + (player.getScaleX() > 0 ? swordOffset : -swordOffset),
                        player.getY() - ((double) hitBoxHeight / 2) + player.getHeight() / 2));
        MainApp.addToHitBoxDestruction(meleeHitBox);
    }

    @Override
    public int getDuration(boolean ultimateActivated) {
        int attackDuration = 750; // charge-up time of attacking in milliseconds
        int ultimateChargeDuration = 1000; // charge-up time of attacking in milliseconds
        this.ultimateActivated = ultimateActivated;
        return ultimateActivated ? ultimateChargeDuration : attackDuration;
    }

    @Override
    public Image getWeaponSprite() {
        return new Image("assets/textures/ui/weapons/sword1_ui.png");
    }

    @Override
    public String getWeaponIconPath() {
        return "ui/inventory/sword1.png";
    }

    @Override
    public String getName() {
        return "Golden Sword";
    }

    @Override
    public String getDescription() {
        return "Squire Knight's Sword wielded by the kingdom's newest warriors. "
            + "The length of this sword allows intermediary reach. When casting the "
            + "ultimate, all enemies surrounding the wielder shall be struck.";
    }

    @Override
    public boolean isMeleeAttack() {
        return true;
    }

    @Override
    public int getUltimateCD() {
        return 3;
    }
}
