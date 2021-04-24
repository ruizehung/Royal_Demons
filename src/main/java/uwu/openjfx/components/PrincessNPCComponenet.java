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

public class PrincessNPCComponenet extends Component implements Interactable {
    private int width;
    private int height;

    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public PrincessNPCComponenet() {
        this.width = 37;
        this.height = 39;
        // Change this to princess sprite sheet
        animIdle = new AnimationChannel(FXGL.image("creatures/ally/elf_f_green_37x39.png"),
                8, width, height, Duration.seconds(0.5), 0, 3);

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
        String s = "Thanks for saving me, babe! May I marry you?";
        Text text = FXGL.getUIFactoryService().newText(s);
        text.setWrappingWidth(620);
        VBox content = new VBox(
                FXGL.getAssetLoader().loadTexture("creatures/ally/elf_f.png"),
                text
        );

        Button btnClose = FXGL.getUIFactoryService().newButton("Press me to close");
        btnClose.setPrefWidth(300);

        FXGL.getDialogService().showBox("Dino Baby's Girl Friend", content, btnClose);
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
