package com.sarwar.test.Service.Interfaces;

import org.springframework.data.domain.Page;

import com.sarwar.test.Model.Dto.Request.EmployeeRequest;
import com.sarwar.test.Model.Dto.Response.EmployeeResponse;

public interface IEmployeeService {
    public EmployeeResponse createEmployee(EmployeeRequest request);
    public Page<EmployeeResponse> getEmployees(int page, int size);
    public EmployeeResponse getEmployeeById(Long id);
    public EmployeeResponse updateEmployee(EmployeeRequest request, Long id);
    public boolean deleteEmployee(Long id);
}
