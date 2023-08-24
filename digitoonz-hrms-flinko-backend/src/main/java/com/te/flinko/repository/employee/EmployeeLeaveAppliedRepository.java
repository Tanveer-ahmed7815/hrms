package com.te.flinko.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.employee.EmployeeLeaveApplied;

public interface EmployeeLeaveAppliedRepository extends JpaRepository<EmployeeLeaveApplied, Long> {
	List<EmployeeLeaveApplied> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyId(String status, Long companyId);
	
	List<EmployeeLeaveApplied> findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(String status, Long companyId, List<Long> employeeInfoIdList);

	Optional<EmployeeLeaveApplied> findByLeaveAppliedIdAndEmployeePersonalInfoCompanyInfoCompanyIdAndStatus(
			Long leaveAppliedId, Long companyId, String status);

	Optional<EmployeeLeaveApplied> findByLeaveAppliedIdAndEmployeePersonalInfoCompanyInfoCompanyId(Long leaveAppliedId,
			Long companyId);

	Optional<List<EmployeeLeaveApplied>> findByLeaveOfTypeAndStatusInAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
			String leaveOfType, List<String> status, Long employeeInfoId, Long companyId);
	
	Long countByStatusAndEmployeePersonalInfoEmployeeInfoId(String status, Long employeeInfoId);

	Long countByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);

	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);
	
	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoIdOrderByStartDateDesc(Long employeeInfoId);

	List<EmployeeLeaveApplied> findByEmployeePersonalInfoEmployeeInfoIdAndStatusOrderByStartDateDesc(Long employeeInfoId, String status);
	
	Optional<EmployeeLeaveApplied> findByLeaveAppliedIdAndEmployeePersonalInfoEmployeeInfoId(Long leaveAppliedId, Long employeeInfoId);
}
