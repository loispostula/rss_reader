package javafx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import util.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by lpostula on 10/05/14.
 * Documentation de la classe acceptFriendController
 */
public class FriendRequestController implements DialogController {
    private FXMLDialog dialog;
    private ScreensConfiguration screens;
    @FXML
    TableView tableView;

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public FriendRequestController(ScreensConfiguration screens){
        this.screens = screens;
    }




    public void showRequest() {
        constructTable();
        ObservableList<FriendRequest> requests = getRequests();
        tableView.setItems(requests);
    }

    private void constructTable() {
        TableColumn avatar = new TableColumn("Avatar");
        avatar.setCellValueFactory(new PropertyValueFactory("friendAvatar"));

        avatar.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                TableCell cell = new TableCell(){
                    ImageView imageView = new ImageView();
                    @Override
                    protected void updateItem(Object o, boolean b) {
                        HBox box = new HBox();
                        box.setSpacing(10);
                        imageView.setImage((Image) o );
                        box.getChildren().add(imageView);
                        setGraphic(box);
                    }
                };
                return cell;
            }
        });

        TableColumn email = new TableColumn("Email");
        email.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("friendEmail"));

        TableColumn nickname = new TableColumn("Nickname");
        nickname.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("friendNikName"));

        tableView.getColumns().addAll(avatar, email, nickname);
    }

    private ObservableList<FriendRequest> getRequests() {
        ArrayList<FriendRequest> request = new ArrayList<FriendRequest>();
        Database db = new Database();
        int nmb = 0;
        String query = "SELECT * FROM user u INNER JOIN friendship f ON f.user1_email = u.email WHERE f.user2_email = \"" + screens.getConnectedUser().getEmail() + "\" AND f.accepted = 0";
        ResultSet res = db.querry(query);
        try {
            while(res.next()){
                FriendRequest fr = new FriendRequest(res.getString("avatar"), res.getString("email"), res.getString("nickname"));
                request.add(fr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(request);
    }

    public class FriendRequest{
        private SimpleObjectProperty friendAvatar;
        private SimpleStringProperty friendEmail;
        private SimpleStringProperty friendNickName;

        public FriendRequest(String image, String email, String nickname){
            friendAvatar = new SimpleObjectProperty(new Image("file:///" + image, 50, 50, true, true));
            friendEmail = new SimpleStringProperty(email);
            friendNickName = new SimpleStringProperty(nickname);
        }

        public Object getFriendAvatar() {
            return friendAvatar.get();
        }

        public void setFriendAvatar(Object friendAvatar) {
            this.friendAvatar.set(friendAvatar);
        }

        public String getFriendEmail() {
            return friendEmail.get();
        }
        public void setFriendEmail(String friendEmail) {
            this.friendEmail.set(friendEmail);
        }

        public String getFriendNickName() {
            return friendNickName.get();
        }

        public void setFriendNickName(String friendNickName) {
            this.friendNickName.set(friendNickName);
        }


    }
}
