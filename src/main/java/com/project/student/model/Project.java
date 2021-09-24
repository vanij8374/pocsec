package com.project.student.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Project {

    @Id
    @NotNull(message = "projectId should not null")
    private String projectId;

    private String projectName;

    private String duration;


}
