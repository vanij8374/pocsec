package com.project.student.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message){
        super(message);
    }
}
