package com.san.mbean.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.san.mbean.bean.ClassLoaderMXBean;

public class ClassLoaderBeanImpl extends ClassLoader implements ClassLoaderMXBean {

	@Override
	public boolean classExists(String name) {
		boolean exists = false;
		try {
			Class<?> clazz = findClass(name);
			exists = clazz != null;
		} catch (Exception e) {
		}
		return exists;
	}

	public Class<?> findClass(String name) {
		byte[] bt = loadClassData(name);
		return defineClass(name, bt, 0, bt.length);
	}

	private byte[] loadClassData(String className) {
		InputStream is = getClass().getClassLoader().getResourceAsStream(className.replace(".", "/") + ".class");
		ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
		byte[] classBytes = null;
		int len = 0;
		try {
			while ((len = is.read()) != -1) {
				byteSt.write(len);
			}
			classBytes = byteSt.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				byteSt.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return classBytes;
	}

}
