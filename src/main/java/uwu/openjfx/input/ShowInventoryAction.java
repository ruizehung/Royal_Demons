package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
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
import uwu.openjfx.components.PlayerComponent;
import uwu.openjfx.weapons.Weapon;

import java.util.List;

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
        GridPane inventoryGrid = new GridPane();
        Text itemInfoTitle = FXGL.getUIFactoryService().newText("");

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
        itemInfoPane.setPadding(new Insets(0, 0, 0, 10));


        inventoryGrid.setHgap(3);
        inventoryGrid.setVgap(3);

        final int rows = 3;
        final int cols = 4;

        BorderPane[][] borderPanes = new BorderPane[rows][cols];

        String cssLayout = "-fx-border-color: white; -fx-border-insets: 5; -fx-border-width: 3;";

        EventHandler<MouseEvent> showInfo = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                BorderPane slot = (BorderPane) e.getSource();
                if (slot.getCenter() == null) {
                    return;
                }

                ObservableMap obm = slot.getProperties();
                Weapon selectedWeapon = (Weapon) obm.get("item");
                itemInfoTitle.setText(selectedWeapon.getName());
                Text text = FXGL.getUIFactoryService().newText(selectedWeapon.getDescription());
                text.setWrappingWidth(200);

                Texture weaponIcon = getAssetLoader().loadTexture(selectedWeapon.
                    getWeaponIconPath());
                VBox descriptionAndIcon = new VBox(weaponIcon, text);
                descriptionAndIcon.setAlignment(Pos.TOP_CENTER);
                descriptionAndIcon.setSpacing(10);
                itemInfoPane.setCenter(descriptionAndIcon);

                Button select = FXGL.getUIFactoryService().newButton("Select");
                Button drop = FXGL.getUIFactoryService().newButton("Drop");
                VBox buttons = new VBox(select, drop);

                select.setOnMouseClicked((event) -> {
                    if (!PlayerComponent.getCurrentWeapon().getName()
                            .equals(selectedWeapon.getName())) {
                        PlayerComponent.setCurrentWeapon(selectedWeapon);
                    }
                });

                drop.setOnMouseClicked((event) -> {
                    if (!PlayerComponent.getCurrentWeapon().getName()
                            .equals(selectedWeapon.getName())) {
                        PlayerComponent.getWeaponInventoryList().remove(selectedWeapon);
                        slot.setCenter(null);
                        slot.getProperties().remove("item");
                        itemInfoTitle.setText("");
                        descriptionAndIcon.getChildren().clear();
                        itemInfoPane.setBottom(null);
                    }
                });
                itemInfoPane.setBottom(buttons);
            }
        };

        List<Weapon> playerWeaponList = PlayerComponent.getWeaponInventoryList();

        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                borderPanes[r][c] = new BorderPane();
                borderPanes[r][c].setStyle(cssLayout);
                borderPanes[r][c].setPrefWidth(96);
                borderPanes[r][c].setPrefHeight(96);
                if (r * cols + c < playerWeaponList.size()) {
                    System.out.println("setting " + r * cols + c);
                    borderPanes[r][c].setCenter(getAssetLoader()
                            .loadTexture(playerWeaponList.get(r * cols + c)
                                    .getWeaponIconPath()));
                    borderPanes[r][c].getProperties()
                            .put("item", playerWeaponList.get(r * cols + c));
                    borderPanes[r][c].addEventFilter(MouseEvent.MOUSE_CLICKED, showInfo);
                }
                /*
                Text count = FXGL.getUIFactoryService().newText("0");
                HBox hBox = new HBox(count);
                hBox.setAlignment(Pos.BOTTOM_RIGHT);
                borderPanes[r][c].setBottom(hBox);
                 */

                inventoryGrid.add(borderPanes[r][c], c, r);

                /*
                borderPanes[r][c].hoverProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        System.out.println("on hover");
                    } else {
                        System.out.println("finish hover");
                    }
                });
                 */
            }
        }

        inventoryGrid.setAlignment(Pos.CENTER);

        Button btnClose = getUIFactoryService().newButton("Close");
        btnClose.setPrefWidth(300);

        FXGL.getDialogService().showBox("", inventory, btnClose);
    }
}
