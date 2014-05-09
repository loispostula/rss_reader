package entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.Database;
import util.FeedParser;
import static org.apache.commons.lang3.StringEscapeUtils.*;

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

    public static Feed getFeedFromFile(String url){
        Feed feed = getFeedFromDb(url);
        if(feed == null){
            FeedParser pars = new FeedParser(url);
            feed = pars.getFeed();
        }
        return feed;
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

    public List<Publication> getAllPublications(){
        Database db = new Database();
        ArrayList<Publication> publications = new ArrayList<Publication>();
        String query ="SELECT p.url FROM publication p " +
                "INNER JOIN contain cnt " +
                "ON cnt.publication_url = p.url " +
                "INNER JOIN feed f " +
                "ON f.url = cnt.feed_url " +
                "WHERE f.url = \""+ escapeXml(this.getUrl()) + "\"";
        System.out.println(query);
        ResultSet res = db.querry(query);
        try {
            while(res.next()){
                publications.add(Publication.getPublicationFromDb(res.getString("url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publications;
    }
    


    public List<Publication> getUnreadPublications(User user){
        Database db = new Database();
        ArrayList<Publication> publications = new ArrayList<Publication>();
        String query ="SELECT p.url FROM publication p " +
                "INNER JOIN contain cnt " +
                "ON cnt.publication_url = p.url " +
                "INNER JOIN feed f " +
                "ON f.url = cnt.feed_url " +
                "WHERE f.url = \""+ escapeXml(this.getUrl()) + "\" " +
                "AND NOT EXISTS (SELECT * FROM readstatus r WHERE r.publication_url = p.url AND r.feed_url = f.url AND r.user_email = \""+ user.getEmail() +"\")";
        System.out.println(query);
        ResultSet res = db.querry(query);
        try {
            while(res.next()){
            	publications.add(Publication.getPublicationFromDb(res.getString("url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publications;
    }

    public List<Publication> getAllShares(){
        Database db = new Database();
        ArrayList<Publication> publications = new ArrayList<Publication>();
        String query ="SELECT * FROM sharedpublication s " +
                "WHERE s.user_email = \""+ this.getUrl() + "\"";
        ResultSet res = db.querry(query);
        try {
            while(res.next()){
                publications.add(Publication.getPublicationFromDb(res.getString("publication_url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publications;
    }

    public void save(){
        Database db = new Database();
        if (getFeedFromDb(url) == null){
        	db.update("INSERT INTO `feed` (`url`, `title`, `link`, `description`, `image`) VALUES "
        		+ "('"+ url +"', '"+title +"', '"+ link +"', '"+ description +"', '"+image+"')");
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
