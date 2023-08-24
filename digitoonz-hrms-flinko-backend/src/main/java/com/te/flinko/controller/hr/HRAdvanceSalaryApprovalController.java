package com.te.flinko.controller.hr;

import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.FETCH_ADVANCE_SALARY_DETAILS_WITH_PARTICULAR_EMPLOYEE;
import static com.te.flinko.common.admin.EmployeeAdvanceSalaryConstants.FETCH_ALL_ADVANCE_SALARY_DETAILS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.admin.AdminApprovedRejectDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.HRAdvanceSalaryApprovalService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/hr/advance-salary-approval")
@RequiredArgsConstructor
public class HRAdvanceSalaryApprovalController extends BaseConfigController {

	private final HRAdvanceSalaryApprovalService service;

	@GetMapping("/{status}")
	public ResponseEntity<SuccessResponse> getAllEmployeeAdvanceSalary(@PathVariable String status) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(FETCH_ALL_ADVANCE_SALARY_DETAILS)
						.data(service.getAdvanceSalaryByStatus(getCompanyId(), status, getUserId())).build());
	}

	@GetMapping("by-id/{salaryId}")
	public ResponseEntity<SuccessResponse> getEmployeeAdvanceSalary(@PathVariable Long salaryId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(FETCH_ADVANCE_SALARY_DETAILS_WITH_PARTICULAR_EMPLOYEE)
						.data(service.getEmployeeAdvanceSalary(getCompanyId(), salaryId)).build());
	}

	@PutMapping("/{salaryId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> addEmployeeAdvanceSalary(@PathVariable Long salaryId,
			@PathVariable Long employeeInfoId, @RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {
		return ResponseEntity.status(HttpStatus.OK).body(
				SuccessResponse.builder().error(Boolean.FALSE).message(service.addEmployeeAdvanceSalary(getCompanyId(),
						salaryId, employeeInfoId, getEmployeeId(), adminApprovedRejectDto)).build());
	}

}
