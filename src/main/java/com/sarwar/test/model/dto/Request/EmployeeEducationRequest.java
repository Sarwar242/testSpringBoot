package com.sarwar.test.model.dto.request;

import lombok.Data;
import com.sarwar.test.model.enums.EducationType;

@Data
public class EmployeeEducationRequest {
    private Long id;
    private EducationType type;
    private String institutionName;
    private String board;
    private String passingYear;
    private String result;
    private String scale;
} 