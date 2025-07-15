package com.sarwar.test.api;

import com.sarwar.test.model.dto.request.EmployeeRequest;
import com.sarwar.test.model.dto.response.EmployeeResponse;
import com.sarwar.test.service.interfaces.IEmployeeService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private final IEmployeeService _service;

    public EmployeeController(IEmployeeService service) {
        _service = service;
    }

    @GetMapping("/api/v1/employees")
    public ResponseEntity<Page<EmployeeResponse>> Index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<EmployeeResponse> response = _service.getEmployees(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/v1/employee/{id}")
    public ResponseEntity<EmployeeResponse> GetById(@PathVariable Long id){
        EmployeeResponse response = _service.getEmployeeById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/v1/employee")
    public ResponseEntity<EmployeeResponse> Create(@Valid @RequestBody EmployeeRequest request){
        EmployeeResponse response = _service.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }    
    
    @PutMapping("/api/v1/employee")
    public ResponseEntity<EmployeeResponse> Update(@Valid @RequestBody EmployeeRequest request){
        EmployeeResponse response = _service.updateEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }    
    @DeleteMapping("/api/v1/employee/{id}")
    public ResponseEntity<Boolean> Delete(@PathVariable Long id){
        boolean response = _service.deleteEmployee(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }    
    
    @DeleteMapping("/api/v1/employee-education/{id}")
    public ResponseEntity<Boolean> DeleteEducation(@PathVariable Long id){
        boolean response = _service.deleteEmployeeEducation(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/v1/employees/search")
    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String birthPlace,
            @RequestParam(required = false) String dob,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<EmployeeResponse> response = _service.searchEmployees(name, gender, age, birthPlace, dob, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
