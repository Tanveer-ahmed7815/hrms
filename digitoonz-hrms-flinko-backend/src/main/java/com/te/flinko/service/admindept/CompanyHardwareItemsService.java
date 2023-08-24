package com.te.flinko.service.admindept;

import java.util.List;


import com.te.flinko.dto.admindept.CompanyHardwareItemsDTO;
import com.te.flinko.dto.admindept.ProductNameDTO;
import com.te.flinko.dto.admindept.SubjectDTO;

/**
 * 
 * 
 * @author 
 *
 *
 **/

public interface CompanyHardwareItemsService {

	
	CompanyHardwareItemsDTO addHardware(CompanyHardwareItemsDTO companyHardwareItemsDTO, Long companyId);
	
	List<SubjectDTO> getAllSubjects(Long companyId, String inOut,Integer status);
	
	List<ProductNameDTO> getAllProducts(Long companyId, Long subjectId , String inOut);
	
	List<CompanyHardwareItemsDTO> getAllCompanyHardwareDetails (Long companyId);
	
	String updateHardware (CompanyHardwareItemsDTO companyHardwareItemsDTO, Long companyId);

	String updateHardwareEmployeeInfo(Long companyId, CompanyHardwareItemsDTO companyHardwareItemsDTO);
	
	CompanyHardwareItemsDTO getCompanyHardwareDetails(Long companyId, String indentificationNumber);	

	List<String> getAllIndentification();

	String markNotWorking(Long companyId, CompanyHardwareItemsDTO companyHardwareItemsDTO);
	
	
}
