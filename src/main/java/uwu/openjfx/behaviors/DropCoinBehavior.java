package uwu.openjfx.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;


public class DropCoinBehavior implements Behavior {
    private int minCoinAmount;
    private int maxCoinAmount;

    // currently minMinimumCoinAmount and maxMinimumCoinAmount do not matter
    public DropCoinBehavior(int minCoinAmount, int maxCoinAmount) {
        this.minCoinAmount = minCoinAmount;
        this.maxCoinAmount = maxCoinAmount;
    }

    public int getMinCoinAmount() {
        return minCoinAmount;
    }

    public void setMinCoinAmount(int minCoinAmount) {
        this.minCoinAmount = minCoinAmount;
    }

    public int getMaxCoinAmount() {
        return maxCoinAmount;
    }

    public void setMaxCoinAmount(int maxCoinAmount) {
        this.maxCoinAmount = maxCoinAmount;
    }

    @Override
    public void act(Entity entity) {
        if (FXGL.random() < 0.5 && minCoinAmount > 0) {
            FXGL.spawn("coin", entity.getX() + entity.getWidth() / 2,
                    entity.getY() + entity.getHeight() / 2);
        }
    }
}
