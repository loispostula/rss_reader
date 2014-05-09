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
	
	public Database(){
		
	}
	
	public ResultSet querry(String req){
		ResultSet resultat = null;
		try {
		    connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
		    
			Statement statement = connexion.createStatement();
			resultat = statement.executeQuery( req );

		} catch ( SQLException e ) {
			System.out.print(e);
		} finally {
		    if ( connexion != null )
		        try {
		            connexion.close();
		        } catch ( SQLException ignore ) { }
		}
		return resultat;
	}
	
	public int update(String req){
		int status = -1;
		try {
		    connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
		    
			Statement statement = connexion.createStatement();
			status = statement.executeUpdate( req);
			
		} catch ( SQLException e ) {
			System.out.print(e);
		} finally {
		    if ( connexion != null )
		        try {
		            connexion.close();
		        } catch ( SQLException ignore ) { }
		}
		return status;
	}

}
