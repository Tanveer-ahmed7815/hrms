package com.te.flinko.controller.admin;

import static com.te.flinko.common.admin.EmployeeNotificationReferralRewardConstants.EMPLOYEE_REFERRAL_REWARD_FETCH_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeNotificationReferralRewardConstants.FETCH_ALL_EMPLOYEE_REFERRAL_REWARD_SUCCESSFULLY;
import static com.te.flinko.common.admin.EmployeeNotificationReferralRewardConstants.NO_EMPLOYEE_FOUND_FOR_REWARD;

import java.util.Set;

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
import com.te.flinko.dto.admin.EmployeeNotificationReferralRewardDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.AdminNotificationReferralRewardService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RequestMapping("api/v1/admin")
@RestController
@RequiredArgsConstructor
public class EmployeeNotificationReferralRewardController extends BaseConfigController {

	private final AdminNotificationReferralRewardService employeeNotificationReferralRewardService;

	@GetMapping("employee-reward")
	public ResponseEntity<SuccessResponse> getAllEmployeeNotificationReferralReward() {
		Set<EmployeeNotificationReferralRewardDto> allEmployeeNotificationReferralReward = employeeNotificationReferralRewardService
				.getAllEmployeeNotificationReferralReward(getCompanyId());
		if (!allEmployeeNotificationReferralReward.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(SuccessResponse.builder().error(Boolean.FALSE)
							.message(FETCH_ALL_EMPLOYEE_REFERRAL_REWARD_SUCCESSFULLY)
							.data(allEmployeeNotificationReferralReward).build());
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(SuccessResponse.builder().error(Boolean.FALSE)
							.message(NO_EMPLOYEE_FOUND_FOR_REWARD)
							.data(allEmployeeNotificationReferralReward).build());
		}
	}

	@GetMapping("employee-reward/{referenceId}")
	public ResponseEntity<SuccessResponse> getEmployeeNotificationReferralReward(@PathVariable Long referenceId) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(EMPLOYEE_REFERRAL_REWARD_FETCH_SUCCESSFULLY).data(employeeNotificationReferralRewardService
						.getEmployeeNotificationReferralReward(getCompanyId(), referenceId))
				.build());
	}

	@PutMapping("employee-reward")
	public ResponseEntity<SuccessResponse> updateEmployeeNotificationReferralReward(
			@RequestBody EmployeeNotificationReferralRewardDto employeeNotificationReferralRewardDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(employeeNotificationReferralRewardService.updateEmployeeNotificationReferralReward(
								getCompanyId(), employeeNotificationReferralRewardDto))
						.build());
	}

}
