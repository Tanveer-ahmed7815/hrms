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
import com.te.flinko.service.reportingmanager.ReportingManagerReimbursementApprovalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/rm/reimbursement-approval")
public class ReportingManagerReimbursementApprovalController extends BaseConfigController {

	private final ReportingManagerReimbursementApprovalService service;

	@GetMapping("/by-status/{status}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getReimbursementByStatus(@PathVariable String status,
			@PathVariable Long employeeInfoId) {

		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false).message("Data Fetched Successfully")
				.data(service.getReimbursementByStatus(getCompanyId(), status, employeeInfoId)).build());
	}
	
	@GetMapping("/{reimbursementId}")
	public ResponseEntity<SuccessResponse> getReimbursementDetails(
			@PathVariable Long reimbursementId) {

		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false).message("Data Fetched Successfully")
				.data(service.getReimbursementDetails(getCompanyId(), reimbursementId)).build());
	}
	
	@PutMapping("/add-response/{reimbursementId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> addResponse(
			@PathVariable Long reimbursementId,@PathVariable Long employeeInfoId,@RequestBody AdminApprovedRejectDto adminApprovedRejectDTO) {

		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false).message("Response Added Successfully")
				.data(service.addEmployeeReimbursementInfo(getCompanyId(), reimbursementId, employeeInfoId, getEmployeeId(), adminApprovedRejectDTO)).build());
	}
}
