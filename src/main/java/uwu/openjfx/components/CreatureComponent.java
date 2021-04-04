package uwu.openjfx.components;

import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import uwu.openjfx.creatureLifeCycle.DieBehavior;
import uwu.openjfx.creatureLifeCycle.LifeBehavior;

import java.util.Timer;

public class CreatureComponent extends Component implements LifeBehavior {

    private int healthPoints;
    private int maxHealthPoints;
    private boolean isInvulnerable;
    private IntegerProperty playerHealthIntegerProperty;
    private DieBehavior dieBehavior;

    public CreatureComponent(int healthPoints, int maxHealthPoints, DieBehavior dieBehavior) {
        this.healthPoints = healthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.playerHealthIntegerProperty = new SimpleIntegerProperty(healthPoints);
        setDieBehavior(dieBehavior);
    }

    public CreatureComponent(int healthPoints, int maxHealthPoints) {
        this(healthPoints, maxHealthPoints, null);
    }

    @Override
    public void increaseHealth(int point) {
        this.healthPoints = Math.max(this.healthPoints + point, maxHealthPoints);
        playerHealthIntegerProperty.set(healthPoints);
    }

    @Override
    public void deductHealth(int point) {
        healthPoints -= point;
        if (healthPoints <= 0) { // die
            healthPoints = 0;
            die();
        } else {
            isInvulnerable = true;
            invulnerability();
        }
        playerHealthIntegerProperty.set(healthPoints);
    }

    private void invulnerability() {
        isInvulnerable = true;
        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        isInvulnerable = false;
                        t.cancel();
                    }
                }, 2000
        );
    }

    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    @Override
    public void setHealthPoints(int healthPoints) {
        playerHealthIntegerProperty.set(healthPoints);
        this.healthPoints = healthPoints;
    }

    @Override
    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    @Override
    public void setMaxHealthPoints(int maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }

    @Override
    public void setDieBehavior(DieBehavior dieBehavior) {
        this.dieBehavior = dieBehavior;
    }

    @Override
    public DieBehavior getDieBehavior() {
        return dieBehavior;
    }

    @Override
    public boolean dead() {
        return healthPoints <= 0;
    }

    @Override
    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    @Override
    public IntegerProperty getHealthIntegerProperty() {
        return playerHealthIntegerProperty;
    }

    @Override
    public void die() {
        if (dieBehavior != null) {
            dieBehavior.die(getEntity().getX(), getEntity().getY());
        }
    }
    
}
