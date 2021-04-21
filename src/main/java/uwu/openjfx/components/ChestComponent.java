package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.ImageView;
import uwu.openjfx.MainApp;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.behaviors.CanOnlyInteractOnce;
import uwu.openjfx.behaviors.DropItemComponent;
import uwu.openjfx.behaviors.Interactable;

import java.util.Arrays;

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
        if (!MainApp.isIsTesting()) {
            Room currentRoom = FXGL.geto("curRoom");
            int distanceFromStartRoom = currentRoom.getDistFromInitRoom();
            int itemIndex = FXGL.random(0, 2);

            if (distanceFromStartRoom <= 2) {
                return Arrays.asList("sword0", "bow0", "staff0").get(itemIndex);
            } else if (distanceFromStartRoom <= 4) {
                return Arrays.asList("sword1", "bow1", "staff1").get(itemIndex);
            } else {
                return Arrays.asList("sword2", "bow2", "staff2").get(itemIndex);
            }
        }
        return null;
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
