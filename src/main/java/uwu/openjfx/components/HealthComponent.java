package uwu.openjfx.components;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.IDComponent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import uwu.openjfx.DieScreenMenu;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;

// Handles any altercations to health of creature
public class HealthComponent extends Component {
    private int healthPoints;
    private IntegerProperty playerHealth;

    public HealthComponent(int healthPoints) {
        this.healthPoints = healthPoints;
        this.playerHealth = new SimpleIntegerProperty(healthPoints);
    }

    public void deductHealth() {
        healthPoints--;
        playerHealth.set(healthPoints);
        if (healthPoints <= 0) { // die
            healthPoints = 0;
            if (!getEntity().isType(RoyalType.PLAYER)) {
                getEntity().removeFromWorld();
                IDComponent idComponent = getEntity().getComponent(IDComponent.class);
                Room curRoom = FXGL.geto("curRoom");
                curRoom.setEntityData(idComponent.getId(), "isAlive", 0);
            } else {

                FXGL.getSceneService().pushSubScene(new DieScreenMenu(MenuType.GAME_MENU));
                //FXGL.getSceneService().getCurrentScene().addChild(box);
//                FXGL.getDialogService().showMessageBox("L", () -> {
//                    MainMenu.resetToMainMenu();
//                });
            }
        }

    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public IntegerProperty getPlayerHealth() {
        return playerHealth;
    }

}
