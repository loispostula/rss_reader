package entities;

import java.util.Date;
import java.util.List;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe User
 */
public class User {

    private String email;
    private String password;
    private String nickname;
    private String city;
    private String avatar; //todo check if this can't be a image
    private String biography;
    private Date joinedDate;
    private List<Feed> subscribedFeeds;
    private List<User> friends;
    private Feed shared;


    public User() {
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

    public Date getSubscribeDate() {
        return joinedDate;
    }

    public void setSubscribeDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }
}
