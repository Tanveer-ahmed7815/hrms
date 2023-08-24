package com.te.flinko.service.admin;

/**
 * 
 * @author Brunda
 *
 */

import org.springframework.web.multipart.MultipartFile;

import com.te.flinko.dto.admin.CompanyInfoDTO;


public interface CompanyInfoService {

	CompanyInfoDTO getCompanyInfoDetails(Long companyId) ;

	String updateCompanyInfo(CompanyInfoDTO companyInfoDto, MultipartFile companylogo);
}
