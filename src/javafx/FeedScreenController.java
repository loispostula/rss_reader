package javafx;

import entities.Feed;
import entities.Publication;
import entities.User;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import util.Database;
import util.FeedParser;

import static org.apache.commons.lang3.StringEscapeUtils.*;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe FeedScreenController
 */
public class FeedScreenController implements DialogController {
    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public FeedScreenController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML
    Accordion accordion;

    @FXML
    ImageView friendRequest;
    @FXML
    StackPane stackPane;

    List<TitledPane> feedPane;

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public void logout() {
        dialog.close();
        screens.disconnectUser();
        screens.loginDialog().show();
    }

    public void addFriend() {
        String email;
        //TODO get email via inputbox
        email = "lois@postula.be";
        if (User.getUserFromDb(email) != null) {
            Database db = new Database();
            db.update("INSERT INTO `friendship` (`user1_email`, `user2_email`, `requestDate`, `accepted`) VALUES"
                    + "('" + screens.getConnectedUser().getEmail() + "', '" + email + "', NOW(), 0)");
            db.close();
        }
    }

    public ArrayList<User> getFirendRequest() {
        Database db = new Database();
        String query = "SELECT * FROM `friendship` WHERE `user2_email` = \"" + screens.getConnectedUser().getEmail() + "\" AND accepted = 0";
        ResultSet res = db.querry(query);
        ArrayList<User> requests = new ArrayList<User>();
        try {
            while (res.next()) {
                requests.add(User.getUserFromDb(res.getString("user1_email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void acceptFriend() {
        FXMLDialog dial= this.screens.friendRequestDialog();
        dial.show();
        ((FriendRequestController)dial.getController()).showRequest();
        this.setFriendRequestCount();
//        if (User.getUserFromDb(email) != null) {
//            Database db = new Database();
//            db.update("UPDATE `rssreader`.`friendship` SET `accepted` = '0' "
//                    + "WHERE `friendship`.`user1_email` = '" + email + "' AND `friendship`.`user2_email` = '" + screens.getConnectedUser().getEmail() + "'");
//            db.close();
//        }
    }

    public void share(Publication publication, String text) {
        Database db = new Database();
        db.update("INSERT INTO `rssreader`.`sharedpublication` (`user_email`, `publication_url`, `sharedDate`, `text`) VALUES"
                + "('" + screens.getConnectedUser().getEmail() + "', '" + publication.getUrl() + "', NOW(), '" + escapeXml(text) + "')");
        db.close();
    }

    public void addComment(Publication publication, String text, Feed feed) {
        Database db = new Database();
        db.update("INSERT INTO `rssreader`.`comment` (`feed_url`, `publication_url`, `user_email`, `date`, `text`) VALUES"
                + "('" + feed.getUrl() + "', '" + publication.getUrl() + "', '" + screens.getConnectedUser().getEmail() + "', NOW(), '" + escapeXml(text) + "')");
        db.close();
    }

    public void showProfil() {
        FXMLDialog dial = screens.profileDialog();
        ProfileController controller = (ProfileController) dial.getController();
        dial.show();
        controller.loadUser(screens.getConnectedUser());

    }

    public void addNewFeed(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(this.dialog);
        if (file != null) {
            Feed feed = Feed.getFeedFromFile(file.getAbsolutePath());
            screens.getConnectedUser().subscribe(feed);
        }
    }

    private void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Choose Feed");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Feed XML", "*.xml")
        );
    }

    private void setFriendRequestCount(){
        Database db = new Database();
        int nmb = 0;
        String query = "SELECT COUNT(*) FROM `friendship` WHERE `user2_email` = \"" + screens.getConnectedUser().getEmail() + "\" AND accepted = 0";
        ResultSet res = db.querry(query);
        try {
            if(res.next()){
                nmb = res.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Label friendReq = new Label(" " + Integer.toString(nmb) + " ");
        friendReq.setTranslateX(10);
        friendReq.setTranslateY(10);
        friendReq.setTextFill(Color.WHITE);
        friendReq.setStyle("-fx-background-color: red; -fx-background-radius: 5; -fx-font-family: Lucida Grande, Verdana, Arial, sans-serif; -fx-font-size: 10;");
        stackPane.getChildren().add(friendReq);
    }

    public void refresh(){
        setFriendRequestCount();
        //loadFeeds();
    }

    private void loadFeeds() {
        List<Feed> feeds = screens.getConnectedUser().getAllSubscription();
        for (int i = 0; i < feeds.size(); ++i) {
            Feed feed = feeds.get(i);
            TitledPane pane = new TitledPane();
            pane.setText(feed.getTitle());
            pane.setTooltip(new Tooltip(feed.getDescription()));
            if (!feed.getImage().isEmpty()) {
                pane.setGraphic(new ImageView(new Image(feed.getImage())));

            }
            TableView<Publication> table = new TableView<Publication>();
            table.setTableMenuButtonVisible(true);

            TableColumn pubEnclosure = new TableColumn("Image");
            pubEnclosure.setCellValueFactory(new PropertyValueFactory("image"));

            pubEnclosure.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn tableColumn) {
                    TableCell cell = new TableCell() {
                        ImageView imageView = new ImageView();

                        @Override
                        protected void updateItem(Object o, boolean b) {
                            if (o != null) {
                                HBox box = new HBox();
                                box.setSpacing(10);
                                imageView.setImage((Image) o);
                                box.getChildren().add(imageView);
                                setGraphic(box);
                            }
                        }
                    };
                    return cell;
                }
            });

            TableColumn pubDate = new TableColumn("Date");
            pubDate.setMinWidth(50);
            pubDate.setCellValueFactory(new PropertyValueFactory<Publication, String>("releaseDate"));

            TableColumn pubTitle = new TableColumn("title");
            pubTitle.setMinWidth(200);
            pubTitle.setCellValueFactory(new PropertyValueFactory<Publication, String>("title"));

//            TableColumn pubOpen = new TableColumn();
//            pubOpen.setCellValueFactory(new PropertyValueFactory("openP"));

            table.setItems(FXCollections.observableArrayList(feed.getUnreadPublications(screens.getConnectedUser())));
            table.getColumns().addAll(pubEnclosure, pubDate, pubTitle);
            pane.setContent(table);
            accordion.getPanes().add(pane);
        }
        //TODO refactorer ceci
//        feeds = screens.getConnectedUser().getAllFriendsSubscription();
//        for (Feed feed : feeds) {
//            TitledPane pane = new TitledPane();
//            pane.setText(feed.getTitle());
//            pane.setTooltip(new Tooltip(feed.getDescription()));
//            if(!feed.getImage().isEmpty()){
//                pane.setGraphic(new ImageView(new Image(feed.getImage())));
//
//            }
//            TableView<Publication> table = new TableView<Publication>();
//            table.setTableMenuButtonVisible(true);
//
//            TableColumn pubEnclosure = new TableColumn("Image");
//            pubEnclosure.setCellValueFactory(new PropertyValueFactory("image"));
//
//            pubEnclosure.setCellFactory(new Callback<TableColumn, TableCell>() {
//                @Override
//                public TableCell call(TableColumn tableColumn) {
//                    TableCell cell = new TableCell() {
//                        ImageView imageView = new ImageView();
//
//                        @Override
//                        protected void updateItem(Object o, boolean b) {
//                            if (o != null) {
//                                HBox box = new HBox();
//                                box.setSpacing(10);
//                                imageView.setFitHeight(60);
//                                imageView.setFitWidth(80);
//                                imageView.setImage(new Image((String) o));
//                                box.getChildren().addAll(imageView);
//                                setGraphic(box);
//                            }
//                        }
//                    };
//                    return cell;
//                }
//            });
//
//            TableColumn pubDate = new TableColumn("Date");
//            pubDate.setMinWidth(50);
//            pubDate.setCellValueFactory(new PropertyValueFactory<Publication, String>("releaseDate"));
//
//            TableColumn pubTitle = new TableColumn("title");
//            pubTitle.setMinWidth(200);
//            pubTitle.setCellValueFactory(new PropertyValueFactory<Publication, String>("title"));
//
////            TableColumn pubOpen = new TableColumn();
////            pubOpen.setCellValueFactory(new PropertyValueFactory("openP"));
//
//            table.setItems(FXCollections.observableArrayList(feed.getUnreadShares(screens.getConnectedUser())));
//            table.getColumns().addAll(pubEnclosure, pubDate, pubTitle);
//            pane.setContent(table);
//            accordion.getPanes().add(pane);
//        }
    }
}
