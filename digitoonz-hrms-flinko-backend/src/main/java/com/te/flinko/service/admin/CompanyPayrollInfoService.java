package com.te.flinko.service.admin;

import com.te.flinko.dto.admin.CompanyPayrollInfoDTO;
import com.te.flinko.dto.admin.CompanyPayrollInfoResponseDTO;

//@author Rakesh Kumar Nayak
public interface CompanyPayrollInfoService  {

	public Boolean createPayrollInfo(CompanyPayrollInfoDTO companyPayrollInfoDto,Long companyId);
	
	public CompanyPayrollInfoResponseDTO getAllCompanyPayrollInfo(Long companyId);
	
	public Boolean updateCompanyPayrollInfo(CompanyPayrollInfoDTO companyPayrollInfoDto,Long companyId);
}
