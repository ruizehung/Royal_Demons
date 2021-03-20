package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class SwordComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle, regSlash;

    public SwordComponent() {
        animIdle = new AnimationChannel(FXGL.image("weapon_golden_sword_32x32.png"), 1,
                20, 44, Duration.seconds(1), 0, 0);
        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(0, 0));
        entity.getViewComponent().addChild(texture);
    }
}
