package javafx;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FeedPreloader extends Preloader {
    ProgressBar bar;
    Stage stage;

    private Scene createPreloaderScene(){
        bar = new ProgressBar();
        BorderPane p = new BorderPane();
        p.setCenter(bar);
        return new Scene(p, 300, 150);
    }

    public void start(Stage primaryStage) throws Exception{
        this.stage = primaryStage;
        stage.setScene(createPreloaderScene());
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification progressNotification) {
        bar.setProgress(progressNotification.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_START){
            stage.hide();
        }
    }
}
