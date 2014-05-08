<<<<<<< HEAD:src/sample/Main.java
package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        //XMLParser parser = new XMLParser("train.xml");
    }
}
=======
package javafx;

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
>>>>>>> 1232cb38b7e89ea86023c41d091503352402c324:src/javafx/Main.java
