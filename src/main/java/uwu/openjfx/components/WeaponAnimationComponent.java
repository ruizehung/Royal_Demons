package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
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
    private AnimationChannel animIdle;
    private double duration;
    private Image weaponSprite;

    public WeaponAnimationComponent() {
        // Making sure there is a default constructor
    }

    public WeaponAnimationComponent(String weapon, int duration,
                                    int frameWidth, int frameHeight, int fpr, Image sprite) {
        // parameter weapon: tells us the specific weapon sprite we have in our files and
        // therefore which animation
        // parameter duration: tells us how long the charge-up of the attack is
        // parameter fpr: frames per row
        // parameter sprite: the weapon's image
        weaponSprite = sprite;
        System.out.println("This is where the image was found: " + weaponSprite.getUrl());
        AnimationChannel animAttack = new AnimationChannel(
                FXGL.image("./weapons/" + weapon + ".png"), fpr,
                frameWidth, frameHeight, Duration.millis(duration), 0, fpr - 1);
        texture = new AnimatedTexture(animAttack);
        texture.playAnimationChannel(animAttack);
        this.duration = duration;
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(0, 0));
        entity.getViewComponent().addChild(texture);
    }

    public void onUpdate(double tpf) {
        // Remove weapon after it has done its attack animation
        if (this != null && getEntity() != null) {
            FXGL.getGameTimer().runAtInterval(() -> {
                if (getEntity() != null) {
                    getEntity().removeFromWorld();
                }
            }, Duration.millis(duration - 50));
        }
    }

    public Image getWeaponSprite() {
        return weaponSprite;
    }
}
