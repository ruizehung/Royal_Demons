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
import uwu.openjfx.components.AttackThroughComponent;
import uwu.openjfx.components.MagicComponent;
import uwu.openjfx.components.SwordComponent;

public class WeaponFactory implements EntityFactory {
    
    @Spawns("meleeSword1")
    public Entity newMeleeSword1(SpawnData data) {
        return FXGL.entityBuilder(data)
                .with(new SwordComponent())
                .type(RoyalType.TRAP_TRIGGER)
                .viewWithBBox("weapon_golden_sword_32x32.png")
                .build();
    }

    @Spawns("meleeSword1HitBox")
    public Entity newMeleeSword1HitBox(SpawnData data) {
        Rectangle hitBox = new Rectangle(80, 110, Color.WHITE);
        hitBox.setOpacity(0.5);
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .viewWithBBox(hitBox)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("meleeUltimateHitBox")
    public Entity newMeleeUltimateHitBox(SpawnData data) {
        Rectangle hitBox = new Rectangle(175, 175, Color.ORANGE);
        hitBox.setOpacity(0.5);
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .with(new AttackThroughComponent())
                .viewWithBBox(hitBox)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("rangedArrow1HitBox")
    public Entity newRangedArrow1HitBox(SpawnData data) {
        Point2D dir = data.get("dir");
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .viewWithBBox("arrow_temp.png")
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(dir, 400))
                .build();
    }

    @Spawns("rangedBowUltimateHitBox")
    public Entity newRangedUltimateHitBox(SpawnData data) {
        Point2D dir = data.get("dir");
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .viewWithBBox("arrow_temp.png")
                .with(new AttackThroughComponent())
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(dir, 400))
                .build();
    }

    @Spawns("rangedMagic1HitBox")
    public Entity newRangedMagic1HitBox(SpawnData data) {
        Point2D dir = data.get("dir");
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .bbox(new HitBox(BoundingShape.polygon(new Point2D(0, 0), new Point2D(64, 0),
                        new Point2D(64, 64), new Point2D(0, 64))))
                .with(new MagicComponent("fireball"))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(dir, 400))
                .build();
    }

    @Spawns("rangedMagicUltimateHitBox")
    public Entity newRangedMagicUltimateHitBox(SpawnData data) {
        Point2D dir = data.get("dir");
        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYERATTACK)
                .bbox(new HitBox(BoundingShape.polygon(new Point2D(0, 0), new Point2D(32, 0),
                        new Point2D(32, 32), new Point2D(0, 32))))
                .with(new MagicComponent("ultimate"))
                .with(new AttackThroughComponent())
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(dir, 400))
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
}
