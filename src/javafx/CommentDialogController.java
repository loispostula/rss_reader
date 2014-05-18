package javafx;

import entities.Comment;
import entities.Feed;
import entities.Publication;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.Date;

import static org.apache.commons.lang3.StringEscapeUtils.escapeXml;


/**
 * Created by lpostula on 16/05/14.
 * Documentation de la classe CommentDialogController
 */
public class CommentDialogController implements DialogController {
    private ScreensConfiguration screens;
    private FXMLDialog dialog;


    @FXML
    private ImageView pubImage;

    @FXML
    private Text pubTitle;

    @FXML
    TextArea commentArea;

    @FXML
    TableView<Comment> tableView;

    @FXML
    Label dialogTitle;

    @FXML
    Button comment;

    private Feed feed;
    private Publication publication;


    public CommentDialogController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @Override
    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public void loadPublication(Publication pub, Feed feed){
        publication= pub;
        this.feed = feed;
        pubImage.setImage(new Image(pub.getImageUrl()));
        pubTitle.setText(pub.getTitle());
        pubTitle.setWrappingWidth(500);
        loadComments();
    }

    private void loadComments(){
        final TableColumn commentUser = new TableColumn("User");
        commentUser.setCellValueFactory(new PropertyValueFactory("userNick"));

        final TableColumn commentDate = new TableColumn("Date");
        commentDate.setCellValueFactory(new PropertyValueFactory("dateP"));

        TableColumn commentText = new TableColumn("Text");
        commentText.setPrefWidth(350);
        commentText.setCellValueFactory(new PropertyValueFactory<Publication, String>("text"));
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                final TableCell cell = new TableCell() {
                    private Text text;

                    @Override
                    protected void updateItem(Object o, boolean b) {
                        super.updateItem(o, b);
                        if (!isEmpty()) {
                            text = new Text(o.toString());
                            text.setWrappingWidth(340);
                            setGraphic(text);
                        }
                    }
                };
                return cell;
            }
        };
        commentText.setCellFactory(cellFactory);

        tableView.getColumns().setAll(commentUser, commentDate, commentText);
        tableView.setItems(FXCollections.observableArrayList(publication.getComments(feed)));
    }


    public void doComment(ActionEvent actionEvent) {
        if (! commentArea.getText().isEmpty()){
            Comment comment = new Comment(feed, publication, screens.getConnectedUser(), new Date(), escapeXml(commentArea.getText()));
            comment.save();
            //TODO clear table
            loadComments();
        }
    }
}
