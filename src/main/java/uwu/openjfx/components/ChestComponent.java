package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.IDComponent;
import javafx.scene.image.ImageView;
import uwu.openjfx.MapGeneration.Room;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class ChestComponent extends Component {
    private List<String> itemsList = new ArrayList<>(4);
    private boolean hasBeenOpened = false;

    public ChestComponent() {
        itemsList.add("HealthPotion");
        itemsList.add("RagePotion");
    }

    public boolean isOpened() {
        return hasBeenOpened;
    }

    public void setHasBeenOpened(boolean hasBeenOpened) {
        this.hasBeenOpened = hasBeenOpened;
    }

    public void open() {
        changeToOpenedView();
        dropItems();
    }

    private void dropItems() {
        if (!hasBeenOpened) {
            changeToOpenedView();
            for (String itemName : itemsList) {
                spawn("itemOnFloor",
                        new SpawnData(getEntity().getX() + FXGL.random(-32, 32),
                                getEntity().getY() + FXGL.random(-32, 32))
                                .put("name", itemName));
            }
            hasBeenOpened = true;
            IDComponent idComponent = getEntity().getComponent(IDComponent.class);
            Room curRoom = FXGL.geto("curRoom");
            curRoom.setChestData(idComponent.getId(), "opened", 1);
        }
    }

    public void changeToOpenedView() {
        getEntity().getViewComponent().clearChildren();
        getEntity().getViewComponent().addChild(new ImageView(FXGL.image(
                "chest_empty_open_anim_f2_32x32.png"
        )));
    }

}
