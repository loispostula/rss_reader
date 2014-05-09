package javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.DialogController;

import java.io.IOException;
import java.net.URL;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe javafx.FXMLDialog
 */
public class FXMLDialog extends Stage {

    DialogController controller;

    public FXMLDialog(DialogController controller, URL fxml, Window owner) {
        this(controller, fxml, owner, StageStyle.DECORATED);
    }

    public FXMLDialog(final DialogController controller, URL fxml, Window owner, StageStyle style){
        super(style);
        this.controller = controller;
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader = new FXMLLoader(fxml);
        try{
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return controller;
                }
            });
            controller.setDialog(this);
            setScene(new Scene((Parent) loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DialogController getController()
    {
        return this.controller;
    }
}
