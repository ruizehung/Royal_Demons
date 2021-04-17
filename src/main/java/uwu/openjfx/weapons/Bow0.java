package uwu.openjfx.weapons;

import javafx.scene.image.Image;
/*
    This class is responsible for creating the first level Bow.
 */
public class Bow0 extends Bow implements Weapon, AngleBehavior {
    public Bow0() {
        super(new Image(
            "assets/textures/ui/weapons/bow0_ui.png"),
            "bow0_arrow",
            60,
            "bow0_charge_44x43",
            "bow0_charge_44x43",
            900, 1000);
        this.name = "Archer's Bow";
        this.description
            = "An Archer's Bow may not offer the most stability, but slow and good aim leads "
            + "to very punishing hits. When casting the ultimate, a volley of arrows will be shot "
            + "outwards from the wielder.";
        this.inventoryIconPath = "ui/inventory/bow0.png";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bow0;
    }
}
