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
        this.name = "buwu";
        this.description = "This is a buwu";
        this.inventoryIconPath = "ui/inventory/bow.png";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bow0;
    }
}
