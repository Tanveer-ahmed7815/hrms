package com.te.flinko.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.te.flinko.dto.admin.CompanyWorkWeekRuleNameDTO;
import com.te.flinko.entity.admin.CompanyWorkWeekRule;
import com.te.flinko.responsedto.admin.CompanyWorkWeekRuleResponseDto;


@Repository
public interface CompanyWorkWeekRuleRepository extends JpaRepository<CompanyWorkWeekRule,Long> {

	public 	Optional<CompanyWorkWeekRule>    findByRuleNameAndCompanyInfoCompanyName(String ruleName,String companyName);
	
	public	Optional<CompanyWorkWeekRule> findByWorkWeekRuleIdAndCompanyInfoCompanyId(Long workWeekRuleId,Long companyId);
		
	public	 Optional<CompanyWorkWeekRule> findByRuleNameAndCompanyInfoCompanyId(String ruleName,Long companyId);
		 
		// Optional<CompanyWorkWeekRule> findByWorkWeekRuleIdAndCompanyInfoCompanyId(Long workWeekRuleId,Long companyId);
		 
	public Optional<List<CompanyWorkWeekRule>>	 findByIsDefaultAndCompanyInfoCompanyId(Boolean isDefault,Long companyId);

	public Optional<CompanyWorkWeekRule> findByWorkWeekRuleIdAndIsDefault(Long workWeekRuleId,Boolean isDefault);

	public Optional<List<CompanyWorkWeekRule>> findByCompanyInfoCompanyId(Long companyId);


	@Query(value = "select new com.te.flinko.dto.admin.CompanyWorkWeekRuleNameDTO(cwwr.workWeekRuleId,cwwr.ruleName)from CompanyWorkWeekRule cwwr where cwwr.companyInfo.companyId=?1")
	public Optional<List<CompanyWorkWeekRuleNameDTO>> getWorkWeekByCompanyId(Long companyId);

	@Query(value="select new com.te.flinko.responsedto.admin.CompanyWorkWeekRuleResponseDto(cwwr.workWeekRuleId, cwwr.ruleName, cwwr.isDefault) from CompanyWorkWeekRule cwwr where cwwr.companyInfo.companyId=:companyId")
	Optional<List<CompanyWorkWeekRuleResponseDto>> getAllWorkWeekRule(@Param("companyId") Long companyId);

}
