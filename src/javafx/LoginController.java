package javafx;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Date;

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
        if(textEmail.getText().isEmpty() && textPassword.getText().isEmpty()){
            labelHeader.setText("Please enter the email and the password: ");
            labelHeader.setTextFill(Color.DARKRED);
            return;
        }
        User usr = User.getUserFromDb(textEmail.getText(), textPassword.getText());
        if(usr == null){
            labelHeader.setText("Login failure, please try again: ");
            labelHeader.setTextFill(Color.DARKRED);
            return;
        }
        screens.connectUser(usr);
        dialog.close();
        screens.feedScreen().show();

    }

    public void newUser() {
        screens.registrationDialog().show();
    }
}
