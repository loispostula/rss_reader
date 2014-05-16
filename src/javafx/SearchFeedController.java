package javafx;

import entities.Feed;
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
import util.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by lpostula on 11/05/14.
 * Documentation de la classe FindFriendController
 */
public class SearchFeedController implements DialogController {
    FXMLDialog dialog;
    ScreensConfiguration screens;


    @FXML
    ChoiceBox<String> criteriaBox;

    @FXML
    TableView tableView;

    @FXML
    TextField querryVal;

    public SearchFeedController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public void populateCriteria() {
        criteriaBox.getItems().addAll(
                "R2 : Feed Followed By User Who Follow Two Or More Feed Of X' subscription",
                "R3 : Feed From X, with no shared Publication and no Friend Following",
                "R5 : Feed of X with detail"
        );
        criteriaBox.getSelectionModel().selectFirst();
        this.constructTableForResult();
    }

    public void close() {
        this.dialog.close();
    }

    private void constructTableForResult() {
        TableColumn imageC = new TableColumn("Image");
        imageC.setCellValueFactory(new PropertyValueFactory("image"));
        imageC.setMinWidth(80);
        imageC.setCellFactory(new Callback<TableColumn, TableCell>() {
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

        TableColumn titleC = new TableColumn("Title");
        titleC.setCellValueFactory(new PropertyValueFactory<User, String>("title"));
        titleC.setMinWidth(250);
        TableColumn pubView = new TableColumn("#Pub read");
        pubView.setCellValueFactory(new PropertyValueFactory<Feed, Integer>("pubView"));
        pubView.setMinWidth(100);
        TableColumn pubShared = new TableColumn("#Pub Shared");
        pubShared.setCellValueFactory(new PropertyValueFactory<Feed, Integer>("pubShared"));
        pubShared.setMinWidth(100);
        TableColumn pubRatio = new TableColumn("Ratio");
        pubRatio.setCellValueFactory(new PropertyValueFactory<Feed, Integer>("pubRatio"));
        pubRatio.setMinWidth(100);
        tableView.getColumns().setAll(imageC, titleC, pubView, pubShared, pubRatio);
    }

    public void search() {
        //do some Stuff
        ArrayList<Feed> searchResult = new ArrayList<Feed>();
        int querryType = criteriaBox.getSelectionModel().getSelectedIndex();
        switch (querryType) {
            case 0:
                    searchResult = Request.feedFollowedByUserWhoFollowTwoOrMoreFeedOfX(querryVal.getText());
                break;
            case 1:
                searchResult = Request.notFollowedByFriendAndNotShared(querryVal.getText());
                break;
            case 2:
                searchResult = Request.feedReadShareInfo(querryVal.getText());
                break;
        }
        tableView.setItems(FXCollections.observableArrayList(searchResult));
    }
}
