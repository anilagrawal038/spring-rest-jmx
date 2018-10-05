package com.san.mbean.bean;

public interface LoggerMXBean {

	public String getLevel();
	
	public void setLevel(String level);
	
	public String getBeanClassLoader();

}
