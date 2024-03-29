package entities;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import org.apache.commons.lang3.StringEscapeUtils;
import util.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe Publications
 */
public class Publication {
    private SimpleStringProperty url;
    private SimpleStringProperty title;
    private SimpleStringProperty releaseDate;
    private Date releaseDateF;
    private SimpleStringProperty description;
    private static SimpleObjectProperty<Image> imageLoad = new SimpleObjectProperty<Image>(
            new Image(
    "file://" + Publication.class.getResource("/javafx/icons/clickToLoad.png").getPath()
            )

    );

    private String imageUrl;
    public Publication() {
    }



    public Publication(String url, String title, Date releaseDate,
			String description, String image) {
		super();
		this.url = new SimpleStringProperty(url);
		this.title = new SimpleStringProperty(title);
		this.releaseDate = new SimpleStringProperty(releaseDate.toString());
        this.releaseDateF = releaseDate;
		this.description = new SimpleStringProperty(description);
        this.imageUrl = image;
        if (imageUrl == ""){
        	imageUrl = "file://"+System.getProperty("user.dir").replace("\\", "/")+"/icons/rss_icon.png";
        }
	}



    public static Publication getPublicationFromDb(String url){
        Database db = new Database();
        Publication publication = null;
        ResultSet res = db.querry("SELECT * FROM `publication` WHERE `url` LIKE \"" + url +"\"");
        try {
            if (res.next()){
                publication = new Publication(url, StringEscapeUtils.unescapeHtml4(res.getString("title")).replace("&apos;", "'"), res.getDate("releaseDate"), res.getString("description"), res.getString("image"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db.close();
        return publication;
    }

    public void save(){
        Database db = new Database();

		java.text.SimpleDateFormat sdf =
			     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (getPublicationFromDb(url.get()) == null){
        	db.update("INSERT INTO `publication` (`url`, `title`, `releaseDate`, `description`, `image`) VALUES "
        		+ "('"+ url.get() +"', '"+title.get() +"', '"+ sdf.format(releaseDateF) +"', '"+ description.get() +"', '"+ imageUrl +"')");
        	db.close();
        }
    }

    public List<Comment> getComments(Feed feed){
        Database db = new Database();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        String query ="SELECT * FROM comment c " +
                "WHERE c.publication_url = \""+ this.getUrl() + "\" AND c.feed_url = \""+ feed.getUrl() +"\"";
        ResultSet res = db.querry(query);
        try {
            while(res.next()){
                comments.add(Comment.getCommentFromDb(res.getString("feed_url"), res.getString("user_email"), res.getString("publication_url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public void markAsRead(User user, Feed feed){
        Database db = new Database();
        db.update("INSERT INTO readstatus (user_email, publication_url, feed_url, date) VALUES "
            + "('"+ user.getEmail() +"', '"+this.getUrl() +"', '"+ feed.getUrl() +"', NOW() ) "
            		+ "ON DUPLICATE KEY UPDATE date = NOW()");
        db.close();
    }



	public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set( url);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getReleaseDate() {
        return releaseDate.get();
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate.set(releaseDate);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Object getImageLoad(){
        return imageLoad.get();
    }

    public String getImageUrl(){
        return imageUrl;
    }
}
