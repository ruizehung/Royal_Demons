package uwu.openjfx;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import uwu.openjfx.model.PlayerControl;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class SimpleFactory implements EntityFactory {
//    @Spawns("enemy")
//    public Entity newEnemy(SpawnData data) {
//        return FXGL.entityBuilder(data)
////                .view(new Rectangle(40, 40, Color.RED))
//                .view("skelet_idle_anim_f0_32x32.png")
//                .with(new ProjectileComponent(new Point2D(1, 0), 150))
//                .build();
//
//    }

//    @Spawns("ally")
//    public Entity newAlly(SpawnData data) {
//        return FXGL.entityBuilder(data)
//                .view("lizard_m_idle_anim_f0_40x70.png")
//                .with(new ProjectileComponent(new Point2D(-1, 0), 150))
//                .build();
//    }

    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        return FXGL.entityBuilder()
                .from(data)
                .type(RoyalType.WALL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
//        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));

        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYER)
                .with(physics)
                .viewWithBBox(new Rectangle(30, 30, Color.TRANSPARENT))
                .with(new CollidableComponent(true))
                .with(new PlayerControl())
                .build();
    }


}
