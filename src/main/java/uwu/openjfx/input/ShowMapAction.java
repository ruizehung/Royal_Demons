package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import uwu.openjfx.MapGeneration.Coordinate;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;

public class ShowMapAction extends UserAction {

    public ShowMapAction(@NotNull String name) {
        super(name);
    }

    @Override
    protected void onActionBegin() {
        Boolean developerCheat = FXGL.getWorldProperties().getBoolean("developerCheat");

        GridPane mapGrid = new GridPane();
        mapGrid.setHgap(3);
        mapGrid.setVgap(3);

        VBox[][] vBoxes = new VBox[100][100];


        GameMap gameMap = FXGL.geto("gameMap");
        for (Room room: gameMap.getRooms().values()) {
            int x = room.getCoordinate().getX() + 50;
            int y = 50 - room.getCoordinate().getY();
            vBoxes[x][y] = new VBox();
            Text t = new Text("R");
            t.setFill(room.visited() ? Color.WHITE : Color.BLACK);
            if (developerCheat) {
                t.setFill(Color.WHITE);
            }
            vBoxes[x][y].getChildren().add(t);
            mapGrid.add(vBoxes[x][y], x, y);
        }

        Coordinate coordinate;
        // Identifying starting room
        coordinate = gameMap.getInitialRoom().getCoordinate();
        Text startRoomText = (Text) vBoxes[coordinate.getX() + 50][50 - coordinate.getY()]
                .getChildren().get(0);
        startRoomText.setText("S");
        startRoomText.setStyle("-fx-fill: red;");

        // Identifying boss room
        coordinate = gameMap.getBossRoom().getCoordinate();
        Text bossRoomText = (Text) vBoxes[coordinate.getX() + 50][50 - coordinate.getY()]
                .getChildren().get(0);
        bossRoomText.setText("B");
        bossRoomText.setFill(gameMap.getBossRoom().visited() ? Color.RED : Color.BLACK);
        if (developerCheat) {
            bossRoomText.setFill(Color.RED);
        }

        // Highlight current room
        coordinate = ((Room) FXGL.geto("curRoom")).getCoordinate();
        vBoxes[coordinate.getX() + 50][50 - coordinate.getY()]
                .setStyle("-fx-background-color: yellow;");


        Button btnClose = getUIFactoryService().newButton("Press me to close");
        btnClose.setPrefWidth(300);

        FXGL.getDialogService().showBox("Map", mapGrid, btnClose);
    }
}
