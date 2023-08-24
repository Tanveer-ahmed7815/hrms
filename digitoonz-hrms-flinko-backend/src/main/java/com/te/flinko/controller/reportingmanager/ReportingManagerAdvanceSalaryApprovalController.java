package com.te.flinko.controller.reportingmanager;

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
import com.te.flinko.service.reportingmanager.ReportingManagerAdvanceSalaryApprovalService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/rm/advance-salary-approval")
@RequiredArgsConstructor
public class ReportingManagerAdvanceSalaryApprovalController extends BaseConfigController{
	
	private final ReportingManagerAdvanceSalaryApprovalService service;

	@GetMapping("/all/{status}")
	public ResponseEntity<SuccessResponse> getAdavanceSalaryByStatus(@PathVariable String status) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Data Fetched Successfully")
						.data(service.getAdvanceSalaryByStatus(getCompanyId(), status, getUserId())).build());
	}
	
	@GetMapping("/{advanceSalaryId}")
	public ResponseEntity<SuccessResponse> getEmployeeAdvanceSalary(@PathVariable Long advanceSalaryId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Data Fetched Successfully")
						.data(service.getEmployeeAdvanceSalary(getCompanyId(), advanceSalaryId)).build());
	}
	
	@PutMapping("/add-response/{advanceSalaryId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> addEmployeeAdvanceSalary(@PathVariable Long advanceSalaryId,@PathVariable Long employeeInfoId,@RequestBody AdminApprovedRejectDto adminApprovedRejectDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Response Added Sucessfully")
						.data(service.addEmployeeAdvanceSalary( getCompanyId(),advanceSalaryId, employeeInfoId,getEmployeeId(), adminApprovedRejectDTO)).build());
	}
}
