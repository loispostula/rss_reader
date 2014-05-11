package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import util.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe User
 */
public class User {

    private SimpleStringProperty email;
    private SimpleStringProperty password;
    private SimpleStringProperty nickname;
    private SimpleStringProperty city;
    private SimpleStringProperty country;
    private SimpleObjectProperty avatar;

    private String avatarS;
    private SimpleStringProperty biography;
    private SimpleStringProperty joinedDate;
    private Date joinedDateS;

    private SimpleIntegerProperty pubByDay;
    private SimpleIntegerProperty numOfFriend;


    public User() {
    }


    public static User getUserFromDb(String email) {
        Database db = new Database();
        User user = null;
        ResultSet res = db.querry("SELECT * FROM `user` WHERE `email` = \"" + email + "\"");
        try {
            if (res != null && res.next()) {
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

    public static User getUserFromDb(String email, String password) {

        Database db = new Database();
        User user = null;
        String query = "SELECT * FROM `user` WHERE `email` = \"" + email + "\" AND `password` = PASSWORD(\"" + password + "\")";
        ResultSet res = db.querry(query);
        try {
            if (res != null && res.next()) {
                user = new User(res.getString("email"), res.getString("password"), res.getString("nickname"), res.getString("city"), res.getString("country")
                        , res.getString("avatar"), res.getString("biography"), res.getDate("joinedDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        return user;
    }

    public User(String email, String password, String nickname, String city,
                String country, String avatar, String biography, Date joinedDate) {
        super();
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.nickname = new SimpleStringProperty(nickname);
        this.city = new SimpleStringProperty(city);
        this.country = new SimpleStringProperty(country);
        this.avatar = new SimpleObjectProperty(new Image("file:///" + avatar));
        this.avatarS = avatar;
        this.biography = new SimpleStringProperty(biography);
        this.joinedDate = new SimpleStringProperty(joinedDate.toString());
        this.joinedDateS = joinedDate;
    }

    public void save() {
        Database db = new Database();
        if (getUserFromDb(this.getEmail()) == null) {
            db.update("INSERT INTO `user` (`email`, `nickname`, `password`, `country`, `city`, `avatar`, `biography`, `joinedDate`) VALUES "
                    + "('" +
                    this.getEmail() + "', '" +
                    this.getNickname() + "', PASSWORD(\"" +
                    this.getPassword() + "\"), '" +
                    this.getCountry() + "', '" +
                    this.getCity() + "', '" +
                    this.getAvatarS() + "', '" +
                    this.getBiography() + "', '" +
                    new java.sql.Date(this.getJoinedDateS().getTime()) + "')");
        } else {
            String query = "UPDATE `user` SET"
                    + " `nickname` = '" + this.getNickname() + "', `password` =PASSWORD(\"" + this.getPassword() + "\"), `country` = '" + country + "', `city` = '" + city + "', "
                    + "`avatar` = '" + this.getAvatarS() + "', `biography` = '" + this.getBiography() + "' WHERE `email` = \"" + this.getEmail() + "\"";
            db.update(query);
        }
    }


    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getNickname() {
        return nickname.get();
    }

    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public Object getAvatar() {
        return avatar.get();
    }

    public void setAvatar(Object avatar) {
        this.avatar.set(avatar);
    }

    public void setAvatar(String avatar) {
        this.avatarS = avatar;
        this.avatar = new SimpleObjectProperty(new Image("file:///" + avatar));
    }

    public String getBiography() {
        return biography.get();
    }

    public void setBiography(String biography) {
        this.biography.set(biography);
    }

    public String getJoinedDate() {
        return joinedDate.get();
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate.set(joinedDate);
    }

    public void setJoinedDate(Date date) {
        this.joinedDateS = date;
        this.joinedDate = new SimpleStringProperty(date.toString());
    }


    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getAvatarS() {
        return avatarS;
    }

    public Date getJoinedDateS() {
        return joinedDateS;
    }

    public int getPubByDay() {
        return pubByDay.get();
    }

    public int getNumOfFriend() {
        return numOfFriend.get();
    }

    public void setPubByDay(int pubByDay) {
        this.pubByDay.set(pubByDay);
    }

    public void setNumOfFriend(int numOfFriend) {
        this.numOfFriend.set(numOfFriend);
    }

    public void subscribe(Feed feed) {
        Database db = new Database();
        String query = "INSERT INTO `feedsubscription` (user_email, feed_url, subscribedDate)" +
                "VALUES (\"" + this.getEmail() + "\", \"" + feed.getUrl() + "\" , NOW())";
        db.update(query);
        db.close();
    }

    public List<Feed> getAllSubscription() {
        ArrayList<Feed> feeds = new ArrayList<Feed>();
        Database db = new Database();
        //todo jointur subscribe
        String query = "SELECT `url` FROM `feed` f " +
                "INNER JOIN feedsubscription fs " +
                "ON fs.feed_url = f.url " +
                "INNER JOIN user u " +
                "ON u.email = fs.user_email " +
                "WHERE u.email = \"" + this.getEmail() + "\"";
        ResultSet res = db.querry(query);
        try {
            while (res.next()) {
                feeds.add(Feed.getFeedFromDb(res.getString("url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        return feeds;
    }

    public List<Feed> getAllFriendsSubscription() {
        ArrayList<Feed> feeds = new ArrayList<Feed>();
        Database db = new Database();
        //todo jointur subscribe
        String query = "SELECT `user2_email` FROM `friendship` WHERE `user1_email` = \"" + this.email + "\" AND accepted = 1";
        ResultSet res = db.querry(query);
        try {
            while (res.next()) {
                feeds.add(Feed.getFeedFromDb(res.getString("user2_email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query = "SELECT `user1_email` FROM `friendship` WHERE `user2_email` = \"" + this.email + "\" AND accepted = 1";
        res = db.querry(query);
        try {
            while (res.next()) {
                feeds.add(Feed.getFeedFromDb(res.getString("user1_email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        return feeds;
    }

    public void receiveRequest(User sender) {
        String email = sender.getEmail();
        if (User.getUserFromDb(email) != null) {
            Database db = new Database();
            db.update("INSERT INTO `friendship` (`user1_email`, `user2_email`, `requestDate`, `accepted`) VALUES"
                    + "('" + email + "', '" + this.getEmail() + "', NOW(), 0)");
            db.close();
        }
    }

    public void computeStat() {
        pubByDay = new SimpleIntegerProperty(0);
        numOfFriend = new SimpleIntegerProperty(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return ((User) obj).getEmail() == this.getEmail();
        }
        return false;
    }
}
