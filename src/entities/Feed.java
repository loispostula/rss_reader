package entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import util.Database;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe Feed
 */
public class Feed {

    private String url;
    private String title;
    private String description;
    private String link;

    public Feed() {
    }
    
    

    public Feed(String url, String title, String description, String link) {
		super();
		this.url = url;
		this.title = title;
		this.description = description;
		this.link = link;
	}



    public static Feed getFeedFromDb(String url){
        Database db = new Database();
        Feed feed = null;
        ResultSet res = db.querry("SELECT * FROM `feed` WHERE `url` == \"" + url +"\"");
        try {
            if (res.next()){
                feed = new Feed(url, res.getString("title"), res.getString("description"), res.getString("link"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db.close();
        return feed;
    }

    public void save(){
        Database db = new Database();
        if (getFeedFromDb(url) == null){
        	db.update("INSERT INTO `feed` (`url`, `title`, `link`, `description`"
        		+ "('"+ url +"', '"+title +"', '"+ link +"', '"+ description +"')");
        }
    }



	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
