package com.te.flinko.service.admin;

import java.util.List;

import com.te.flinko.dto.admin.CompanyLeadCategoriesDTO;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public interface CompanyLeadCategoriesService {


	List<CompanyLeadCategoriesDTO> getLead(Long companyId);

	String updateLead(List<CompanyLeadCategoriesDTO> companyLeadCategoriesDtoList, Long companyId);


}
