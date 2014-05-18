package javafx;

import com.sun.org.apache.xpath.internal.operations.Bool;
import entities.Feed;
import entities.Publication;
import entities.SharedPublication;
import entities.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;
import util.Database;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml;

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
    @FXML
    ToggleButton switchRead;
    @FXML
    ImageView switchReadIV;
    Image unread = new Image("file:///" + getClass().getResource("icons/pubUnread.png").getPath());
    Image read = new Image("file:///" + getClass().getResource("icons/pubRead.png").getPath());

    @FXML
    ToggleButton switchFeed;
    @FXML
    ImageView switchFeedIV;
    Image sFeed = new Image("file:///" + getClass().getResource("icons/switchFeed.png").getPath());
    Image sPub = new Image("file:///" + getClass().getResource("icons/switchPub.png").getPath());


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
        FXMLDialog dial = this.screens.findFriendDialog();
        ((FindFriendController) dial.getController()).populateCriteria();
        dial.showAndWait();
    }

    public void acceptFriend() {
        FXMLDialog dial = this.screens.friendRequestDialog();
        ((FriendRequestController) dial.getController()).showRequest();
        dial.showAndWait();
        this.refresh();
    }

    public void share(Publication publication, String text) {
        Database db = new Database();
        db.update("INSERT INTO `rssreader`.`sharedpublication` (`user_email`, `publication_url`, `sharedDate`, `text`) VALUES"
                + "('" + screens.getConnectedUser().getEmail() + "', '" + publication.getUrl() + "', NOW(), '" + escapeXml(text) + "')");
        db.update("INSERT INTO `rssreader`.`contain` (`feed_url`, `publication_url`) VALUES"
                + "('" + "feed://"+screens.getConnectedUser().getEmail() + "', '" + publication.getUrl() + "')");
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
        refresh();
    }

    private void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Choose Feed");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Feed XML", "*.xml")
        );
    }

    private void setFriendRequestCount() {
        Database db = new Database();
        int nmb = 0;
        String query = "SELECT COUNT(*) FROM `friendship` WHERE `user2_email` = \"" + screens.getConnectedUser().getEmail() + "\" AND accepted = 0";
        ResultSet res = db.querry(query);
        try {
            if (res.next()) {
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

    public void refresh() {
        setFriendRequestCount();
        loadFeeds();
    }

    private void loadFeeds() {
        accordion.getPanes().removeAll(accordion.getPanes());
        List<Feed> feeds = null;
        if(!switchFeed.isSelected()){
        feeds = screens.getConnectedUser().getAllSubscription();
        }
        else{
            feeds = new ArrayList<Feed>();
            feeds.add(new Feed("", "All Publication","", "", "file://" + getClass().getResource("icons/rss_icon.png") ));
        }
        for (int i = 0; i < feeds.size(); ++i) {
            final Feed feed = feeds.get(i);
            TitledPane pane = new TitledPane();
            pane.setText(feed.getTitle());
            //pane.setTooltip(new Tooltip(feed.getDescription()));
            if (!feed.getImageS().isEmpty()) {
                ImageView temp = new ImageView((Image)feed.getImage());
                temp.setPreserveRatio(true);
                temp.setFitHeight(40);
                temp.setFitWidth(120);
                pane.setGraphic(temp);

            }
            final TableView<Publication> table = new TableView<Publication>();
            table.setTableMenuButtonVisible(true);

            final TableColumn pubEnclosure = new TableColumn("Image");
            pubEnclosure.setCellValueFactory(new PropertyValueFactory("imageLoad"));

            pubEnclosure.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn tableColumn) {
                    final TableCell cell = new TableCell() {
                        ImageView imageView = new ImageView();
                        Button btn = new Button("", imageView);
                        @Override
                        protected void updateItem(Object o, boolean b) {
                            if (o != null) {
                                HBox box = new HBox();
                                box.setSpacing(10);
                                imageView.setImage((Image) o);
                                imageView.setPreserveRatio(true);
                                imageView.setFitHeight(70);
                                imageView.setFitWidth(140);
                                btn.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        int index = getTableRow().getIndex();
                                        Publication curr = table.getItems().get(index);
                                        Image newI = new Image(curr.getImageUrl());
                                        imageView.setImage(newI);
                                        imageView.setPreserveRatio(true);
                                        imageView.setFitHeight(70);
                                        imageView.setFitWidth(140);
                                    }
                                });
                                box.getChildren().add(btn);
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
            pubTitle.setPrefWidth(300);
            pubTitle.setCellValueFactory(new PropertyValueFactory<Publication, String>("title"));
            Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn tableColumn) {
                    final TableCell cell = new TableCell() {
                        private TextFlow text;
                        @Override
                        protected void updateItem(Object o, boolean b) {
                            super.updateItem(o, b);
                            if (!isEmpty()) {
                                TableRow row = getTableRow();
                                Publication pub = (Publication) row.getTableView().getItems().get(row.getIndex());
                                Text text1 = new Text(o.toString());
                                text1.setWrappingWidth(290);
                                text1.setStyle("-fx-font-weight: bold");
                                Text skip = new Text("\n");
                                Text text2 = null;
                                if (feed.isSharedFeed()){
                                    text2 = new Text();//here for the comment
                                }
                                else text2 = new Text(pub.getDescription().substring(0, pub.getDescription().length() < 100? pub.getDescription().length() : 100).replace("&apos;", "'") + " ...");
                                text2.setWrappingWidth(290);
                                text = new TextFlow(text1, skip, text2);
                                setGraphic(text);
                            }
                        }
                    };
                    return cell;
                }
            };
            pubTitle.setCellFactory(cellFactory);

            TableColumn openPub = new TableColumn();
            openPub.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn tableColumn) {
                    TableCell cell = new TableCell() {
                        @Override
                        protected void updateItem(Object o, boolean b) {
                            super.updateItem(o, b);

                            final VBox vbox = new VBox(5);
                            Image image = new Image("file:///" + getClass().getResource("icons/openPub.png").getPath());
                            Button button = new Button("", new ImageView(image));
                            final TableCell c = this;
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    TableRow row = c.getTableRow();
                                    Publication pub = (Publication) row.getTableView().getItems().get(row.getIndex());
                                    openBrowser(pub.getUrl());
                                    if(!switchFeed.isSelected()){
                                    	pub.markAsRead(screens.getConnectedUser(), feed);
                                    }
                                }
                            });
                            vbox.getChildren().add(button);
                            setGraphic(vbox);
                        }
                    };
                    return cell;
                }
            });

            TableColumn sharePub = new TableColumn();
            sharePub.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn tableColumn) {
                    TableCell cell = new TableCell() {
                        @Override
                        protected void updateItem(Object o, boolean b) {
                            super.updateItem(o, b);

                            final VBox vbox = new VBox(5);
                            Image image = new Image("file:///" + getClass().getResource("icons/sharePub.png").getPath());
                            Button button = new Button("", new ImageView(image));
                            final TableCell c = this;
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    TableRow row = c.getTableRow();
                                    Publication pub = (Publication) row.getTableView().getItems().get(row.getIndex());
                                    String sharedComment = getComment();
                                    share(pub, sharedComment);//todo get the text
                                }
                            });
                            if (switchFeed.isSelected()){
                                button.setDisable(true);
                            }
                            vbox.getChildren().add(button);
                            setGraphic(vbox);
                        }
                    };
                    return cell;
                }
            });

            TableColumn commentPub = new TableColumn();
            commentPub.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn tableColumn) {
                    TableCell cell = new TableCell() {
                        @Override
                        protected void updateItem(Object o, boolean b) {
                            super.updateItem(o, b);
                            final VBox vbox = new VBox(5);
                            Image image = new Image("file:///" + getClass().getResource("icons/commentPub.png").getPath());
                            Button button = new Button("", new ImageView(image));
                            final TableCell c = this;
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    TableRow row = c.getTableRow();
                                    Publication pub = (Publication) row.getTableView().getItems().get(row.getIndex());
                                    FXMLDialog dial = screens.commentDialog();
                                    dial.show();
                                    ((CommentDialogController) dial.getController()).loadPublication(pub, feed);
                                }
                            });
                            if (switchFeed.isSelected()){
                                button.setDisable(true);
                            }
                            vbox.getChildren().add(button);
                            setGraphic(vbox);
                        }
                    };
                    return cell;
                }
            });
            if(switchRead.isSelected()){
                table.setItems(FXCollections.observableArrayList(feed.getAllPublications()));
            }
            else
            {
                if(switchFeed.isSelected()){
                    table.setItems(FXCollections.observableArrayList(screens.getConnectedUser().getAllpublication()));
                }
                else{
                    table.setItems(FXCollections.observableArrayList(feed.getUnreadPublications(screens.getConnectedUser())));
                }
            }
            table.getColumns().addAll(pubEnclosure, pubDate, pubTitle, openPub, sharePub, commentPub);
            pane.setContent(table);
            pane.setMinWidth(800);
            accordion.getPanes().add(pane);
        }
    }

    private void openBrowser(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

        try {

            if (os.indexOf("win") >= 0) {

                // this doesn't support showing urls in the form of "page.html#nameLink"
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);

            } else if (os.indexOf("mac") >= 0) {

                rt.exec("open " + url);

            } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {

                // Do a best guess on unix until we get a platform independent way
                // Build a list of browsers to try, in this order.
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                        "netscape", "opera", "links", "lynx"};

                // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++)
                    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");

                rt.exec(new String[]{"sh", "-c", cmd.toString()});

            } else {
                return;
            }
        } catch (Exception e) {
            return;
        }
    }

    public String getComment(){
        String comment = Dialogs.create()
                .title("Comment")
                .masthead("Comment")
                .message("Please insert your comment")
                .showTextInput();
        return comment;
    }

    public void switchFeed() {
        if (switchFeed.isSelected()){
            //first we change the image
            switchFeedIV.setImage(sFeed);
            switchRead.setSelected(false);
            switchRead.setDisable(true);
        }
        else{
            //first we change the image
            switchFeedIV.setImage(sPub);
            switchRead.setDisable(false);
        }
        refresh();
    }

    public void switchRead() {
        if (switchRead.isSelected()){
            //first we change the image
            switchReadIV.setImage(read);
        }
        else{
            //first we change the image
            switchReadIV.setImage(unread);
        }
        refresh();
    }

    public void searchFeed() {
        FXMLDialog dial = screens.searchFeedDialog();
        ((SearchFeedController)dial.getController()).populateCriteria();
        dial.showAndWait();
        refresh();
    }
}
