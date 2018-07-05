package org.excilys.computer_database.spring;

import org.excilys.computer_database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  private UserService userService;
  private PasswordEncoder passwordEncoder;
  
  public SpringSecurityConfig(UserService userService) {
    this.userService = userService;
  }
  
  @Autowired
  public void configure(AuthenticationManagerBuilder authenticationMgr) throws Exception {
    authenticationMgr.userDetailsService(userService).passwordEncoder(passwordEncoder);
  }
  
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.sessionManagement().maximumSessions(1).expiredUrl("/loginPage");
    http.authorizeRequests()
      .antMatchers("/computers").access("hasRole('USER')")
      .and()
        .formLogin()
        .loginPage("/loginPage")
        .defaultSuccessUrl("/computers")
        .failureUrl("/loginPage?error")
        .usernameParameter("username")
        .passwordParameter("password")        
      .and()
        .logout()
        .logoutSuccessUrl("/loginPage?logout"); 
    
  }
}