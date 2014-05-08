package entities;

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

    public Publication() {
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
}
