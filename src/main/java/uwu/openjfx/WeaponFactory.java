package uwu.openjfx;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import uwu.openjfx.components.AttackDamageComponent;
import uwu.openjfx.components.ProjectileAnimationComponent;
import uwu.openjfx.components.WeaponAnimationComponent;

/*
    This class is responsible for creating the following:
    - weapon sprites
    - attack hitboxes to collide with enemies
    - projectiles
 */
public class WeaponFactory implements EntityFactory {

    // region Animations
    @Spawns("weapon")
    public Entity newWeapon(SpawnData data) {
        String weapon = data.get("weaponFile");
        int duration = data.get("duration");
        int frameWidth = data.get("frameWidth");
        int frameHeight = data.get("frameHeight");
        int fpr = data.get("fpr");
        return FXGL.entityBuilder(data)
                .with(new WeaponAnimationComponent(weapon, duration, frameWidth, frameHeight, fpr))
                .type(RoyalType.TRAP_TRIGGER)
                .build();
    }
    //endregion

    // region Hitboxes
    @Spawns("meleeSwordHitBox")
    public Entity newMeleeSword1HitBox(SpawnData data) {
        int rectWidth = data.get("width");
        int rectHeight = data.get("height");
        double attackDamage = data.get("damage");
        Rectangle hitBox = new Rectangle(rectWidth, rectHeight, Color.WHITE);
        //hitBox.setOpacity(.5);
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .viewWithBBox(hitBox)
                .with(new AttackDamageComponent(true, attackDamage))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("rangedArrowHitBox")
    public Entity newRangedArrowHitBox(SpawnData data) {
        int topBotOffset = data.get("topBotOffset");
        int leftOffset = data.get("leftOffset");
        int rightOffset = data.get("rightOffset");
        int frameWidth = data.get("frameWidth");
        int frameHeight = data.get("frameHeight");
        Point2D dir = data.get("dir");
        int speed = data.get("speed");
        boolean ultimateActive = data.get("ultimateActive");
        String weapon = data.get("weapon");
        int duration = data.get("duration");
        int fpr = data.get("fpr");
        boolean isArrow = data.get("isArrow");
        boolean isMagic = data.get("isMagic");
        double attackDamage = data.get("damage");

        double widthHitBox = (frameWidth - rightOffset) - leftOffset;
        double heightHitBox = (frameHeight - topBotOffset) - topBotOffset;
        double newCenterXOffset = leftOffset;
        double newCenterYOffset = topBotOffset;
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .view("./weapons/" + weapon + ".png")
                .bbox(new HitBox(
                        new Point2D(
                                newCenterXOffset,
                                newCenterYOffset),
                        BoundingShape.box(
                                widthHitBox,
                                heightHitBox)))
                .with(new ProjectileAnimationComponent(
                        weapon, duration, frameWidth, frameHeight, fpr, isArrow, isMagic))
                .with(new AttackDamageComponent(ultimateActive, attackDamage))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(dir, speed))
                .build();
    }

    @Spawns("rangedMagicHitBox")
    public Entity newRangedMagicHitBox(SpawnData data) {
        int topBotOffset = data.get("topBotOffset");
        int leftOffset = data.get("leftOffset");
        int rightOffset = data.get("rightOffset");
        int frameWidth = data.get("frameWidth");
        int frameHeight = data.get("frameHeight");
        Point2D dir = data.get("dir");
        int speed = data.get("speed");
        boolean ultimateActive = data.get("ultimateActive");
        String weapon = data.get("weapon");
        int duration = data.get("duration");
        int fpr = data.get("fpr");
        boolean isArrow = data.get("isArrow");
        boolean isMagic = data.get("isMagic");
        double attackDamage = data.get("damage");

        double widthHitBox = (frameWidth - rightOffset) - leftOffset;
        double heightHitBox = (frameHeight - topBotOffset) - topBotOffset;
        double newCenterXOffset = leftOffset;
        double newCenterYOffset = topBotOffset;
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .bbox(new HitBox(
                        new Point2D(
                                newCenterXOffset,
                                newCenterYOffset),
                        BoundingShape.box(
                                widthHitBox,
                                heightHitBox)))
                .with(new ProjectileAnimationComponent(
                        weapon, duration, frameWidth, frameHeight, fpr, isArrow, isMagic))
                .with(new AttackDamageComponent(ultimateActive, attackDamage))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(dir, speed))
                .build();
    }

    @Spawns("meleeEnemyPunch")
    public Entity newMeleeHitBox(SpawnData data) {
        int frameWidth = data.get("widthBox");
        int frameHeight = data.get("heightBox");
        Rectangle hitBox = new Rectangle(frameWidth, frameHeight, Color.RED);
        hitBox.setOpacity(0.5);
        return FXGL.entityBuilder(data)
                .type(RoyalType.ENEMYATTACK)
                .viewWithBBox(hitBox)
                .with(new CollidableComponent(true))
                .build();
    }
    // endregion
}
