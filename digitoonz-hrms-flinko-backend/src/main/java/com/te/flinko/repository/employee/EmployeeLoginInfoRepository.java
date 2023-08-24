
package com.te.flinko.repository.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.employee.EmployeeLoginInfo;

/**
 * @author Sahid
 *
 */
public interface EmployeeLoginInfoRepository extends JpaRepository<EmployeeLoginInfo, Long> {

	Optional<EmployeeLoginInfo> findByEmployeeIdAndCurrentPassword(String employeeId, String password);

	Optional<List<EmployeeLoginInfo>> findByEmployeeId(String employeeId);

	Optional<EmployeeLoginInfo> findByEmployeeIdAndEmployeePersonalInfoCompanyInfoCompanyId(String employeeId,
			Long companyId);

	Optional<EmployeeLoginInfo> findByEmployeeIdAndCurrentPasswordAndEmployeePersonalInfoIsActive(String employeeId,
			String password, Boolean isActive);

	EmployeeLoginInfo findByEmployeeIdAndCurrentPasswordAndEmployeePersonalInfoIsActiveFalse(String employeeId,
			String password);

	Optional<EmployeeLoginInfo> findByEmployeeIdAndEmployeePersonalInfoIsActive(String employeeId, Boolean isActive);

	Optional<EmployeeLoginInfo> findByEmployeePersonalInfoEmployeeOfficialInfoOfficialEmailIdAndEmployeePersonalInfoIsActive(
			String emailId, Boolean isActive);

	EmployeeLoginInfo findByEmployeePersonalInfoEmployeeOfficialInfoOfficialEmailIdAndEmployeePersonalInfoIsActiveFalse(
			String emailId);
	
	Optional<EmployeeLoginInfo> findByEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeOfficialInfoOfficialId(Long companyId,Long officialId);
}
