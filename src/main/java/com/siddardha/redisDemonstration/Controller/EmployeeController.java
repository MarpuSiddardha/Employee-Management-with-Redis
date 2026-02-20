package com.siddardha.redisDemonstration.Controller;

import com.siddardha.redisDemonstration.DTO.EmployeePartialUpdateRequest;
import com.siddardha.redisDemonstration.DTO.EmployeeRequest;
import com.siddardha.redisDemonstration.Mapper.EmployeeMapper;
import com.siddardha.redisDemonstration.Model.Employee;
import com.siddardha.redisDemonstration.Service.EmployeeService;
import com.siddardha.redisDemonstration.Service.RedisLimiterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final RedisLimiterService redisLimiterService;

    public EmployeeController(EmployeeService employeeService, RedisLimiterService redisLimiterService) {
        this.employeeService = employeeService;
        this.redisLimiterService = redisLimiterService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping("/userId/{id}")
    public ResponseEntity<?> findEmployee(@PathVariable Long id, @RequestHeader("X-Users-Id") String userId) {
        if (!redisLimiterService.isAllowed(userId)) {
            Long ttl = redisLimiterService.getRemainingTtl(userId);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded. Please try again after " + ttl + " seconds.");
        }
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @PostMapping
    public ResponseEntity<?> createEmployee( @Valid @RequestBody EmployeeRequest employeeRequest) {
        Employee saved = employeeService.addEmployee(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.mapToResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        return ResponseEntity.status(HttpStatus.FOUND).body(allEmployees);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateEmployee(@PathVariable Long id,  @RequestBody EmployeePartialUpdateRequest employeeRequest) {
        Employee partialUpdatedEmployee = employeeService.partialUpdateEmployee(id, employeeRequest);
        return ResponseEntity.status(HttpStatus.OK).body(EmployeeMapper.mapToResponse(partialUpdatedEmployee));
    }

    @PutMapping(("/{id}"))
    public ResponseEntity<?> replaceEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest employeeRequest ) {
        Employee replacedEmployee = employeeService.replaceEmployee(id, employeeRequest);
        return ResponseEntity.status(HttpStatus.OK).body(EmployeeMapper.mapToResponse(replacedEmployee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.OK).body("Employee deleted successfully");
    }

}
