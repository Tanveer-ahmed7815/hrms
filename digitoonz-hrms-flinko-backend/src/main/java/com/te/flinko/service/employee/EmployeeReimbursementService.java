package com.te.flinko.service.employee;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.te.flinko.dto.employee.EmployeeReimbursementDTO;
import com.te.flinko.dto.employee.EmployeeReimbursementExpenseCategoryDTO;
import com.te.flinko.entity.employee.EmployeeReimbursementInfo;

public interface EmployeeReimbursementService {

	List<EmployeeReimbursementExpenseCategoryDTO> findByExpenseCategoryId(Long companyId);
	
	EmployeeReimbursementDTO saveEmployeeReimbursement(EmployeeReimbursementDTO reimbursementDTO,Long employeeInfoId,Long companyId);
	
	List<EmployeeReimbursementDTO> getReimbursementDTOList(Long employeeInfoId,Long companyId,String status);
	
	EmployeeReimbursementDTO getEmployeeReimbursement(Long employeeInfoId,Long reimbursementId, Long companyId);
	
	void deleteReimbursementRequest(Long reimbursementId);
	
	EmployeeReimbursementDTO editReimbursementRequest(EmployeeReimbursementDTO reimbursementDTO, Long reimbursementId);
}
