package com.san.mbean;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import com.san.mbean.impl.ClassLoaderBeanImpl;
import com.san.mbean.impl.DynamicBeanImpl;
import com.san.mbean.impl.LoggerBeanImpl;
import com.san.mbean.impl.StudentBeanImpl;
import com.san.mbean.impl.TestGaugeBeanImpl;
import com.san.service.StudentService;

// Ref : https://stackify.com/jmx/

public class MBeanAgent {

	private static boolean _initialized = false;

	public void initialize(StudentService studentService) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
		if (!_initialized) {

			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

			ObjectName name = new ObjectName("spring-jmx:type=MStudent");
			StudentBeanImpl studentMBean = new StudentBeanImpl(studentService);
			mBeanServer.registerMBean(studentMBean, name);

			LoggerBeanImpl loggerMBean = new LoggerBeanImpl();
			mBeanServer.registerMBean(loggerMBean, new ObjectName("spring-jmx:type=MLogger"));

			ClassLoaderBeanImpl classLoaderMBean = new ClassLoaderBeanImpl();
			mBeanServer.registerMBean(classLoaderMBean, new ObjectName(loggerMBean.getBeanClassLoader()));

			DynamicBeanImpl dynamicBeanImpl = new DynamicBeanImpl();
			mBeanServer.registerMBean(dynamicBeanImpl, new ObjectName("spring-jmx:type=MDynamicBean"));
			_initialized = true;

			TestGaugeBeanImpl testGaugeBeanImpl = new TestGaugeBeanImpl();
			mBeanServer.registerMBean(testGaugeBeanImpl, new ObjectName("spring-jmx:type=TestGaugeBean"));
			testGaugeBeanImpl.start();

			// SNMP Traps
			// Ref : https://docs.oracle.com/cd/E19698-01/816-7609/6mdjrf88a/index.html
			// Ref : https://jpminetti.developpez.com/articles/jmx/?utm_source=Home+page&utm_medium=sourceforge.net&utm_campaign=articles
			// Ref : http://snmpadaptor4j.sourceforge.net/
			// Ref : https://raw.githubusercontent.com/RestComm/snmp-adaptor/master/adaptor-core/src/main/java/org/jboss/jmx/adaptor/snmp/agent/TrapEmitter.java

		}
	}

	// Ref : https://www.cs.mun.ca/java-api-1.5/guide/jmx/tutorial/security.html#wp997030

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void secureJMXServer(MBeanServer mBeanServer) throws IOException {
		HashMap env = new HashMap();
		SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
		SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
		env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
		env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);

		env.put("jmx.remote.x.password.file", "config" + File.separator + "password.properties");
		env.put("jmx.remote.x.access.file", "config" + File.separator + "access.properties");
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/server");
		JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mBeanServer);
		cs.start();
	}

}
