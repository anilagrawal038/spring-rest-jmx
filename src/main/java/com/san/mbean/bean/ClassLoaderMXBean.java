package com.san.mbean.bean;

public interface ClassLoaderMXBean {

	// public Class<?> findClass(String name);

	public boolean classExists(String name);

}
