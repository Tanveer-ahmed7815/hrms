package com.te.flinko.repository.admin;

/**
 * 
 * @author Brunda
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.admin.CompanyRuleInfo;

public interface CompanyRuleRepository extends JpaRepository<CompanyRuleInfo,Long> {

//	List<CompanyRuleInfo> findByCompanyInfoCompanyId(Long companyId);
}
