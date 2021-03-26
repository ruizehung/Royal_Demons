package uwu.openjfx.components;

import com.almasb.fxgl.entity.component.Component;

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
