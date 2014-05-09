package javafx;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.commons.validator.EmailValidator;
import java.io.File;
import java.util.Date;

/**
 * Created by lpostula on 09/05/14.
 * Documentation de la classe registrationController
 */
public class RegistrationController implements DialogController {
    ScreensConfiguration screens;
    FXMLDialog dialog;

    public RegistrationController(ScreensConfiguration screensConfiguration) {
        this.screens = screensConfiguration;
    }

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    @FXML
    Label passwordInfo;
    @FXML
    Label mainError;
    @FXML
    PasswordField passwordField;
    @FXML
    PasswordField passwordConf;
    @FXML
    TextField emailField;
    @FXML
    TextField nicknameField;
    @FXML
    TextField cityField;
    @FXML
    TextField countryField;
    @FXML
    TextArea biographyArea;
    @FXML
    Image avatar_img;
    @FXML
    ImageView avatar;

    private String img_path;



    public void changeAvatar(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                System.out.println("Changing avatar");
                FileChooser fileChooser = new FileChooser();
                configureFileChooser(fileChooser);
                File file = fileChooser.showOpenDialog(this.dialog);
                if(file != null){
                    img_path = file.getAbsolutePath();
                    avatar_img = new Image("file://" + file.getAbsoluteFile());
                    avatar.setImage(avatar_img);
                }
            }
        }
    }

    private void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Choose Avatar");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    public void cancel() {
        this.dialog.close();
    }

    public void registerUser() {
        if(emailField.getText().isEmpty() ||
                cityField.getText().isEmpty() ||
                nicknameField.getText().isEmpty() ||
                countryField.getText().isEmpty()){
            mainError.setText("All field with a (*) must be filled");
            mainError.setTextFill(Color.DARKRED);
            return;
        }
        if(!passwordField.getText().equals(passwordConf.getText())){
            passwordInfo.setText("The password did not match: ");
            passwordInfo.setTextFill(Color.DARKRED);
            return;
        }
        if(passwordField.getText().length() < 6){
            passwordInfo.setText("The password must be at least 6 characters;");
            passwordInfo.setTextFill(Color.DARKRED);
            return;
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        Boolean valid = emailValidator.isValid(emailField.getText());
        if(!valid){
            mainError.setText("The email must be in a valid format (john@doe.com)");
            mainError.setTextFill(Color.DARKRED);
            return;
        }
        //todo save the new user
        User usr = new User(emailField.getText(),
                passwordField.getText(),
                nicknameField.getText(),
                cityField.getText(),
                countryField.getText(),
                img_path,
                biographyArea.getText(),
                new Date());
        usr.save();
        this.dialog.close();

    }
}
