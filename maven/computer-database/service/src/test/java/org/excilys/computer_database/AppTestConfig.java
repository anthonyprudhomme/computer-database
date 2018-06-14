package org.excilys.computer_database;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.excilys.computer_database.util.Util;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
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
  /**
   * Get the session factory.
   * @return the session factory
   */
  @Bean
  public SessionFactory sessionFactory() {
    org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
    configuration.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbc.JDBCDriver");
    configuration.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:computerdatabase");
    SessionFactory sessionFactory = configuration.buildSessionFactory();
    return sessionFactory;
  }
}