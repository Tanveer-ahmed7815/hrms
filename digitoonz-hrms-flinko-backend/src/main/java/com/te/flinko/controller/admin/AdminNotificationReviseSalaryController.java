package com.te.flinko.controller.admin;

import static com.te.flinko.common.hr.HrConstants.EMPLOYEES_FETCHED_SUCCESSFULLY;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.employee.EmployeeReviseSalaryDTO;
import com.te.flinko.dto.employee.ReviseSalaryByIdDTO;
import com.te.flinko.dto.hr.UpdateReviseSalaryDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.AdminNotificationReviseSalaryService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin/notification")
public class AdminNotificationReviseSalaryController extends BaseConfigController {
	@Autowired
	AdminNotificationReviseSalaryService notificationReviseSalaryRepo;

	@GetMapping("/revise-salaries")
	public ResponseEntity<SuccessResponse> reviseSalary() {
		List<EmployeeReviseSalaryDTO> reviseSalary = notificationReviseSalaryRepo.reviseSalary(getCompanyId());
		return new ResponseEntity<>(new SuccessResponse(false, EMPLOYEES_FETCHED_SUCCESSFULLY, reviseSalary),
				HttpStatus.OK);

	}

	@GetMapping("/revise-salary/{reviseSalaryId}")
	public ResponseEntity<SuccessResponse> reviseSalaryById(@PathVariable Long reviseSalaryId) {
		ReviseSalaryByIdDTO reviseSalaryById = notificationReviseSalaryRepo.reviseSalaryById(getCompanyId(),
				reviseSalaryId);
		return new ResponseEntity<>(new SuccessResponse(false, EMPLOYEES_FETCHED_SUCCESSFULLY, reviseSalaryById),
				HttpStatus.OK);
	}

	@PostMapping("/revise-salary")
	public ResponseEntity<SuccessResponse> updateRevisedsalary(@RequestBody UpdateReviseSalaryDTO reviseSalaryDTO) {
		UpdateReviseSalaryDTO updateRevisedsalary = notificationReviseSalaryRepo.updateRevisedsalary(reviseSalaryDTO);
		return new ResponseEntity<>(
				new SuccessResponse(false, "Employee Eevise Salary Updated Successfully", updateRevisedsalary),
				HttpStatus.OK);
	}
}