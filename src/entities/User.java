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
    private String country;
    private String avatar; //todo check if this can't be a image
    private String biography;
    private Date joinedDate;


    public User() {
    }

    public static User getUserFromDb(String email, String password){

        return null;
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
        //todo db access to save
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
}
