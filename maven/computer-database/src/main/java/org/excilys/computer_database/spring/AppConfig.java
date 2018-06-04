package org.excilys.computer_database.spring;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "org.excilys.computer_database")
public class AppConfig {

  public static boolean testMode = false;
  /**
   * Create the datasource using Hikari.
   * @return the datasource
   */
  @Bean
  public DataSource dataSource() {
    HikariConfig hikariConfig;
    HikariDataSource hikariDataSource;
    ResourceBundle resourceBundle;
    if (testMode) {
      resourceBundle = ResourceBundle.getBundle("testConfig");
    } else {
      resourceBundle = ResourceBundle.getBundle("config");
    }
    Properties properties = convertResourceBundleToProperties(resourceBundle);
    hikariConfig = new HikariConfig(properties);
    hikariDataSource = new HikariDataSource(hikariConfig);
    return hikariDataSource;
  }

  /**
   * Convert ResourceBundle into a Properties object.
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
