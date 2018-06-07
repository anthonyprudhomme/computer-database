package org.excilys.computer_database.spring;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.excilys.computer_database.util.Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.excilys.computer_database.controllers",
    "org.excilys.computer_database.service",
    "org.excilys.computer_database.dao",
    "org.excilys.computer_database.ui",
"org.excilys.computer_database.validator"})
public class AppConfig implements WebMvcConfigurer {

  /**
   * Create the datasource using Hikari.
   * @return the datasource
   */
  @Bean
  public DataSource dataSource() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    Properties properties = Util.convertResourceBundleToProperties(resourceBundle);
    HikariConfig hikariConfig = new HikariConfig(properties);
    HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
    return hikariDataSource;
  }
  /**
   * Resolves views.
   * @return the view
   */
  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setViewClass(JstlView.class);
    viewResolver.setPrefix("/WEB-INF/views/");
    viewResolver.setSuffix(".jsp");

    return viewResolver;
  }

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
  }

  /**
   * Make resource reloadable.
   * @return the messagesource.
   */
  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
  /**
   * Cookies resolver.
   * @return the locale resolver
   */
  @Bean
  public CookieLocaleResolver localeResolver() {
    CookieLocaleResolver localeResolver = new CookieLocaleResolver();
    localeResolver.setDefaultLocale(Locale.ENGLISH);
    localeResolver.setCookieName("my-locale-cookie");
    localeResolver.setCookieMaxAge(3600);
    return localeResolver;
  }
  /**
   * Intercepts the change of Locale.
   * @return the interceptor
   */
  @Bean
  public LocaleChangeInterceptor localeInterceptor() {
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeInterceptor());
  }
}
