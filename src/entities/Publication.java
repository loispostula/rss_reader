package entities;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import util.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe Publications
 */
public class Publication {
    private String url;
    private String title;
    private Date releaseDate;
    private String description;

    private String image;
    public Publication() {
    }
    
    

    public Publication(String url, String title, Date releaseDate,
			String description) {
		super();
		this.url = url;
		this.title = title;
		this.releaseDate = releaseDate;
		this.description = description;
	}



    public static Publication getPublicationFromDb(String url){
        Database db = new Database();
        Publication publication = null;
        ResultSet res = db.querry("SELECT * FROM `publication` WHERE `url` == \"" + url +"\"");
        try {
            if (res.next()){
                publication = new Publication(url, res.getString("title"), res.getDate("releaseDate"), res.getString("description"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db.close();
        return publication;
    }

    public void save(){
        Database db = new Database();
        if (getPublicationFromDb(url) == null){
        	db.update("INSERT INTO `publication` (`url`, `title`, `releaseDate`, `description`"
        		+ "('"+ url +"', '"+title +"', '"+ releaseDate +"', '"+ description +"')");
        	db.close();
        }
    }



	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
