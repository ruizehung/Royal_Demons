package uwu.openjfx;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import uwu.openjfx.components.TrapComponent;


public class StructureFactory implements EntityFactory {
    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.WALL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("door")
    public Entity newDoor(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.DOOR)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("trap-cover")
    public Entity newTrapCover(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.TRAP)
                .viewWithBBox(new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"), Color.BLACK))
                .with(new TrapComponent())
                .build();
    }
    @Spawns("trap-tile")
    public Entity newTrapTile(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.TRAP)
                .view(data.<String>get("tile"))
                .with(new TrapComponent())
                .build();
    }

    @Spawns("trigger")
    public Entity newTrigger(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.TRAP_TRIGGER)
                .viewWithBBox(new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"), Color.TRANSPARENT))
                .with(new CollidableComponent(true))
                .with(new TrapComponent())
                .build();
    }

    @Spawns("point")
    public Entity newPoint(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.POINT)
                .build();
    }

}
