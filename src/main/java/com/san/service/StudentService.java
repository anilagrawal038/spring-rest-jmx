//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-rest-swagger
//Module						: spring-jpa-rest-swagger
//Package Name					: com.san.service
//File Name						: StudentService.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 11-Mar-2017 7:25:38 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//11-Mar-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.service;

import java.util.ArrayList;
import java.util.List;

import javax.management.Notification;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedNotification;
import org.springframework.jmx.export.annotation.ManagedNotifications;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.san.co.NewStudentCO;
import com.san.co.StudentCO;
import com.san.domain.Student;
import com.san.repo.StudentRepository;

// Note : We don't need to register MBeans manually to MBeanServer which are exposed via @ManagedResource annotation

@Component
@ManagedResource(description = "Student resource", objectName = "spring-rest-jmx:Type=StudentResource") // It will Expose Spring Component as JMX MBean
@ManagedNotifications({ @ManagedNotification(name = "javax.management.Notification", notificationTypes = { "Delete" }, description = "Student deleted action performed") }) // // It will Expose JMX MBean Notification MetaData
public class StudentService implements NotificationPublisherAware {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	private EntityManager entityManager;

	public long addCounter, listCounter, searchCounter, updateCounter, deleteCounter, getCounter;

	@Transactional
	public StudentCO addStudent(NewStudentCO newStudentCO) {
		Student student = new Student();
		student.setAddress(newStudentCO.getAddress());
		student.setFirstName(newStudentCO.getFirstName());
		student.setLastName(newStudentCO.getLastName());
		student.setStandard(newStudentCO.getStandard());
		student.setRollNo(newStudentCO.getRollNo());
		student = studentRepository.save(student);
		StudentCO studentCO = new StudentCO();
		studentCO.setAddress(student.getAddress());
		studentCO.setFirstName(student.getFirstName());
		studentCO.setLastName(student.getLastName());
		studentCO.setStandard(student.getStandard());
		studentCO.setRollNo(student.getRollNo());
		studentCO.setStudentId(student.getId());
		addCounter++;
		return studentCO;
	}

	@Transactional(readOnly = true)
	public List<StudentCO> list() {
		List<Student> students = studentRepository.findAll();
		List<StudentCO> studentsCO = new ArrayList<StudentCO>();
		for (Student student : students) {
			StudentCO studentCO = new StudentCO();
			studentCO.setAddress(student.getAddress());
			studentCO.setFirstName(student.getFirstName());
			studentCO.setLastName(student.getLastName());
			studentCO.setStandard(student.getStandard());
			studentCO.setRollNo(student.getRollNo());
			studentCO.setStudentId(student.getId());
			studentsCO.add(studentCO);
		}
		logger.debug("Students list API invoked (Debug)");
		logger.info("Students list API invoked (Info)");
		logger.fatal("Students list API invoked (Fatal)");
		listCounter++;
		return studentsCO;
	}

	@Transactional(readOnly = true)
	public StudentCO getStudent(long studentId) {
		Student student = studentRepository.findOne(studentId);
		StudentCO studentCO = new StudentCO();
		studentCO.setAddress(student.getAddress());
		studentCO.setFirstName(student.getFirstName());
		studentCO.setLastName(student.getLastName());
		studentCO.setStandard(student.getStandard());
		studentCO.setRollNo(student.getRollNo());
		studentCO.setStudentId(student.getId());
		getCounter++;
		return studentCO;
	}

	@Transactional
	public StudentCO updateStudent(long studentId, NewStudentCO newStudentCO) {
		Student student = studentRepository.findOne(studentId);
		student.setAddress(newStudentCO.getAddress());
		student.setFirstName(newStudentCO.getFirstName());
		student.setLastName(newStudentCO.getLastName());
		student.setStandard(newStudentCO.getStandard());
		student.setRollNo(newStudentCO.getRollNo());
		student = studentRepository.save(student);
		StudentCO studentCO = new StudentCO();
		studentCO.setAddress(student.getAddress());
		studentCO.setFirstName(student.getFirstName());
		studentCO.setLastName(student.getLastName());
		studentCO.setStandard(student.getStandard());
		studentCO.setRollNo(student.getRollNo());
		studentCO.setStudentId(student.getId());
		updateCounter++;
		return studentCO;
	}

	@Transactional
	public String deleteStudent(long studentId) {
		Student student = studentRepository.findOne(studentId);
		deleteCounter++;
		if (student != null) {
			studentRepository.delete(student);
			sendNotificationOnDeleteStudentOperation(student.getFirstName());
			return "Student deleted successfully";
		} else {
			return "Student not exist";
		}
	}

	public List<StudentCO> searchStudents(NewStudentCO newStudentCO) {
		initializeEntityManager();
		@SuppressWarnings("deprecation")
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Student.class);
		// Criteria criteria = session.createCriteria(Student.class);
		if (newStudentCO.getAddress() != null && newStudentCO.getAddress().length() > 0) {
			criteria = criteria.add(Restrictions.eq("address", newStudentCO.getAddress()));

		}
		if (newStudentCO.getFirstName() != null && newStudentCO.getFirstName().length() > 0) {
			criteria = criteria.add(Restrictions.eq("firstName", newStudentCO.getFirstName()));

		}
		if (newStudentCO.getLastName() != null && newStudentCO.getLastName().length() > 0) {
			criteria = criteria.add(Restrictions.eq("lastName", newStudentCO.getLastName()));

		}
		if (newStudentCO.getStandard() > 0) {
			criteria = criteria.add(Restrictions.eq("standard", newStudentCO.getStandard()));

		}
		@SuppressWarnings("unchecked")
		List<Student> students = criteria.list();
		List<StudentCO> studentsCO = new ArrayList<StudentCO>();
		for (Student student : students) {
			StudentCO studentCO = new StudentCO();
			studentCO.setAddress(student.getAddress());
			studentCO.setFirstName(student.getFirstName());
			studentCO.setLastName(student.getLastName());
			studentCO.setStandard(student.getStandard());
			studentCO.setRollNo(student.getRollNo());
			studentCO.setStudentId(student.getId());
			studentsCO.add(studentCO);
		}
		searchCounter++;
		return studentsCO;
	}

	private void initializeEntityManager() {
		if (entityManager == null) {
			entityManager = entityManagerFactory.createEntityManager();
		}
	}

	// JMX Bean related code

	// This Object will be automatically set by callback setNotificationPublisher()
	private NotificationPublisher publisher;

	@ManagedOperation(description = "operation to test functionality") // It will Expose function as JMX MBean operation
	public boolean testOperation() {
		return true;
	}

	@ManagedAttribute(description = "Logging level of current resource") // It will Expose attribute as JMX MBean readable attribute
	public String getLoggerLevel() {
		return logger.getLevel().toString();
	}

	@ManagedAttribute(description = "Change Logging level of current resource") // It will Expose attribute as JMX MBean writable attribute
	public void setLoggerLevel(String level) {
		logger.setLevel(Level.toLevel(level));
	}

	// This method invoked by itself and provide object NotificationPublisher to send notifications
	@Override
	public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
		this.publisher = notificationPublisher;
	}

	// This is custom method to send notifications
	private void sendNotificationOnDeleteStudentOperation(String firstName) {
		this.publisher.sendNotification(new Notification("Delete", this, 0, "Student : " + firstName + " deleted"));
	}
}
