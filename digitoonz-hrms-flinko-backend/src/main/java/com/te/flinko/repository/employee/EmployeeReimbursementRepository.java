package com.te.flinko.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.te.flinko.entity.employee.EmployeeReimbursementInfo;

@Repository
public interface EmployeeReimbursementRepository extends JpaRepository<EmployeeReimbursementInfo, Long> {

	EmployeeReimbursementInfo  findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyId(Long reimbursementId,Long companyId);
	
	
}
