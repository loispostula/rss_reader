package javafx;

import util.FeedParser;
import util.GenerateXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ScreensConfiguration screens = new ScreensConfiguration();
        screens.setPrimaryStage(primaryStage);
        screens.loginDialog().show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
