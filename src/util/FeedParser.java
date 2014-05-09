package util;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import entities.Feed;
import entities.Publication;

import static org.apache.commons.lang3.StringEscapeUtils.*;

public class FeedParser {

	static org.jdom2.Document document;
	static Element root;
	static String path;

	public FeedParser(String path) {
		this.path = path;
		SAXBuilder sxb = new SAXBuilder();
		File file = new File(path);
		try
	    {
			document = sxb.build(file);
	    }
	    catch(Exception e){ System.out.println( e ); }

		root = document.getRootElement();

		parseAll();

	   }

	   
	static void parseAll()
	{
		Element info = root.getChild("channel");
		List<Element> items = info.getChildren("item");
		String title = escapeXml(info.getChild("title").getText());
		String description = escapeXml(info.getChild("description").getText());
		String link = escapeXml(info.getChild("link").getText());
		String image = escapeXml(info.getChild("image").getChild("url").getText());
		
		Feed feed = new Feed(path, title, description, link, image);
		feed.save();
		
		Iterator i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();
			
			String linkPub = escapeXml(current.getChild("link").getText());
			String titlePub = escapeXml(current.getChild("title").getText());
			Date datePub = new Date(current.getChild("pubDate").getText());
			String descriptionPub = escapeXml(current.getChild("description").getText());
            String imagePub = "";

			Publication publication = new Publication(linkPub, titlePub, datePub, descriptionPub, imagePub);
	        
			publication.save();
			
			Database db = new Database();
	        db.update("INSERT INTO `contain` (`feed_url`, `publication_url`) VALUES"
	        	+ "('"+ feed.getUrl() +"', '"+publication.getUrl() +"')");
	        db.close();
		}
		
	}

}
