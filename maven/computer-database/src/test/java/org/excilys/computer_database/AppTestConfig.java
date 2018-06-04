package org.excilys.computer_database;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.excilys.computer_database.util.Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "org.excilys.computer_database")
public class AppTestConfig {
  /**
   * Create the datasource using Hikari.
   * @return the datasource
   */
  @Bean
  public DataSource dataSource() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("testConfig");
    Properties properties = Util.convertResourceBundleToProperties(resourceBundle);
    HikariConfig hikariConfig = new HikariConfig(properties);
    HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
    return hikariDataSource;
  }
}