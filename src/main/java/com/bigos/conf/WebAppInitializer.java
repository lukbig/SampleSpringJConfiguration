package com.bigos.conf;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer {

    public void onStartup(ServletContext context) throws ServletException {
        AnnotationConfigWebApplicationContext dispatcherServlet = new AnnotationConfigWebApplicationContext();
        dispatcherServlet.register(MvcConfig.class);
        dispatcherServlet.setServletContext(context);
        ServletRegistration.Dynamic servlet = context.addServlet("dispatcher", new DispatcherServlet(dispatcherServlet));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }
}
