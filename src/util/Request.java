package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Feed;
import entities.User;

public class Request {

	public static ArrayList<User> noMoreThanTwoFriend(){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate FROM user u "+
				"LEFT OUTER JOIN friendship f "+
				"ON (f.user1_email = u.email OR f.user2_email = u.email) AND accepted = TRUE "+
				"GROUP BY u.email "+
				"HAVING COUNT(u.email) < 3");
        ArrayList<User> requests = new ArrayList<User>();
        try {
            while (res.next()) {
                requests.add(new User(res.getString("email"), res.getString("password"), res.getString("nickname"), res.getString("city"),
                		res.getString("country"), res.getString("avatar"), res.getString("biography"), res.getDate("joinedDate")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public static ArrayList<Feed> feedFollowedByUserWhoFollowTwoOrMoreFeedOfX(String email){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT f.url, f.title, f.description, f.link, f.image FROM feed f FROM publication f "
				+ "INNER JOIN feedsubscription c ON c.feed_url = f.url "
				+ "INNER JOIN ("
					+ "SELECT b.user_email FROM `feedsubscription` b "
					+ "INNER JOIN ("
						+ "SELECT feed_url FROM feedsubscription "
						+ "WHERE user_email = \""+ email +"\""
					+ ") a "
					+ "ON a.feed_url = b.feed_url "
					+ "GROUP BY b.user_email"
					+ " HAVING COUNT(b.user_email) > 1"
				+ ") d "
				+ "ON d.user_email = c.user_email "
				+ "GROUP BY c.feed_url");
        ArrayList<Feed> requests = new ArrayList<Feed>();
        try {
            while (res.next()) {
            	requests.add(new Feed(res.getString("url"), res.getString("title"), res.getString("description"), res.getString("link"), res.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public static ArrayList<Feed> notFollowedByFriendAndNotShared(String email){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT f.url, f.title, f.description, f.link, f.image FROM feed f "
				+ "INNER JOIN feedsubscription fs1 ON fs1.feed_url=f.url AND fs1.user_email=\""+ email +"\" "
				+ "WHERE NOT EXISTS ( "
					+ "SELECT c1.feed_url FROM contain c1 "
					+ "INNER JOIN contain c2 ON c2.publication_url = c1.publication_url AND c2.feed_url = \"feed://"+ email +"\" "
					+ "WHERE c1.feed_url=f.url ) "
				+ "AND NOT EXISTS ( "
					+ "SELECT fs2.feed_url FROM feedsubscription fs2 "
					+ "INNER JOIN friendship fr ON ("
						+ "(fr.user1_email=fs2.user_email AND fr.user2_email=\""+ email +"\") "
						+ "OR (fr.user2_email=fs2.user_email AND fr.user1_email=\""+ email +"\") "
						+ "AND accepted = TRUE) "
					+ "WHERE fs2.feed_url = f.url )");
        ArrayList<Feed> requests = new ArrayList<Feed>();
        try {
            while (res.next()) {
            	requests.add(new Feed(res.getString("url"), res.getString("title"), res.getString("description"), res.getString("link"), res.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public static ArrayList<User> resharedMoreThanThreeTime(String email){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate FROM user u "
				+ "INNER JOIN shared publication s ON s.user_email = u.email "
				+ "INNER JOIN (SELECT publication_url FROM sharedpublication WHERE user_email = \""+ email +"\") a "
				+ "ON s.publication_url = a.publication_url "
				+ "GROUP BY s.user_email "
				+ "HAVING COUNT(s.user_email) > 2");
        ArrayList<User> requests = new ArrayList<User>();
        try {
            while (res.next()) {
                requests.add(new User(res.getString("email"), res.getString("password"), res.getString("nickname"), res.getString("city"),
                		res.getString("country"), res.getString("avatar"), res.getString("biography"), res.getDate("joinedDate")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public static ArrayList<Feed> feedReadShareInfo(String email){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT f.url, f.title, f.description, f.link, f.image FROM feed f "
				+ "SELECT fs.feed_url, "
				+ "(SELECT COUNT(*) FROM readstatus rs WHERE rs.publication_url = c.publication_url AND rs.user_email = fs.user_email AND TO_DAYS(NOW())-TO_DAYS(rs.date) < 30) AS nread, "
				+ "(SELECT COUNT(*) FROM sharedpublication sp WHERE sp.publication_url = c.publication_url AND sp.user_email = fs.user_email AND TO_DAYS(NOW())-TO_DAYS(sp.sharedDate) < 30) AS nshared "
				+ ", (SELECT nshared/nread) AS ratio "
				+ "FROM publication f "
				+ "INNER JOIN feedsubscription fs ON fs.feed_url = f.url"
				+ "INNER JOIN contain c ON c.feed_url = fs.feed_url "
				+ "WHERE fs.user_email = \""+ email +"\" "
				+ "GROUP BY fs.feed_url "
				+ "ORDER BY nshared");
        ArrayList<Feed> requests = new ArrayList<Feed>();
        try {
            while (res.next()) {
            	requests.add(new Feed(res.getString("url"), res.getString("title"), res.getString("description"), res.getString("link"), res.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public static ArrayList<User> friendReadFriendInfo(String email){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate, "
				+ "(SELECT COUNT(*) FROM readstatus rs WHERE rs.user_email = u.email)/(TO_DAYS(NOW())-TO_DAYS(u.joinedDate)) AS mread, "
				+ "(SELECT COUNT(u2.email) FROM user u2 INNER JOIN friendship f2 ON f2.user1_email = u2.email OR f2.user2_email = u2.email WHERE u2.email = u.email GROUP BY u2.email) AS nfriend "
				+ "FROM user u "
				+ "INNER JOIN friendship f ON f.user1_email = u.email OR f.user2_email = u.email "
				+ "WHERE (f.user1_email = \""+ email +"\" AND u.email <> f.user1_email) OR (f.user2_email = \""+ email +"\" AND u.email <> f.user2_email) "
				+ "ORDER BY mread");
        ArrayList<User> requests = new ArrayList<User>();
        try {
            while (res.next()) {
                requests.add(new User(res.getString("email"), res.getString("password"), res.getString("nickname"), res.getString("city"),
                		res.getString("country"), res.getString("avatar"), res.getString("biography"), res.getDate("joinedDate")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

}
