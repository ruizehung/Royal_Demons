package uwu.openjfx.Controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import uwu.openjfx.Model.Coordinate;
import uwu.openjfx.Model.GameMap;
import uwu.openjfx.Model.Room;

import java.util.Map;

public class GameMapScene {
    private Map<Coordinate, Room> roomsHashMap;
    private GridPane gridPane;
    private Text[][] texts;

    public GameMapScene(GameMap gameMap) {
        roomsHashMap = gameMap.getRooms();
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(3);
        gridPane.setVgap(3);

        texts = new Text[100][100];

        Coordinate coordinate = new Coordinate(0, 0);
        for (int x = 0; x < 100; ++x) {
            for (int y = 0; y < 100; ++y) {
                coordinate.setX(x - 50);
                coordinate.setY(y - 50);
                if (roomsHashMap.get(coordinate) != null) {
                    texts[x][y] = new Text("R");
                    gridPane.add(texts[x][y], x, y);
                }
            }
        }

        // Identifying starting room
        texts[50][50].setText("S");
        texts[50][50].setStyle("-fx-fill: red;");

        // Identifying boss room
        coordinate = gameMap.getBossRoom().getCoordinate();
        texts[coordinate.getX() + 50][coordinate.getY() + 50].setText("B");
        texts[coordinate.getX() + 50][coordinate.getY() + 50].setStyle("-fx-fill: red;");
    }

    public Scene getScene() {
        return new Scene(gridPane, 960, 640);
    }
}
