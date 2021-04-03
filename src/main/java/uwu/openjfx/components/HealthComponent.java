package uwu.openjfx.components;

import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.Timer;

/*
    This class is responsible for handing any alterations to the health of a creature.
 */
public abstract class HealthComponent extends Component {
    private int healthPoints;
    private int maxHealthPoints;
    private boolean isInvulnerable;
    private IntegerProperty playerHealth;

    public HealthComponent(int healthPoints) {
        this.healthPoints = healthPoints;
        this.maxHealthPoints = healthPoints;
        this.playerHealth = new SimpleIntegerProperty(healthPoints);
    }

    public void deductHealth() {
        healthPoints--;
        playerHealth.set(healthPoints);
        if (healthPoints <= 0) { // die
            healthPoints = 0;
            die();
        } else {
            isInvulnerable = true;
            invulnerability();
        }
    }

    public void addHealth(int health) {
        healthPoints += health;
        if (healthPoints > maxHealthPoints) { // die
            healthPoints = maxHealthPoints;
        }
        playerHealth.set(healthPoints);
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

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public void setMaxHealthPoints(int maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }

    public boolean getIsInvulnerable() {
        return isInvulnerable;
    }

    public IntegerProperty getPlayerHealth() {
        return playerHealth;
    }

    public abstract void die();
}
