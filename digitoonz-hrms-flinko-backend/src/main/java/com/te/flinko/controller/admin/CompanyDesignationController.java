package com.te.flinko.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.admin.CompanyDesignationInfoDto;
import com.te.flinko.dto.admin.DeleteCompanyDesignationDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.CompanyDesignationService;

@CrossOrigin(origins = "*")
/**
 * @author Tanveer Ahmed
 *
 */

@RestController
@RequestMapping("/api/v1/company-info/designation")
public class CompanyDesignationController extends BaseConfigController {

	@Autowired
	private CompanyDesignationService companyDesignationService;

	@PostMapping("/{parentDesignationId}")
	public ResponseEntity<SuccessResponse> addCompanyDesignation(
			@PathVariable long parentDesignationId, @RequestBody CompanyDesignationInfoDto companyDesignationInfoDto) {
			
		return ResponseEntity
				.status(HttpStatus.CREATED).body(SuccessResponse.builder().error(Boolean.FALSE)
						.message("Designation Addedd Successfully").data(companyDesignationService
								.addCompanyDesignation(getCompanyId(), parentDesignationId, companyDesignationInfoDto))
						.build());

	}

	@PutMapping
	public ResponseEntity<SuccessResponse> updateCompanyDesignation(
			@RequestBody CompanyDesignationInfoDto companyDesignationInfoDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Designation Name Update SuccessFully")
						.data(companyDesignationService.updateCompanyDesignation(getCompanyId(), companyDesignationInfoDto))
						.build());

	}

	@GetMapping("/{departmentName}")
	public ResponseEntity<SuccessResponse> getAllDepartmentDesignation(
			@PathVariable String departmentName) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Designations Feteched Successfully")
						.data(companyDesignationService.getAllDepartmentDesignation(getCompanyId(), departmentName))
						.build());

	}

	@DeleteMapping
	public ResponseEntity<SuccessResponse> deleteCompanyDesignation(
			@RequestBody DeleteCompanyDesignationDto deleteCompanyDesignationDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Designation Deleted SuccessFully")
						.data(companyDesignationService.deleteCompanyDesignation(deleteCompanyDesignationDto)).build());

	}

}
