package com.sarwar.test.Api;

import com.sarwar.test.Model.Dto.Request.EmployeeRequest;
import com.sarwar.test.Model.Dto.Response.EmployeeResponse;
import com.sarwar.test.Service.Interfaces.IEmployeeService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
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
    
    @PutMapping("/api/v1/employee/{id}")
    public ResponseEntity<EmployeeResponse> Update(@Valid @RequestBody EmployeeRequest request, @PathVariable Long id){
        EmployeeResponse response = _service.updateEmployee(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }    
    @DeleteMapping("/api/v1/employee/{id}")
    public ResponseEntity<Boolean> Delete(@PathVariable Long id){
        boolean response = _service.deleteEmployee(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
