package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	String url = "jdbc:mysql://localhost:3306/rssreader";
	String utilisateur = "root";
	String motDePasse = "";
	Connection connexion = null;
	ResultSet res = null;
	Statement statement = null;
	
	public Database(){
		
	}
	
	public ResultSet querry(String req){
		try {
		    connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
			statement = connexion.createStatement();
			
			res = statement.executeQuery( req );

		} catch ( SQLException e ) {
			System.out.print(e);
		}
		return res;
	}
	
	public int update(String req){
		int status = -1;
		try {
		    connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
			statement = connexion.createStatement();
			
			status = statement.executeUpdate( req);
			
		} catch ( SQLException e ) {
			System.out.print(e);
		}
		return status;
	}
	
	public void close(){
		if ( res != null ) {
			try {
				res.close();
			} catch ( SQLException ignore ) {
			}
		}
		if ( statement != null ) {
			try {
				statement.close();
			} catch ( SQLException ignore ) {
			}
		}
		if ( connexion != null ) {
			try {
				connexion.close();
			} catch ( SQLException ignore ) {
			}
		}
	}

}
