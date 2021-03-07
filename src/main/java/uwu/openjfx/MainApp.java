package uwu.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uwu.openjfx.Controller.WelcomeScreenController;
import uwu.openjfx.Model.*;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Resources resources = Resources.getInstance();
        resources.loadResources();

        WelcomeScreenController welcomeScreen = new WelcomeScreenController(stage);
        Scene welcomeScene = welcomeScreen.getWelcomeScene();
        stage.setTitle("Royal Demons");
        stage.setScene(welcomeScene);
        stage.show();
        UserSetting.reset();
        GameState.getInstance().generateMap();
        GameMap gameMap = GameState.getInstance().getGameMap();

        System.out.println(gameMap.getRooms().values().size());
        for (Room room : gameMap.getRooms().values()) {
            System.out.print(room.getCoordinate() + ",");
        }
        System.out.println("\nBoss room: ");
        System.out.println(gameMap.getBossRoom().getCoordinate());
    }

    public static void main(String[] args) {
        launch(args);
    }

}