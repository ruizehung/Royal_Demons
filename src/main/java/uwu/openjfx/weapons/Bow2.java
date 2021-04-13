package uwu.openjfx.weapons;

import javafx.scene.image.Image;
/*
    This class is responsible for creating the third level Bow.
 */
public class Bow2 extends Bow implements Weapon, AngleBehavior {
    public Bow2() {
        super(new Image(
                "assets/textures/ui/weapons/bow2_ui.png"),
            "bow2_arrow",
            75,
            "bow2_charge_44x43",
            "bow2_ult_75x43",
            650, 800);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bow2;
    }
}
