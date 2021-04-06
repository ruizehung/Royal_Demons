package uwu.openjfx.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class DropItemComponent extends Component implements Behavior {

    private List<String> itemsList;

    public DropItemComponent(List<String> itemsList) {
        this.itemsList = itemsList;
    }

    public DropItemComponent() {
        this.itemsList = new ArrayList<>();
    }

    public List<String> getDropItemsList() {
        return itemsList;
    }

    public void addDropItem(String itemName) {
        itemsList.add(itemName);
    }

    @Override
    public void act(Entity entity) {
        if (itemsList.size() > 0) {
            for (String itemName : itemsList) {
                spawn("itemOnFloor",
                        new SpawnData(entity.getX() + entity.getWidth() / 2 + FXGL.random(-32, 32),
                                entity.getY() + entity.getHeight() / 2 + FXGL.random(-32, 32))
                                .put("name", itemName));
            }
        }
    }
}
