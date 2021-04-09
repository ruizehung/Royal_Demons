package uwu.openjfx.items;

import uwu.openjfx.behaviors.IncreaseMaxHealth;

public class Heart extends Item {

    public Heart(String name, int maxHealthIncreased) {
        super(name, false);
        setPickupBehavior(new IncreaseMaxHealth(maxHealthIncreased));
    }
}
