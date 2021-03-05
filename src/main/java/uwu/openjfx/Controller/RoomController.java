package uwu.openjfx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import uwu.openjfx.Model.Resources;
import uwu.openjfx.Model.Room;
import uwu.openjfx.Model.UserSetting;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class RoomController implements Initializable {

    @FXML
    private GridPane blockGrid;
    @FXML
    private Label coins;
//    @FXML
//    private Label coordinates;

    private Room room;
    private ImageView[][] imageViews;
    private Image floor;

    private Random random = new Random();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        imageViews = new ImageView[15][23];

        // 15 row x 23 cols
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 23; col++) {
//                if (row == 0 && col == 12) {
//                    imageViews[row][col] = new ImageView(Resources.doors_leaf_closed_32x32);
//                    blockGrid.add(imageViews[row][col], col, row, 2, 2);
//                } else {
//                    imageViews[row][col] = new ImageView(Resources.floor_1_32x32);
//                    blockGrid.add(imageViews[row][col], col, row);
//                }

//                imageViews[row][col] = new ImageView(Resources.floors[random.nextInt(8)]);
                imageViews[row][col] = new ImageView(Resources.floors[0]);
                blockGrid.add(imageViews[row][col], col, row);
            }
        }

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
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
//        coordinates.setText(room.getCoordinate().toString());
    }
}
