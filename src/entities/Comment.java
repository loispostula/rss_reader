package entities;

import java.util.Date;

/**
 * Created by lpostula on 08/05/14.
 * Documentation de la classe Comments
 */
public class Comment {
    
    private Feed feed;
    private Publication publication;
    private User user;
    private Date date;
    private String text;

    public Comment() {
    }
}
