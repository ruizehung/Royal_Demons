package uwu.openjfx;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;



public class DieScreenMenu extends FXGLMenu {

    private Rectangle masker = new Rectangle(
            FXGL.getAppWidth(), FXGL.getAppHeight(), Color.color(0.0, 0.0, 0.0, 0.25));
    private VBox content;

    private Animation<?> animation;

    public DieScreenMenu(MenuType type) {
        super(type);
        content = createContentPane(type);
        content.getChildren().add(createContent());

        content.setTranslateX(FXGL.getAppWidth() / 4.0);
        content.setTranslateY(FXGL.getAppHeight() / 15.0);

        getContentRoot().getChildren().setAll(masker, content);

        animation = FXGL.animationBuilder()
                .duration(Duration.seconds(0.5))
                .interpolator(Interpolators.BACK.EASE_OUT())
                .translate(content)
                .from(new Point2D(FXGL.getAppWidth() / 4.0, -400.0))
                .to(new Point2D(FXGL.getAppWidth() / 4.0, FXGL.getAppHeight() / 15.0))
                .build();
    }

    @Override
    public void onCreate() {
        animation.start();
    }

    protected void onUpdate(double tpf) {
        animation.onUpdate(tpf);
    }

    private VBox createContentPane(MenuType type) {
        if (type == MenuType.GAME_MENU) {
            return new VBox(FXGL.texture("dissapointed_musaev.png"));
        } else {
            return new VBox(FXGL.texture("proud_musaev_resize.png"));
        }
    }

    private Parent createContent() {
        Button btnResume = FXGL.getUIFactoryService()
                .newButton("Main Menu");
        btnResume.setOnAction(e -> MainMenu.resetToMainMenu());

        Button btnExit = FXGL.getUIFactoryService()
                .newButton("Exit");
        btnExit.setOnAction(e -> {
            fireExit();
        });

        HBox vbox = new HBox(15.0, btnResume, btnExit);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: black");
        //vbox.setPrefSize(250.0, 400.0);

        return vbox;
    }
}
