package uwu.openjfx.components;

import com.almasb.fxgl.entity.component.Component;

public class RicochetComponent extends Component {
    private int wallHits = 0;

    @Override
    public void onUpdate(double tpf) {
        if (wallHits >= 10 && getEntity() != null) {
            getEntity().removeFromWorld();
        }
    }

    public void incrementWallHit() {
        wallHits++;
    }
}
