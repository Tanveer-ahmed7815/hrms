package com.te.flinko.service.hr;

import java.util.List;

import com.te.flinko.dto.employee.EmployeeReviseSalaryDTO;
import com.te.flinko.dto.employee.ReviseSalaryByIdDTO;
import com.te.flinko.dto.hr.UpdateReviseSalaryDTO;

public interface NotificationReviseSalaryService {
	public List<EmployeeReviseSalaryDTO> reviseSalary(Long companyId);

	public ReviseSalaryByIdDTO reviseSalaryById(Long companyId, Long reviseSalaryId);

	public UpdateReviseSalaryDTO updateRevisedsalary(UpdateReviseSalaryDTO reviseSalaryDTO);

}
