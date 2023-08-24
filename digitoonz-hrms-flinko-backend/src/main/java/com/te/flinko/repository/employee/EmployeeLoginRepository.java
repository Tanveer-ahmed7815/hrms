package com.te.flinko.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.employee.EmployeeLoginInfo;

public interface EmployeeLoginRepository extends JpaRepository<EmployeeLoginInfo, Long>{

}
