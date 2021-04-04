package uwu.openjfx.items;


import com.almasb.fxgl.entity.Entity;
import uwu.openjfx.components.CreatureComponent;

public class HealWhenPickup implements PickupBehavior {

    private int healPoints;

    public HealWhenPickup(int healPoints) {
        this.healPoints = healPoints;
    }

    @Override
    public void onPickUp(Entity entity) {
        CreatureComponent creatureComponent = entity.getObject("CreatureComponent");
        creatureComponent.increaseHealth(healPoints);
    }
}
