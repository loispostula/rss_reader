package javafx;

import entities.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Created by lpostula on 11/05/14.
 * Documentation de la classe FindFriendController
 */
public class FindFriendController implements DialogController {
    FXMLDialog dialog;
    ScreensConfiguration screens;


    @FXML
    ChoiceBox<String> criteriaBox;

    public FindFriendController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public void populateCriteria(){
        criteriaBox.getItems().addAll(
                "Email",
                "Nickname",
                "Number Of Friend",
                "Shared # Publications with X (#, X)",
                "Friend of X"
        );
    }

    public void close(){
        this.dialog.close();
    }

    private void constructTableForResult(Boolean custom){
        TableColumn avatar = new TableColumn("Avatar");
        avatar.setCellValueFactory(new PropertyValueFactory("avatar"));

        avatar.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                TableCell cell = new TableCell() {
                    ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Object o, boolean b) {
                        HBox box = new HBox();
                        box.setSpacing(10);
                        imageView.setImage((Image) o);
                        box.getChildren().add(imageView);
                        setGraphic(box);
                    }
                };
                return cell;
            }
        });

        TableColumn email = new TableColumn("Email");
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

        TableColumn nickname = new TableColumn("Nickname");
        nickname.setCellValueFactory(new PropertyValueFactory<User, String>("nickname"));

        TableColumn pubByDay = new TableColumn("#Pub / Jour");
        pubByDay.setCellValueFactory(new PropertyValueFactory<User, Integer>("pubByDay"));

        TableColumn friends = new TableColumn("#Friends");
        friends.setCellValueFactory(new PropertyValueFactory<User, Integer>("numOfFriend"));

        TableColumn sendRequest = new TableColumn("sendRequest");
        sendRequest.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                TableCell cell = new TableCell() {
                    @Override
                    protected void updateItem(Object o, boolean b) {
                        super.updateItem(o, b);

                        final VBox vbox = new VBox(5);
                        Image image = new Image("file:///"+getClass().getResource("icons/sendRequest.png").getPath());
                        Button button = new Button("", new ImageView(image));
                        final TableCell c = this;
                        button.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                TableRow row = c.getTableRow();
                                User receiver = (User) row.getTableView().getItems().get(row.getIndex());
                                receiver.receiveRequest(screens.getConnectedUser());
                            }
                        });
                        vbox.getChildren().add(button);
                        setGraphic(vbox);
                    }
                };
                return cell;
            }
        });
    }

    public void search(){
        //do some Stuff
    }
}
