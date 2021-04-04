package uwu.openjfx.components;

import uwu.openjfx.MainApp;

public class DropItemAndCoinWhenDie implements DieBehavior {
    private DropCoinWhenDie dropCoinBehavior;
    private DropItemWhenDie dropItemBehavior;

    public DropItemAndCoinWhenDie(int entityWidth, int entityHeight, int minCoinAmount, int maxCoinAmount) {
        assert minCoinAmount <= maxCoinAmount;
        dropCoinBehavior = new DropCoinWhenDie(minCoinAmount, maxCoinAmount, entityWidth, entityHeight);
        dropItemBehavior = new DropItemWhenDie(entityWidth, entityHeight);
    }

    @Override
    public void die(double x, double y) {
        if (!MainApp.isIsTesting()) {
            dropCoinBehavior.die(x, y);
            dropItemBehavior.die(x, y);
        }
    }
}
