package com.te.flinko.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.EmployeeEducationInfo;

@Repository
public interface EmployeeEducationInfoRepo extends JpaRepository<EmployeeEducationInfo, Long>{

}
