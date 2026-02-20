package com.siddardha.redisDemonstration.Service;

import com.siddardha.redisDemonstration.DTO.EmployeePartialUpdateRequest;
import com.siddardha.redisDemonstration.DTO.EmployeeRequest;
import com.siddardha.redisDemonstration.Exception.EmployeeAlreadyExistsException;
import com.siddardha.redisDemonstration.Exception.EmployeeNotFoundException;
import com.siddardha.redisDemonstration.Model.Employee;
import com.siddardha.redisDemonstration.Repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmployeeService {
    private static final Logger log  = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public EmployeeService(EmployeeRepository employeeRepository, RedisTemplate<String, Object> redisTemplate) {
        this.employeeRepository=employeeRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * READ: Cache read-through
     * - On cache hit → returns cached Employee (method body is skipped).
     * - On cache miss → executes, returns result, and caches it under employees::id.
     */
    @Cacheable(cacheNames = "employee", key = "#id")
    public Employee getEmployee(Long id) {
        log.info("Fetching employee with id: " + id);
        // Simulate database lookup - throw exception if not found
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
     return employeeRepository.findById(id)
             .orElseThrow(()-> new EmployeeNotFoundException("Employee not found with id: " + id));
    }


    @CacheEvict(cacheNames = "employees", allEntries = true)
    public Employee addEmployee(EmployeeRequest employeeRequest) {
        if(employeeRepository.existsByEmployeeId(employeeRequest.getEmployeeId())) {
            throw new EmployeeAlreadyExistsException("Employee already exists with employeeId: " + employeeRequest.getEmployeeId());
        }

        Employee emp = new Employee();
        emp.setEmployeeId(employeeRequest.getEmployeeId());
        emp.setName(employeeRequest.getName());
        emp.setRole(employeeRequest.getRole());
        emp.setSalary(employeeRequest.getSalary());
        return employeeRepository.save(emp);
    }

    @Cacheable(cacheNames = "employees", key = "'all-employees'")
    public List<Employee> getAllEmployees() {
        List<Employee> allEmployees = employeeRepository.findAll();
        if(allEmployees.isEmpty()) {
            throw new EmployeeNotFoundException("No Employees Present");
        }
       return allEmployees;
    }


    @Caching(
            put = { @CachePut(cacheNames = "employee", key = "#id") },
            evict =  {@CacheEvict(cacheNames = "employees", allEntries = true) }
    )
    public Employee partialUpdateEmployee(Long id, EmployeePartialUpdateRequest employeeRequest) {

       Employee emp = (Employee) employeeRepository.findById(id)
               .orElseThrow(()-> new EmployeeNotFoundException("There is no employee with this Id :"+id));

       if(employeeRequest.getName()!=null && !employeeRequest.getName().isBlank()) {
           emp.setName(employeeRequest.getName());
       }
        if(employeeRequest.getRole()!=null && !employeeRequest.getRole().isBlank()) {
            emp.setRole(employeeRequest.getRole());
        }
        if(employeeRequest.getSalary()!=null) {
            emp.setSalary(employeeRequest.getSalary());
        }
        return employeeRepository.save(emp);
    }

    @Caching(
            put = { @CachePut(cacheNames = "employee", key = "#id") },
            evict =  {@CacheEvict(cacheNames = "employees", allEntries = true) }
    )
    public Employee replaceEmployee(Long id, EmployeeRequest request) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("No employee with Id: "+id+ "is available . Try updating valid employee"));

        emp.setName(request.getName());
        emp.setRole(request.getRole());
        emp.setSalary(request.getSalary());

        return employeeRepository.save(emp);
    }

    @Caching(
            evict = {
    @CacheEvict(cacheNames = "employee" , key = "#id"),
    @CacheEvict(cacheNames = "employees", allEntries = true) }
    )
    public void deleteEmployee(Long id) {
      Employee emp = employeeRepository.findById(id)
              .orElseThrow(()-> new EmployeeNotFoundException("Employee not present with this id: "+id));
      employeeRepository.delete(emp);
    }
}
