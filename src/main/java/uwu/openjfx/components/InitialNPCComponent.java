package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import uwu.openjfx.behaviors.Interactable;

public class InitialNPCComponent extends Component implements Interactable {
    private int width;
    private int height;

    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public InitialNPCComponent() {
        this.width = 57;
        this.height = 57;
        animIdle = new AnimationChannel(FXGL.image("creatures/ally/Wizard_57x57.png"),
            4, width, height, Duration.seconds(0.5), 0, 3);

        texture = new AnimatedTexture(animIdle);

        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(width / 2.0, height / 2.0));
        entity.getViewComponent().addChild(texture);
        getEntity().setScaleX(-1);
    }

    @Override
    public void interact() {
        String s = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
            + "Maecenas tincidunt euismod arcu sit amet maximus. Ut dignissim eleifend "
            + "enim sit amet maximus. Curabitur tempor sem nunc, et semper est pellentesque "
            + "a. Donec at feugiat erat. Donec ultrices libero diam, ac suscipit elit varius "
            + "elementum. Proin tempor lacus a felis vestibulum egestas. Phasellus pellentesque "
            + "tincidunt nunc, eu vehicula massa convallis ut. Donec gravida felis sit amet purus "
            + "accumsan, nec dapibus sem suscipit. Nulla ornare condimentum nisi, ac molestie ipsum"
            + " maximus placerat. Pellentesque tincidunt nisi ante, eget tempor urna imperdiet id.";
        Text text = FXGL.getUIFactoryService().newText(s);
        text.setWrappingWidth(450);
        VBox content = new VBox(
                FXGL.getAssetLoader().loadTexture("creatures/ally/Wizard.png"),
                text
        );

        Button btnClose = FXGL.getUIFactoryService().newButton("Press me to close");
        btnClose.setPrefWidth(300);

        FXGL.getDialogService().showBox("Welcome to the dungeon!", content, btnClose);
    }

    @Override
    public boolean hasInteractedBefore() {
        return false;
    }

    @Override
    public void setInteractedBefore(boolean interactedBefore) {
        return;
    }
}
