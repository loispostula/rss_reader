package entities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.Database;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe User
 */
public class User {

    private String email;
    private String password;
    private String nickname;
    private String city;
    private String country;
    private String avatar; //todo check if this can't be a image
    private String biography;
    private Date joinedDate;


    public User() {
    }



    public static User getUserFromDb(String email){
        Database db = new Database();
        User user = null;
        ResultSet res = db.querry("SELECT * FROM `user` WHERE `email` = \"" + email +"\"");
        try {
            if (res != null && res.next()){
                user = new User(res.getString("email"), res.getString("password"), res.getString("nickname"), res.getString("city"), res.getString("country")
                        , res.getString("avatar"), res.getString("biography"), res.getDate("joinedDate"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db.close();
        return user;
    }

    public static User getUserFromDb(String email, String password){

    	Database db = new Database();
    	User user = null;
        String query = "SELECT * FROM `user` WHERE `email` = \"" + email +"\" AND `password` = PASSWORD(\"" + password + "\")";
        System.out.println(query);
    	ResultSet res = db.querry(query);
    	try {
			if (res != null && res.next()){
				 user = new User(res.getString("email"), res.getString("password"), res.getString("nickname"), res.getString("city"), res.getString("country")
						, res.getString("avatar"), res.getString("biography"), res.getDate("joinedDate"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        db.close();
    	return user;
    }

    public User(String email, String password, String nickname, String city,
			String country, String avatar, String biography, Date joinedDate) {
		super();
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.city = city;
        this.country = country;
		this.avatar = avatar;
		this.biography = biography;
		this.joinedDate = joinedDate;
	}

    public void save(){
        Database db = new Database();
        if (getUserFromDb(email) == null){
        	db.update("INSERT INTO `user` (`email`, `nickname`, `password`, `country`, `city`, `avatar`, `biography`, `joinedDate`) VALUES "
        		+ "('"+ email +"', '"+ nickname +"', PASSWORD(\""+ password +"\"), '"+ country +"', '"+ city +"', '"+ avatar +"', '"+ biography +"', '"+ new java.sql.Date(joinedDate.getTime()) +"')");
        }
        else{
            String query = "UPDATE `user` SET"
                    + " `nickname` = '"+ nickname +"', `password` =PASSWORD(\""+ password +"\"), `country` = '"+ country +"', `city` = '"+ city +"', "
                    + "`avatar` = '"+ avatar +"', `biography` = '"+ biography + "' WHERE `email` = \"" + email + "\"";
            db.update(query);
        }
    }
        

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void subscribe(Feed feed){
        Database db = new Database();
        String query = "INSERT INTO `feedsubscription` (user_email, feed_url, subscribedDate)" +
                "VALUES (\""+this.email+"\", \""+feed.getUrl()+"\" , NOW())";
        db.update(query);
        db.close();
    }

    public List<Feed> getAllSubscription(){
        ArrayList<Feed> feeds = new ArrayList<Feed>();
        Database db = new Database();
        //todo jointur subscribe
        String query = "SELECT * FROM `feed` f " +
                "INNER JOIN feedsubscription fs " +
                "ON fs.feed_url = f.url " +
                "INNER JOIN user u " +
                "ON u.email = fs.user_email " +
                "WHERE u.email = \""+this.email+"\"";
        ResultSet res = db.querry(query);
        try {
            while (res.next()){
                feeds.add(new Feed(res.getString("url"), res.getString("title"), res.getString("description"), res.getString("link"), res.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        return feeds;
    }
}
