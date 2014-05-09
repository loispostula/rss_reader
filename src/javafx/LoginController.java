package javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LoginController implements DialogController{
    private FXMLDialog dialog;
    private ScreensConfiguration screens;

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public LoginController(ScreensConfiguration screens){
        this.screens = screens;
    }

    @FXML
    private Label labelHeader;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textPassword;

    @FXML
    public void login(){
        try{
            //test if the user can be logged
            if(true){

            }
            else throw new BadLoginException();
        }
        catch (BadLoginException e) {
            labelHeader.setText("Login failure, please try again: ");
            labelHeader.setTextFill(Color.DARKRED);
            return;
        }
        dialog.close();
        screens.feedScreen().show();

    }

    public void newUser() {
        screens.registrationDialog().show();
    }
}
