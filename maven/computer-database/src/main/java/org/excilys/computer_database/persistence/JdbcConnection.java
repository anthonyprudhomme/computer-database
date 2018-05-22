package org.excilys.computer_database.persistence;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Singleton
public class JdbcConnection {

  private static JdbcConnection instance = null;
  private Connection connection;
  private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnection.class);

  private static String defaultUrl;
  private static String usedUrl;
  private static String testUrl;

  private static String username;
  private static String password;

  public static boolean testMode = false;

  /**
   * Set configuration parameters to test mode.
   */
  private static void setTestConfiguration() {
    usedUrl = testUrl;
  }

  /**
   * Set configuration parameters to default.
   */
  private static void setDefaultConfiguration() {
    usedUrl = defaultUrl;
  }

  /**
   * Create a connection.
   * @return connection object
   * @throws SQLException if there is an SQLException
   */
  private static Connection getTestConnection() {
    try {
      return DriverManager.getConnection(usedUrl);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }


  /**
   * Singleton. Used to do the connection to the database.
   */
  private JdbcConnection() {
    readPropertyFile();
    if (testMode) {
      setTestConfiguration();
      connection = getTestConnection();
    } else {
      setDefaultConfiguration();
      System.out.println("Connecting database...");

      try {
        connection = DriverManager.getConnection(usedUrl, username, password);
        System.out.println("Database connected!");
      } catch (SQLException e) {
        throw new IllegalStateException("Cannot connect the database!", e);
      }
    }
  }

  /**
   * Read the .properties file to get credentials.
   */
  private void readPropertyFile() {
    final Properties prop = new Properties();
    InputStream input = null;
    try {
      input = new FileInputStream("config.properties");
      prop.load(input);
      defaultUrl = prop.getProperty("default.url");
      username = prop.getProperty("default.username");
      password = prop.getProperty("default.password");
      testUrl = prop.getProperty("test.url");

    } catch (final IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Returns a connection.
   * @return a newly opened connection
   */
  public static Connection getConnection() {
    LOGGER.debug("Access to JDBC connection");
    try {
      if (instance == null) {
        instance = new JdbcConnection();
      } else {
        if (testMode) {
          setTestConfiguration();
          instance.connection = getTestConnection();
        } else {
          setDefaultConfiguration();
          if (instance.connection.isClosed()) {
            instance.connection = DriverManager.getConnection(usedUrl, username, password);
          }
        }
      }

    } catch (SQLException e) {
      throw new IllegalStateException("Cannot connect the database!", e);
    }
    return instance.connection;
  }

}
