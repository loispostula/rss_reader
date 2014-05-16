package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Feed;
import entities.User;

public class Request {

	public ArrayList<User> noMoreThanTwoFriend(){
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

	public ArrayList<Feed> feedFollowedByUserWhoFollowTwoOrMoreFeedOfX(User user){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT f.url, f.title, f.description, f.link, f.image FROM feed f FROM publication f "
				+ "INNER JOIN feedsubscription c ON c.feed_url = f.url "
				+ "INNER JOIN ("
					+ "SELECT b.user_email FROM `feedsubscription` b "
					+ "INNER JOIN ("
						+ "SELECT feed_url FROM feedsubscription "
						+ "WHERE user_email = \""+ user.getEmail() +"\""
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

	public ArrayList<Feed> notFollowedByFriendAndNotShared(User user){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT f.url, f.title, f.description, f.link, f.image FROM feed f FROM publication f "
				+ "INNER JOIN contain c ON f.url = c.feed_url "
				+ "INNER JOIN feedsubscription s "
				+ "ON c.feed_url = s.feed_url "
				+ "INNER JOIN friendship f "
				+ "ON (s.user_email = f.user1_email OR s.user_email = f.user2_email) AND f.accepted = 1 "
				+ "WHERE (f.user1_email = \""+ user.getEmail() +"\" OR f.user2_email = \""+ user.getEmail() +"\") "
					+ "AND NOT EXISTS(SELECT * FROM sharedpublication WHERE user_email = s.user_email AND publication_url = c.publication_url) "
				+ "GROUP BY c.feed_url "
				+ "HAVING COUNT(c.feed_url) = (SELECT(SELECT COUNT(c2.feed_url) FROM contain c2 WHERE c2.feed_url = c.feed_url GROUP BY c2.feed_url)*(SELECT COUNT(*) FROM friendship WHERE user1_email = \""+ user.getEmail() +"\" OR user2_email = \""+ user.getEmail() +"\"))");
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

	public ArrayList<User> resharedMoreThanThreeTime(User user){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate FROM user u "
				+ "INNER JOIN shared publication s ON s.user_email = u.email "
				+ "INNER JOIN (SELECT publication_url FROM sharedpublication WHERE user_email = \""+ user.getEmail() +"\") a "
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

	public ArrayList<Feed> feedReadShareInfo(User user){
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
				+ "WHERE fs.user_email = \""+ user.getEmail() +"\" "
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

	public ArrayList<User> friendReadFriendInfo(User user){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT u.email, u.password, u.nickname, u.city, u.country, u.avatar, u.biography, u.joinedDate, "
				+ "(SELECT COUNT(*) FROM readstatus rs WHERE rs.user_email = u.email)/(TO_DAYS(NOW())-TO_DAYS(u.joinedDate)) AS mread, "
				+ "(SELECT COUNT(u2.email) FROM user u2 INNER JOIN friendship f2 ON f2.user1_email = u2.email OR f2.user2_email = u2.email WHERE u2.email = u.email GROUP BY u2.email) AS nfriend "
				+ "FROM user u "
				+ "INNER JOIN friendship f ON f.user1_email = u.email OR f.user2_email = u.email "
				+ "WHERE (f.user1_email = \""+ user.getEmail() +"\" AND u.email <> f.user1_email) OR (f.user2_email = \""+ user.getEmail() +"\" AND u.email <> f.user2_email) "
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
