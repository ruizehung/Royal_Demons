package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;

public class DropCoinWhenDie implements DieBehavior {
    private int minCoinAmount;
    private int maxCoinAmount;
    private int entityWidth;
    private int entityHeight;

    // currently minMinimumCoinAmount and maxMinimumCoinAmount do not matter
    public DropCoinWhenDie(int minCoinAmount, int maxCoinAmount, int entityWidth, int entityHeight) {
        this.minCoinAmount = minCoinAmount;
        this.maxCoinAmount = maxCoinAmount;
        this.entityWidth = entityWidth;
        this.entityHeight = entityHeight;
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
    public void die(double x, double y) {
        if (FXGL.random() < 0.5 && minCoinAmount > 0) {
            FXGL.spawn("coin", x + entityWidth / 2, y + entityHeight / 2);
        }
    }
}
