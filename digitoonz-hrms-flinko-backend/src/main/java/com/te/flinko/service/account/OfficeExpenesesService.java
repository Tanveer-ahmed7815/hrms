package com.te.flinko.service.account;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.te.flinko.dto.account.OfficeExpensesDTO;
import com.te.flinko.dto.account.OfficeExpensesTotalCostDTO;

public interface OfficeExpenesesService {
	
	List<OfficeExpensesTotalCostDTO> getOfficeExpenseDetails(Long companyId);

	OfficeExpensesDTO addOfficeExpenses(OfficeExpensesDTO addOfficeExpensesDTO, MultipartFile multipartFile,Long companyId);

	OfficeExpensesDTO getReimbursementById(Long reimbursementId);

	
}
