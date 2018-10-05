# spring-rest-jmx
A sample app to demonstrate integration

Some usefull info

#API Documentation URL (Swagger URL) : http://localhost:8080/<app_name>/api/swagger-ui.html

#UnitTest for StudentService is StudentServiceTest.java

#Student APIs are as below :


  #1- List Students
  
        URL : http://localhost:8080/spring-jpa-rest-swagger/api/student
        
        Request : N/A
        
        Method : GET
        
        Response : Array of Student details
        
        
  #2- Add Students
  
        URL : http://localhost:8080/spring-jpa-rest-swagger/api/student
        
        Request : {"rollNo" : 90367,"firstName" : "asdf","lastName": "sdf","standard" : 5,"address":"555"}
        
        Method : POST
        
        Response : Student detail
        
        
  #3- Get Student Detail
  
        URL : http://localhost:8080/spring-jpa-rest-swagger/api/student/3
        
        Request : N/A
        
        Method : GET
        
        Response : {"rollNo" : 90367,"firstName" : "asdf","lastName": "sdf","standard" : 5,"address":"555", "studentId":3}
        
        
  #4- Update Student
  
        URL : http://localhost:8080/spring-jpa-rest-swagger/api/student/4
        
        Request : {"rollNo" : 90367,"firstName" : "asdf","lastName": "sdf","standard" : 5,"address":"555"}
        
        Method : PUT
        
        Response : {"rollNo" : 90367,"firstName" : "asdf","lastName": "sdf","standard" : 5,"address":"555"}
        
        
  #5- Delete Students
  
        URL : http://localhost:8080/spring-jpa-rest-swagger/api/student/3
        
        Request : N/A
        
        Method : DELETE
        
        Response : Student deleted successfully
        
        
  #6- Search Students
  
        URL : http://localhost:8080/spring-jpa-rest-swagger/api/student
        
        Request : {"rollNo" : 90367,"firstName" : "asdf","lastName": "sdf","standard" : 5,"address":"555"}
        
                    Note : In request you may pass only required attributes to search students
                    
        Method : PATCH
        
        Response : Array of Student details
        
        
