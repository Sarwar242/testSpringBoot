package com.sarwar.test.model.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class EmployeeResponse {
    private Long id;
    private String name;
    private int age;
    private String gender;
    private Date dob;
    private String birthPlace;
    private Set<EmployeeEducationResponse> educationDetails;
}
