package com.te.flinko.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.employee.EmployeeBankInfo;

public interface EmployeeBankInfoRepository extends JpaRepository<EmployeeBankInfo, Long> {

}
