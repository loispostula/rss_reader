package javafx;

import entities.Feed;
import entities.Publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.List;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe FeedScreenController
 */
public class FeedScreenController implements DialogController{
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

    }

    public void loadFeeds(){
        List<Feed> feeds = Feed.getAllFeeds();
        for (Feed feed : feeds){
            TitledPane pane = new TitledPane();
            pane.setText(feed.getTitle());
            pane.setTooltip(new Tooltip(feed.getDescription()));
            pane.setGraphic(new ImageView(new Image(feed.getImage())));
            TableView<Publication> table = new TableView<Publication>();

            TableColumn pubEnclosure = new TableColumn();
            pubEnclosure.setCellValueFactory(new PropertyValueFactory("enclosureP"));

            pubEnclosure.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn tableColumn) {
                    TableCell cell = new TableCell(){
                        ImageView imageView = new ImageView();
                        @Override
                        protected void updateItem(Object o, boolean b) {
                            if (o != null && o instanceof Publication){
                                HBox box= new HBox();
                                box.setSpacing(10) ;
                                imageView.setFitHeight(50);
                                imageView.setFitWidth(50);
                                imageView.setImage(new Image(((Publication)o).getImage()));
                                box.getChildren().addAll(imageView);
                                setGraphic(box);
                            }
                        }
                    };
                    return cell;
                }
            });

            TableColumn pubDate = new TableColumn();
            pubDate.setCellValueFactory(new PropertyValueFactory("dateP"));

            TableColumn pubTitle = new TableColumn();
            pubTitle.setCellValueFactory(new PropertyValueFactory("titleP"));

            TableColumn pubOpen = new TableColumn();
            pubOpen.setCellValueFactory(new PropertyValueFactory("openP"));






            accordion.getPanes().add(pane);
        }
    }
}
