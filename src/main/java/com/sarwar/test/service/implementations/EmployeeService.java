package com.sarwar.test.service.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

import com.sarwar.test.model.dto.request.EmployeeRequest;
import com.sarwar.test.model.dto.response.EmployeeResponse;
import com.sarwar.test.model.entity.Employee;
import com.sarwar.test.repository.EmployeeRepository;
import com.sarwar.test.service.interfaces.IEmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository _employeeRepository;
    private ModelMapper _modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper){
        _employeeRepository=employeeRepository;
        _modelMapper=modelMapper;
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        try {
            Employee newEmployee = new Employee();
            _modelMapper.map(request, Employee.class);

            _employeeRepository.save(newEmployee);

            return _modelMapper.map(newEmployee, EmployeeResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<EmployeeResponse> getEmployees(int page, int size) {
        Page<Employee> employees = _employeeRepository.findAll(PageRequest.of(page, size));
        return employees.map(employee -> _modelMapper.map(employee, EmployeeResponse.class));
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id){
        Employee employee = _employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found!"));

        return _modelMapper.map(employee, EmployeeResponse.class);
    }
    
    @Override
    public EmployeeResponse updateEmployee(EmployeeRequest request, Long id) {
        Employee employee = _employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        _modelMapper.map(request, Employee.class);
        
        _employeeRepository.save(employee);
        return _modelMapper.map(employee, EmployeeResponse.class);
    }
    
    @Override
    public boolean deleteEmployee(Long id) {
        Employee employee = _employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found!"));
        _employeeRepository.delete(employee);
        
        return true;
    }

    @Override
    public Page<EmployeeResponse> searchEmployees(String name, String gender, Integer age, String birthPlace,
            String dob, int page, int size) {
                System.out.println(dob);
        Specification<Employee> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (gender != null && !gender.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("gender")), gender.toLowerCase()));
            }
            if (age != null) {
                predicates.add(cb.equal(root.get("age"), age));
            }
            if (birthPlace != null && !birthPlace.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("birthPlace")), "%" + birthPlace.toLowerCase() + "%"));
            }
            if (dob != null && !dob.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dobDate = sdf.parse(dob);
                    // Calculate start and end of the day
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dobDate);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    Date startOfDay = cal.getTime();

                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    Date endOfDay = cal.getTime();

                    predicates.add(cb.between(root.get("dob"), startOfDay, endOfDay));
                } catch (ParseException e) {
                    // Ignore invalid date format
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Employee> employees = _employeeRepository.findAll(spec, PageRequest.of(page, size));
        return employees.map(employee -> _modelMapper.map(employee, EmployeeResponse.class));
    }
}
