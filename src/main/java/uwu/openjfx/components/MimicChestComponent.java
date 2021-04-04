package uwu.openjfx.components;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import javafx.scene.image.ImageView;
import uwu.openjfx.MapGeneration.Room;

public class MimicChestComponent extends CanOnlyInteractOnce {

    public MimicChestComponent() {
    }

    @Override
    public void interact() {
        super.interact();
        changeToOpenedView();
    }

    @Override
    public void disable() {
        setInteractedBefore(true);
        changeToOpenedView();
    }

    public void changeToOpenedView() {
        getEntity().getViewComponent().clearChildren();
        getEntity().getViewComponent().addChild(new ImageView(FXGL.image(
                "chest_mimic_open_anim_f2_32x32.png"
        )));
    }
}
