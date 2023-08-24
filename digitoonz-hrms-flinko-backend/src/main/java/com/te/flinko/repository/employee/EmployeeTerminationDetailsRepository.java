package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.employee.EmployeeTerminationDetails;

public interface EmployeeTerminationDetailsRepository extends JpaRepository<EmployeeTerminationDetails, Long>{

	List<EmployeeTerminationDetails> findByEmployeePersonalInfoEmployeeInfoIdAndCompanyInfoCompanyId(Long employeeInfoId,Long companyId);

	List<EmployeeTerminationDetails> findByEmployeePersonalInfoEmployeeInfoId(Long employeeId);

}
