package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

/*
    This class is responsible for ANY weapon animation.
    It does the following:
    - Finds an asset by its file name in the resources directory
    - Takes into account its file informations (i.e dimensions, fpr, etc.)
    - Takes in how long it should perform the animation per second
    - Disappears shortly after attacking
 */
public class WeaponAnimationComponent extends Component {
    private AnimatedTexture texture;
    private boolean ghostFrame;

    public WeaponAnimationComponent() {
        // Making sure there is a default constructor
    }

    public WeaponAnimationComponent(String weapon, int duration,
                                    int frameWidth, int frameHeight, int fpr) {
        // parameter weapon: tells us the specific weapon sprite we have in our files and
        // therefore which animation
        // parameter duration: tells us how long the charge-up of the attack is
        // parameter fpr: frames per row
        if (weapon.length() == 0) {
            ghostFrame = true;
            return;
        }
        AnimationChannel animAttack = new AnimationChannel(
            FXGL.image("./weapons/" + weapon + ".png"), fpr,
            frameWidth, frameHeight, Duration.millis(duration), 0, fpr - 1);
        AnimationChannel idle = new AnimationChannel(
            FXGL.image("./weapons/" + weapon + ".png"), fpr,
            frameWidth, frameHeight, Duration.millis(duration), fpr, fpr);

        texture = new AnimatedTexture(animAttack);
        texture.playAnimationChannel(animAttack);
        texture.setOnCycleFinished(() -> {
            texture.set(new AnimatedTexture(idle));
            texture.playAnimationChannel(idle);
        });
    }

    @Override
    public void onAdded() {
        if (!ghostFrame) {
            entity.getTransformComponent().setScaleOrigin(new Point2D(0, 0));
            entity.getViewComponent().addChild(texture);
        }
    }
}
