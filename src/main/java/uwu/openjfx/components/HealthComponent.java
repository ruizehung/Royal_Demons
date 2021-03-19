package uwu.openjfx.components;

import com.almasb.fxgl.entity.component.Component;

// Handles any altercations to health of creature
public class HealthComponent extends Component {
    private int healthPoints;

    public HealthComponent(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void deductHealth() {
        healthPoints--;
        if (healthPoints <= 0) { // die
            healthPoints = 0;
            getEntity().removeFromWorld();
        }
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
}
