package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

/*
    This class is responsible for ANY weapon animation
 */
public class WeaponAnimationComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public WeaponAnimationComponent() {
        // Making sure there is a default constructor
    }

    public WeaponAnimationComponent(String weapon, int duration,
                                    int frameWidth, int frameHeight, int fpr) {
        // parameter weapon: tells us the specific weapon sprite we have in our files and
        // therefore which animation
        // parameter duration: tells us how long the charge-up of the attack is
        // parameter fpr: frames per row
        AnimationChannel animAttack = new AnimationChannel(
                FXGL.image("./weapons/" + weapon + ".png"), fpr,
                frameWidth, frameHeight, Duration.millis(duration), 0, fpr - 1);
        texture = new AnimatedTexture(animAttack);
        texture.playAnimationChannel(animAttack);
        // Remove weapon after it has done its attack animation
        FXGL.getGameTimer().runAtInterval(() -> {
            if (getEntity() != null) {
                getEntity().removeFromWorld();
            }
        }, Duration.millis(duration));
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(0, 0));
        entity.getViewComponent().addChild(texture);
    }
}
