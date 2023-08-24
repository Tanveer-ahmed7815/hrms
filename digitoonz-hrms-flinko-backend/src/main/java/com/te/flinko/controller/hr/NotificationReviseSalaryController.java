package com.te.flinko.controller.hr;

import static com.te.flinko.common.hr.HrConstants.EMPLOYEES_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.EMPLOYEE_REVISE_SALARY_SUCCESSFULLY_UPDATED;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.dto.employee.EmployeeReviseSalaryDTO;
import com.te.flinko.dto.employee.ReviseSalaryByIdDTO;
import com.te.flinko.dto.hr.UpdateReviseSalaryDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.NotificationReviseSalaryService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/hr/notification")
public class NotificationReviseSalaryController {
	@Autowired
	NotificationReviseSalaryService notificationReviseSalaryRepo;

	@GetMapping("/revise-salary")
	public ResponseEntity<SuccessResponse> reviseSalary(@RequestParam Long companyId){
		List<EmployeeReviseSalaryDTO> reviseSalary = notificationReviseSalaryRepo.reviseSalary(companyId);
		return new ResponseEntity<>(new SuccessResponse(false,EMPLOYEES_FETCHED_SUCCESSFULLY, reviseSalary),
				HttpStatus.OK);

	}
	@GetMapping("/revise-salaryById")
	public ResponseEntity<SuccessResponse> reviseSalaryById(@RequestParam Long companyId,@RequestParam Long reviseSalaryId ){
		ReviseSalaryByIdDTO reviseSalaryById = notificationReviseSalaryRepo.reviseSalaryById(companyId,reviseSalaryId);
		return new ResponseEntity<>(new SuccessResponse(false,EMPLOYEES_FETCHED_SUCCESSFULLY, reviseSalaryById),
				HttpStatus.OK);
	}
@PostMapping("/update-revise-salary")
public ResponseEntity<SuccessResponse> updateRevisedsalary(@RequestBody UpdateReviseSalaryDTO reviseSalaryDTO){
	UpdateReviseSalaryDTO updateRevisedsalary = notificationReviseSalaryRepo.updateRevisedsalary(reviseSalaryDTO);
	return new ResponseEntity<>(new SuccessResponse(false,EMPLOYEE_REVISE_SALARY_SUCCESSFULLY_UPDATED, updateRevisedsalary),
			HttpStatus.OK);
}
}