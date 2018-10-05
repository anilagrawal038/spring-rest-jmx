//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-rest-swagger
//Module						: spring-jpa-rest-swagger
//Package Name					: com.san.rest
//File Name						: StudentController.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 11-Mar-2017 7:15:31 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//11-Mar-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.san.co.NewStudentCO;
import com.san.co.StudentCO;
import com.san.service.StudentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "student")
public class StudentController {

	@Autowired
	StudentService studentService;

	@ApiOperation(value = "list student", notes = "list all students")
	@ApiResponses({ @ApiResponse(code = 200, message = "student list fetched successfully") })
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<StudentCO> list() {
		return studentService.list();
	}

	@ApiOperation(value = "add student", notes = "add new student")
	@ApiResponses({ @ApiResponse(code = 201, message = "student added successfully") })
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public StudentCO addStudent(@RequestBody NewStudentCO newStudentCO) {
		System.out.println("Inside addStudent");
		return studentService.addStudent(newStudentCO);
	}

	@ApiOperation(value = "get student", notes = "get student details")
	@ApiResponses({ @ApiResponse(code = 200, message = "student details fetched successfully") })
	@RequestMapping(value = "/{studentId}", method = RequestMethod.GET, produces = "application/json")
	public StudentCO getStudent(@PathVariable long studentId) {
		return studentService.getStudent(studentId);
	}

	@ApiOperation(value = "update student", notes = "update student")
	@ApiResponses({ @ApiResponse(code = 200, message = "student updated successfully") })
	@RequestMapping(value = "/{studentId}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public StudentCO updateStudent(@PathVariable long studentId, @RequestBody NewStudentCO newStudentCO) {
		System.out.println("Inside update student");
		return studentService.updateStudent(studentId, newStudentCO);
	}

	@ApiOperation(value = "delete student", notes = "delete student ")
	@ApiResponses({ @ApiResponse(code = 200, message = "student deleted successfully") })
	@RequestMapping(value = "/{studentId}", method = RequestMethod.DELETE)
	public String deleteStudent(@PathVariable long studentId) {
		return studentService.deleteStudent(studentId);
	}

	@ApiOperation(value = "search students", notes = "search students")
	@ApiResponses({ @ApiResponse(code = 200, message = "student searched successfully") })
	@RequestMapping(method = RequestMethod.PATCH, produces = "application/json")
	public List<StudentCO> searchStudents(@RequestBody NewStudentCO newStudentCO) {
		return studentService.searchStudents(newStudentCO);
	}

}
