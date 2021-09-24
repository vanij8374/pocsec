package com.project.student.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Student {
    @Id
    @NotNull(message = "id should not null")
    private String id;

    private String firstName;

    private String lastName;

    private Integer mobileNumber;

    private String email;

    @Valid
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Project> projects = new ArrayList<>();

}
