package javafx;

import util.Database;
import util.GenerateXML;
import util.XMLParser;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ScreensConfiguration screens = new ScreensConfiguration();
        screens.setPrimaryStage(primaryStage);
        screens.loginDialog().show();
    }


    public static void main(String[] args) {
        launch(args);
        /*Database db = new Database();
    	db.update("TRUNCATE TABLE `user`");
    	db.update("TRUNCATE TABLE `comment`");
    	db.update("TRUNCATE TABLE `feed`");
    	db.update("TRUNCATE TABLE `contain`");
    	db.update("TRUNCATE TABLE `feedsubscription`");
    	db.update("TRUNCATE TABLE `friendship`");
    	db.update("TRUNCATE TABLE `publication`");
    	db.update("TRUNCATE TABLE `readstatus`");
    	db.update("TRUNCATE TABLE `sharedpublication`");
    	db.close();
    	System.out.println("Done clearing bdd");
    	XMLParser a = new XMLParser("xmltest.xml");*/
    	//GenerateXML.run();
    	//System.out.println("done");
    }
}
