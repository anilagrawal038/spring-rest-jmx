//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-rest-swagger
//Module						: spring-jpa-rest-swagger
//Package Name					: com.san.repo
//File Name						: StudentRepository.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 11-Mar-2017 7:12:46 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//11-Mar-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.san.domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
