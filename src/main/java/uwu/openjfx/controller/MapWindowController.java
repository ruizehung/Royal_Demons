package uwu.openjfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import uwu.openjfx.model.Coordinate;
import uwu.openjfx.model.GameState;
import uwu.openjfx.model.Room;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MapWindowController implements Initializable {

    private Map<Coordinate, Room> roomsHashMap;
    private VBox[][] vBoxes;
    @FXML
    private GridPane mapGrid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roomsHashMap = GameState.getInstance().getGameMap().getRooms();

        vBoxes = new VBox[100][100];

        for (Room room: GameState.getInstance().getGameMap().getRooms().values()) {
            int x = room.getCoordinate().getX() + 50;
            int y = 50 - room.getCoordinate().getY();
            vBoxes[x][y] = new VBox();
            vBoxes[x][y].getChildren().add(new Text("R"));
            mapGrid.add(vBoxes[x][y], x, y);
        }

        Coordinate coordinate;
        // Identifying starting room
        coordinate = GameState.getInstance().getGameMap().getInitialRoom().getCoordinate();
        ((Text) vBoxes[coordinate.getX() + 50][50 - coordinate.getY()].getChildren().get(0))
                .setText("S");
        ((Text) vBoxes[coordinate.getX() + 50][50 - coordinate.getY()].getChildren().get(0))
                .setStyle("-fx-fill: red;");

        // Identifying boss room
        coordinate = GameState.getInstance().getGameMap().getBossRoom().getCoordinate();
        ((Text) vBoxes[coordinate.getX() + 50][50 - coordinate.getY()].getChildren().get(0))
                .setText("B");
        ((Text) vBoxes[coordinate.getX() + 50][50 - coordinate.getY()].getChildren().get(0))
                .setStyle("-fx-fill: red;");

        // Highlight current room
        coordinate = GameState.getInstance().getCurrentRoom().getCoordinate();
        vBoxes[coordinate.getX() + 50][50 - coordinate.getY()]
                .setStyle("-fx-background-color: yellow;");
    }
}
