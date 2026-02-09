package com.siddardha.redisDemonstration.Repository;

import com.siddardha.redisDemonstration.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
