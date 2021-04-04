package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
import uwu.openjfx.items.Item;

import java.util.Map;

public class PlayerDroppedItemCollisionHandler extends CollisionHandler {
    public PlayerDroppedItemCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.DROPPEDITEM);
    }

    @Override
    protected void onCollision(Entity player, Entity itemEntity) {
        if (FXGL.getb("Epressed")) {
            String itemName = itemEntity.getString("name");
            System.out.println("Player picked up " + itemName);

            Item itemObj;
            Map<String, Item> itemNameObjMap = FXGL.geto("itemNameObjMap");
            if (itemNameObjMap.containsKey(itemName)) {
                itemObj = itemNameObjMap.get(itemName);
                itemObj.onPickUp(player);
                // Todo: add itemObj to inventory ?
            }

            itemEntity.removeFromWorld();

            // For items that is spawned by map (not from chest or monster), we don't
            // want it to be respawned each time palyer enter this room
            try {
                IDComponent idComponent = itemEntity.getComponent(IDComponent.class);
                Room curRoom = FXGL.geto("curRoom");
                curRoom.setDroppedItemData(idComponent.getId(), "picked", 1);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}
