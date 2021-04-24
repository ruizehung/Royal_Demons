package uwu.openjfx.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import uwu.openjfx.MapGeneration.Room;

public class SpawnMinionsBehavior implements Behavior {
    private int numberOfMinions;
    private String category;

    public SpawnMinionsBehavior(int numberOfMinions, String category) {
        this.numberOfMinions = numberOfMinions;
        this.category = category;
    }

    @Override
    public void act(Entity entity) {
        Room curRoom = FXGL.geto("curRoom");
        for (int i = 0; i < numberOfMinions; ++i) {
            Entity enemy = FXGL.spawn(category, entity.getX() + FXGL.random(-180, 180),
                    entity.getY() + FXGL.random(-180, 180));
            IDComponent idComponent = new IDComponent("enemy", 5000 + i);
            enemy.addComponent(idComponent);
            curRoom.setEntityData(idComponent.getId(), "isAlive", 1);
        }
    }
}
