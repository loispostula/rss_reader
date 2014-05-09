package javafx;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe ScreensConfiguration
 */
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScreensConfiguration {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showScreen(Parent screen) {
        primaryStage.setScene(new Scene(screen, 777, 500));
        primaryStage.show();
    }

    FXMLDialog loginDialog() {
        return new FXMLDialog(loginController(), getClass().getResource("loginForm.fxml"), primaryStage, StageStyle.UNDECORATED);
    }

    LoginController loginController() {
        return new LoginController(this);
    }

    FXMLDialog feedScreen() {
        return new FXMLDialog(feedScreenController(), getClass().getResource("feedScreen.fxml"), primaryStage, StageStyle.DECORATED);
    }

    FeedScreenController feedScreenController(){
        return new FeedScreenController(this);
    }

    FXMLDialog profileDialog() {
        return new FXMLDialog(profileController(), getClass().getResource("profileForm.fxml"), primaryStage, StageStyle.UNDECORATED);
    }

    ProfileController profileController(){
        return new ProfileController(this);
    }

    FXMLDialog registrationDialog() {
        return new FXMLDialog(registrationController(), getClass().getResource("registrationForm.fxml"), primaryStage, StageStyle.UNDECORATED);
    }

    ProfileController registrationController(){
        return new registrationController(this);
    }
}