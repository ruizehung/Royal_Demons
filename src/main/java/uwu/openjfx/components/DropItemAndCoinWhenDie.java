package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import uwu.openjfx.MainApp;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class DropItemAndCoinWhenDie implements DieBehavior {
    private int minCoinAmount;
    private int maxCoinAmount;
    private int entityWidth;
    private int entityHeight;
    private List<String> itemsDropList;

    // currently minMinimumCoinAmount and maxMinimumCoinAmount do not matter
    public DropItemAndCoinWhenDie(int entityWidth, int entityHeight, int minCoinAmount, int maxCoinAmount) {
        assert minCoinAmount <= maxCoinAmount;
        this.minCoinAmount = minCoinAmount;
        this.maxCoinAmount = maxCoinAmount;
        this.entityWidth = entityWidth;
        this.entityHeight = entityHeight;
        this.itemsDropList = new ArrayList<>();
    }

    @Override
    public void die(double x, double y) {
        if (!MainApp.isIsTesting()) {
            // please set entityX and entityY calling deductHealth()
            assert x != -Double.MAX_VALUE && y != -Double.MAX_VALUE;
            if (FXGL.random() < 0.5 && minCoinAmount > 0) {
                FXGL.spawn("coin", x + entityWidth / 2, y + entityHeight / 2);
            }

            if (itemsDropList.size() > 0) {
                for (String itemName : itemsDropList) {
                    spawn("itemOnFloor",
                            new SpawnData(x + entityWidth / 2 + 16, y + entityHeight / 2 + 16)
                                    .put("name", itemName));
                }
            }
        }
    }

    public List<String> getItemsDropList() {
        return itemsDropList;
    }

    public void setItemsDropList(List<String> itemsDropList) {
        this.itemsDropList = itemsDropList;
    }

    public void addDropItem(String itemName) {
        itemsDropList.add(itemName);
    }
}
