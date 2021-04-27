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
        String s = "The Royal Kingdom once lived in harmony with the wild’s various types of"
                + " creatures. Aimed for the goal of living peacefully with nature’s wildlife, "
                + "the Royal Kingdom set out to bring humans and creatures together in one "
                + "environment. After a century-long established kingdom of peace and prosperity, "
                + "a demonized man soon realized another perspective to view these creatures, "
                + "to carry out his own greedy desires. Using dark magic to possess these "
                + "creatures and slowly take over the kingdom, the evil man kidnaps creatures "
                + "one by one to his dungeon and uses them against the Royal Kingdom. It "
                + "didn’t take long for Demon Man to acquire his new army of once-happily-living"
                + " possessed creatures. After having just captured his best friend, Dino Baby "
                + "journeys out in vengeance to retrieve his GF and restore "
                + "balance to his kingdom.";
        Text text = FXGL.getUIFactoryService().newText(s);
        text.setWrappingWidth(450);
        VBox content = new VBox(
                FXGL.getAssetLoader().loadTexture("creatures/ally/Wizard.png"),
                text
        );
        content.setSpacing(5);

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
