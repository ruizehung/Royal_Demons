package uwu.openjfx;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import uwu.openjfx.components.SwordComponent;

public class WeaponFactory implements EntityFactory {
    @Spawns("meleeSword1")
    public Entity newMeleeSword1(SpawnData data) {
        return FXGL.entityBuilder(data)
                .with(new SwordComponent())
                .build();
    }

    @Spawns("meleeSword1HitBoxRect")
    public Entity newMeleeSword1HitBox(SpawnData data) {
        Rectangle hitBox = new Rectangle(50, 75, Color.WHITE);
        System.out.println(hitBox.getX());
        hitBox.setOpacity(0.5);
        return FXGL.entityBuilder(data)
                .type(RoyalType.MELEE)
                .viewWithBBox(hitBox)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("meleeSword1HitBoxCurveRight")
    public Entity newMeleeSword1HitBoxCurveRight(SpawnData data) {
        QuadCurve curve = new QuadCurve();
        curve.setFill(Color.WHITE);
        curve.setStartX(0);
        curve.setStartY(20);
        curve.setEndX(90);
        curve.setEndY(0);
        curve.setControlX(30);
        curve.setControlY(-70);
        curve.setOpacity(0.5);
        return FXGL.entityBuilder(data)
                .type(RoyalType.MELEE)
                .viewWithBBox(curve)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("meleeSword1HitBoxCurveLeft")
    public Entity newMeleeSword1HitBoxCurveLeft(SpawnData data) {
        QuadCurve curve = new QuadCurve();
        curve.setFill(Color.WHITE);
        curve.setStartX(40);
        curve.setStartY(20);
        curve.setEndX(-50);
        curve.setEndY(0);
        curve.setControlX(30);
        curve.setControlY(-70);
        curve.setOpacity(0.5);
        return FXGL.entityBuilder(data)
                .type(RoyalType.MELEE)
                .viewWithBBox(curve)
                .with(new CollidableComponent(true))
                .build();
    }
}
