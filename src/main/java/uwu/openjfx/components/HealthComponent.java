package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.IDComponent;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;

import java.util.Timer;

// Handles any altercations to health of creature
public class HealthComponent extends Component {
    private int healthPoints;
    private boolean isInvulnerable;

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
        isInvulnerable = true;
        invulnerability();
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

    public boolean getIsInvulnerable() {
        return isInvulnerable;
    }
}
