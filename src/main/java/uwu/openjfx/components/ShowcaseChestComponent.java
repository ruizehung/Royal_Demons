package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.ImageView;
import uwu.openjfx.MainApp;
import uwu.openjfx.behaviors.CanOnlyInteractOnce;
import uwu.openjfx.behaviors.DropItemComponent;
import uwu.openjfx.behaviors.Interactable;

public class ShowcaseChestComponent extends CanOnlyInteractOnce implements Interactable {
    private DropItemComponent dropItemComponent;
    private String weaponName;

    public ShowcaseChestComponent(String weaponName) {
        this.weaponName = weaponName;
    }

    @Override
    public void onAdded() {
        super.setBehavior(dropItemComponent);
        dropItemComponent.addDropItem(weaponName);
    }

    @Override
    public void interact() {
        super.interact();
        if (!MainApp.isIsTesting()) {
            changeToOpenedView();
        }
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
