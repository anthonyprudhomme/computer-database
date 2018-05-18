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

  /**
   * Singleton. Used to do the connection to the database.
   */
  private JdbcConnection() {
    String url = "jdbc:mysql://localhost/computer-database-db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

    String username = "admincdb";
    String password = "qwerty1234";

    System.out.println("Connecting database...");

    try {
      connection = DriverManager.getConnection(url, username, password);
      System.out.println("Database connected!");
    } catch (SQLException e) {
      throw new IllegalStateException("Cannot connect the database!", e);
    }
  }

  /**
   * Returns a connection.
   * @return a newly opened conenctio
   */
  public static Connection getConnection() {
    LOGGER.debug("Access to JDBC connection");
    try {
      if (instance == null) {
        instance = new JdbcConnection();
      } else {
        if (instance.connection.isClosed()) {
          String url = "jdbc:mysql://localhost/computer-database-db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
          String username = "admincdb";
          String password = "qwerty1234";
          instance.connection = DriverManager.getConnection(url, username, password);
        }
      }

    } catch (SQLException e) {
      throw new IllegalStateException("Cannot connect the database!", e);
    }
    return instance.connection;
  }

}
