package uwu.openjfx.items;

public class Heart extends Item {

    public Heart(String name, int healPoints) {
        super(name);
        setPickupBehavior(new HealWhenPickup(healPoints));
    }
}
