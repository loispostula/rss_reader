package entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private String image;

    public Feed() {
    }
    
    

    public Feed(String url, String title, String description, String link, String image) {
		super();
		this.url = url;
		this.title = title;
		this.description = description;
		this.link = link;
		this.image = image;
	}



    public static Feed getFeedFromDb(String url){
        Database db = new Database();
        Feed feed = null;
        ResultSet res = db.querry("SELECT * FROM `feed` WHERE `url` == \"" + url +"\"");
        try {
            if (res.next()){
                feed = new Feed(url, res.getString("title"), res.getString("description"), res.getString("link"), res.getString("image"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db.close();
        return feed;
    }

    public static List<Feed> getAllFeeds(){
        ArrayList<Feed> feeds = new ArrayList<>();
        Database db = new Database();
        ResultSet res = db.querry("Select * FROM `feed`");
        try {
            while (res.next()){
                feeds.add(new Feed(res.getString("url"), res.getString("title"), res.getString("description"), res.getString("link"), res.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        return feeds;
    }

    public void save(){
        Database db = new Database();
        if (getFeedFromDb(url) == null){
        	db.update("INSERT INTO `feed` (`url`, `title`, `link`, `description`"
        		+ "('"+ url +"', '"+title +"', '"+ link +"', '"+ description +"')");
        }
        db.close();
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



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
