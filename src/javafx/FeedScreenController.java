package javafx;

import javafx.event.ActionEvent;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe FeedScreenController
 */
public class FeedScreenController implements DialogController{
    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public FeedScreenController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public void logout() {
        dialog.close();
        screens.loginDialog().show();
    }

    public void addFriend() {
    }

    public void showProfil() {
        FXMLDialog dial = screens.profileDialog();
        ProfileController controller = (ProfileController) dial.getController();
        dial.show();
        controller.loadUser(screens.getConnectedUser());

    }

    public void addNewFeed(ActionEvent actionEvent) {

    }
}
