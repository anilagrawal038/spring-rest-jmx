package com.san.listener;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.san.service.BootstrapService;

public class AppContextLoaderListener extends ContextLoaderListener {

	Logger logger = Logger.getLogger(this.getClass());
	BootstrapService bootstrapService;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		bootstrapService = ctx.getBean(BootstrapService.class);
		bootstrapService.startup();
		logger.trace("ServletContextListener started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
		bootstrapService.destroy();
		logger.trace("ServletContextListener destroyed");
	}

}
