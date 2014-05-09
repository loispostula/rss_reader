package javafx;

import entities.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe ProfileController
 */
public class ProfileController implements DialogController {
    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public ProfileController(ScreensConfiguration screens) {
        this.screens = screens;
    }

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

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }


    public void changeAvatar(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                System.out.println("Changing avatar");
                FileChooser fileChooser = new FileChooser();
                configureFileChooser(fileChooser);
                File file = fileChooser.showOpenDialog(this.dialog);
                if(file != null){
                    System.out.println(file.getAbsolutePath());
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

    public void saveProfil() {
        //todo save the profil
        this.dialog.close();
    }

    public void savePassword(){
        //todo save the password
        this.dialog.close();
    }

    public void loadUser(User user){
        this.emailField.setText(user.getEmail());
        this.nicknameField.setText(user.getNickname());
        this.cityField.setText(user.getCity());
        this.countryField.setText(user.getCountry());
    }
}