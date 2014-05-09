package javafx;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe ScreensConfiguration
 */
import entities.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScreensConfiguration {
    private Stage primaryStage;
    private User connectedUser;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showScreen(Parent screen) {
        primaryStage.setScene(new Scene(screen, 777, 500));
        primaryStage.show();
    }

    FXMLDialog loginDialog() {
        return new FXMLDialog(loginController(), getClass().getResource("fxml/loginForm.fxml"), primaryStage, StageStyle.UNDECORATED);
    }

    LoginController loginController() {
        return new LoginController(this);
    }

    FXMLDialog feedScreen() {
        return new FXMLDialog(feedScreenController(), getClass().getResource("fxml/feedScreen.fxml"), primaryStage, StageStyle.DECORATED);
    }

    FeedScreenController feedScreenController(){
        return new FeedScreenController(this);
    }

    FXMLDialog profileDialog() {
        return new FXMLDialog(profileController(), getClass().getResource("fxml/profileForm.fxml"), primaryStage, StageStyle.UNDECORATED);
    }

    ProfileController profileController(){
        return new ProfileController(this);
    }

    FXMLDialog registrationDialog() {
        return new FXMLDialog(registrationController(), getClass().getResource("fxml/registrationForm.fxml"), primaryStage, StageStyle.UNDECORATED);
    }

    RegistrationController registrationController(){
        return new RegistrationController(this);
    }

    public void connectUser(User user){
        this.connectedUser = user;
    }

    public User getConnectedUser(){
        return this.connectedUser;
    }

    public void disconnectUser(){
        this.connectedUser = null;
    }
}