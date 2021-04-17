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
        this.name = "Huntsman's Bow";
        this.description
            = "A Huntsman's Bow offers decent stability, allowing for quicker aim and stronger "
            + "hits. When casting the ultimate, a volley of arrows will be shot outwards from the "
            + "wielder.";
        this.inventoryIconPath = "ui/inventory/bow1.png";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bow1;
    }
}
