package co.istad.restfulemployeeapi.service.impl;

import co.istad.restfulemployeeapi.domain.Employee;
import co.istad.restfulemployeeapi.dto.CreateEmployeeRequest;
import co.istad.restfulemployeeapi.dto.EmployeeResponse;
import co.istad.restfulemployeeapi.dto.UpdateEmployeeRequest;
import co.istad.restfulemployeeapi.mapper.EmployeeMapper;
import co.istad.restfulemployeeapi.repository.EmployeeRepository;
import co.istad.restfulemployeeapi.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper employeeMapper;

    private static int nextId = 3;

    @Override
    public List<EmployeeResponse> getAll() {
        return repository.getEmployees()
                .stream()
                .map(employeeMapper::toResponse)
                // emp -> mapper.toResponse(emp)
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Integer id) {
        return repository.getEmployees()
                .stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst()
                .map(employeeMapper::toResponse)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Employee with id: %d not found".formatted(id)
                        )
                );
    }
    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest employeeRequest) {

        if (checkIfEmailExists(employeeRequest.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        Employee employee = employeeMapper.fromCreateEmployeeRequest(employeeRequest);
        employee.setId(nextId++);
        employee.setIsActive(Boolean.TRUE);

        repository.getEmployees().add(employee);

        return employeeMapper.toResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployeeById(Integer id, UpdateEmployeeRequest updateEmployeeRequest) {
        Employee employee = repository.getEmployees().stream()
                .filter(emp->emp.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee with id: %d not  found".formatted(id))
                );

        employee.setName(updateEmployeeRequest.name());

        if(updateEmployeeRequest.email() != null){
            String newEmail = updateEmployeeRequest.email();
            if(!newEmail.equals(employee.getEmail()) && checkIfEmailExists(newEmail)){
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Email already exists");
            }
            employee.setEmail(newEmail);
        }

        employee.setSalary(updateEmployeeRequest.salary());

        return employeeMapper.toResponse(employee);
    }

    @Override
    public void deleteEmployeeById(Integer id) {
        Employee employee = repository.getEmployees().stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee with id: %d not found".formatted(id))
                );
        repository.getEmployees().remove(employee);
    }

    @Override
    public List<EmployeeResponse> searchEmployeeByName(String name) {
        return repository.getEmployees().stream()
                .filter(emp -> emp.getName().toLowerCase().contains(name.toLowerCase()))
                .map(employeeMapper::toResponse)
                .toList();
    }


    private boolean checkIfEmailExists(String email) {
        return repository.getEmployees()
                .stream()
                .anyMatch(emp -> emp.getEmail().equals(email));
    }

}

