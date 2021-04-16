package uwu.openjfx.components;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import uwu.openjfx.MainApp;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for creating an explosion AOE effect.
 */
public class ExplosionAtDistComponent extends Component {
    private Entity explosionHitBox;
    private Point2D origDist;
    private final boolean explodeColl;

    public ExplosionAtDistComponent(boolean explodeOnCollision) {
        this.explodeColl = explodeOnCollision;
    }

    public void onAdded() {
        origDist = new Point2D(getEntity().getX(), getEntity().getY());
    }
    @Override
    public void onUpdate(double tpf) {
        double x = getEntity().getX() - origDist.getX();
        double y = getEntity().getY() - origDist.getY();
        if (Math.sqrt(x * x + y * y) > 250) {
            explode();
            getEntity().removeFromWorld();
        }
    }

    public void explode() {
        int hitBoxWidth = 175;
        int hitBoxHeight = 175;
        explosionHitBox = spawn("meleeSwordHitBox",
            new SpawnData(getEntity().getX() - 55,
                getEntity().getY() - ((double) hitBoxHeight / 2)).
                put("width", hitBoxWidth).put("height", hitBoxHeight).
                put("damage", 50.0));
        Runnable runnable = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainApp.addToHitBoxDestruction(explosionHitBox);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public boolean getExplodeColl() {
        return explodeColl;
    }
}
