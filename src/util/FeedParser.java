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
    Feed feed;

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

    public Feed getFeed(){
        return feed;
    }

    void parseAll()
	{
		Element info = root.getChild("channel");
		List<Element> items = info.getChildren("item");
		
		String title = escapeXml(info.getChild("title").getText());
		String description;
		try{
			description = escapeXml(info.getChild("description").getText());
		}catch (Exception e){
			description = title+" feed";
		}
		
		String link;
		try{
			link = escapeXml(info.getChild("link").getText());
		}
		catch(Exception e){
			link = "NaN";
		}
		String image;
		try{
			image = escapeXml(info.getChild("image").getChild("url").getText());
		}catch (Exception e){
			image = ("file:////"+System.getProperty("user.dir").replace("\\","//")+"//src//javafx//icons//rss_icon.png");
		}
		
		this.feed = new Feed(path, title, description, link, image);
		this.feed.save();
		
		Iterator i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();
			
			String linkPub = escapeXml(current.getChild("link").getText());
			String titlePub = escapeXml(current.getChild("title").getText());
			Date datePub = new Date(current.getChild("pubDate").getText());
			String descriptionPub = escapeXml(current.getChild("description").getText());
			String imagePub;
			try{
				imagePub = escapeXml(current.getChild("enclosure").getAttribute("url").getValue());
			}catch (Exception e){
				imagePub = "";
			}

			Publication publication = new Publication(linkPub, titlePub, datePub, descriptionPub, imagePub);
			publication.save();
			
			Database db = new Database();
	        db.update("INSERT INTO `contain` (`feed_url`, `publication_url`) VALUES"
	        	+ "('"+ feed.getUrl() +"', '"+publication.getUrl() +"')");
	        db.close();
		}
		
	}

}
