package entities;

import java.util.Date;

import util.Database;

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
    
    

    public Friendship(User userA, User userB, Date date, Boolean accepted) {
		super();
		this.userA = userA;
		this.userB = userB;
		this.date = date;
		this.accepted = accepted;
	}
    
    public void save(){
    	Database db = new Database();
    	String accept = "0";
    	if (accepted){
    		accept = "1";
    	}
        db.update("INSERT INTO `friendship` (user1_email, user2_email, requestDate, accepted) VALUES"
                + "('"+ userA.getEmail() +"', '"+userB.getEmail() +"', '"+new java.sql.Date(date.getTime()) +"', '"+accept +"')");
        if (accepted){
	        db.update("INSERT INTO `feedsubscription` (user_email, feed_url, subscribedDate) VALUES"
	                + "('"+ userA.getEmail() +"', '"+userB.getEmail() +"', '"+new java.sql.Date(date.getTime()) +"')");
	        //db.update("INSERT INTO `feedsubscription` (user_email, feed_url, subscribedDate) VALUES"
	        //        + "('"+ userB.getEmail() +"', '"+userA.getEmail() +"', '"+new java.sql.Date(date.getTime()) +"')");
        }
        db.close();
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
