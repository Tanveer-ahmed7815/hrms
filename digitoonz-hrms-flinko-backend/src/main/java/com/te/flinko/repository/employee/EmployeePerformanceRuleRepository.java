package com.te.flinko.repository.employee;


import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.employee.EmployeePerformanceRule;

public interface EmployeePerformanceRuleRepository extends JpaRepository<EmployeePerformanceRule, Long> {

}
