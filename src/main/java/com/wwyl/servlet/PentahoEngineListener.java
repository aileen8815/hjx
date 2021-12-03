package com.wwyl.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
/**
 * Application Lifecycle Listener implementation class InitListener
 *
 */
@WebListener
public class PentahoEngineListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
      ClassicEngineBoot.getInstance().start();
    }
    
    public void contextDestroyed(ServletContextEvent event) {
    }
}