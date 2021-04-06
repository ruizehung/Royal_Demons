package uwu.openjfx.items;

import uwu.openjfx.behaviors.IncreaseMaxHealth;

public class Heart extends Item {

    public Heart(String name, int healPoints) {
        super(name, false);
        setPickupBehavior(new IncreaseMaxHealth(healPoints));
    }
}
