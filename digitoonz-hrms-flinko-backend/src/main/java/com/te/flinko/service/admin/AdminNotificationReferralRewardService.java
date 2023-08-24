package com.te.flinko.service.admin;

import java.util.Set;

import com.te.flinko.dto.admin.EmployeeNotificationReferralRewardDto;

public interface AdminNotificationReferralRewardService {
	
	Set<EmployeeNotificationReferralRewardDto> getAllEmployeeNotificationReferralReward(Long companyId);
	
	EmployeeNotificationReferralRewardDto getEmployeeNotificationReferralReward(Long companyId,Long referenceId);
	
	String updateEmployeeNotificationReferralReward(Long companyId,EmployeeNotificationReferralRewardDto employeeNotificationReferralRewardDto );

}
