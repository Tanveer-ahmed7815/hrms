package com.te.flinko.controller.admin;

import static com.te.flinko.common.admin.LevelsOfApprovalConstants.FETCH_LEVEL_OF_APPROVAL_SUCCESSFULLY;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.admin.LevelsOfApprovalDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.LevelsOfApprovalService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RequestMapping("api/v1/admin")
@RestController
@RequiredArgsConstructor
public class LevelsOfApprovalController extends BaseConfigController {

	private final LevelsOfApprovalService approvalService;
	
	@PostMapping("level-of-approval")
	public ResponseEntity<SuccessResponse> addLevelsOfApproval(@RequestBody @Valid LevelsOfApprovalDto levelsOfApprovalDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(approvalService.addLevelsOfApproval(levelsOfApprovalDto, getCompanyId())).build());
	}

	@GetMapping("level-of-approval")
	public ResponseEntity<SuccessResponse> getLevelsOfApproval() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(FETCH_LEVEL_OF_APPROVAL_SUCCESSFULLY)
						.data(approvalService.getLevelsOfApproval(getCompanyId())).build());
	}

}
