package javafx;

import entities.User;
import javafx.collections.FXCollections;
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
import util.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by lpostula on 11/05/14.
 * Documentation de la classe FindFriendController
 */
public class FindFriendController implements DialogController {
    FXMLDialog dialog;
    ScreensConfiguration screens;


    @FXML
    ChoiceBox<String> criteriaBox;

    @FXML
    TableView tableView;

    @FXML
    TextField querryVal;

    public FindFriendController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public void populateCriteria() {
        criteriaBox.getItems().addAll(
                "Email",
                "Nickname",
                "Number Of Friend (#start, #end)",
                "Shared # Publications with X (#, X)",
                "Friend of X"
        );
        criteriaBox.getSelectionModel().selectFirst();
        this.constructTableForResult();
    }

    public void close() {
        this.dialog.close();
    }

    private void constructTableForResult() {
        TableColumn avatar = new TableColumn("Avatar");
        avatar.setCellValueFactory(new PropertyValueFactory("avatar"));
        avatar.setMinWidth(80);
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
                        imageView.setFitWidth(50);
                        imageView.setPreserveRatio(true);
                        box.getChildren().add(imageView);
                        setGraphic(box);
                    }
                };
                return cell;
            }
        });

        TableColumn email = new TableColumn("Email");
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        email.setMinWidth(250);
        TableColumn nickname = new TableColumn("Nickname");
        nickname.setCellValueFactory(new PropertyValueFactory<User, String>("nickname"));
        nickname.setMinWidth(80);
        TableColumn pubByDay = new TableColumn("#Pub / Jour");
        pubByDay.setCellValueFactory(new PropertyValueFactory<User, Integer>("pubByDay"));
        pubByDay.setMinWidth(100);
        TableColumn friends = new TableColumn("#Friends");
        friends.setCellValueFactory(new PropertyValueFactory<User, Integer>("numOfFriend"));
        friends.setMinWidth(80);
        TableColumn sendRequest = new TableColumn("sendRequest");
        sendRequest.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                TableCell cell = new TableCell() {
                    @Override
                    protected void updateItem(Object o, boolean b) {
                        super.updateItem(o, b);

                        final VBox vbox = new VBox(5);
                        Image image = new Image("file:///" + getClass().getResource("icons/sendRequest.png").getPath());
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
        sendRequest.setMinWidth(80);
        tableView.getColumns().setAll(avatar, email, nickname, pubByDay, friends, sendRequest);
    }

    public void search() {
        //do some Stuff
        ArrayList<User> searchResult = new ArrayList<User>();
        int querryType = criteriaBox.getSelectionModel().getSelectedIndex();
        String querry = "";
        switch (querryType) {
            case 0:
                //email search
                querry = "SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate FROM user u WHERE email = \"" + querryVal.getText() + "\"";
                break;
            case 1:
                //nickname
                querry = "SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate FROM user u WHERE nickname = \"" + querryVal.getText() + "\"";
                break;
            case 2:
                //numberOfFriend
                String[] born = querryVal.getText().split(",");
                if (born.length == 2) {
                    querry = "SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate, COUNT(u.email)  FROM user u " +
                            "INNER JOIN friendship f " +
                            "ON f.user1_email = u.email OR " +
                            "f.user2_email = u.email " +
                            "GROUP BY u.email " +
                            "HAVING COUNT(u.email) < " + born[1];
                    if (born[0] == "0") {
                        querry += " UNION " +
                                "SELECT u.email, 0 FROM user u " +
                                "WHERE u.email not in (SELECT user1_email from friendship) " +
                                "AND u.email not in (SELECT user2_email from friendship)";
                    } else {
                        querry += " AND COUNT(u.email) > " + born[0];
                    }
                }
                System.out.println(querry);
                break;
            case 3:
                //shared publication
                String[] arg = querryVal.getText().split(",");
                if (arg.length == 2) {
                    querry = "SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate FROM user u "
                    		+ "INNER JOIN sharedpublication s ON s.user_email = u.email " +
                            "INNER JOIN " +
                            "(" +
                            "SELECT publication_url " +
                            "FROM sharedpublication " +
                            "WHERE user_email = \"" + arg[1] + "\"" +
                            ") a " +
                            "ON s.publication_url = a.publication_url " +
                            "GROUP BY s.user_email " +
                            "HAVING COUNT(s.user_email) > " + arg[0];
                }
                break;
            case 4:
                //friends of X
                querry = "SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate FROM user u " +
                        "INNER JOIN friendship f " +
                        "ON f.user1_email = u.email OR " +
                        "f.user2_email = u.email " +
                        "WHERE (f.user1_email = \"" + querryVal.getText() + "\" OR " +
                        "f.user2_email = \"" + querryVal.getText() + "\") AND (u.email != \""+querryVal.getText()+"\") AND (f.accepted = 1)";
                break;
        }
        Database db = new Database();
        ResultSet res = db.querry(querry);
        try {
            while (res.next()) {
                User user = new User(res.getString("email"), res.getString("password"), res.getString("nickname"), res.getString("city"),
                		res.getString("country"), res.getString("avatar"), res.getString("biography"), res.getDate("joinedDate"));
                if (!user.equals(screens.getConnectedUser())) {
                    user.computeStat();
                    searchResult.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(FXCollections.observableArrayList(searchResult));
    }
}
