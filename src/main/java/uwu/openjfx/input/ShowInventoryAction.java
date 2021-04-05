package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;

public class ShowInventoryAction extends UserAction {

    public ShowInventoryAction(@NotNull String name) {
        super(name);
    }

    @Override
    protected void onActionBegin() {
        BorderPane inventory = new BorderPane();

        BorderPane itemInfoPane = new BorderPane();
        itemInfoPane.setPrefWidth(100);
        itemInfoPane.setPrefHeight(200);
        Text itemInfoTitle = FXGL.getUIFactoryService().newText("");
        itemInfoPane.setTop(itemInfoTitle);

        inventory.setRight(itemInfoPane);

        GridPane inventoryGrid = new GridPane();
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
                System.out.println(obm.get("item"));
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
                        itemInfoTitle.setText("golden_sword");
                    } else {
                        itemInfoTitle.setText("");
                        System.out.println("finish hover");
                    }
                });
            }
        }

        inventoryGrid.setAlignment(Pos.CENTER);
        inventory.setCenter(inventoryGrid);

        Button btnClose = getUIFactoryService().newButton("Close");
        btnClose.setPrefWidth(300);

        FXGL.getDialogService().showBox("Inventory", inventory, btnClose);
    }
}
