package com.sarwar.test.service.interfaces;

import org.springframework.data.domain.Page;

import com.sarwar.test.model.dto.request.EmployeeRequest;
import com.sarwar.test.model.dto.response.EmployeeResponse;

public interface IEmployeeService {
    public EmployeeResponse createEmployee(EmployeeRequest request);
    public Page<EmployeeResponse> getEmployees(int page, int size);
    public EmployeeResponse getEmployeeById(Long id);
    public EmployeeResponse updateEmployee(EmployeeRequest request);
    public boolean deleteEmployee(Long id);
    public Page<EmployeeResponse> searchEmployees(String name, String gender, Integer age, String birthPlace,
            String dob, int page, int size);
}
