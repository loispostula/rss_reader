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
				"SELECT u.email, COUNT(u.email) FROM user u "+
				"INNER JOIN friendship f "+
				"ON f.user1_email = u.email OR f.user2_email = u.email "+
				"GROUP  BY u.email "+
				"HAVING COUNT(u.email) < 3");
        ArrayList<User> requests = new ArrayList<User>();
        try {
            while (res.next()) {
                requests.add(User.getUserFromDb(res.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public ArrayList<Feed> feedFollowedByUserWhoFollowTwoOrMoreFeedOfX(User user){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT c.feed_url FROM feedsubscription c "
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
                requests.add(Feed.getFeedFromDb(res.getString("feed_url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public ArrayList<Feed> notFollowedByFriendAndNotShared(User user){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT c.feed_url FROM contain c "
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
                requests.add(Feed.getFeedFromDb(res.getString("feed_url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public ArrayList<User> resharedMoreThanThreeTime(User user){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT s.user_email FROM sharedpublication s "
				+ "INNER JOIN (SELECT publication_url FROM sharedpublication WHERE user_email = \""+ user.getEmail() +"\") a "
				+ "ON s.publication_url = a.publication_url "
				+ "GROUP BY s.user_email "
				+ "HAVING COUNT(s.user_email) > 2");
        ArrayList<User> requests = new ArrayList<User>();
        try {
            while (res.next()) {
                requests.add(User.getUserFromDb(res.getString("user_email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public ArrayList<Feed> feedReadShareInfo(User user){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT fs.feed_url, "
				+ "(SELECT COUNT(*) FROM readstatus rs WHERE rs.publication_url = c.publication_url AND rs.user_email = fs.user_email AND TO_DAYS(NOW())-TO_DAYS(rs.date) < 30) AS nread, "
				+ "(SELECT COUNT(*) FROM sharedpublication sp WHERE sp.publication_url = c.publication_url AND sp.user_email = fs.user_email AND TO_DAYS(NOW())-TO_DAYS(sp.sharedDate) < 30) AS nshared "
				+ ", (SELECT nshared/nread) AS ratio "
				+ "FROM feedsubscription fs "
				+ "INNER JOIN contain c ON c.feed_url = fs.feed_url "
				+ "WHERE fs.user_email = \""+ user.getEmail() +"\" "
				+ "GROUP BY fs.feed_url "
				+ "ORDER BY nshared");
        ArrayList<Feed> requests = new ArrayList<Feed>();
        try {
            while (res.next()) {
                requests.add(Feed.getFeedFromDb(res.getString("feed_url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

	public ArrayList<Feed> friendReadFriendInfo(User user){
		Database db = new Database();
		ResultSet res = db.querry(
				"SELECT u.email, "
				+ "(SELECT COUNT(*) FROM readstatus rs WHERE rs.user_email = u.email)/(TO_DAYS(NOW())-TO_DAYS(u.joinedDate)) AS mread, "
				+ "(SELECT COUNT(u2.email) FROM user u2 INNER JOIN friendship f2 ON f2.user1_email = u2.email OR f2.user2_email = u2.email WHERE u2.email = u.email GROUP BY u2.email) AS nfriend "
				+ "FROM user u "
				+ "INNER JOIN friendship f ON f.user1_email = u.email OR f.user2_email = u.email "
				+ "WHERE (f.user1_email = \""+ user.getEmail() +"\" AND u.email <> f.user1_email) OR (f.user2_email = \""+ user.getEmail() +"\" AND u.email <> f.user2_email) "
				+ "ORDER BY mread");
        ArrayList<Feed> requests = new ArrayList<Feed>();
        try {
            while (res.next()) {
                requests.add(Feed.getFeedFromDb(res.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
	}

}
