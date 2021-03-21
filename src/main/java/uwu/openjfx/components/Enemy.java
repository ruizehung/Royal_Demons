//package uwu.openjfx.components;
//
//import com.almasb.fxgl.dsl.FXGL;
//import com.almasb.fxgl.dsl.components.RandomAStarMoveComponent;
//import com.almasb.fxgl.entity.Entity;
//import com.almasb.fxgl.entity.component.Component;
//import com.almasb.fxgl.pathfinding.CellState;
//import com.almasb.fxgl.pathfinding.astar.AStarCell;
//import com.almasb.fxgl.pathfinding.astar.AStarGrid;
//import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
//import com.almasb.fxgl.pathfinding.astar.AStarPathfinder;
//import com.almasb.fxgl.physics.PhysicsComponent;
//import com.almasb.fxgl.texture.AnimatedTexture;
//import com.almasb.fxgl.texture.AnimationChannel;
//import com.almasb.fxgl.time.LocalTimer;
//import javafx.geometry.Point2D;
//import javafx.util.Duration;
//
//import java.util.Hashtable;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class Enemy extends Component {
//    private PhysicsComponent physics;
//
//    private int width;
//    private int height;
//
//    private AnimatedTexture texture;
//    private AnimationChannel animIdle, animWalk;
//
//    private LocalTimer moveTimer;
//
//    public Enemy(String type, int width, int height) {
//        this.width = width;
//        this.height = height;
//
//        animIdle = new AnimationChannel(FXGL.image("creatures/minions/" + type), 8, width, height, Duration.seconds(0.5), 0, 3);
//        animWalk = new AnimationChannel(FXGL.image("creatures/minions/" + type), 8, width, height, Duration.seconds(0.5), 4, 7);
//
//        texture = new AnimatedTexture(animIdle);
//
//        texture.loop();
//    }
//
//    @Override
//    public void onAdded() {
//        entity.getTransformComponent().setScaleOrigin(new Point2D(width / 2, height / 2));
//        entity.getViewComponent().addChild(texture);
//
//        moveTimer = FXGL.newLocalTimer();
//        moveTimer.capture();
//    }
//
//    @Override
//    public void onUpdate(double tpf) {
//        Entity player = FXGL.geto("player");
//        if (moveTimer.elapsed(Duration.seconds(1))) {
//            if (getEntity().distance(player) < 75) {
//                physics.setVelocityX(0);
//                physics.setVelocityY(0);
//            } else if (getEntity().distance(player) < 300) {
//                // constantly signal other AI that player is clos
//                double xDir = (player.getX() + 20) - getEntity().getX() > 0 ? 1 : -1;
//                double yDir = (player.getY() + 27.5) - getEntity().getY() > 0 ? 1 : -1;
//                physics.setVelocityX(70 * xDir);
//                physics.setVelocityY(70 * yDir);
//            } else {
//                physics.setVelocityX(0);
//                physics.setVelocityY(0);
//            }
//            moveTimer.capture();
//        }
//
//        if (physics.isMoving()) {
//            if (texture.getAnimationChannel() != animWalk) {
//                texture.loopAnimationChannel(animWalk);
//            }
//        } else {
//            if (texture.getAnimationChannel() != animIdle) {
//                texture.loopAnimationChannel(animIdle);
//            }
//        }
//
//
//    }
//
//}