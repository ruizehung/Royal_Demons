package uwu.openjfx.weapons;

import javafx.scene.image.Image;
/*
    This class is responsible for creating the second level Bow.
 */
public class Bow1 extends Bow implements Weapon, AngleBehavior {
    public Bow1() {
        super(new Image(
            "assets/textures/ui/weapons/bow1_ui.png"),
            "bow1_arrow",
            75,
            "bow1_charge_44x43",
            "bow1_charge_44x43",
            750, 850);
        this.name = "buwu 1";
        this.description = "This is a buwu 1";
        this.inventoryIconPath = "ui/inventory/bow1.png";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bow1;
    }
}
