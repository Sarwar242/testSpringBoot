package com.sarwar.test.model.dto.request;

import lombok.Data;

import java.util.Date;
import java.util.Set;

import jakarta.annotation.Nullable;

@Data
public class EmployeeRequest {
    private Long id;
    private String name;
    private int age;
    private String gender;
    private Date dob;
    private String birthPlace;
    @Nullable
    private Set<EmployeeEducationRequest> educationDetails;
}
