package uwu.openjfx.items;


import com.almasb.fxgl.entity.Entity;

public class Item implements Usable, PickupBehavior {
    private String name;
    private Usable useBehavior;
    private PickupBehavior pickupBehavior;

    public Item(String name) {
        this.name = name;
        this.useBehavior = new CannotUse();
        this.pickupBehavior = new DoNothingOnPickup();
    }

    public String getName() {
        return name;
    }

    public Usable getUseBehavior() {
        return useBehavior;
    }

    public void setUseBehavior(Usable useBehavior) {
        this.useBehavior = useBehavior;
    }

    public PickupBehavior getPickupBehavior() {
        return pickupBehavior;
    }

    public void setPickupBehavior(PickupBehavior pickupBehavior) {
        this.pickupBehavior = pickupBehavior;
    }

    public boolean isUsable() {
        return useBehavior.isUsable();
    }

    public void use(Entity entity) {
        if (useBehavior.isUsable()) {
            useBehavior.use(entity);
        }
    }

    @Override
    public void onPickUp(Entity entity) {
        pickupBehavior.onPickUp(entity);
    }
}
