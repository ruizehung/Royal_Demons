package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;

public class ShowInventoryAction extends UserAction {

    private BorderPane inventory = new BorderPane();
    private BorderPane itemInfoPane = new BorderPane();
    GridPane inventoryGrid = new GridPane();
    private Text itemInfoTitle = FXGL.getUIFactoryService().newText("");

    public ShowInventoryAction(@NotNull String name) {
        super(name);
        HBox title = new HBox(FXGL.getUIFactoryService().newText("Inventory", 30));
        title.setAlignment(Pos.TOP_CENTER);
        Insets insets = new Insets(0, 0, 70, 0);
        title.setPadding(insets);
        inventory.setTop(title);
        inventory.setCenter(inventoryGrid);
        inventory.setRight(itemInfoPane);
        itemInfoPane.setPrefWidth(100);
        itemInfoPane.setPrefHeight(200);
        itemInfoPane.setTop(itemInfoTitle);
    }

    @Override
    protected void onActionBegin() {
        inventoryGrid.setHgap(3);
        inventoryGrid.setVgap(3);

        final int rows = 3;
        final int cols = 4;

        BorderPane[][] borderPanes = new BorderPane[rows][cols];

        String cssLayout = "-fx-border-color: white; -fx-border-insets: 5; -fx-border-width: 3;";

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                ObservableMap obm = ((BorderPane) e.getSource()).getProperties();
                System.out.println(obm.get("item") + "got clicked!");

                itemInfoTitle.setText((String) obm.get("item"));
                Text text = FXGL.getUIFactoryService().newText("some description ....");
                text.setWrappingWidth(120);

                VBox vBox = new VBox(getAssetLoader()
                        .loadTexture("weapon_golden_sword_32x32.png"), text);
                vBox.setAlignment(Pos.TOP_CENTER);
                vBox.setSpacing(10);
                itemInfoPane.setCenter(vBox);
                itemInfoPane.setBottom(FXGL.getUIFactoryService().newButton("Drop"));
            }
        };


        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                borderPanes[r][c] = new BorderPane();
                borderPanes[r][c].setStyle(cssLayout);
                borderPanes[r][c].setPrefWidth(96);
                borderPanes[r][c].setPrefHeight(96);

                borderPanes[r][c].setCenter(getAssetLoader()
                        .loadTexture("weapon_golden_sword_32x32.png"));
                borderPanes[r][c].getProperties().put("item", "golden_sword");

                Text count = FXGL.getUIFactoryService().newText("0");
                HBox hBox = new HBox(count);
                hBox.setAlignment(Pos.BOTTOM_RIGHT);
                borderPanes[r][c].setBottom(hBox);
                inventoryGrid.add(borderPanes[r][c], c, r);
                borderPanes[r][c].addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

                borderPanes[r][c].hoverProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        System.out.println("on hover");
                    } else {
                        System.out.println("finish hover");
                    }
                });
            }
        }

        inventoryGrid.setAlignment(Pos.CENTER);


        Button btnClose = getUIFactoryService().newButton("Close");
        btnClose.setPrefWidth(300);

        FXGL.getDialogService().showBox("", inventory, btnClose);
    }
}
