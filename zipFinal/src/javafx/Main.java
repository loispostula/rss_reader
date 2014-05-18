package javafx;

import org.controlsfx.dialog.Dialogs;
import util.Database;
import util.GenerateXML;
import util.XMLParser;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
