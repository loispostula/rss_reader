package util;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import entities.Feed;
import entities.Publication;

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
		
		Feed feed = new Feed(path, info.getChild("title").getText(), info.getChild("desciption").getText(),
				info.getChild("link").getText(), info.getChild("image").getChild("url").getText());


		Iterator i = items.iterator();
		while(i.hasNext())
		{	
			Element current = (Element)i.next();

			Publication publication = new Publication(current.getChild("link").getText(), current.getChild("title").getText(), new Date(current.getChild("pubDate").getText()), current.getChild("description").getText());
	        
			Database db = new Database();
	        db.update("INSERT INTO `contain` (`feed_url`, `publication_url`"
	        	+ "('"+ feed.getUrl() +"', '"+publication.getUrl() +"')");
	        db.close();
		}
		
	}

}
