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
import uwu.openjfx.components.ArrowComponent;
import uwu.openjfx.components.AttackThroughComponent;
import uwu.openjfx.components.BowComponent;
import uwu.openjfx.components.MagicComponent;
import uwu.openjfx.components.SwordComponent;

public class WeaponFactory implements EntityFactory {

    // region Animations
    @Spawns("meleeSword")
    public Entity newMeleeSword(SpawnData data) {
        String weapon = data.get("weapon");
        int duration = data.get("duration");
        int frameWidth = data.get("frameWidth");
        int frameHeight = data.get("frameHeight");
        int fpr = data.get("fpr");
        return FXGL.entityBuilder(data)
                .with(new SwordComponent(weapon, duration, frameWidth, frameHeight, fpr))
                .type(RoyalType.TRAP_TRIGGER)
                .build();
    }

    @Spawns("rangedBow")
    public Entity newRangedBow(SpawnData data) {
        String weapon = data.get("weapon");
        int duration = data.get("duration");
        int frameWidth = data.get("frameWidth");
        int frameHeight = data.get("frameHeight");
        int fpr = data.get("fpr");
        return FXGL.entityBuilder(data)
                .with(new BowComponent(weapon, duration, frameWidth, frameHeight, fpr))
                .type(RoyalType.TRAP_TRIGGER)
                .build();
    }
    //endregion

    // region Hitboxes
    @Spawns("meleeSwordHitBox")
    public Entity newMeleeSword1HitBox(SpawnData data) {
        int rectWidth = data.get("width");
        int rectHeight = data.get("height");
        Rectangle hitBox = new Rectangle(rectWidth, rectHeight, Color.WHITE);
        hitBox.setOpacity(.5);
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .viewWithBBox(hitBox)
                .with(new AttackThroughComponent(true))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("rangedArrowHitBox")
    public Entity newRangedArrowHitBox(SpawnData data) {
        int leftOffset = data.get("leftOffset");
        int rightOffset = data.get("rightOffset");
        int frameWidth = data.get("frameWidth");
        int frameHeight = data.get("frameHeight");
        Point2D dir = data.get("dir");
        int speed = data.get("speed");
        boolean ultimateActive = data.get("ultimateActive");
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .view("./weapons/bow/arrow.png")
                .bbox(new HitBox(BoundingShape.polygon(
                        new Point2D(leftOffset, leftOffset),
                        new Point2D(frameWidth - rightOffset, leftOffset),
                        new Point2D(frameWidth - rightOffset, frameHeight - leftOffset),
                        new Point2D(leftOffset, frameHeight - leftOffset))))
                .with(new ArrowComponent())
                .with(new AttackThroughComponent(ultimateActive))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(dir, speed))
                .build();
    }

    @Spawns("rangedMagicHitBox")
    public Entity newRangedMagic1HitBox(SpawnData data) {
        int leftOffset = data.get("leftOffset");
        int rightOffset = data.get("rightOffset");
        int frameWidth = data.get("frameWidth");
        int frameHeight = data.get("frameHeight");
        Point2D dir = data.get("dir");
        int speed = data.get("speed");
        boolean ultimateActive = data.get("ultimateActive");
        String magicSpell = data.get("spell");
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .bbox(new HitBox(BoundingShape.polygon(
                        new Point2D(leftOffset, leftOffset),
                        new Point2D(frameWidth - rightOffset, leftOffset),
                        new Point2D(frameWidth - rightOffset, frameHeight - leftOffset),
                        new Point2D(leftOffset, frameHeight - leftOffset))))
                .with(new MagicComponent(magicSpell))
                .with(new AttackThroughComponent(ultimateActive))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(dir, speed))
                .build();
    }

    @Spawns("meleeEnemyAttack")
    public Entity newMeleeHitBox(SpawnData data) {
        Rectangle hitBox = new Rectangle(80, 60, Color.RED);
        hitBox.setOpacity(0.5);
        return FXGL.entityBuilder(data)
                .type(RoyalType.ENEMYATTACK)
                .viewWithBBox(hitBox)
                .with(new CollidableComponent(true))
                .build();
    }
    // endregion
}
