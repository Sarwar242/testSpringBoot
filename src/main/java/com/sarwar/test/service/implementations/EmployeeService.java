package com.sarwar.test.service.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

import com.sarwar.test.model.dto.request.EmployeeRequest;
import com.sarwar.test.model.dto.response.EmployeeResponse;
import com.sarwar.test.model.entity.Employee;
import com.sarwar.test.model.entity.EmployeeEducation;
import com.sarwar.test.repository.EmployeeEducationRepository;
import com.sarwar.test.repository.EmployeeRepository;
import com.sarwar.test.service.interfaces.IEmployeeService;
import org.springframework.stereotype.Service;
import com.sarwar.test.model.dto.request.EmployeeEducationRequest;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository _employeeRepository;
    private EmployeeEducationRepository _employeeEducationRepository;
    private ModelMapper _modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeEducationRepository employeeEducationRepository, ModelMapper modelMapper){
        _employeeRepository=employeeRepository;
        _employeeEducationRepository=employeeEducationRepository;
        _modelMapper=modelMapper;
        _modelMapper.typeMap(EmployeeRequest.class, Employee.class)
            .addMappings(mapper -> mapper.skip(Employee::setId));
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        try {
            // Only keep the first occurrence of each education type
            List<EmployeeEducationRequest> uniqueEducationDetails = new ArrayList<>();
            Set<Object> types = new HashSet<>();
            if (request.getEducationDetails() != null) {
                for (EmployeeEducationRequest eduReq : request.getEducationDetails()) {
                    if (types.add(eduReq.getType())) {
                        uniqueEducationDetails.add(eduReq);
                    }
                }
            }
            Employee newEmployee = _modelMapper.map(request, Employee.class);
            // Map education details if present
            if (!uniqueEducationDetails.isEmpty()) {
                Set<EmployeeEducation> educationEntities = new HashSet<>();
                for (EmployeeEducationRequest eduReq : uniqueEducationDetails) {
                    EmployeeEducation eduEntity = _modelMapper.map(eduReq, EmployeeEducation.class);
                    eduEntity.setEmployee(newEmployee);
                    educationEntities.add(eduEntity);
                }
                newEmployee.setEducationDetails(educationEntities);
            }
            _employeeRepository.save(newEmployee);
            return _modelMapper.map(newEmployee, EmployeeResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<EmployeeResponse> getEmployees(int page, int size) {
        Page<Employee> employees = _employeeRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id")));
        return employees.map(employee -> _modelMapper.map(employee, EmployeeResponse.class));
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id){
        Employee employee = _employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found!"));

        return _modelMapper.map(employee, EmployeeResponse.class);
    }
    
    @Override
    @Transactional
    public EmployeeResponse updateEmployee(EmployeeRequest request) {
        Employee employee = _employeeRepository.findById(request.getId())
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Only keep the first occurrence of each education type
        List<EmployeeEducationRequest> uniqueEducationDetails = new ArrayList<>();
        Set<Object> types = new HashSet<>();
        if (request.getEducationDetails() != null) {
            for (EmployeeEducationRequest eduReq : request.getEducationDetails()) {
                if (types.add(eduReq.getType())) {
                    uniqueEducationDetails.add(eduReq);
                }
            }
        }

        // Manually update mutable fields (never id)
        employee.setName(request.getName());
        employee.setAge(request.getAge());
        employee.setGender(request.getGender());
        employee.setDob(request.getDob());
        employee.setBirthPlace(request.getBirthPlace());

        // Prepare current educations map
        Set<EmployeeEducation> currentEducations = employee.getEducationDetails() != null
            ? employee.getEducationDetails()
            : new HashSet<>();
        Map<Long, EmployeeEducation> currentById = currentEducations.stream()
            .filter(e -> e.getId() != null)
            .collect(Collectors.toMap(EmployeeEducation::getId, e -> e));

        Set<EmployeeEducation> updatedEducations = new HashSet<>();
        for (EmployeeEducationRequest eduReq : uniqueEducationDetails) {
            if (eduReq.getId() != null) {
                // Update existing
                EmployeeEducation existing = currentById.get(eduReq.getId());
                if (existing != null) {
                    // Manually update fields, never id
                    existing.setType(eduReq.getType());
                    existing.setInstitutionName(eduReq.getInstitutionName());
                    existing.setBoard(eduReq.getBoard());
                    existing.setPassingYear(eduReq.getPassingYear());
                    existing.setResult(eduReq.getResult());
                    existing.setScale(eduReq.getScale());
                    updatedEducations.add(existing);
                }
                // else: ignore or throw error if you want strictness
            } else {
                // Add new
                EmployeeEducation newEdu = new EmployeeEducation();
                newEdu.setType(eduReq.getType());
                newEdu.setInstitutionName(eduReq.getInstitutionName());
                newEdu.setBoard(eduReq.getBoard());
                newEdu.setPassingYear(eduReq.getPassingYear());
                newEdu.setResult(eduReq.getResult());
                newEdu.setScale(eduReq.getScale());
                newEdu.setEmployee(employee);
                updatedEducations.add(newEdu);
            }
        }
        // Always clear/add to the existing collection, never replace
        if (employee.getEducationDetails() == null) {
            employee.setEducationDetails(new HashSet<>());
        }
        employee.getEducationDetails().clear();
        employee.getEducationDetails().addAll(updatedEducations);

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
    public boolean deleteEmployeeEducation(Long id) {
        EmployeeEducation employeeEducation = _employeeEducationRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found!"));
        _employeeEducationRepository.delete(employeeEducation);
        
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
