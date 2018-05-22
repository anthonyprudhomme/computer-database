package org.excilys.computer_database.persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Singleton
public class JdbcConnection {

  private static JdbcConnection instance = null;
  private Connection connection;
  private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnection.class);

  private static String url = "jdbc:mysql://localhost/computer-database-db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

  private static String username = "admincdb";
  private static String password = "qwerty1234";

  public static boolean testMode = false;

  /**
   * Set configuration parameters to test mode.
   */
  private static void setTestConfiguration() {
    url = "jdbc:hsqldb:mem:computerdatabase";
  }

  /**
   * Set configuration parameters to default.
   */
  private static void setDefaultConfiguration() {
    url = "jdbc:mysql://localhost/computer-database-db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    username = "admincdb";
    password = "qwerty1234";
  }

  /**
   * Create a connection.
   * @return connection object
   * @throws SQLException if there is an SQLException
   */
  private static Connection getTestConnection() {
    try {
      return DriverManager.getConnection(url);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }


  /**
   * Singleton. Used to do the connection to the database.
   */
  private JdbcConnection() {
    if (testMode) {
      setTestConfiguration();
      connection = getTestConnection();
    } else {
      setDefaultConfiguration();
      System.out.println("Connecting database...");

      try {
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Database connected!");
      } catch (SQLException e) {
        throw new IllegalStateException("Cannot connect the database!", e);
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
            instance.connection = DriverManager.getConnection(url, username, password);
          }
        }
      }

    } catch (SQLException e) {
      throw new IllegalStateException("Cannot connect the database!", e);
    }
    return instance.connection;
  }

}
