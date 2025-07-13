package com.sarwar.test.Service.Implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.sarwar.test.Model.Dto.Request.EmployeeRequest;
import com.sarwar.test.Model.Dto.Response.EmployeeResponse;
import com.sarwar.test.Model.Entity.Employee;
import com.sarwar.test.Repository.EmployeeRepository;
import com.sarwar.test.Service.Interfaces.IEmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private EmployeeRepository _employeeRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        try {
            Employee newEmployee = new Employee();
            newEmployee.setName(request.getName());
            newEmployee.setAge(request.getAge());
            newEmployee.setGender(request.getGender());
            newEmployee.setBirthPlace(request.getBirthPlace());
            newEmployee.setDob(request.getDob());

            _employeeRepository.save(newEmployee);

            return mapToResponse(newEmployee);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Page<EmployeeResponse> getEmployees(int page, int size) {
        Page<Employee> employees = _employeeRepository.findAll(PageRequest.of(page, size));
        return employees.map(this::mapToResponse);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id){
        Employee employee = _employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found!"));

        return mapToResponse(employee);
    }
    
    @Override
    public EmployeeResponse updateEmployee(EmployeeRequest request, Long id) {
        Employee employee = _employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setName(request.getName());
        employee.setAge(request.getAge());
        employee.setGender(request.getGender());
        employee.setBirthPlace(request.getBirthPlace());
        employee.setDob(request.getDob());
        _employeeRepository.save(employee);
        return mapToResponse(employee);
    }
    
    @Override
    public boolean deleteEmployee(Long id) {
        Employee employee = _employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found!"));
        _employeeRepository.delete(employee);
        
        return true;
    }

    private EmployeeResponse mapToResponse(Employee newEmployee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(newEmployee.getId());
        response.setName(newEmployee.getName());
        response.setAge(newEmployee.getAge());
        response.setGender(newEmployee.getGender());
        response.setBirthPlace(newEmployee.getBirthPlace());
        response.setDob(newEmployee.getDob());
    
        return response;
    }

}
