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
import entities.feedSubscription;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

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

		//On initialise un nouvel élément racine avec l'élément racine du document.
		racine = document.getRootElement();

		//Méthode définie dans la partie 3.2. de cet article
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

		Iterator i = items.iterator();
		while(i.hasNext())
		{
			Element current = (Element)i.next();
			User user = new User(current.getChild("email").getText(), current.getChild("password").getText(), current.getChild("nickname").getText(), current.getChild("city").getText(),
					current.getChild("avatar").getText(), current.getChild("biography").getText(), new Date(current.getChild("subscribeDate").getText()));
			//TODO subscribedate string to date
		}
		
		items = racine.getChild("friends").getChildren("friend");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();
			
			List<Element> emails = current.getChildren("email");
			
			User userA = new User(emails.get(0).getText());
			User userB = new User(emails.get(0).getText());
			//TODO user from email
			Friendship user = new Friendship(userA, userB, current.getChild("date").getText(), current.getChild("accepted").getText());
			//TODO date string to date
			//TODO accepted string to boolean
		}
		
		items = racine.getChild("feeds").getChildren("feed");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();

			Feed feed = new Feed(current.getChild("URL").getText(), current.getChild("title").getText(), current.getChild("description").getText(), current.getChild("link").getText());
		}
		
		items = racine.getChild("items").getChildren("item");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();

			Publication publication = new Publication(current.getChild("URL").getText(), current.getChild("title").getText(), current.getChild("date").getText(),
					current.getChild("description").getText());
			//TODO date string to date
		}
		
		items = racine.getChild("comments").getChildren("comment");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();

			Comment comment = new Comment(new Feed(current.getChild("feed").getText()), new Publication(current.getChild("item").getText()), new User(current.getChild("email").getText()), new Date(current.getChild("date").getText()),
					current.getChild("text").getText());
			//TODO date string to date
			//TODO user from email
			//TODO feed from url
			//TODO publication from url
			
		}
		
		items = racine.getChild("shares").getChildren("share");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();

			SharedPublication comment = new SharedPublication(new Feed(current.getChild("feed").getText()), new Publication(current.getChild("item").getText()), new User(current.getChild("email").getText()), new Date(current.getChild("date").getText()),
					current.getChild("text").getText());
			//TODO date string to date
			//TODO user from email
			//TODO feed from url
			//TODO publication from url
			
		}
		
		items = racine.getChild("subscriptions").getChildren("subscription");

		i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();

			feedSubscription subscription = new feedSubscription(new User(current.getChild("email").getText()), new Feed(current.getChild("feed").getText()), new Date(current.getChild("date").getText()));
			//TODO date string to date
			//TODO user from email
			//TODO feed from url
			//TODO publication from url
			
		}
	}

}
