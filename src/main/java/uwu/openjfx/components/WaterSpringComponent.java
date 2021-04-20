package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uwu.openjfx.MainApp;

public class WaterSpringComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public WaterSpringComponent() {
        if (!MainApp.isIsTesting()) {
            animIdle = new AnimationChannel(FXGL.image("waterSpring.png"), 3,
                    32, 64, Duration.seconds(0.5), 0, 2);
            texture = new AnimatedTexture(animIdle);
            texture.loop();
        }
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(12, 32));
        entity.getViewComponent().addChild(texture);
    }
}
