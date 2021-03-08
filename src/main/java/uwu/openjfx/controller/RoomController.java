package uwu.openjfx.controller;

import animatefx.animation.Bounce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import uwu.openjfx.model.GameState;
import uwu.openjfx.model.Resources;
import uwu.openjfx.model.Room;
import uwu.openjfx.model.UserSetting;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class RoomController implements Initializable {

    @FXML
    private GridPane blockGrid;
    @FXML
    private Button mapButton;
    @FXML
    private Pane coinPane;
    @FXML
    private Label coins;
    @FXML
    private Label coordinates;
    @FXML
    private Button goNorthButton;
    @FXML
    private Button goEastButton;
    @FXML
    private Button goSouthButton;
    @FXML
    private Button goWestButton;

    private Room room = GameState.getInstance().getCurrentRoom();
    private int rows = 15;
    private int columns = 23;
    private ImageView[][] imageViews;

    private Random random = new Random();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Bounce(coinPane).setDelay(Duration.valueOf("1000ms")).play();
        switch (UserSetting.getDifficulty()) {
        case "Easy":
            coins.setText("1000");
            break;
        case "Medium":
            coins.setText("800");
            break;
        case "Hard":
            coins.setText("600");
            break;
        default:
        }

        imageViews = new ImageView[rows][columns];

        // 15 row x 23 cols
        // todo: this room cell generation logic should be in Room class itself
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (col == 0) {
                    if (row == 0) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("topLeftEdge.png"));
                    } else if (row == 1) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("topLeftWall.png"));
                    } else if (row == rows - 2) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("bottomLeftCorner.png"));
                    } else if (row == rows - 1) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("bottomLeftWall.png"));
                    } else {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("centerLeftWall.png"));
                    }
                } else if (col == columns - 1) {
                    if (row == 0) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("topRightEdge.png"));
                    } else if (row == 1) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("topRightWall.png"));
                    } else if (row == rows - 2) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("bottomRightCorner.png"));
                    } else if (row == rows - 1) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("bottomRightWall.png"));
                    } else {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("centerRightWall.png"));
                    }
                } else {
                    if (row == 0) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("topMiddleEdge.png"));
                    } else if (row == 1 || row == rows - 1) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("middleWall.png"));
                    } else if (row == rows - 2) {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("bottomMiddleEdge.png"));
                    } else {
                        imageViews[row][col] = new ImageView(Resources.getInstance()
                                .get("floor_1_32x32.png"));
                    }
                }
                // imageViews[row][col] = new ImageView(Resources.floors[random.nextInt(8)]);
                blockGrid.add(imageViews[row][col], col, row);
            }
        }

        configureSwitchRoomButtons();
        coordinates.setText(room.getCoordinate().toString());
    }

    private void configureSwitchRoomButtons() {
        if (room.getNorthRoom() == null) {
            goNorthButton.setDisable(true);
        }
        if (room.getEastRoom() == null) {
            goEastButton.setDisable(true);
        }
        if (room.getSouthRoom() == null) {
            goSouthButton.setDisable(true);
        }
        if (room.getWestRoom() == null) {
            goWestButton.setDisable(true);
        }
    }

    @FXML
    public void switchRoom(ActionEvent event) {
        if (event.getSource().equals(goNorthButton)) {
            GameState.getInstance().setCurrentRoom(room.getNorthRoom());
        } else if (event.getSource().equals(goEastButton)) {
            GameState.getInstance().setCurrentRoom(room.getEastRoom());
        } else if (event.getSource().equals(goSouthButton)) {
            GameState.getInstance().setCurrentRoom(room.getSouthRoom());
        } else if (event.getSource().equals(goWestButton)) {
            GameState.getInstance().setCurrentRoom(room.getWestRoom());
        }

        Stage stage = (Stage) blockGrid.getScene().getWindow();
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass()
                    .getResource("/uwu/openjfx/view/roomView.fxml")));
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showMapWindow() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mapButton.getScene().getWindow());
        dialog.setTitle("Map");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/uwu/openjfx/view/mapWindow.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the Map window!");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> result = dialog.showAndWait();
    }
}
