package uwu.openjfx.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.IDComponent;
import uwu.openjfx.MapGeneration.Room;

public abstract class CanOnlyInteractOnce extends Component implements Interactable {
    private boolean hasInteractedBefore = false;

    // Todo: This should be a list of Behaviors
    private Behavior behavior;

    public boolean isHasInteractedBefore() {
        return hasInteractedBefore;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public void interact() {
        behavior.act(getEntity());
        hasInteractedBefore = true;
        IDComponent idComponent = entity.getComponent(IDComponent.class);
        Room curRoom = FXGL.geto("curRoom");
        curRoom.setChestData(idComponent.getId(), "hasInteracted", 1);
    }

    @Override
    public boolean hasInteractedBefore() {
        return hasInteractedBefore;
    }

    @Override
    public void setInteractedBefore(boolean interactedBefore) {
        hasInteractedBefore = interactedBefore;
    }

    public abstract void disable();
}
