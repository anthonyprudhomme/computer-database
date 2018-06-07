package org.excilys.computer_database.spring;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


public class ServerConfig implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
      AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
      context.register(AppConfig.class);
      ContextLoaderListener contextLoaderListener = new ContextLoaderListener(context);
      servletContext.addListener(contextLoaderListener);
      DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
      dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
      ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);
      servlet.setLoadOnStartup(1);
      servlet.addMapping("/");
  }

}
