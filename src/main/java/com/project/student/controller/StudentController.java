package com.project.student.controller;

import com.project.student.model.Student;
import com.project.student.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("studentservice")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService service;

    @Secured("ROLE_ADMIN")
    @GetMapping("students")
    public ResponseEntity<Object> getAllStudents(){
        List<Student> students = service.getAllStudents();
        if(students.isEmpty()){
            return new ResponseEntity("Students not found",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(students);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("student/{id}")
    public ResponseEntity<Object> findStudentById(@PathVariable String id){
        return ResponseEntity.ok(service.findStudentById(id));
    }

    @Secured({"ROLE_ADMIN","ROLE_STUDENT"})
    @PostMapping("student")
    public ResponseEntity<String> createStudent(@Valid @RequestBody Student student){
        service.createStudent(student);
        return ResponseEntity.ok("Student Created Successfully");
    }
}