package com.te.flinko.controller.hr;

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
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.HRLeaveApprovalService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/hr/leave-approval")
public class HRLeaveApprovalController extends BaseConfigController {
	
	@Autowired
	private HRLeaveApprovalService leaveApprovalService;

	@GetMapping("/{status}")
	public ResponseEntity<SuccessResponse> getLeaveDetailsByStatus(@PathVariable String status) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Leave approval details")
						.data(leaveApprovalService.getLeaveDetailsByStatus(getCompanyId(), status, getUserId())).build());
	}

	@GetMapping("by-id/{leaveAppliedId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getLeaveDetailsById(@PathVariable Long leaveAppliedId,
			@PathVariable Long employeeInfoId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Leave approval detail")
						.data(leaveApprovalService.getLeaveDetailsById(leaveAppliedId, employeeInfoId, getCompanyId()))
						.build());
	}

	@PutMapping("/{leaveAppliedId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> updateLeaveStatus(@PathVariable Long leaveAppliedId,
			@PathVariable Long employeeInfoId, @RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {
		String result = leaveApprovalService.updateLeaveStatus(getCompanyId(), employeeInfoId,
				leaveAppliedId, getEmployeeId(), adminApprovedRejectDto);
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(result)
						.data(result)
						.build());
	}

}
