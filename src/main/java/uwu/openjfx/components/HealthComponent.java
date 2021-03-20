package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.IDComponent;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;

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
            if (!getEntity().isType(RoyalType.PLAYER)) {
                getEntity().removeFromWorld();
                IDComponent idComponent = getEntity().getComponent(IDComponent.class);
                Room curRoom = FXGL.geto("curRoom");
                curRoom.setEntityData(idComponent.getId(), "isAlive", 0);
            }
        }
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
}
