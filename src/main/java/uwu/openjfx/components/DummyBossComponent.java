package uwu.openjfx.components;

public class DummyBossComponent extends BossComponent {

    private final int healingBufferAfterInjury = 120;
    private int healingTimer;

    public DummyBossComponent(int healthPoints, String assetName,
                              int width, int height, int frames) {
        super(healthPoints, assetName, width, height, frames, "melee");
    }

    @Override
    public void onAdded() {
        super.onAdded();
        getEntity().setScaleX(-1);
        healingTimer = healingBufferAfterInjury;
    }

    @Override
    public void onUpdate(double tpf) {
        if (healingTimer <= 0) {
            if (super.getHealthPoints() < super.getMaxHealthPoints()) {
                super.increaseHealth(1);
            }
        } else {
            --healingTimer;
        }
        getEntity().setX(952);
        getEntity().setY(596);
    }

    @Override
    public void deductHealth(double point, double attackPower, double blockProb,
                             double armor, int pierce) {
        super.deductHealth(point, attackPower, blockProb, armor, pierce);
        healingTimer = healingBufferAfterInjury;
    }
}


