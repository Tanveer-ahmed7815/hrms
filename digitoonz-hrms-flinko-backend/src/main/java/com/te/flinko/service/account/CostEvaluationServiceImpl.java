package com.te.flinko.service.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.account.CostEvaluationDTO;
import com.te.flinko.entity.employee.EmployeeSalaryDetails;
import com.te.flinko.repository.employee.EmployeeSalaryDetailsRepository;

@Service
public class CostEvaluationServiceImpl {
	
	@Autowired
	private EmployeeSalaryDetailsRepository employeeSalaryDetailsRepository;
	
	public void getCostEvaluated(Long companyId) {
		List<CostEvaluationDTO> costEvaluationDTOList = new ArrayList<>();
		List<EmployeeSalaryDetails> employeeSalaryDetails = employeeSalaryDetailsRepository.findByCompanyInfoCompanyIdAndIsPaidAndYear(companyId, true, LocalDate.now().getDayOfYear());
		double sum = employeeSalaryDetails.stream().mapToDouble(employeeSalary -> employeeSalary.getNetPay().doubleValue()).sum();
		costEvaluationDTOList.add(new CostEvaluationDTO("Salary", BigDecimal.valueOf(sum)));
	}
	

}
