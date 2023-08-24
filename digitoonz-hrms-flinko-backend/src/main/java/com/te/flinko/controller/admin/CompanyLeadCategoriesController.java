package com.te.flinko.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.admin.CompanyLeadCategoriesDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.CompanyLeadCategoriesService;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/company")
public class CompanyLeadCategoriesController extends BaseConfigController {

	@Autowired
	CompanyLeadCategoriesService companyLeadCategoriesService;

	@GetMapping("/lead")
	public ResponseEntity<SuccessResponse> getLead() {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(" Company lead catagories fetched successfully ")
						.data(companyLeadCategoriesService.getLead(getCompanyId())).build());
	}

	@PutMapping("/lead")
	public ResponseEntity<SuccessResponse> updateLead(
			@RequestBody List<CompanyLeadCategoriesDTO> companyLeadCategoriesDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Lead Updated Successfully")
						.data(companyLeadCategoriesService.updateLead(companyLeadCategoriesDto, getCompanyId()))
						.build());
	}

}
