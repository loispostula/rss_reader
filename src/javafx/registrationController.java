package javafx;

/**
 * Created by lpostula on 09/05/14.
 * Documentation de la classe registrationController
 */
public class registrationController implements DialogController {
    ScreensConfiguration screens;
    FXMLDialog dialog;

    public registrationController(ScreensConfiguration screensConfiguration) {
        this.screens = screensConfiguration;
    }

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }
}
