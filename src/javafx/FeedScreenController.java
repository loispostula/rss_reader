package javafx;

import entities.Feed;
import entities.Publication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import util.FeedParser;

import java.io.File;
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
            FeedParser pars = new FeedParser(file.getAbsolutePath());
            screens.getConnectedUser().subscribe(pars.getFeed());
        }
    }

    private void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Choose Feed");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Feed XML", "*.xml")
        );
    }

    public void loadFeeds() {
        List<Feed> feeds = Feed.getAllFeeds();
        for (Feed feed : feeds) {
            TitledPane pane = new TitledPane();
            pane.setText(feed.getTitle());
            pane.setTooltip(new Tooltip(feed.getDescription()));
            if(!feed.getImage().isEmpty()){
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
                                imageView.setFitHeight(60);
                                imageView.setFitWidth(80);
                                imageView.setImage(new Image((String) o));
                                box.getChildren().addAll(imageView);
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

            table.setItems(FXCollections.observableArrayList(feed.getAllPublications()));
            table.getColumns().addAll(pubEnclosure, pubDate, pubTitle);
            pane.setContent(table);
            accordion.getPanes().add(pane);
        }
    }
}
