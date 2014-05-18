package util;

import java.io.*;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.filter.*;

import entities.Feed;
import entities.Friendship;
import entities.Publication;
import entities.SharedPublication;
import entities.User;
import entities.contains;
import entities.feedSubscription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.Locale;

import javafx.scene.image.Image;
import static org.apache.commons.lang3.StringEscapeUtils.escapeXml;

public class XMLParser {

	static org.jdom2.Document document;
	static Element racine;

	public XMLParser(String path) {
		SAXBuilder sxb = new SAXBuilder();
		File file = new File(path);
		try
	    {
			document = sxb.build(file);
	    }
	    catch(Exception e){ System.out.println( e ); }

		racine = document.getRootElement();
		parseAll();

	   }
	
	public static org.jdom2.Document getDocument() {
		return document;
	}

	public static void setDocument(org.jdom2.Document document) {
		XMLParser.document = document;
	}

	   
	static void parseAll()
	{
		List items = racine.getChild("users").getChildren("user");
		Date d = new Date();

		Iterator i = items.iterator();
		String avatarPath = "", imgPath = "";
		while(i.hasNext())
		{
			Element current = (Element)i.next();
			try {
				d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(current.getChild("date").getText());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			avatarPath = "file:////"+current.getChild("avatar").getText();
			Image temp = new Image(avatarPath);
			if (temp.getHeight() == 0){
				avatarPath = ("file:////"+System.getProperty("user.dir").replace("\\","//")+"//src//javafx//icons//rss_icon.png");
			}
			User user = new User(current.getChild("email").getText(), current.getChild("password").getText(), current.getChild("nickname").getText(), current.getChild("city").getText(), current.getChild("country").getText(),
					avatarPath, current.getChild("biography").getText(), d);
			user.save();

			Feed feed = new Feed("feed://"+current.getChild("email").getText(), current.getChild("nickname").getText()+" personnal feed","Feed with all the publication which "+ current.getChild("nickname").getText()+" shares.", "None", avatarPath);
			feed.save();
			
			feedSubscription subscription = new feedSubscription(user, feed, d);
			subscription.save();
		}
    	System.out.println("\nDone adding user\n");
		
		items = racine.getChild("friends").getChildren("friend");
		
		Boolean accepted = false;
		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();
			try {
				d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(current.getChild("date").getText());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<Element> emails = current.getChildren("email");
			
			User userA = User.getUserFromDb(emails.get(0).getText());
			User userB = User.getUserFromDb(emails.get(1).getText());
			if (current.getChild("accepted").getText().equals("True")){
				accepted = true;
			}
			else{
				accepted = false;
			}
			Friendship frsh = new Friendship(userA, userB, d, accepted);
			frsh.save();
		}
    	System.out.println("\nDone adding friendship\n");
		
		items = racine.getChild("feeds").getChildren("feed");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();
			imgPath = current.getChild("image").getText();
			try{
			Image temp = new Image(imgPath);
			if (temp.getHeight() == 0){
				imgPath = ("file:////"+System.getProperty("user.dir").replace("\\","//")+"//src//javafx//icons//rss_icon.png");
			}
			}catch(Exception e){
				imgPath = ("file:////"+System.getProperty("user.dir").replace("\\","//")+"//src//javafx//icons//rss_icon.png");
			}


			Feed feed = new Feed(current.getChild("url").getText(), escapeXml(current.getChild("title").getText()), escapeXml(current.getChild("description").getText()), current.getChild("link").getText(), imgPath);
			feed.save();
		}
    	System.out.println("\nDone adding feeds\n");
		
		items = racine.getChild("items").getChildren("item");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();
			try {
				d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(current.getChild("date").getText());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Publication publication = new Publication(current.getChild("url").getText(), escapeXml(current.getChild("title").getText()),d,
					escapeXml(current.getChild("description").getText()), current.getChild("image").getText());
			publication.save();
		}
    	System.out.println("\nDone adding publications\n");
		
		/*items = racine.getChild("comments").getChildren("comment");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();

			//Comment comment = new Comment(new Feed(current.getChild("feed").getText()), new Publication(current.getChild("item").getText()), new User(current.getChild("email").getText()), new Date(current.getChild("date").getText()),
			//		current.getChild("text").getText());
			//TODO date string to date
			//TODO user from email
			//TODO feed from url
			//TODO publication from url
			
		}*/
		
		items = racine.getChild("shares").getChildren("share");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();
			try {
				d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(current.getChild("date").getText());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SharedPublication share = new SharedPublication(Feed.getFeedFromDb(current.getChild("feed").getText()), Publication.getPublicationFromDb(current.getChild("item").getText()), User.getUserFromDb(current.getChild("email").getText()), d,
					current.getChild("text").getText());
			share.save();
			
		}
    	System.out.println("\nDone adding shares\n");
		
		items = racine.getChild("subscriptions").getChildren("subscription");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();
			try {
				d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(current.getChild("date").getText());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			feedSubscription subscription = new feedSubscription(User.getUserFromDb(current.getChild("email").getText()), Feed.getFeedFromDb(current.getChild("url").getText()), d);
			subscription.save();
		}
    	System.out.println("\nDone adding subscriptions\n");
		
		items = racine.getChild("contains").getChildren("contain");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();

			//contains contain = new contains(User.getUserFromDb(current.getChild("email").getText()), Feed.getFeedFromDb(current.getChild("feed").getText()), new Date(current.getChild("date").getText()));
			//contain.save();
			Database db = new Database();
	        db.update("INSERT INTO contain (`publication_url`, `feed_url`) VALUES"
	            	+ "('"+ current.getChild("item").getText() +"', '"+ current.getChild("feed").getText() +"')");
	        db.close();
		}
    	System.out.println("\nDone adding contains\n");
		
		items = racine.getChild("reads").getChildren("read");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();
			try {
				d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(current.getChild("date").getText());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//contains contain = new contains(User.getUserFromDb(current.getChild("email").getText()), Feed.getFeedFromDb(current.getChild("feed").getText()), new Date(current.getChild("date").getText()));
			//contain.save();
			Database db = new Database();
	        db.update("INSERT INTO readstatus (`publication_url`, `feed_url`,`user_email`,`date`) VALUES "
	            	+ "('"+ current.getChild("item").getText() +"', '"+ current.getChild("feed").getText() +"', '"+ current.getChild("email").getText() +"', '"+ new java.sql.Date(d.getTime()) +"')");
	        db.close();
		}
    	System.out.println("\nDone adding reads\n");
	}

}
