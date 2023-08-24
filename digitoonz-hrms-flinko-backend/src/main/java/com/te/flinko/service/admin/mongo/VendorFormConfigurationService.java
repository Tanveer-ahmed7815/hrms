package com.te.flinko.service.admin.mongo;

import com.te.flinko.dto.admin.mongo.VendorFormConfigurationDto;

public interface VendorFormConfigurationService {
	String addVendorFormConfiguration(VendorFormConfigurationDto vendorFormConfigurationDto, Long companyId);

	VendorFormConfigurationDto getVendorFormConfiguration(Long companyId);
}
