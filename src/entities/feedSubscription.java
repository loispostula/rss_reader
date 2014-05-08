package entities;

import java.util.Date;

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
