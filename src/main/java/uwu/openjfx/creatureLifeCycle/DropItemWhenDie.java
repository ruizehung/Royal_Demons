package uwu.openjfx.creatureLifeCycle;

import com.almasb.fxgl.entity.SpawnData;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class DropItemWhenDie implements DieBehavior {

    private int entityWidth;
    private int entityHeight;
    private List<String> itemsList;

    public DropItemWhenDie(int entityWidth, int entityHeight, List<String> itemsList) {
        this.entityWidth = entityWidth;
        this.entityHeight = entityHeight;
        this.itemsList = itemsList;
    }

    public DropItemWhenDie(int entityWidth, int entityHeight) {
        this.entityWidth = entityWidth;
        this.entityHeight = entityHeight;
        this.itemsList = new ArrayList<>();
    }

    public List<String> getDropItemsList() {
        return itemsList;
    }

    public void addDropItem(String itemName) {
        itemsList.add(itemName);
    }

    @Override
    public void die(double x, double y) {
        if (itemsList.size() > 0) {
            for (String itemName : itemsList) {
                spawn("itemOnFloor",
                        new SpawnData(x + entityWidth / 2 + 16, y + entityHeight / 2 + 16)
                                .put("name", itemName));
            }
        }
    }
}
