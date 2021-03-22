package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class MagicComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animFireball, animUltimate;

    public MagicComponent(String magicType) {
        animFireball = new AnimationChannel(FXGL.image("fireball_64x64.png"), 60,
                64, 64, Duration.seconds(.5), 0, 59);
        animUltimate = new AnimationChannel(FXGL.image("magic1_32x32.png"), 30,
                32, 32, Duration.seconds(.5), 0, 29);
        switch (magicType) {
            case "fireball":
                texture = new AnimatedTexture(animFireball);
                break;
            case "ultimate":
                texture = new AnimatedTexture(animUltimate);
                break;
            default:
        }
        texture.loop();
    }

    @Override
    public void onAdded() {
//        entity.getTransformComponent().setScaleOrigin(new Point2D(32, 32));
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        // texture.loopAnimationChannel(animFireball);
    }
}
