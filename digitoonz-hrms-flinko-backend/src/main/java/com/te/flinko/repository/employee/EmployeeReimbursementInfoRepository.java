package com.te.flinko.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.EmployeeReimbursementInfo;

@Repository
public interface EmployeeReimbursementInfoRepository extends JpaRepository<EmployeeReimbursementInfo, Long> {

	Optional<EmployeeReimbursementInfo> findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyId(
			Long reimbursementId, Long companyId);

	Optional<EmployeeReimbursementInfo> findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoId(
			Long employeeReimbursementId, Long companyId, Long employeeInfoId);

	List<EmployeeReimbursementInfo> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyId(String status,
			Long companyId);
	
	List<EmployeeReimbursementInfo> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(String status,
			Long companyId, List<Long> employeeInfoIdList);

	List<EmployeeReimbursementInfo> findByCompanyExpenseCategoriesCompanyInfoCompanyId(Long companyId);

}
