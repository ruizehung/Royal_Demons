package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class BowComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private AnimationChannel animAttack;
    private boolean destroyed;

    public BowComponent() {

    }

    public BowComponent(String weapon, int duration, int frameWidth, int frameHeight, int fpr) {
        destroyed = false;
        animAttack = new AnimationChannel(FXGL.image("./weapons/bow/" + weapon + ".png"), fpr,
                frameWidth, frameHeight, Duration.millis(duration), 0, fpr - 1);
        texture = new AnimatedTexture(animAttack);
        texture.playAnimationChannel(animAttack);
        FXGL.getGameTimer().runAtInterval(() -> {
            if (!destroyed) {
                getEntity().removeFromWorld();
                destroyed = true;
            }
        }, Duration.millis(duration));
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(0, 0));
        entity.getViewComponent().addChild(texture);
    }
}
