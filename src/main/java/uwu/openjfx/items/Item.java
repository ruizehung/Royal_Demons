package uwu.openjfx.items;


import com.almasb.fxgl.entity.Entity;
import uwu.openjfx.behaviors.Behavior;
import uwu.openjfx.behaviors.DoNothing;

public class Item implements Usable, PickupBehavior {
    private String name;
    private Behavior useBehavior;
    private Behavior pickupBehavior;
    private boolean canBeUsed;

    public Item(String name, boolean canBeUsed) {
        this.name = name;
        this.canBeUsed = canBeUsed;
        this.useBehavior = new DoNothing();
        this.pickupBehavior = new DoNothing();
    }

    public String getName() {
        return name;
    }

    public Behavior getUseBehavior() {
        return useBehavior;
    }

    public void setUseBehavior(Behavior useBehavior) {
        this.useBehavior = useBehavior;
    }

    public Behavior getPickupBehavior() {
        return pickupBehavior;
    }

    public void setPickupBehavior(Behavior pickupBehavior) {
        this.pickupBehavior = pickupBehavior;
    }

    public boolean isUsable() {
        return canBeUsed;
    }

    @Override
    public void use(Entity entity) {
        if (canBeUsed) {
            useBehavior.act(entity);
        }
    }

    @Override
    public void onPickUp(Entity entity) {
        pickupBehavior.act(entity);
    }
}
