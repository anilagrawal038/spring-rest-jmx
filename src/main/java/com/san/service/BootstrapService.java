package com.san.service;

import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.san.mbean.MBeanAgent;

@Component
@PropertySource(value = { "classpath:application.properties" }, ignoreResourceNotFound = true)
public class BootstrapService {

	Logger logger = Logger.getLogger(this.getClass());

	@SuppressWarnings("unused")
	@Autowired
	private Environment env;

	@Autowired
	StudentService studentService;

	@PostConstruct
	public void init() {
		logger.debug("BootstrapService initialized");
		// testLoggingLevel();
	}

	public void startup() {
		logger.debug("Application being starting-up");
		// changeLoggingLevel(Level.WARN);
		testThreads();

		// Initialize MBeanAgent
		MBeanAgent agent = new MBeanAgent();
		try {
			agent.initialize(studentService);
		} catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
			logger.error("Exception occurred while initializing MBeanAgent.\n", e);
		}
	}

	public void destroy() {
		logger.debug("Application being shutting down");
	}

	@SuppressWarnings("unused")
	private void testLoggingLevel() {
		new Thread("BootstrapService-testLoggingLevel()-Thread") {
			public void run() {
				int counter = 0;
				while (true) {
					counter++;
					try {
						logger.info("Logger(Info) test counter : " + counter);
						logger.debug("Logger(Debug) test counter : " + counter);
						logger.trace("Logger(Trace) test counter : " + counter);
						logger.error("Logger(Error) test counter : " + counter);
						logger.fatal("Logger(Fatal) test counter : " + counter);
						logger.warn("Logger(Warn) test counter : " + counter);
						Thread.sleep(5000);
					} catch (Exception e) {
					}
				}
			}
		}.start();
	}

	@SuppressWarnings("unused")
	private void changeLoggingLevel(Level level) {
		new Thread("BootstrapService-changeLoggingLevel()-Thread") {
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
				}
				logger.info("Changing logger level to : " + level);
				Logger root = Logger.getRootLogger();
				@SuppressWarnings("unchecked")
				Enumeration<Category> allLoggers = root.getLoggerRepository().getCurrentCategories();
				while (allLoggers.hasMoreElements()) {
					Category tmpLogger = allLoggers.nextElement();
					tmpLogger.setLevel(level);
				}
				root.setLevel(level);
			}
		}.start();
	}

	private void testThreads() {
		new Thread("BootstrapService-testThreads()-Thread-1") {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
			}
		}.start();

		new Thread("BootstrapService-testThreads()-Thread-2") {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1500);
					} catch (Exception e) {
					}
				}
			}
		}.start();

		new Thread("BootstrapService-testThreads()-Thread-3") {
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
					}
				}
			}
		}.start();
	}
}
