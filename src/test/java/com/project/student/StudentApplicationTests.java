package com.project.student;

import com.project.student.dao.StudentDao;
import com.project.student.model.Project;
import com.project.student.model.Student;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(classes = StudentApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class StudentApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private StudentDao dao;

	private String server = "http://localhost:";
	private String service = "/studentservice/";

	@BeforeEach
	void setup(){
		dao.deleteAll();
	}

	@Test
	@Order(1)
	void callCreateStudentWhenStudentDataIsNotValid() {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		setStudentUser(httpHeaders);

		Student student = new Student();
		student.setId(null);
		HttpEntity<Student> httpEntity = new HttpEntity<>(student,httpHeaders);
		String url = server+port+service+"student";
		ResponseEntity<JSONObject> error = restTemplate.exchange(url, HttpMethod.POST,httpEntity, JSONObject.class);
		Assertions.assertEquals(400,error.getStatusCodeValue(),"Should be 400 bad request");
	}
	@Test
	@Order(2)
	void callCreateStudentWhenUserIsNotValid() {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		setInVaildUser(httpHeaders);

		Student user = new Student();
		user.setId(null);
		HttpEntity<Student> httpEntity = new HttpEntity<>(user,httpHeaders);
		String url = server+port+service+"student";
		ResponseEntity<JSONObject> error = restTemplate.exchange(url, HttpMethod.POST,httpEntity, JSONObject.class);
		Assertions.assertEquals(401,error.getStatusCodeValue(),"Should be 401 unauthorised");
	}

	@Test
	@Order(3)
	void callCreateStudentWithValidData() {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.setBasicAuth("student","password");
		setStudentUser(httpHeaders);
		HttpEntity<Student> httpEntity = new HttpEntity<>(createStudent(),httpHeaders);
		String url = server+port+service+"student";
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST,httpEntity, String.class);
		Assertions.assertEquals(200,responseEntity.getStatusCodeValue(),"Should be 200 and user created");
	}
	@Test
	@Order(4)
	void callCreateStudentWithValidDataUsingAdminUser() {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		setAdminUser(httpHeaders);
		HttpEntity<Student> httpEntity = new HttpEntity<>(createStudent(),httpHeaders);
		String url = server+port+service+"student";
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST,httpEntity, String.class);
		Assertions.assertEquals(200,responseEntity.getStatusCodeValue(),"Should be 200 and user created");
	}

	@Test
	@Order(5)
	void callAllStudentsWithStudentUser(){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		setStudentUser(httpHeaders);
		HttpEntity<Student> httpEntity = new HttpEntity<>(createStudent(),httpHeaders);
		String url = server+port+service+"students";
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,httpEntity, String.class);
		Assertions.assertEquals(403,responseEntity.getStatusCodeValue(),"Should be 403 and user created");
	}

	@Test
	@Order(6)
	void callAllStudentsWithAdminUser(){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		setAdminUser(httpHeaders);
		HttpEntity<Student> httpEntity = new HttpEntity<>(createStudent(),httpHeaders);
		String url = server+port+service+"students";
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,httpEntity, String.class);
		Assertions.assertEquals(404,responseEntity.getStatusCodeValue(),"Should be 404 and users not found");
	}

	@Test
	@Order(7)
	void callAllStudentsWithAdminUserGotUsers(){
		dao.save(createStudent());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		setAdminUser(httpHeaders);
		HttpEntity<Student> httpEntity = new HttpEntity<>(createStudent(),httpHeaders);
		String url = server+port+service+"students";
		ResponseEntity<List> responseEntity = restTemplate.exchange(url, HttpMethod.GET,httpEntity, List.class);
		Assertions.assertEquals(200,responseEntity.getStatusCodeValue(),"Should be 200 and users found");
		List students = responseEntity.getBody();
		Assertions.assertFalse(students.isEmpty(),"Studets present");
	}

	@Test
	@Order(5)
	void callFindStudentByIdWithStudentUser(){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		setStudentUser(httpHeaders);
		HttpEntity<Student> httpEntity = new HttpEntity<>(createStudent(),httpHeaders);
		String url = server+port+service+"student/id";
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,httpEntity, String.class);
		Assertions.assertEquals(403,responseEntity.getStatusCodeValue(),"Should be 403 and user created");
	}

	@Test
	@Order(6)
	void callFindStudentByIdWithAdminUser(){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		setAdminUser(httpHeaders);
		HttpEntity<Student> httpEntity = new HttpEntity<>(createStudent(),httpHeaders);
		String url = server+port+service+"student/id";
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,httpEntity, String.class);
		Assertions.assertEquals(404,responseEntity.getStatusCodeValue(),"Should be 404 and users not found");
	}

	@Test
	@Order(7)
	void callFindStudentByIdWithAdminUserGotUsers(){
		dao.save(createStudent());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		setAdminUser(httpHeaders);
		HttpEntity<Student> httpEntity = new HttpEntity<>(createStudent(),httpHeaders);
		String url = server+port+service+"student/id";
		ResponseEntity<Student> responseEntity = restTemplate.exchange(url, HttpMethod.GET,httpEntity, Student.class);
		Assertions.assertEquals(200,responseEntity.getStatusCodeValue(),"Should be 200 and users found");
		Student student = responseEntity.getBody();
		Assertions.assertNotNull(student,"Student present");
	}


	private Student createStudent(){
		Student student = new Student();
		student.setId("id");
		student.setFirstName("name");
		student.setLastName("last");
		student.setEmail("s@gmail.com");
		student.setMobileNumber(999999);
		Project project = new Project();
		project.setProjectId("proid");
		project.setDuration("3 months");
		project.setProjectName("proname");

		student.getProjects().add(project);
		return student;
	}

	private void setStudentUser(HttpHeaders httpHeaders){
		httpHeaders.setBasicAuth("student","password");
	}

	private void setInVaildUser(HttpHeaders httpHeaders){
		httpHeaders.setBasicAuth("stud","p");
	}

	private void setAdminUser(HttpHeaders httpHeaders){
		httpHeaders.setBasicAuth("admin","admin");
	}

}
