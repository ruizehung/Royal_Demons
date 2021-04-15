package uwu.openjfx.components;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class ExplosionAtDistComponent extends Component {
    private final boolean explodeColl;
    public ExplosionAtDistComponent(boolean explodeOnCollision) {
        this.explodeColl = explodeOnCollision;
    }

    public void explode() {
        int hitBoxWidth = 150;
        int hitBoxHeight = 150;
        Entity explosionHitBox = spawn("meleeSwordHitBox",
            new SpawnData(getEntity().getX() - 45,
                getEntity().getY() - ((double) hitBoxHeight / 2)).
                put("width", hitBoxWidth).put("height", hitBoxHeight).
                put("damage", 50.0));
        FXGL.getGameTimer().runAtInterval(explosionHitBox::removeFromWorld, Duration.seconds(0.1));
    }
    public boolean getExplodeColl() {
        return explodeColl;
    }
}
