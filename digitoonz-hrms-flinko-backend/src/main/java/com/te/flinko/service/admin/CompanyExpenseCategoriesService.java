package com.te.flinko.service.admin;

import java.util.List;

import com.te.flinko.dto.admin.CompanyExpenseCategoriesDTO;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public interface CompanyExpenseCategoriesService {

	List<CompanyExpenseCategoriesDTO> getExpense(CompanyExpenseCategoriesDTO companyExpenseCategoriesDto,
			Long companyId);

	String updateExpense(List<CompanyExpenseCategoriesDTO> companyExpenseCategoriesDto,
			Long companyId);

}
