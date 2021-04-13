package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import uwu.openjfx.behaviors.CanOnlyInteractOnce;
import uwu.openjfx.behaviors.SpawnMinions;


public class ChallengeNPCComponent extends CanOnlyInteractOnce {

    private int width;
    private int height;

    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public ChallengeNPCComponent() {
        this.width = 57;
        this.height = 57;
        animIdle = new AnimationChannel(FXGL.image("creatures/ally/Wizard_57x57.png"),
                4, width, height, Duration.seconds(0.5), 0, 3);

        texture = new AnimatedTexture(animIdle);

        texture.loop();

        setBehavior(new SpawnMinions(5, "minion"));
    }


    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(width / 2.0, height / 2.0));
        entity.getViewComponent().addChild(texture);
        getEntity().setScaleX(-1);
    }

    @Override
    public void interact() {
        if (!hasInteractedBefore()) {
            FXGL.getDialogService().showConfirmationBox(
                    "Hero, would you accept the challenge?", answer -> {
                        if (answer) {
                            super.interact();
                        }
                    });
        } else {
            String s = "Hmmm .... What's up?";
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
        setInteractedBefore(true);
    }

    @Override
    public void disable() {
        setInteractedBefore(true);
    }
}
