package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.RandomAStarMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarCell;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarPathfinder;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.Hashtable;

public class Enemy extends Component {
    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk;

    private LocalTimer moveTimer;

    public Enemy(String type) {
        animIdle = new AnimationChannel(FXGL.image(type + ".png"), 8, 32, 32, Duration.seconds(0.5), 0, 3);
        animWalk = new AnimationChannel(FXGL.image(type + ".png"), 8, 32, 32, Duration.seconds(0.5), 4, 7);

        texture = new AnimatedTexture(animIdle);

        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 16));
        entity.getViewComponent().addChild(texture);

        moveTimer = FXGL.newLocalTimer();
        moveTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        Entity player = FXGL.geto("player");

//        if (getEntity().distance(player) < 200) {
//            // constantly signal other AI that player is clos
//            double xDir = player.getX() - getEntity().getX() > 0 ? 1 : -1;
//            double yDir = player.getY() - getEntity().getY() > 0 ? 1 : -1;
//            physics.setVelocityX(50 * xDir);
//            physics.setVelocityY(50 * yDir);
//        } else {
//            physics.setVelocityX(0);
//            physics.setVelocityY(0);
//        }
//        moveTimer.capture();

        if (physics.isMoving()) {
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
        }


    }

}
