package com.te.flinko.service.admindept;

import java.util.List;

import com.te.flinko.dto.admindept.CompanySoftwareDetailsDto;
/**
 * 
 * @author Manish Kumar
 *
 */
public interface CompanySoftwareDetailsService {

	public Boolean createSoftware(CompanySoftwareDetailsDto companySoftwareDetailsDto, Long companyId);

	public List<CompanySoftwareDetailsDto> getAllSoftware(Long companyId);
	
	
	public Boolean updatesoftware(CompanySoftwareDetailsDto companySoftwareDetailsDto, Long companyId, Long softwareId);

}
