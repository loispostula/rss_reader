package entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import util.Database;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe Comments
 */
public class Comment {

    private Feed feed;
    private Publication publication;
    private User user;
    private Date date;
    private String text;

    public Comment() {
    }
    
    

    public Comment(Feed feed, Publication publication, User user, Date date, String text) {
		super();
		this.feed = feed;
		this.publication = publication;
		this.user = user;
		this.date = date;
		this.text = text;
	}

    public static Comment getCommentFromDb(String feed_url, String user_email, String publication_url){
    	Database db = new Database();
    	Comment comment = null;
    	ResultSet res = db.querry("SELECT * FROM `comment` WHERE `feed_url` LIKE \"" + feed_url +"\" "
    			+ "AND publication_url LIKE\""+ publication_url +"\" AND user_email LIKE \"" + user_email +"\"" );
    	try {
			if (res.next()){
				//	, res.getString("avatar"), res.getString("biography"), res.getDate("joinedDate"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	db.close();
    	return comment;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
