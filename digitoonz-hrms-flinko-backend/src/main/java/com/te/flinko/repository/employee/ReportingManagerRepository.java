package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;

@Repository
public interface ReportingManagerRepository extends JpaRepository<EmployeeReportingInfo, Long>{

	List<EmployeeReportingInfo> findByEmployeePersonalInfo(EmployeePersonalInfo employeePersonalInfo);
	
	List<EmployeeReportingInfo> findByReportingManagerEmployeeInfoId(Long employeeInfoId);
	
	List<EmployeeReportingInfo> findByReportingManagerEmployeeInfoIdAndReportingManagerCompanyInfoCompanyId(Long employeeInfoId, Long companyId);

//	List<EmployeeReportingInfo> findByReportingManagerEmployeeInfoId(Long userId);

	List<EmployeeReportingInfo> findByReportingManagerEmployeeInfoIdAndReportingManagerApprisalMeetingInfoListNotNull(
			Long userId);
	List<EmployeeReportingInfo> findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);
	

}
