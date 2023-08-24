package com.te.flinko.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.te.flinko.entity.admin.CompanyInfo;

/**
 * 
 * @author Brunda
 *
 */

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

	Optional<CompanyInfo> findByCompanyId(Long companyId);

	Optional<CompanyInfo> findByCompanyIdAndCompanyName(Long companyId, String companyName);

	Optional<CompanyInfo> findByCompanyIdOrCompanyName(Long companyId, String companyName);

	public CompanyInfo findByCompanyIdAndEmployeePersonalInfoListIsActive(Long companyId, Boolean isActive);

	Optional<CompanyInfo> findByCompanyIdAndIsSubmitedNull(Long companyId);
	
	Optional<CompanyInfo> findByCompanyIdAndIsSubmited(Long companyId, boolean isSubmited);

	Optional<CompanyInfo> findByCompanyName(String companyName);

	public CompanyInfo findByCompanyIdAndCompanyWorkWeekRuleListWorkWeekRuleId(Long companyId, Long workWeekRuleId);

	public Optional<CompanyInfo> findByCompanyIdAndEmployeePersonalInfoListEmployeeInfoIdAndEmployeePersonalInfoListIsActive(
			Long companyId, Long employeeInfoId, Boolean isActive);

	@Query(value = "SELECT c.companyId,c.companyName FROM CompanyInfo c")
	Object[] getAllCompany();

	public CompanyInfo findByCompanyIdAndEmployeePersonalInfoListIsActive(Long companyId, boolean b);

}
