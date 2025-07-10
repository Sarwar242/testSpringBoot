package com.sarwar.test.Model.Dto.Request;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeRequest {
    private String name;
    private int age;
    private String gender;
    private Date dob;
    private String birthPlace;
}
