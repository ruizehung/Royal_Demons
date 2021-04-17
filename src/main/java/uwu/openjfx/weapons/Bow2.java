package uwu.openjfx.weapons;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import uwu.openjfx.components.AttackDamageComponent;
import uwu.openjfx.components.ExplosionAtDistComponent;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for creating the third level Bow.
 */
public class Bow2 extends Bow implements Weapon, AngleBehavior {
    public Bow2() {
        super(new Image(
            "assets/textures/ui/weapons/bow2_ui.png"),
            "bow2_arrow",
            80,
            "bow2_charge_44x43",
            "bow2_charge_44x43",
            650, 800);
        this.name = "Marksman's Bow";
        this.description
            = "A Marksman's Bow offers the highest stability, allowing for rapid aim and punishing "
            + "hits. When casting the ultimate, an explosive arrow will be shot and will explode, "
            + "damaging any enemies nearby.";
        this.inventoryIconPath = "ui/inventory/bow2.png";
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        calculateAnglePlayerRelative(player, mouseCurrX, mouseCurrY);
        // top offset used to shrink the top/bot edges of hitbox
        int topBottomOffset = 5;
        // left offset used to shrink the left edge of hitbox
        int leftOffset = 20;
        // right offset used to shrink the right edge of hitbox
        int rightOffset = 12;
        // width of the original frame (32 / 64)
        int frameWidth = 48;
        // height of original frame (32 / 64)
        int frameHeight = 16;

        // the center of the NEW and MODIFIED hitbox
        double centerX = ((double) (leftOffset + (frameWidth - rightOffset)) / 2);
        double centerY = ((double) (topBottomOffset + (frameHeight - topBottomOffset)) / 2);
        double attackDamage = 80;
        String arrow = !ultimateActivated ? "bow2_arrow" : "bombArrow";

        int speed = !ultimateActivated ? 300 : 400; // speed at which magic spell goes
        /*
            Instantiate a brand new arrow that will hold the
            corresponding dimensions, components, and speed. It will temporarily
            spawn the magic spell at the players ORIGINAL getX() and getY() excluding
            its modified hitbox done in CreatureFactory.
         */
        Entity rangedHitBox = spawn("rangedArrowHitBox",
            new SpawnData(
                player.getX(), player.getY()).
                put("dir", dir.toPoint2D()).
                put("speed", speed).
                put("weapon", arrow).
                put("duration", 500).
                put("fpr", 1).
                put("ultimateActive", ultimateActivated).
                put("topBotOffset", topBottomOffset).
                put("leftOffset", leftOffset).
                put("rightOffset", rightOffset).
                put("frameWidth", frameWidth).
                put("frameHeight", frameHeight).
                put("isArrow", true).
                put("isMagic", false).
                put("damage", attackDamage));
        /*
            setLocalAnchor(...) will ensure that the anchor/pivot point of the
            arrow is located at the CENTER of the NEW hitbox.
            setAnchoredPosition(...) will spawn the arrow to the right
            of the player if player is facing right, and left if the player is
            facing left, and located at the player's "hands".
            setRotationOrigin(...) will ensure that the rotation anchor/pivot
            point of the arrow is located at the CENTER of the NEW hitbox.
            The arguments are offsets based off of the top-left point of the
            ORIGINAL frameWidth x frameHeight frame. Therefore, we need to offset
            centerX in the x-direction, and the center of the arrow will
            CONSISTENTLY be at its midpoint in the y-direction.
         */
        rangedHitBox.getComponent(AttackDamageComponent.class).setActive(false);
        rangedHitBox.setLocalAnchor(new Point2D(centerX, centerY));
        rangedHitBox.setAnchoredPosition(
            (player.getX() + playerHitBoxOffsetX
                + (player.getScaleX() > 0 ? playerHitBoxWidth : 0)), // right-left side
            (player.getY() + playerHitBoxOffsetY
                + (playerHitBoxHeight / 2))); // midpoint player hitbox
        rangedHitBox.getTransformComponent().setRotationOrigin(
            new Point2D(centerX, ((double) (frameHeight)) / 2));
        rangedHitBox.addComponent(new ExplosionAtDistComponent(ultimateActivated));

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bow2;
    }
}
