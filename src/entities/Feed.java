package entities;

import java.util.List;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe Feed
 */
public class Feed {

    private String url;
    private String title;
    private String description;
    private String link;

    public Feed() {
    }
    
    

    public Feed(String url, String title, String description, String link) {
		super();
		this.url = url;
		this.title = title;
		this.description = description;
		this.link = link;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
