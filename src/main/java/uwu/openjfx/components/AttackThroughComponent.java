package uwu.openjfx.components;

import com.almasb.fxgl.entity.component.Component;

public class AttackThroughComponent extends Component {
    public boolean active;
    public AttackThroughComponent(boolean active) {
        this.active = active;
    }
    public AttackThroughComponent() {}
}
