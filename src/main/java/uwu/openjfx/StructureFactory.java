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
import javafx.beans.property.BooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import uwu.openjfx.components.ChestComponent;
import uwu.openjfx.components.ItemComponent;
import uwu.openjfx.components.MimicChestComponent;
import uwu.openjfx.components.TrapComponent;

import java.util.Map;

public class StructureFactory implements EntityFactory {
    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.WALL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get(
                        "width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("projectile-penetrable-wall")
    public Entity newProjectilePenetrableWall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.PPWall)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get(
                        "width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("door")
    public Entity newDoor(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.DOOR)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),
                        data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("trap-cover")
    public Entity newTrapCover(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.TRAP)
                .viewWithBBox(new Rectangle(data.<Integer>get("width"),
                        data.<Integer>get("height"), Color.BLACK))
                .with(new TrapComponent())
                .build();
    }

    @Spawns("trap-black-wall")
    public Entity newTrapWall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.TRAP)
                .viewWithBBox(new Rectangle(data.<Integer>get("width"),
                        data.<Integer>get("height"), Color.BLACK))
                .with(new PhysicsComponent())
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
                .viewWithBBox(new Rectangle(data.<Integer>get("width"),
                        data.<Integer>get("height"), Color.TRANSPARENT))
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

    @Spawns("lever")
    public Entity newLever(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.TRAP_TRIGGER)
                .viewWithBBox("lever-left.png")
                .with(new TrapComponent(true))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("itemOnFloor")
    public Entity newItemFromMap(SpawnData data) {
        String itemName = data.<String>get("name");
        String assetName = ((Map<String, String>) FXGL.geto("itemsNameAssetMap"))
                .get(itemName);

        int degree = data.<Boolean>get("isWeapon") ? 80 : 0;

        return FXGL.entityBuilder(data)
                .type(RoyalType.DROPPEDITEM)
                .viewWithBBox(assetName)
                .rotate(degree)
                .with(new ItemComponent(itemName))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("chest")
    public Entity newChest(SpawnData data) {
        ChestComponent chestComponent = new ChestComponent();
        return FXGL.entityBuilder(data)
                .type(RoyalType.CHEST)
                .viewWithBBox("chest_empty_open_anim_f0_32x32.png")
                .with(chestComponent)
                .with(new CollidableComponent(true))
                .with("chestComponent", chestComponent)
                .build();
    }

    @Spawns("mimicChest")
    public Entity newMimicChest(SpawnData data) {
        MimicChestComponent mimicChestComponent = new MimicChestComponent();
        return FXGL.entityBuilder(data)
                .type(RoyalType.CHEST)
                .viewWithBBox("chest_empty_open_anim_f0_32x32.png")
                .with(mimicChestComponent)
                .with(new CollidableComponent(true))
                .with("chestComponent", mimicChestComponent)
                .build();
    }

}
