package entities;

import java.util.Date;

import util.Database;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe SharedPublication
 */
public class SharedPublication {
    private User user;
    private Publication publication;
    private Feed feed;
    private Date sharedDate;
    private String text;

    public SharedPublication() {
    }

    public SharedPublication(Feed feed, Publication publication, User user, Date sharedDate,
			String text) {
        this.user = user;
        this.publication = publication;
        this.feed = feed;
        this.sharedDate = sharedDate;
        this.text = text;
    }

    public void save(){
        Database db = new Database();
        db.update("INSERT INTO `sharedPublication` (`publication_url`, `user_email`, `text`, `sharedDate`) VALUES "
            	+ "('"+ publication.getUrl() +"', '"+ user.getEmail() +"', '"+ text +"', '"+ new java.sql.Date(sharedDate.getTime()) +"')");
        db.update("INSERT INTO contain (`publication_url`, `feed_url`) VALUES "
            	+ "('"+ publication.getUrl() +"', 'feed://"+ user.getEmail() +"')");
        db.close();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Date getSharedDate() {
        return sharedDate;
    }

    public void setSharedDate(Date sharedDate) {
        this.sharedDate = sharedDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
