package com.te.flinko.service.admin;

import com.te.flinko.dto.admin.LevelsOfApprovalDto;

public interface LevelsOfApprovalService {

	String addLevelsOfApproval(LevelsOfApprovalDto approvalDto,Long companyId);

	LevelsOfApprovalDto getLevelsOfApproval(Long companyId);

}
