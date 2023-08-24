package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.employee.EmployeeSalaryDetails;

public interface EmployeeSalaryDetailsRepository extends JpaRepository<EmployeeSalaryDetails, Long> {

	List<EmployeeSalaryDetails> findByCompanyInfoCompanyIdAndMonthInAndEmployeePersonalInfoEmployeeOfficialInfoDepartmentIn(Long companyId,
			List<Integer> month, List<String> department);

	List<EmployeeSalaryDetails>  findByCompanyInfoCompanyIdAndMonthIn(Long companyId, List<Integer> month);

	List<EmployeeSalaryDetails> findByemployeeSalaryIdAndCompanyInfoCompanyId(Long employeeSalaryId, Long companyId);
	
	List<EmployeeSalaryDetails> findByCompanyInfoCompanyIdAndIsPaidAndYear(Long companyId, Boolean ispaid, Integer year);

}
