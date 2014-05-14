package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import entities.Publication;

public class GenerateXML {
    
    private static long getRandomTimeBetweenTwoDates (long beginTime, long endTime) {
        long diff = endTime - beginTime + 1;
        return beginTime + (long) (Math.random() * diff);
    }
    
    public static String generate_random_date_java(String beginTimes, String endTimes) {
    	

        long beginTime = Timestamp.valueOf(beginTimes+" 00:00:00").getTime();
        long endTime = Timestamp.valueOf(endTimes+" 00:00:00").getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");

        return dateFormat.format(new Date(getRandomTimeBetweenTwoDates(beginTime, endTime)));
    }
    
    public static String getMinDate(String d1, String d2){

		String sd = "";
		try {
			Date da1 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(d1);
			Date da2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(d2);
    		if (da1.before(da2)){
    			sd = d2;
    		}
    		else{
    			sd = d1;
    		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sd;
    }
	
	public static void run(){
		int readPercent = 25;
		int friendPercent = 75;
		int sharePercent = 10;
		int CommentPercent = 75;
		String adressedufichier = System.getProperty("user.dir") + "/"+ "xmltest.xml";
        Database db = new Database();
        try{
        	FileWriter fw = new FileWriter(adressedufichier, false);
        	BufferedWriter output = new BufferedWriter(fw);
        	output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        	
        	output.write("<items>\n");
	        ResultSet res = db.querry("SELECT * FROM `publication`");
	        ResultSet res2;
	        try {
	            while (res.next()){
	            	output.write("\t<item>\n\t\t<title>"+unescapeXml(res.getString("title"))+"</title>\n\t\t<url>"+res.getString("url")+"</url>\n\t\t<description>"+unescapeXml(res.getString("description")).replace("&",  "")+"</description>\n\t\t<date>"+res.getString("releaseDate")+"</date>\n\t\t<image>"+res.getString("image")+"</image>\n\t</item>\n\n");
	            }
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
        	output.write("</items>\n\n");
        	
        	ArrayList<String> feeds = new ArrayList<String>();
        	
        	output.write("<feeds>\n");
	        res = db.querry("SELECT * FROM `feed`");
	        try {
	            while (res.next()){
	            	feeds.add(unescapeXml(res.getString("url")));
	            	output.write("\t<feed>\n\t\t<title>"+unescapeXml(res.getString("title"))+"</title>\n\t\t<url>"+unescapeXml(res.getString("url"))+"</url>\n\t\t<link>"+res.getString("link")+"</link>\n\t\t<description>"+unescapeXml(res.getString("description"))+"</description>\n\t\t<image>"+res.getString("image")+"</image>\n\t</feed>\n\n");
	            }
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
        	output.write("</feeds>\n\n");
        	
        	output.write("<contains>\n");
	        res = db.querry("SELECT * FROM `contain`");
	        try {
	            while (res.next()){
	            	output.write("\t<contain>\n\t\t<feed>"+res.getString("feed_url")+"</feed>\n\t\t<item>"+res.getString("publication_url")+"</item>\n\t</contain>\n\n");
	            }
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
        	output.write("</contains>\n\n");
        	
        	ArrayList<String> nickname = new ArrayList<String>(Arrays.asList("Talkul", "Yavaëthdun", "Dirlunren", "Olhal", "Falkul", "Duinob", "Himfol", "Ladkel", "Cirkel", "Ismarbret", "Liathbret", "Filliathë", "Ainfar", "Lingeothlian", "Loncil", "Laurdirquë", "Iellimrë", "Obeoth", "Honvirdan", "Bethlaik", "Guldun", "Kirgondor", "Liathbeth", "Vieneälran", "Minlaëthdoth", "Branhin", "Raneothgythe", "Goneoth", "Roglonrod", "Ionbranril", "Mervan", "Isliath", "Grimmorlan", "Osrielaël", "Eänsylrë", "Ciluinsën", "Halbor", "Hingul", "Eäthgonlen", "Brindwimbran", "Caurlingrim", "Caurvanmith", "Hargulkor", "Dargil", "Bethrulthranquë", "Edsarë", "Incirroth", "Dwimborië", "Kirrul", "Tollan", "Duilmar", "Rilhelda", "Sarfal", "Bilkel", "Othgyth", "Gythis", "Ineth", "Roglienfal", "Hongost", "Rulthirsënaë"));
        	ArrayList<String> email = new ArrayList<String>(Arrays.asList("Mirgalsim", "Helmarhar", "Vanam", "Laikasgoth", "Darrogsënaë", "Ionhimbil", "Gasithfar", "Malbaneäth", "Folrin", "Hunfirlim", "Vienling", "Rinerharië", "Theodhal", "Dorlieneor", "Limrielrë", "Isshelmel", "Sarunbeor", "Ranfalmalrë", "Alfil", "Anyavriel", "Melhim", "Kimvielsyl", "Terindil", "Vienellim", "Mechlorman", "Morril", "Ethmecheld", "Elshebdothquë", "Mantoldain", "Iathrilkim", "Marob", "Minaürman", "Ionobë", "Iathbel", "Aürgonien", "Borlur", "Obbareor", "Quendbethquë", "Gothdor", "Gonmith", "Elladquë", "Simheling", "Rocbilper", "Cirdothaür", "Aneth", "Laëthal", "Halmorrë", "Beliath", "Honranrë", "Dilegdor"));
        	ArrayList<String> country = new ArrayList<String>(Arrays.asList("france", "germany", "ghana", "greece", "guatemala", "haiti", "iceland", "india", "indonesia", "iran", "madagascar", "malaysia", "mali", "pakistan", "panama", "syria", "taiwan", "thailand", "tanzania", "tunisia", "turkey", "uganda", "ukraine"));
        	ArrayList<String> city = new ArrayList<String>(Arrays.asList("Remalipont", "Loochristy", "Bonheiden", "Huyvelde", "Venetie", "Heuvelgem", "Tolhoek", "Calenberg", "Zandloper", "Liroux", "Boucaudrie", "Meiji", "Breedhout", "Daalwezeth", "Eindsdijk", "Buisegnem", "Pachtgoed", "Stalhillebrug", "Herdersem", "Toutefaais", "Bell", "Remagne", "Vlekkem", "Ruwmortelsheide", "Voshoek", "Maison", "Lotria", "Molinvau", "Lavaselle", "Eyck", "Mouland", "Pandoerenhoek", "Geuzenbos", "Boulboulle", "Ressegem", "Ruitegem", "Tronchiennes", "Klemskerke", "Nokere", "Everbecq", "Hammeveer", "Haven", "Borrekent", "Doorn", "Drogenbos", "Lanaye", "Juseret", "Neerhavert", "Otaimont", "Trihay"));
        	ArrayList<String> date = new ArrayList<String>();
        	
        	for (int i = 0; i < 50; ++i) {
        		nickname.set(i, nickname.get(i)+Integer.toString((int) (Math.random()*100)));
        		email.set(i, email.get(i).replace('é', 'e').replace('è', 'e').replace('ë', 'e').replace('ê', 'e').replace('ü', 'u').replace('ä', 'a')+"@gmail.com");
        		date.add(generate_random_date_java("2012-01-01","2013-12-28"));
        	}
        	
        	output.write("<users>\n");
        	
        	for (int i = 0; i < 50; ++i) {
            	output.write("\t<user>\n\t\t<email>"+email.get(i)+"</email>\n\t\t<password>azerty</password>\n\t\t<nickname>"+nickname.get(i)+"</nickname>\n\t\t<country>"+country.get((int) (Math.random()*country.size()))+"</country>\n\t\t<city>"+city.get(i)+"</city>\n\t\t<biography></biography>\n\t\t<avatar>avatar.jpg</avatar>\n\t\t<date>"+date.get(i)+"</date>\n\t</user>\n\n");
        	}
        	output.write("</users>\n\n");
        	
        	output.write("<friends>\n");
        	
        	int a, b;
        	Date d, d2;
        	String sd = "", accepted;
        	ArrayList<Integer> friendships = new ArrayList<Integer>();
        	
        	for (int i = 0; i < 750; ++i) {
        		System.out.println(i);
        		a = (int) (Math.random()*50);
        		b = (int) (Math.random()*50);
        		while(a == b || friendships.contains(a*100+ b) || friendships.contains(b*100+ a) ){
            		a = (int) (Math.random()*email.size());
            		b = (int) (Math.random()*email.size());
        		}
        		friendships.add(a*100+ b);
        		sd = getMinDate(date.get(a), date.get(b));
        		accepted = "True";
        		if ((int) (Math.random()*100) > friendPercent){
            		accepted = "False";
        		}
        		
            	output.write("\t<friend>\n\t\t<email>"+email.get(a)+"</email>\n\t\t<email>"+email.get(b)+"</email>\n\t\t<date>"+generate_random_date_java(sd, "2014-05-14")+"</date>\n\t\t<accepted>"+accepted+"</accepted>\n\t</friend>\n\n");
        	}
        	output.write("</friends>\n\n");
        	
        	output.write("<reads>\n");
        	
        	int nbSub;
        	String sub = "", share = "";
        	sub += "<subscriptions>\n";
        	share += "<shares>\n";
        	String currentDate = "";
        	String pubUrl = "";
        	ArrayList<String> text = new ArrayList<String>(Arrays.asList("lol","ptdr","mdr","","check ça","pas mal"));
        	for (int i = 0; i < email.size(); ++i) {
        		nbSub = (int) (Math.random()*feeds.size());
        		for (int j = 0; j < nbSub; ++j){
        			currentDate = generate_random_date_java(date.get(i), "2014-05-14");
        			sub += "\t<subscription>\n\t\t<email>"+email.get(i)+"</email>\n\t\t<url>"+feeds.get(j)+"</url>\n\t\t<date>"+currentDate+"</date>\n\t</subscription>\n\n";

        	        res = db.querry("SELECT publication_url, p.releaseDate FROM `contain` INNER JOIN publication p ON p.url = publication_url WHERE feed_url = \""+feeds.get(j)+"\"");
        	        while (res.next()){
        	        	if ((Math.random()*100)<readPercent){
	            	        output.write("\t<read>\n\t\t<email>"+email.get(i)+"</email>\n\t\t<date>"+getMinDate(currentDate, res.getString("releaseDate"))+"</date>\n\t\t<feed>"+feeds.get(j)+"</feed>\n\t\t<item>"+res.getString("publication_url")+"</item>\n\t</read>\n\n");
	        	        	if ((Math.random()*100)<sharePercent){
	        	        		share +="\t<share>\n\t\t<feed>"+feeds.get(j)+"</feed>\n\t\t<item>"+res.getString("publication_url")+"</item>\n\t\t<date>"+getMinDate(currentDate, res.getString("releaseDate"))+"</date>\n\t\t<email>"+email.get(i)+"</email>\n\t\t<text>"+text.get((int)(Math.random()*text.size()))+"</text>\n\t</share>\n\n";
	        	        	}
        	        	}
        	        }
        		}
        	}
        	sub += "</subscriptions>\n\n";
        	share += "</shares>\n\n";
        	output.write("</reads>\n\n");
        	output.write(sub);
        	output.write(share);
        	
        	output.flush();
	        output.close();
	        db.close();
        }
		catch(IOException ioe){
			System.out.print("Erreur : ");
			ioe.printStackTrace();
		} catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}
	
}
