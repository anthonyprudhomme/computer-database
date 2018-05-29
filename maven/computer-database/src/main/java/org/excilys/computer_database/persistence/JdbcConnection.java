package org.excilys.computer_database.persistence;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// Singleton
public class JdbcConnection {

  //private static JdbcConnection instance = null;
  //private Connection connection;
  private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnection.class);

  public static boolean testMode = false;
  private static HikariConfig hikariConfig;
  private static HikariDataSource hikariDataSource;

  /**
   * Singleton. Used to do the connection to the database.
   */
  private JdbcConnection() {
    ResourceBundle resourceBundle;
    if (testMode) {
      resourceBundle = ResourceBundle.getBundle("testConfig");
    } else {
      resourceBundle = ResourceBundle.getBundle("config");
    }
    Properties properties = convertResourceBundleToProperties(resourceBundle);

    hikariConfig = new HikariConfig(properties);

    hikariConfig.addDataSourceProperty("cachePrepStmts", true);
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", 256);
    hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
    hikariDataSource = new HikariDataSource(hikariConfig);
  }

  /**
   * Returns a connection.
   * @return a newly opened connection
   */
  public static Connection getConnection() {
    LOGGER.debug("Access to JDBC connection");
    try {
      if (hikariDataSource == null) {
        new JdbcConnection();
        return hikariDataSource.getConnection();
      } else {
        return hikariDataSource.getConnection();
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Cannot connect the database!", e);
    }
  }

  /**
   * Convert ResourceBundle into a Properties object.
   *
   * @param resource a resource bundle to convert.
   * @return Properties a properties version of the resource bundle.
   */
  private Properties convertResourceBundleToProperties(ResourceBundle resource) {
    Properties properties = new Properties();
    Enumeration<String> keys = resource.getKeys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      properties.put(key, resource.getString(key));
    }
    return properties;
  }

}
