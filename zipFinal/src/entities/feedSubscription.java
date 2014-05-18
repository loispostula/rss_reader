package entities;

import java.util.Date;

import util.Database;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe feedSubscription
 */
public class feedSubscription {
    private User user;
    private Feed feed;
    private Date subscribedDate;

    public feedSubscription() {
    }
    
    

    public feedSubscription(User user, Feed feed, Date subscribedDate) {
		super();
		this.user = user;
		this.feed = feed;
		this.subscribedDate = subscribedDate;
	}

    public void save(){
        Database db = new Database();
        db.update("INSERT INTO `feedsubscription` (user_email, feed_url, subscribedDate) VALUES"
                + "('"+ user.getEmail() +"', '"+feed.getUrl() +"', '"+new java.sql.Date(subscribedDate.getTime()) +"')");
        db.close();
    }



	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Date getSubscribedDate() {
        return subscribedDate;
    }

    public void setSubscribedDate(Date subscribedDate) {
        this.subscribedDate = subscribedDate;
    }
}
