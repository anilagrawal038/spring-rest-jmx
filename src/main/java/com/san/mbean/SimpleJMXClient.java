package com.san.mbean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.NotificationListener;

import com.san.mbean.bean.LoggerMXBean;
import com.san.mbean.bean.StudentMXBean;
import com.san.mbean.impl.ClientSideBeanImpl;

public class SimpleJMXClient {

	public static void main(final String[] arguments) {
		final String jmxUrlString = "service:jmx:rmi:///jndi/rmi://127.0.0.1:8008/jmxrmi";
		try {
			final MBeanServerConnection mbsc = getMBeanServerConnection(jmxUrlString, "", "");

			try {
				// Register MBean from client
				registerBean(mbsc);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Inspect MBeans
			inspectMBeanServerConnection(mbsc);

			// Add Notification listener for MBean
			notificationHandler(mbsc);

			// Invoke MBean methods
			System.out.println("MBean Logger level: " + getLoggerLevelViaMBean(mbsc));
			System.out.println("MXBean Student Info: " + getStudentInfoViaMXBean(mbsc));
		} catch (MalformedURLException badJmxUrl) {
			System.err.println("ERROR trying to build JMXServiceURL with " + jmxUrlString + ":\n" + badJmxUrl.getMessage());
		} catch (IOException ioEx) {
			System.err.println("ERROR trying to connect.\n" + ioEx.getMessage());
		}

		while (true) {
			try {
				Thread.sleep(5000);
				System.out.println("SimpleJMXClient running ...");
			} catch (Exception e) {
			}
		}
	}

	public static String getLoggerLevelViaMBean(final MBeanServerConnection mbsc) {
		String loggerLevel = "";
		final String mbeanObjectNameStr = "spring-jmx:type=MLogger";
		try {
			final ObjectName objectName = new ObjectName(mbeanObjectNameStr);
			final LoggerMXBean loggerBeanProxy = JMX.newMBeanProxy(mbsc, objectName, LoggerMXBean.class);
			loggerLevel = loggerBeanProxy.getLevel();
		} catch (MalformedObjectNameException badObjectName) {
			System.err.println(mbeanObjectNameStr + " is a bad object name.:\n" + badObjectName.getMessage());
		}

		return loggerLevel;
	}

	public static String getStudentInfoViaMXBean(final MBeanServerConnection mbsc) {
		String studentInfo = "";
		final String mbeanObjectNameStr = "spring-jmx:type=MStudent";
		try {
			final ObjectName objectName = new ObjectName(mbeanObjectNameStr);
			final StudentMXBean studentBeanProxy = (StudentMXBean) JMX.newMXBeanProxy(mbsc, objectName, StudentMXBean.class);
			studentInfo = studentBeanProxy.getStudentInfo();
		} catch (MalformedObjectNameException badObjectName) {
			System.err.println(mbeanObjectNameStr + " is a bad object name.:\n" + badObjectName.getMessage());
		}

		return studentInfo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static MBeanServerConnection getMBeanServerConnection(String jmxUrlString, String uid, String pwd) throws IOException {
		// Provide credentials required by server for user authentication
		HashMap environment = new HashMap();
		String[] credentials = new String[] { uid, pwd };
		environment.put(JMXConnector.CREDENTIALS, credentials);

		// Create JMXServiceURL of JMX Connector (must be known in advance)
		/*
		 * service:jmx:rmi://[host[:port]][urlPath]
		 * 
		 * jmxUrlString = "service:jmx:rmi:///jndi/rmi://localhost:9999/server";
		 * 
		 */
		JMXServiceURL url = new JMXServiceURL(jmxUrlString);

		// Get JMX connector
		JMXConnector jmxc = JMXConnectorFactory.connect(url, environment);
		// Get MBean server connection
		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
		return mbsc;
	}

	public static void inspectMBeanServerConnection(MBeanServerConnection mbsc) throws IOException {
		System.out.println("Total MBeans registerd : " + mbsc.getMBeanCount());
		System.out.println("\n");

		String[] domains = mbsc.getDomains();
		for (int counter = 0; counter < domains.length; counter++) {
			System.out.println("MBean domain (" + (counter + 1) + ") : " + domains[counter]);
		}
		System.out.println("\n");

		Set<ObjectInstance> mBeans = mbsc.queryMBeans(null, null);
		for (ObjectInstance bean : mBeans) {
			System.out.println("MBean Instance : " + bean);
		}
		System.out.println("\n");

		Set<ObjectName> mBeanNames = mbsc.queryNames(null, null);
		for (ObjectName name : mBeanNames) {
			System.out.println("MBean Name : " + name);
		}
		System.out.println("\n");
	}

	public static void registerBean(MBeanServerConnection mbsc)
			throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException, ReflectionException, MBeanException, IOException, InstanceNotFoundException {
		final String mbeanObjectNameStr = "spring-jmx:type=MLogger";
		String classLoader = null;
		try {
			final ObjectName objectName = new ObjectName(mbeanObjectNameStr);
			final LoggerMXBean loggerBeanProxy = JMX.newMBeanProxy(mbsc, objectName, LoggerMXBean.class);
			classLoader = loggerBeanProxy.getBeanClassLoader();
		} catch (MalformedObjectNameException badObjectName) {
			System.err.println(mbeanObjectNameStr + " is a bad object name.:\n" + badObjectName.getMessage());
		}
		mbsc.createMBean(ClientSideBeanImpl.class.getName(), new ObjectName("spring-jmx:type=MTestClientBean"), new ObjectName(classLoader));
	}

	public static void notificationHandler(MBeanServerConnection mbsc) {

		// JMXConnector.addConnectionNotificationListener
		// For testing lost notifications

		try {
			mbsc.addNotificationListener(new ObjectName("spring-jmx:type=MStudent"), new TestNotificationListener(), null, null);
		} catch (InstanceNotFoundException | MalformedObjectNameException | IOException e) {
			e.printStackTrace();
		}
	}

	public static class TestNotificationListener implements NotificationListener {

		@Override
		public void handleNotification(Notification notification, Object handback) {
			System.out.println("\n\n---------------------------------------------------------------------------");
			System.out.println("Notification recieved, SequenceNumber : " + notification.getSequenceNumber());
			System.out.println("Notification recieved, TimeStamp : " + notification.getTimeStamp());
			System.out.println("Notification recieved, Message : " + notification.getMessage());
			System.out.println("Notification recieved, UserData : " + notification.getUserData());
			System.out.println("Notification recieved, Type : " + notification.getType());
			System.out.println("---------------------------------------------------------------------------\n\n");
		}

	}
}
