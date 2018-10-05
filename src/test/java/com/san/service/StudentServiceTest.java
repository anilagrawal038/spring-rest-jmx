//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-rest-swagger
//Module						: spring-jpa-rest-swagger
//Package Name					: com.san.service
//File Name						: StudentServiceTest.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 12-Mar-2017 12:48:33 AM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//12-Mar-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.san.co.NewStudentCO;
import com.san.co.StudentCO;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { com.san.config.AppConfig.class })
public class StudentServiceTest {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	StudentService studentService;

	@Test
	public void testAddStudent() {
		NewStudentCO studentCO = new NewStudentCO();
		studentCO.setAddress("address");
		studentCO.setFirstName("1 name");
		studentCO.setLastName("L name");
		studentCO.setStandard(4);
		studentCO.setRollNo(123);
		StudentCO student = studentService.addStudent(studentCO);
		assertNotNull(student);
		assertTrue(student.getStudentId() > 0);
	}

	@Test
	public void testList() {
		NewStudentCO studentCO = new NewStudentCO();
		studentCO.setAddress("address");
		studentCO.setFirstName("1 name");
		studentCO.setLastName("L name");
		studentCO.setStandard(4);
		studentCO.setRollNo(123);
		StudentCO student = studentService.addStudent(studentCO);
		assertTrue(studentService.list().size() > 0);
	}

	@Test
	public void testGetStudent() {
		NewStudentCO studentCO = new NewStudentCO();
		studentCO.setAddress("address");
		studentCO.setFirstName("1 name");
		studentCO.setLastName("L name");
		studentCO.setStandard(4);
		studentCO.setRollNo(123);
		StudentCO student = studentService.addStudent(studentCO);
		StudentCO student2 = studentService.getStudent(student.getStudentId());
		assertTrue(student2.getRollNo() == student.getRollNo());
	}

	@Test
	public void testUpdateStudent() {
		NewStudentCO studentCO = new NewStudentCO();
		studentCO.setAddress("address");
		studentCO.setFirstName("1 name");
		studentCO.setLastName("L name");
		studentCO.setStandard(4);
		studentCO.setRollNo(123);
		StudentCO student = studentService.addStudent(studentCO);
		studentCO.setRollNo(12345);
		StudentCO student2 = studentService.updateStudent(student.getStudentId(), studentCO);
		assertTrue(student2.getRollNo() == 12345);
	}

	@Test
	public void testDeleteStudent() {
		NewStudentCO studentCO = new NewStudentCO();
		studentCO.setAddress("address");
		studentCO.setFirstName("1 name");
		studentCO.setLastName("L name");
		studentCO.setStandard(4);
		studentCO.setRollNo(123);
		StudentCO student = studentService.addStudent(studentCO);
		String result = studentService.deleteStudent(student.getStudentId());
		assertEquals("Student deleted successfully", result);
	}

	@Test
	public void testSearchStudents() {
		NewStudentCO studentCO = new NewStudentCO();
		studentCO.setAddress("address unique");
		studentCO.setFirstName("1 name unique");
		studentCO.setLastName("L name unique");
		studentCO.setStandard(99);
		studentCO.setRollNo(9876543);
		StudentCO student = studentService.addStudent(studentCO);
		assertTrue(studentService.searchStudents(studentCO).size() >= 1);
	}

}
