package com.te.flinko.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.te.flinko.dto.admin.EmployeeLeaveInfoDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.AdminLeaveDetailsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminLeaveApprovalController extends BaseConfigController {

	@Autowired
	private AdminLeaveDetailsService employeeDetailsService;

	@GetMapping("/leave-approvals/{status}")
	public ResponseEntity<SuccessResponse> leaveApprovals(@PathVariable String status) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message("Leave " + status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase()
								+ " Details")
						.data(employeeDetailsService.leaveApprovals(getCompanyId(), status)).build());
	}

	@GetMapping("/leave-approval/{leaveAppliedId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> leaveApproval(@PathVariable Long leaveAppliedId,
			@PathVariable Long employeeInfoId) {
		EmployeeLeaveInfoDTO leaveApproval = employeeDetailsService.leaveApproval(leaveAppliedId, employeeInfoId,
				getCompanyId());
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(leaveApproval.getRejectedReason() == null ? "Leave Approved Detail" : "Leave Rejected Detail")
				.data(leaveApproval).build());
	}

	@PutMapping("/leave-approval/{leaveAppliedId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> employeeLeaveDetails(@PathVariable Long leaveAppliedId,
			@PathVariable Long employeeInfoId, @RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(employeeDetailsService.addEmployeeLeaveDetails(getCompanyId(), employeeInfoId,
								leaveAppliedId, getEmployeeId(), adminApprovedRejectDto))
						.build());
	}

}
