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
public class ProjectileAnimationComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private AnimationChannel animAttack;
    private boolean isArrow;
    private boolean isMagic;

    public ProjectileAnimationComponent() {
        // Making sure there is a default constructor
    }

    public ProjectileAnimationComponent(String weapon, int duration, int fw, int fh, int fpr,
                                    boolean isArrow, boolean isMagic) {
        // parameter weapon: tells us the specific weapon sprite we have in our files and
        // therefore which animation
        // parameter duration: tells us how long the charge-up of the attack is
        // parameter fpr: frames per row
        animAttack = new AnimationChannel(FXGL.image("./weapons/" + weapon + ".png"), fpr,
                fw, fh, Duration.millis(duration), 0, fpr - 1);
        texture = new AnimatedTexture(animAttack);
        texture.loop();

        this.isArrow = isArrow;
        this.isMagic = isMagic;
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(0, 0));
        entity.getViewComponent().addChild(texture);
    }

    public boolean getIsArrow() {
        return isArrow;
    }

    public boolean getIsMagic() {
        return isMagic;
    }
}
