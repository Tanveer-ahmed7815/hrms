package com.te.flinko.repository.employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.employee.CompanyEmployeeResignationDetails;
import com.te.flinko.entity.employee.EmployeePersonalInfo;

@Repository
public interface EmployeePersonelInfoRepository extends JpaRepository<EmployeePersonalInfo, Long> {

	// @Modifying
	// @Query("from EmployeePersonalInfo")
	// List<EmployeePersonalInfo> findByEmployeeInfoId();
	
List<EmployeePersonalInfo>	findByCompanyInfoCompanyId(Long companyId );

List<EmployeePersonalInfo>	findByCompanyInfoCompanyIdAndEmployeeOfficialInfoDepartmentIn(Long companyId,List<String> department );

EmployeePersonalInfo findByEmployeeOfficialInfoEmployeeId(String employeeId);

}
