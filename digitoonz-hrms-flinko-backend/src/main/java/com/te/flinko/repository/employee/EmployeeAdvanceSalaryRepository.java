package com.te.flinko.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.EmployeeAdvanceSalary;

@Repository
public interface EmployeeAdvanceSalaryRepository extends JpaRepository<EmployeeAdvanceSalary, Long> {

	Optional<EmployeeAdvanceSalary> findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(
			Long advanceSalaryId, Long companyId);

	Optional<EmployeeAdvanceSalary> findByAdvanceSalaryIdAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
			Long advanceSalaryId, Long employeeInfoId, Long companyId);

	List<EmployeeAdvanceSalary> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyId(String status, Long companyId);

	List<EmployeeAdvanceSalary> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(
			String status, Long companyId, List<Long> employeeInfoIdList);

}
