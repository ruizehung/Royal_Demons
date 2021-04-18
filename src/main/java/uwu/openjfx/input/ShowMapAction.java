package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import uwu.openjfx.MapGeneration.Coordinate;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;
import static com.almasb.fxgl.dsl.FXGLForKtKt.random;

public class ShowMapAction extends UserAction {

    private String initRoomLayout = "-fx-border-color: #06f108; -fx-border-insets: 1; -fx-border-width: 2;";
    private String normalRoomLayout = "-fx-border-color: white; -fx-border-insets: 1; -fx-border-width: 2;";
    private String bossRoomLayout = "-fx-border-color: red; -fx-border-insets: 1; -fx-border-width: 2;";
    private String challengeRoomLayout = "-fx-border-color: yellow; -fx-border-insets: 1; -fx-border-width: 3;";

    public ShowMapAction(@NotNull String name) {
        super(name);
    }

    @Override
    protected void onActionBegin() {
        Boolean developerCheat = FXGL.getWorldProperties().getBoolean("developerCheat");

        BorderPane mapWindow = new BorderPane();
        GridPane mapGrid = new GridPane();
        GridPane legend = new GridPane();
        HBox title = new HBox(FXGL.getUIFactoryService().newText("Game Map", 30));
        title.setAlignment(Pos.TOP_CENTER);
        title.setPadding(new Insets(0, 0, 70, 0));

        mapGrid.setAlignment(Pos.CENTER);
        mapGrid.setHgap(1);
        mapGrid.setVgap(1);


        legend.setAlignment(Pos.CENTER_RIGHT);
        legend.setHgap(3);
        legend.setVgap(5);

        mapWindow.setTop(title);
        mapWindow.setCenter(mapGrid);
        mapWindow.setLeft(legend);


        // Regular Room legend
        BorderPane regularRoomLegend = new BorderPane();
        regularRoomLegend.setPrefWidth(24);
        regularRoomLegend.setPrefHeight(24);
        regularRoomLegend.setStyle(normalRoomLayout);
        legend.add(regularRoomLegend, 0, 0);
        Text regularRoomText = new Text("Regular Room");
        regularRoomText.setFill(Color.WHITE);
        legend.add(regularRoomText, 1, 0);

        // Initial Room legend
        BorderPane initialRoomLegend = new BorderPane();
        initialRoomLegend.setPrefWidth(24);
        initialRoomLegend.setPrefHeight(24);
        initialRoomLegend.setStyle(initRoomLayout);
        legend.add(initialRoomLegend, 0, 1);
        Text initialRoomText = new Text("Initial Room");
        initialRoomText.setFill(Color.WHITE);
        legend.add(initialRoomText, 1, 1);

        // Challenge Room legend
        BorderPane challengeRoomLegend = new BorderPane();
        challengeRoomLegend.setPrefWidth(24);
        challengeRoomLegend.setPrefHeight(24);
        challengeRoomLegend.setStyle(challengeRoomLayout);
        legend.add(challengeRoomLegend, 0, 2);
        Text challengeRoomText = new Text("Challenge Room");
        challengeRoomText.setFill(Color.WHITE);
        legend.add(challengeRoomText, 1, 2);

        // Boss Room legend
        BorderPane bossRoomLegend = new BorderPane();
        bossRoomLegend.setPrefWidth(24);
        bossRoomLegend.setPrefHeight(24);
        bossRoomLegend.setStyle(bossRoomLayout);
        legend.add(bossRoomLegend, 0, 3);
        Text bossRoomText = new Text("Boss Room");
        bossRoomText.setFill(Color.WHITE);
        legend.add(bossRoomText, 1, 3);

        // Current Room legend
        BorderPane currentRoomLegend = new BorderPane();
        currentRoomLegend.setPrefWidth(24);
        currentRoomLegend.setPrefHeight(24);
        Text currentRoomLabel = new Text("X");
        currentRoomLabel.setFill(Color.WHITE);
        currentRoomLegend.setCenter(currentRoomLabel);
        legend.add(currentRoomLegend, 0, 4);
        Text currentRoomText = new Text("Current Room");
        currentRoomText.setFill(Color.WHITE);
        legend.add(currentRoomText, 1, 4);


        GameMap gameMap = FXGL.geto("gameMap");

        BorderPane[][] borderPanes = new BorderPane[gameMap.getWidth() + 1][gameMap.getHeight() + 1];

        for (Room room: gameMap.getRooms().values()) {
            int x = room.getCoordinate().getX() - gameMap.getMinX();
            int y = gameMap.getMaxY() - room.getCoordinate().getY();

            borderPanes[x][y] = new BorderPane();

            if (developerCheat || room.visited()) {
                if (room.getCoordinate().equals(gameMap.getInitialRoom().getCoordinate())) {
                    borderPanes[x][y].setStyle(initRoomLayout);
                } else if (room.getCoordinate().equals(gameMap.getBossRoom().getCoordinate())) {
                    borderPanes[x][y].setStyle(bossRoomLayout);
                } else if (room.getRoomType().equals("challengeRoom")) {
                    borderPanes[x][y].setStyle(challengeRoomLayout);
                } else {
                    borderPanes[x][y].setStyle(normalRoomLayout);
                }
            }

            borderPanes[x][y].setPrefWidth(24);
            borderPanes[x][y].setPrefHeight(24);
            mapGrid.add(borderPanes[x][y], x, y);
        }

        // Highlight current room
        Coordinate coordinate = ((Room) FXGL.geto("curRoom")).getCoordinate();
        Text t = new Text("X");
        t.setFill(Color.WHITE);
        borderPanes[coordinate.getX() - gameMap.getMinX()][gameMap.getMaxY() - coordinate.getY()]
                .setCenter(t);


        Button btnClose = getUIFactoryService().newButton("Press me to close");
        btnClose.setPrefWidth(300);

        FXGL.getDialogService().showBox("", mapWindow, btnClose);
    }
}
