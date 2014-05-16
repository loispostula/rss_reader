package javafx;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe ScreensConfiguration
 */
import entities.Publication;
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
        return new FXMLDialog(loginController(), getClass().getResource("fxml/loginForm.fxml"), primaryStage, StageStyle.DECORATED);
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
        return new FXMLDialog(profileController(), getClass().getResource("fxml/profileForm.fxml"), primaryStage, StageStyle.DECORATED);
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

    public FXMLDialog friendRequestDialog() {
        return new FXMLDialog(acceptFriendController(), getClass().getResource("fxml/acceptFriend.fxml"), primaryStage, StageStyle.DECORATED);
    }

    public FriendRequestController acceptFriendController() {
        return new FriendRequestController(this);
    }

    public FXMLDialog findFriendDialog(){
        return new FXMLDialog(findFriendController(), getClass().getResource("fxml/addFriend.fxml"), primaryStage, StageStyle.DECORATED);
    }
    public FindFriendController findFriendController(){
        return new FindFriendController(this);
    }

    public FXMLDialog commentDialog(){
        return new FXMLDialog(commentDialogController(), getClass().getResource("fxml/commentForm.fxml"), primaryStage, StageStyle.DECORATED);
    }
    public CommentDialogController commentDialogController(){
        return new CommentDialogController(this);
    }

    public FXMLDialog publicationView(){
        return new FXMLDialog(publicationViewController(), getClass().getResource("fxml/openPub.fxml"), primaryStage, StageStyle.DECORATED);
    }

    public PublicationViewController publicationViewController(){
        return new PublicationViewController(this);
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