package uwu.openjfx;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uwu.openjfx.controller.SceneSwapController;
import uwu.openjfx.model.*;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneSwapController.setMainStage(stage);
        SceneSwapController.init();

        stage.setTitle("Royal Demons");

        //stage.initStyle(StageStyle.TRANSPARENT);
        Parent root = null;
        try {
            String fxmlLocation = "/uwu/openjfx/view/welcomeScreenView.fxml";
            root = FXMLLoader.load(getClass().getResource(fxmlLocation));
            Scene welcomeScene = new Scene(root);
            SceneSwapController.addScene("welcome", welcomeScene);
            stage.setScene(SceneSwapController.getScene("welcome"));
            new FadeIn(root).play();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setResizable(false);
        stage.show();

        Resources resources = Resources.getInstance();
        resources.loadResources();

        UserSetting.reset();
        GameState.getInstance().generateNewMap();
        GameMap gameMap = GameState.getInstance().getGameMap();

        // debugging print statements for room generation
        //System.out.println(gameMap.getRooms().values().size());
        //for (Room room : gameMap.getRooms().values()) {
        //    System.out.print(room.getCoordinate() + ",");
        //}
        //System.out.println("\nBoss room: ");
        //System.out.println(gameMap.getBossRoom().getDistFromInitRoom());
    }

    public static void main(String[] args) {
        launch(args);
    }

}