package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.EmployeeReportingInfo;

@Repository
public interface EmployeeReportingInfoRepository extends JpaRepository<EmployeeReportingInfo,Long>{
	
	List<EmployeeReportingInfo> findByReportingHREmployeeInfoId(Long employeeInfoId);
	
	List<EmployeeReportingInfo> findByReportingManagerEmployeeInfoId(Long employeeInfoId);

	List<EmployeeReportingInfo> findByReportingManagerEmployeeInfoIdAndReportingManagerCompanyInfoCompanyId(Long employeeInfoId, Long companyId);
	
	EmployeeReportingInfo findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);
	
	//List<EmployeeReportingInfo> findByReportingHREmployeeInfoIdAndEmployeeInfoIdReportingHRCompanyInfoCompanyId(Long employeeInfoId, Long companyId);

}
