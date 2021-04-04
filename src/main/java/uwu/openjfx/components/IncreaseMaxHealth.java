package uwu.openjfx.components;


import com.almasb.fxgl.entity.Entity;

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
