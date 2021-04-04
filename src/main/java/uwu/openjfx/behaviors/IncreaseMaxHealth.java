package uwu.openjfx.behaviors;


import com.almasb.fxgl.entity.Entity;
import uwu.openjfx.components.CreatureComponent;

public class IncreaseMaxHealth implements Behavior {

    private int healPoints;

    public IncreaseMaxHealth(int healPoints) {
        this.healPoints = healPoints;
    }

    @Override
    public void act(Entity entity) {
        CreatureComponent creatureComponent = entity.getObject("CreatureComponent");
        creatureComponent.setMaxHealthPoints(creatureComponent.getMaxHealthPoints() + healPoints);
    }
}
