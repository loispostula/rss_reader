package entities;

import java.util.Date;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe Friendship
 */
public class Friendship {
    private User userA;
    private User userB;
    private Date date;
    private Boolean accepted;

    public Friendship() {
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
