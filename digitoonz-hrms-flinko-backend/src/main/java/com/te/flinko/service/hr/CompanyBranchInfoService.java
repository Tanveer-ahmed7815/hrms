package com.te.flinko.service.hr;

import java.util.List;

import com.te.flinko.dto.hr.CompanyAddressListDTO;

public interface CompanyBranchInfoService {

	List<CompanyAddressListDTO> getCompanyAddressList(Long branchId);
}
