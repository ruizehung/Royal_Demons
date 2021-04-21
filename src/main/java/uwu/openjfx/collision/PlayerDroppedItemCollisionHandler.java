package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MainApp;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
import uwu.openjfx.UI;
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.items.Item;
import uwu.openjfx.weapons.*;

import java.util.Map;

public class PlayerDroppedItemCollisionHandler extends CollisionHandler {
    public PlayerDroppedItemCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.DROPPEDITEM);
    }

    @Override
    public void onCollision(Entity player, Entity itemEntity) {
        if (MainApp.isIsTesting() || FXGL.getb("Epressed")) {
            String itemName = itemEntity.getString("name");
            System.out.println("Player picks up " + itemName);
            if (!MainApp.isIsTesting()) {
                FXGL.play("ui/weapon_swap.wav");
            }

            // This is a piece of shit !!!!!
            // add things to inventory
            if (itemName.equals("HealthPotion")) {
                UI.addHealthPot();
            } else if (itemName.equals("RagePotion")) {
                UI.addRagePot();
            } else {
                switch (itemName) {
                case "sword0":
                    PlayerComponent.addWeaponToInventory(new GoldenSword0());
                    break;
                case "sword1":
                    PlayerComponent.addWeaponToInventory(new GoldenSword1());
                    break;
                case "sword2":
                    PlayerComponent.addWeaponToInventory(new GoldenSword2());
                    break;
                case "staff0":
                    PlayerComponent.addWeaponToInventory(new MagicStaff0());
                    break;
                case "staff1":
                    PlayerComponent.addWeaponToInventory(new MagicStaff1());
                    break;
                case "staff2":
                    PlayerComponent.addWeaponToInventory(new MagicStaff2());
                    break;
                case "bow0":
                    PlayerComponent.addWeaponToInventory(new Bow0());
                    break;
                case "bow1":
                    PlayerComponent.addWeaponToInventory(new Bow1());
                    break;
                case "bow2":
                    PlayerComponent.addWeaponToInventory(new Bow2());
                    break;
                default:
                    System.out.println("Weapon " + itemName + " not found!");
                }
            }

            if (!MainApp.isIsTesting()) {
                // handle onPickup
                FXGL.play("ui/weapon_swap.wav");
                Item itemObj;
                Map<String, Item> itemNameObjMap = FXGL.geto("itemNameObjMap");
                if (itemNameObjMap.containsKey(itemName)) {
                    itemObj = itemNameObjMap.get(itemName);
                    itemObj.onPickUp(player);
                    // Todo: add itemObj to inventory ?
                }

                itemEntity.removeFromWorld();

                // For items that is spawned by map (not from chest or monster), we don't
                // want it to be respawned each time player enter this room
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
}
