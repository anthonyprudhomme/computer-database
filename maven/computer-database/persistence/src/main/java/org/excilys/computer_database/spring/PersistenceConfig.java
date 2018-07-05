package org.excilys.computer_database.spring;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"org.excilys.computer_database.service",
    "org.excilys.computer_database.dao",
    "org.excilys.computer_database.ui",
	"org.excilys.computer_database.validator"})
public class PersistenceConfig {
 
  
  /**
   * Get the session factory.
   * @return the session factory
   */
  @Bean
  public SessionFactory sessionFactory() {
    return new org.hibernate.cfg.Configuration().configure("/resources/hibernate.cfg.xml").buildSessionFactory();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

  /**
   * Return the transaction manager.
   * @return the transaction manager.
   */
  @Bean
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(sessionFactory());
    return transactionManager;
  }
  
}
