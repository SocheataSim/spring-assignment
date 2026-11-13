package co.istad.restfulemployeeapi.service.impl;

import co.istad.restfulemployeeapi.domain.Employee;
import co.istad.restfulemployeeapi.dto.CreateEmployeeRequest;
import co.istad.restfulemployeeapi.dto.EmployeeResponse;
import co.istad.restfulemployeeapi.dto.PatchEmployeeRequest;
import co.istad.restfulemployeeapi.dto.UpdateEmployeeRequest;
import co.istad.restfulemployeeapi.mapper.EmployeeMapper;
import co.istad.restfulemployeeapi.repository.EmployeeRepository;
import co.istad.restfulemployeeapi.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    @Override
    public List<EmployeeResponse> getAll() {
        List<Employee> employees = repository.findAll();
        return employees.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Integer id) {
//        Employee foundEmployee = repository.findById(id).orElseThrow(
//                () -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND,
//                        "Employee has not been found")
//        );
//        return mapper.toResponse(foundEmployee);
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Employee has not been found"
                        )
                );
    }

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest employeeRequest) {
        if(repository.existsByEmail(employeeRequest.email())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email is already exists"
            );
        }

        Employee employee = mapper.fromCreateEmployeeRequest(employeeRequest);
        employee.setIsActive(Boolean.TRUE);

        repository.save(employee);
        return mapper.toResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployeeById(Integer id, UpdateEmployeeRequest updateEmployeeRequest) {
        Employee emp = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee has not been found"
                )
        );
        String newEmail = updateEmployeeRequest.email();
        if(repository.existsByEmail(newEmail)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }
        mapper.updateEntity(emp, updateEmployeeRequest);
        Employee saved = repository.save(emp);
        return mapper.toResponse(saved);
    }

    @Override
    public void deleteEmployeeById(Integer id) {
        if(!repository.existsById(id)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Employee has not been found"
            );
        }
        repository.deleteById(id);
    }

    @Override
    public List<EmployeeResponse> searchEmployeeByName(String name) {
        List<Employee> employees = repository.findAllByNameContainingIgnoreCase(name);
        return employees.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse patchEmployeeById(Integer id, PatchEmployeeRequest patchEmployeeRequest) {
        Employee emp = repository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Employee has not been found"
                        )
                );
        String newEmail = patchEmployeeRequest.email();
        if(repository.existsByEmail(newEmail)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email alredy exists"
            );
        }
        mapper.patchEntity(emp, patchEmployeeRequest);
        Employee saved = repository.save(emp);
        return mapper.toResponse(saved);

    }
}

