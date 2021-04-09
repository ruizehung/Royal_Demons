package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.ImageView;
import uwu.openjfx.MainApp;
import uwu.openjfx.behaviors.CanOnlyInteractOnce;
import uwu.openjfx.behaviors.DropItemComponent;
import uwu.openjfx.behaviors.Interactable;

import java.util.Set;

public class ChestComponent extends CanOnlyInteractOnce implements Interactable {
    private DropItemComponent dropItemComponent;

    @Override
    public void onAdded() {
        super.setBehavior(dropItemComponent);

        double temp = FXGL.random();
        if (temp < 0.33) {
            dropItemComponent.addDropItem("HealthPotion");
            dropItemComponent.addDropItem("RagePotion");
        }  else if (temp < 0.66) {
            dropItemComponent.addDropItem("HealthPotion");
        } else {
            dropItemComponent.addDropItem("RagePotion");
        }
        dropItemComponent.addDropItem(pickAWeaponRandomly());
    }

    @Override
    public void interact() {
        super.interact();
        if (!MainApp.isIsTesting()) {
            changeToOpenedView();
        }
    }

    private String pickAWeaponRandomly() {
        String weaponName = null;
        if (!MainApp.isIsTesting()) {
            Set<String> weaponsSet = FXGL.geto("weaponsSet");
            int item = FXGL.random(0, weaponsSet.size() - 1);
            // In real life, the Random object should be rather more shared than this
            int i = 0;
            for (String weapon : weaponsSet) {
                if (i == item) {
                    weaponName = weapon;
                    break;
                }
                ++i;
            }
        }
        return weaponName;
    }

    // Todo: To be refactored
    public void changeToOpenedView() {
        getEntity().getViewComponent().clearChildren();
        getEntity().getViewComponent().addChild(new ImageView(FXGL.image(
                "chest_empty_open_anim_f2_32x32.png"
        )));
    }

    @Override
    public void disable() {
        setInteractedBefore(true);
        changeToOpenedView();
    }
}
