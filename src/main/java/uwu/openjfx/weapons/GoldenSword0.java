package uwu.openjfx.weapons;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.IrremovableComponent;
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
public class GoldenSword0 implements Weapon {
    private boolean ultimateActivated;
    private final double attackDamage = 50;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GoldenSword0;
    }

    @Override
    public void prepAttack(Entity player) {
        int width; // width of the frame
        int height; // height of the frame
        double swordOffset;
        String chosenAttack;
        int random = (int) (Math.random() * 101);
        if (random < 50) {
            chosenAttack = "gold_knife_swipe_50x100";
            width = 50;
            height = 100;
            swordOffset = 26;

        } else {
            chosenAttack = "gold_knife_stab_69x26";
            width = 69;
            height = 26;
            swordOffset = 22;
        }

        Entity gs = spawn("weapon",
            new SpawnData(
                player.getX(), player.getY()).
                put("weaponFile", !ultimateActivated
                    ? chosenAttack : "").
                put("duration", getDuration(ultimateActivated)).
                put("frameWidth", width).
                put("frameHeight", height).
                put("fpr", !ultimateActivated ? 3 : 4));
        // Spawn the sword at player's "hands"
        gs.getTransformComponent().setAnchoredPosition(
            new Point2D(
                player.getX() - ((double) width / 2) + player.getWidth() / 2
                    + (player.getScaleX() > 0 ? swordOffset : -swordOffset),
                player.getY() - ((double) height / 2) + player.getHeight() / 2));
        if (player.getScaleX() == 1) {
            gs.setScaleX(1);
        } else {
            gs.translateX(width); // smooth reflection over middle axis of player
            gs.setScaleX(-1);
        }
    }

    @Override
    public void attack(Entity player, double mouseCurrX, double mouseCurrY) {
        int hitBoxWidth; // width of the hitbox
        int hitBoxHeight; // height of the hitbox
        double swordOffset; // distance from player the hitbox should spawn

        if (!ultimateActivated) {
            hitBoxWidth = 70;
            hitBoxHeight = 75;
            swordOffset = 22;
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
        } else {
            fanOfKnives(player);
        }
    }

    private void fanOfKnives(Entity player) {
        final double playerHitBoxOffsetX = 3; // player's hitbox own offset from top left
        final double playerHitBoxOffsetY = 15; // player's hitbox own offset from top left
        final double playerHitBoxWidth = 35; // width of player's hitbox from 3 to 38
        final double playerHitBoxHeight = 40; // height of player's hitbox from 15 to 55

        // top offset used to shrink the top/bot edges of hitbox
        int topBottomOffset = 5;
        // left offset used to shrink the left edge of hitbox
        int leftOffset = 8;
        // right offset used to shrink the right edge of hitbox
        int rightOffset = 25;
        // width of the original frame (32 / 64)
        int frameWidth = 48;
        // height of original frame (32 / 64)
        int frameHeight = 16;

        // the center of the NEW and MODIFIED hitbox
        double centerX = ((double) (leftOffset + (frameWidth - rightOffset)) / 2);
        double centerY = ((double) (topBottomOffset + (frameHeight - topBottomOffset)) / 2);

        int speed = 300; // speed at which magic spell goes
        /*
            Instantiate a brand new arrow that will hold the
            corresponding dimensions, components, and speed. It will temporarily
            spawn the magic spell at the players ORIGINAL getX() and getY() excluding
            its modified hitbox done in CreatureFactory.
         */
        int amountOfDaggers = 10;
        Vec2[] angles = new Vec2[amountOfDaggers];
        double angleIncrementer = 2 * Math.PI / amountOfDaggers;
        double angle = 0;
        for (int i = 0; i < angles.length; i++) {
            double x = Math.cos(angle);
            double y = Math.sin(angle);
            angles[i] = new Vec2(new Point2D(x, y));
            angle += angleIncrementer;
        }
        for (Vec2 vec : angles) {
            Entity rangedHitBox = spawn("rangedArrowHitBox",
                new SpawnData(
                    player.getX(), player.getY()).
                    put("dir", vec.toPoint2D()).
                    put("speed", speed).
                    put("weapon", "gold_knife").
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
            rangedHitBox.setLocalAnchor(new Point2D(centerX, centerY));
            rangedHitBox.setAnchoredPosition(
                (player.getX() + playerHitBoxOffsetX + (playerHitBoxWidth / 2)),
                (player.getY() + playerHitBoxOffsetY + (playerHitBoxHeight / 2)));
            rangedHitBox.getTransformComponent().setRotationOrigin(
                new Point2D(centerX, ((double) (frameHeight)) / 2));
            rangedHitBox.addComponent(new IrremovableComponent());
            rangedHitBox.setZIndex(5);
        }
    }

    @Override
    public int getDuration(boolean ultimateActivated) {
        int attackDuration = 450; // charge-up time of attacking in milliseconds
        int ultimateChargeDuration = 500; // charge-up time of attacking in milliseconds
        this.ultimateActivated = ultimateActivated;
        return ultimateActivated ? ultimateChargeDuration : attackDuration;
    }

    @Override
    public Image getWeaponSprite() {
        return new Image("assets/textures/ui/weapons/sword0_ui.png");
    }

    @Override
    public String getWeaponIconPath() {
        return "ui/inventory/sword0.png";
    }

    @Override
    public String getName() {
        return "Golden Dagger";
    }

    @Override
    public String getDescription() {
        return "A Rogue's Dagger may not deal so much damage, but its lightweight aspect allows "
            + "very rapid strikes. When casting the ultimate, the wielder shall shoot a nova "
            + "of daggers outwards.";
    }

    @Override
    public boolean isMeleeAttack() {
        return true;
    }

    @Override
    public int getUltimateCD() {
        return 2;
    }


}
