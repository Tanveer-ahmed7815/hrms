package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.EmployeeOfficialInfo;


@Repository
public interface EmployeeOfficialInfoRepository extends JpaRepository<EmployeeOfficialInfo, Long> {

	List<EmployeeOfficialInfo> findByEmployeeId(String employeeId);

	List<EmployeeOfficialInfo> findByEmployeeIdAndCompanyBranchInfoCompanyInfoCompanyId(String employeeId,Long companyId);
	
	List<EmployeeOfficialInfo> findByEmployeeIdInAndCompanyBranchInfoCompanyInfoCompanyId(List<String> list1, Long companyId);

	List<EmployeeOfficialInfo> findByCompanyBranchInfoCompanyInfoCompanyIdAndEmployeeIdIn(Long companyId,List<String> list);

	List<EmployeeOfficialInfo> findByEmployeeIdIn(List<String> list);

}
