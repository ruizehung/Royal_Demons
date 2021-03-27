package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uwu.openjfx.MainApp;

public class CoinComponent extends Component {

    private int value;

    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public CoinComponent(int value) {
        this.value = value;
        if (!MainApp.isIsTesting()) {
            animIdle = new AnimationChannel(FXGL.image("coin_16x16.png"), 4,
                    16, 16, Duration.seconds(0.5), 0, 3);
            texture = new AnimatedTexture(animIdle);
            texture.loop();
        }
    }

    @Override
    public void onAdded() {
        if (!MainApp.isIsTesting()) {
            entity.getTransformComponent().setScaleOrigin(new Point2D(8, 8));
            entity.getViewComponent().addChild(texture);
        }
    }

    public int getValue() {
        return value;
    }
}
