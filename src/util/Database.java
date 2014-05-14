package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {
    String url;
    String utilisateur;
    String motDePasse;
    Connection connexion = null;
    ResultSet res = null;
    Statement statement = null;

    public Database() {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "database.properties";
            input = getClass().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            url = prop.getProperty("database");
            utilisateur = prop.getProperty("dbuser");
            motDePasse = prop.getProperty("dbpassword");
            
            connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
            statement = connexion.createStatement();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public ResultSet querry(String req) {
        try {

            res = statement.executeQuery(req);

        } catch (SQLException e) {
            System.out.print(e);
        }
        return res;
    }

    public int update(String req) {
        int status = -1;
        try {

            status = statement.executeUpdate(req);

        } catch (SQLException e) {
            System.out.print(e);
        }
        return status;
    }

    public void close() {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException ignore) {
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignore) {
            }
        }
        if (connexion != null) {
            try {
                connexion.close();
            } catch (SQLException ignore) {
            }
        }
    }

}
