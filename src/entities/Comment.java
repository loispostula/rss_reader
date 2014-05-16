package entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import util.Database;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe Comments
 */
public class Comment {

    private Feed feed;
    private Publication publication;
    private User user;
    private SimpleStringProperty userNick;
    private Date date;
    private SimpleStringProperty dateP;
    private SimpleStringProperty text;

    public Comment() {
    }
    
    

    public Comment(Feed feed, Publication publication, User user, Date date, String text) {
		super();
		this.feed = feed;
		this.publication = publication;
		this.user = user;
        this.userNick = new SimpleStringProperty(user.getNickname());
		this.date = date;
        this.dateP = new SimpleStringProperty(date.toString());
		this.text = new SimpleStringProperty(text);
	}

    public static Comment getCommentFromDb(String feed_url, String user_email, String publication_url){
    	Database db = new Database();
    	Comment comment = null;
    	ResultSet res = db.querry("SELECT * FROM `comment` WHERE `feed_url` LIKE \"" + feed_url +"\" "
    			+ "AND publication_url LIKE\""+ publication_url +"\" AND user_email LIKE \"" + user_email +"\"" );
    	try {
			if (res.next()){
				comment = new Comment(Feed.getFeedFromDb(feed_url), Publication.getPublicationFromDb(publication_url), User.getUserFromDb(user_email), res.getDate("date")
						, res.getString("text"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	db.close();
    	return comment;
    }

    public void save(){
        Database db = new Database();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (getCommentFromDb(feed.getUrl(), user.getEmail(), publication.getUrl()) == null){
        	db.update("INSERT INTO comment (feed_url, publication_url, user_email, text, date) "
        		+ "VALUES (\""+ feed.getUrl() +"\", \""+ publication.getUrl() +"\", \""+ user.getEmail() +"\", '"+ getText() +"', '"+ sdf.format(date) +"')");
        	db.close();
        }
    }



	public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public String getDateP(){return dateP.get();}

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public String getUserNick(){
        return this.userNick.get();
    }
}
