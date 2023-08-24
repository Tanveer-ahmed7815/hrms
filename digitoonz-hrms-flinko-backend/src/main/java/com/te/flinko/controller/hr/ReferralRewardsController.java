package com.te.flinko.controller.hr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.dto.hr.ReferralRewardsListDTO;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.ReferralRewardsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/ReferralRewards")
public class ReferralRewardsController {
	@Autowired
	private ReferralRewardsService referralRewardsService;

	@GetMapping("/referenceEmployee/{companyId}")
	ResponseEntity<SuccessResponse> displayReferenceEmployeeList(@PathVariable("companyId") Long companyId) {

		List<ReferralRewardsListDTO> referralRewardsList = referralRewardsService
				.referralRewardseligibleemployee(companyId);

		if (!referralRewardsList.isEmpty()) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(referralRewardsList).error(false).message("Data fetched").build(),
					HttpStatus.OK);
		} else {
			throw new DataNotFoundException("No Reference Employees Found");
		}

	}

	@GetMapping("/getReferenceInfo/{referenceId}")
	ResponseEntity<SuccessResponse> getReferenceInfo(@PathVariable Long referenceId) {
		ReferralRewardsListDTO referalRewardforEmployee = referralRewardsService.referalRewardforEmployee(referenceId);
		if (referalRewardforEmployee != null) {
			return new ResponseEntity<>(SuccessResponse.builder().data(referalRewardforEmployee).error(false)
					.message("Data Fetched").build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(null).error(true).message("Data Not Available").build(),
					HttpStatus.OK);
		}
	}

}
