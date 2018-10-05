package com.san.mbean.impl;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import com.san.mbean.bean.StudentMXBean;
import com.san.service.StudentService;

public class StudentBeanImpl extends NotificationBroadcasterSupport implements StudentMXBean {

	private long sequenceNumber = 1;

	private StudentService studentService;

	public StudentBeanImpl(StudentService studentService) {
		this.studentService = studentService;
	}

	@Override
	public String getStudentInfo() {
		System.out.println("Method : printStudentInfo() invoked");
		return "Sample student info";
	}

	public void testNotification() {
		Notification n = new AttributeChangeNotification(this, // the object name of the source of the notification
				sequenceNumber++, // the sequence number which increments on every sent notification,
				System.currentTimeMillis(), // a timestamp when notification is sent
				"Test attribute has changed", // the content of the notification
				"testAttribute", // name of the attribute which has changed
				"String", // type of the attribute which has changed
				"test1", // the old value of the attribute
				"test2"); // the new value of the attribute

		sendNotification(n);
	}

	@Override
	public MBeanNotificationInfo[] getNotificationInfo() {
		String[] types = new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE };

		String name = AttributeChangeNotification.class.getName();
		String description = "Test attribute of this Bean has changed";
		MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
		return new MBeanNotificationInfo[] { info };
	}

	public long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public long getAddCounter() {
		return studentService.addCounter;
	}

	public long getListCounter() {
		return studentService.listCounter;
	}

	public long getSearchCounter() {
		return studentService.searchCounter;
	}

	public long getUpdateCounter() {
		return studentService.updateCounter;
	}

	public long getDeleteCounter() {
		return studentService.deleteCounter;
	}

	public long getGetCounter() {
		return studentService.getCounter;
	}

}