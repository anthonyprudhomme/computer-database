package org.excilys.computer_database.spring;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class ServerConfig implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
      AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
      context.register(AppConfig.class);
      ContextLoaderListener contextLoaderListener = new ContextLoaderListener(context);
      servletContext.addListener(contextLoaderListener);
  }

}
