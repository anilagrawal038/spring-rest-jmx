package com.san.mbean.impl;

import java.util.Enumeration;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.san.mbean.bean.LoggerMXBean;

public class LoggerBeanImpl implements LoggerMXBean {

	@Override
	public String getLevel() {
		return Logger.getRootLogger().getLevel().toString();
	}

	@Override
	public void setLevel(String levelStr) {
		System.out.println("Provided level : " + levelStr);
		Level level = Level.toLevel(levelStr);
		Logger root = Logger.getRootLogger();
		@SuppressWarnings("unchecked")
		Enumeration<Category> allLoggers = root.getLoggerRepository().getCurrentCategories();
		while (allLoggers.hasMoreElements()) {
			Category tmpLogger = allLoggers.nextElement();
			tmpLogger.setLevel(level);
		}
		root.setLevel(level);
		System.out.println("Updated level : " + level);
	}

	@Override
	public String getBeanClassLoader() {
		return "spring-jmx:type=MClassLoader";
	}

}
