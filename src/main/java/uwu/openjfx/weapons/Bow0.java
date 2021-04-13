package uwu.openjfx.weapons;

import javafx.scene.image.Image;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for creating the first level Bow.
 */
public class Bow0 extends Bow implements Weapon, AngleBehavior {
    public Bow0() {
        super(new Image(
            "assets/textures/ui/weapons/bow0_ui.png"),
            "ui/inventory/bow.png",
            "bow0_arrow",
            "Buwu0",
            60,
            "bow0_charge_44x43",
            "bow0_ult_75x43",
            900, 1000);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bow0;
    }
}
