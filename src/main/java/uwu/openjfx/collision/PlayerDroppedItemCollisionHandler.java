package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
import uwu.openjfx.UI;
import uwu.openjfx.components.ItemComponent;

import java.util.List;

public class PlayerDroppedItemCollisionHandler extends CollisionHandler {
    public PlayerDroppedItemCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.DROPPEDITEM);
    }

    @Override
    protected void onCollision(Entity player, Entity item) {
        if (FXGL.getb("Epressed")) {
            System.out.println("Player picked up "
                    + item.getComponent(ItemComponent.class).getName());
            item.removeFromWorld();
            if (item.getComponent(ItemComponent.class).getName().equals("HealthPotion")) {
                UI.addHealthPot();
            }
            if (item.getComponent(ItemComponent.class).getName().equals("RagePotion")) {
                UI.addRagePot();
            } else {
                List<String> weaponsList = FXGL.geto("weaponsList");
                // todo: weapon interaction
            }

            // For items that is spawned by map (not from chest or monster), we don't
            // want it to be respawned each time palyer enter this room
            try {
                IDComponent idComponent = item.getComponent(IDComponent.class);
                Room curRoom = FXGL.geto("curRoom");
                curRoom.setDroppedItemData(idComponent.getId(), "picked", 1);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}
