package javafx;

import entities.Publication;

/**
 * Created by lpostula on 14/05/14.
 * Documentation de la classe PublicationViewController
 */
public class PublicationViewController implements DialogController{
    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public PublicationViewController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public void loadPublication(Publication publication){

    }
}
