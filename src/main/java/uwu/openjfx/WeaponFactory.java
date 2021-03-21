package uwu.openjfx;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import uwu.openjfx.components.SwordComponent;

public class WeaponFactory implements EntityFactory {
    @Spawns("meleeSword1")
    public Entity newMeleeSword1(SpawnData data) {
        return FXGL.entityBuilder(data)
                .with(new SwordComponent())
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
                .viewWithBBox(hitBox)
                .with(new CollidableComponent(true))
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
