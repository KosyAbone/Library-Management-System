package util;

/**
 *
 * @author kossy
 */
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static Connection conn = null;
    
    public static Connection getConnection() {
        if (conn == null) {
            try {
                // Load the configuration from the properties file
                Properties properties = new Properties();
                try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
                    if (input == null) {
                        System.out.println("Sorry, unable to find config.properties");
                        return null;
                    }
                    properties.load(input);
                } catch (IOException ex) {
                    Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Get database credentials from the properties file
                String url = properties.getProperty("db.url");
                String username = properties.getProperty("db.username");
                String password = properties.getProperty("db.password");
                
                conn = DriverManager.getConnection(url, username, password);
                System.out.println("Connected to database");
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Not connected to database");
            }
        }
        return conn;
    }
    
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
                System.out.println("Connection closed");
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
