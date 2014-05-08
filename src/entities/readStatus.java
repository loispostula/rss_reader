package entities;

import java.util.Date;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe readStatus
 */
public class readStatus {
    private User user;
    private Publication publication;
    private Feed feed;
    private Date readDate;

    public readStatus() {
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

    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }
}
