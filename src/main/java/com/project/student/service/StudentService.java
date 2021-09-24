package com.project.student.service;

import com.project.student.dao.StudentDao;
import com.project.student.exception.StudentNotFoundException;
import com.project.student.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    public List<Student> getAllStudents(){
        List<Student> students = new ArrayList<>();
        for (Student student:studentDao.findAll()){
            students.add(student);
        }
        return students;
    }

    public Student findStudentById(String id){
        Optional<Student> student = studentDao.findById(id);
        if(student.isPresent()){
            return student.get();
        }else {
            log.error("Student not Found for the Id:{}",id);
            throw new StudentNotFoundException("Student Not Found");
        }
    }

    public void createStudent(Student student){
        studentDao.save(student);
    }

}