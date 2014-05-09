package entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
        ResultSet res = db.querry("SELECT * FROM `feed` WHERE `url` LIKE \"" + url +"\"");
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
        ArrayList<Feed> feeds = new ArrayList<Feed>();
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

    public List<Publication> getAllPublications(){
        ArrayList<Publication> publications = new ArrayList<Publication>();
        Publication pub = new Publication(
                "http://www.lalibre.be/culture/cinema/dix-raisons-de-se-souvenir-de-mickey-rooney-534280003570aae038afda0a",
                "Dix raisons de se souvenir de Mickey Rooney",
                new Date("Mon, 07 Apr 2014 10:47:07 +0100"),
                "&lt;img src=&quot;http://r.llb.be/image/62/53427fb23570d35ee3e8e862.jpg&quot; width=&quot;140&quot; height=&quot;70&quot; alt=&quot;&quot; border=&quot;0&quot; hspace=&quot;4&quot; vspace=&quot;4&quot; align=&quot;left&quot; /&gt;Quelque deux mois après Shirley Temple, Hollywood dit adieu à l'autre enfant-star de son âge d'or. \n" +
                        "Retour en dix points sur une carrière exceptionnelle avec Alain Lorfèvre.",
                "http://r.llb.be/image/62/53427fb23570d35ee3e8e862.jpg"
        );
        publications.add(pub);
        return publications;
    }

    public void save(){
        Database db = new Database();
        if (getFeedFromDb(url) == null){
        	db.update("INSERT INTO `feed` (`url`, `title`, `link`, `description`) VALUES "
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
