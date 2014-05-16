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

import java.io.File;

import util.Database;

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
    private User user;
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
    PasswordField passwordField;
    @FXML
    PasswordField passwordConf;
    @FXML
    Image avatar_img;
    @FXML
    ImageView avatar;
    @FXML
    Label joinDate;
    @FXML
    Label passwordInfo;

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
                    avatar_img = new Image("file://" + file.getAbsoluteFile());
                    avatar.setImage(avatar_img);
                    user.setAvatar(file.getAbsolutePath());
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
        if (!nicknameField.getText().isEmpty()){
            user.setNickname(nicknameField.getText());
        }
        if (!cityField.getText().isEmpty()){
            user.setCity(cityField.getText());
        }
        if (!countryField.getText().isEmpty()){
            user.setCountry(countryField.getText());
        }
        if (!biographyArea.getText().isEmpty()){
            user.setBiography(biographyArea.getText());
        }
        user.save();
        //todo save the profil
        modifyNicknameFeed();
        this.dialog.close();
    }
    
    private void modifyNicknameFeed(){
    	Database db = new Database();
    	db.update("UPDATE feed f SET f.description = \"Feed with all the publication which "+ nicknameField.getText()+" shares.\", "
    			+ "f.title = \""+nicknameField.getText()+" personnal feed\" "
    			+ "WHERE f.url = \"feed://"+user.getEmail()+"\"");
    }

    public void savePassword(){
        //todo save the password
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
        user.setPassword(passwordField.getText());
        user.save();
    }

    public void loadUser(User user){
        this.user = user;
        this.emailField.setText(user.getEmail());
        this.nicknameField.setText(user.getNickname());
        this.cityField.setText(user.getCity());
        this.countryField.setText(user.getCountry());
        this.biographyArea.setText(user.getBiography());
        this.joinDate.setText(user.getJoinedDate().toString());
        avatar_img = (Image) user.getAvatar();
        avatar.setImage(avatar_img);
    }
}