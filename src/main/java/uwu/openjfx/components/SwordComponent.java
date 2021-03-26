package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class SwordComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle, animAttack;
    private boolean destroyed;
    public static int temp = 0;

    public SwordComponent() {

    }

    public SwordComponent(String weapon, int duration, int frameWidth, int frameHeight, int fpr) {
        destroyed = false;
        animAttack = new AnimationChannel(FXGL.image("./weapons/" + weapon + ".png"), fpr,
                frameWidth, frameHeight, Duration.millis(duration), 0, fpr - 1);
        texture = new AnimatedTexture(animAttack);
        texture.playAnimationChannel(animAttack);
        FXGL.getGameTimer().runAtInterval(() -> {
            if (!destroyed) {
                temp++;
                System.out.println(temp);
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

    @Override
    public void onUpdate(double tpf) {

    }
}
