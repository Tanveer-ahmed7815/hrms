package com.te.flinko.service.admin;

import com.te.flinko.dto.admin.CompanyRulesDto;

public interface CompanyRuleService {

	CompanyRulesDto getCompanyRules(Long companyId);

	String updateCompanyRule(Long companyId, CompanyRulesDto companyRulesDto);

}
