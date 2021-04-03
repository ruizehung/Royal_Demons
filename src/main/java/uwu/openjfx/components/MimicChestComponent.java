package uwu.openjfx.components;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import javafx.scene.image.ImageView;
import uwu.openjfx.MapGeneration.Room;

public class MimicChestComponent extends ChestComponent {

    public MimicChestComponent() {
    }

    @Override
    public void open() {
        if (!super.isOpened()) {
            super.setHasBeenOpened(true);
            Room curRoom = FXGL.geto("curRoom");
            for (int i = 0; i < 3; ++i) {
                Entity enemy = FXGL.spawn("minion", getEntity().getX() + FXGL.random(-96, 96),
                        getEntity().getY() + FXGL.random(-96, 96));
                IDComponent idComponent = new IDComponent("enemy", 5000 + i);
                enemy.addComponent(idComponent);
                curRoom.setEntityData(idComponent.getId(), "isAlive", 1);
            }

            IDComponent idComponent = getEntity().getComponent(IDComponent.class);
            curRoom.setChestData(idComponent.getId(), "opened", 1);
        }
        changeToOpenedView();
    }

    @Override
    public void changeToOpenedView() {
        getEntity().getViewComponent().clearChildren();
        getEntity().getViewComponent().addChild(new ImageView(FXGL.image(
                "chest_mimic_open_anim_f2_32x32.png"
        )));
    }
}
