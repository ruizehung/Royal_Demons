package uwu.openjfx.components;

import com.almasb.fxgl.entity.component.Component;

/*
    This class is allows a weapon to attack multiple enemies at once, rather than
    disappearing at the first enemy hit.
 */
public class AttackThroughComponent extends Component {
    private boolean active;
    public AttackThroughComponent(boolean active) {
        this.active = active;
    }
    public AttackThroughComponent() { }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
