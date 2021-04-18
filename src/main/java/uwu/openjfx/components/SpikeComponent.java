package uwu.openjfx.components;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uwu.openjfx.MainApp;

public class SpikeComponent extends Component {

    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private AnimationChannel animActive;
    private final int cycleDuration = 120;
    private int activeTimer = cycleDuration;

    public SpikeComponent() {
        if (!MainApp.isIsTesting()) {
            animIdle = new AnimationChannel(FXGL.image("spike.png"), 8,
                    32, 32, Duration.seconds(2), 0, 0);
            animActive = new AnimationChannel(FXGL.image("spike.png"), 8,
                    32, 32, Duration.seconds(2.5), 1, 7);
            texture = new AnimatedTexture(animIdle);
            texture.loop();
        }
    }

    @Override
    public void onAdded() {
        if (!MainApp.isIsTesting()) {
            entity.getTransformComponent().setScaleOrigin(new Point2D(16, 16));
            entity.getViewComponent().addChild(texture);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        --activeTimer;
        if (activeTimer <= 0) {
            if (texture.getAnimationChannel() == animIdle) {
                texture.loopAnimationChannel(animActive);
            } else {
                texture.loopAnimationChannel(animIdle);
            }
            activeTimer = cycleDuration;
        }

    }

    public boolean isActive() {
        return texture.getAnimationChannel() == animActive;
    }
}
