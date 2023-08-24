package com.te.flinko.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.beancopy.BeanCopy;
import com.te.flinko.dto.admin.CompanyInfoDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.CompanyInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Brunda
 *
 */

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyInfoController extends BaseConfigController {

	private final CompanyInfoService companyInfoService;

	@PutMapping
	public ResponseEntity<SuccessResponse> updateCompanyInfo(@RequestPart(required = false) MultipartFile companylogo,
			@RequestParam String data) throws JsonProcessingException {
		log.info("Update Company Information with respect to Id: " + getCompanyId());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(SuccessResponse.builder().error(false)
						.message(companyInfoService
								.updateCompanyInfo(BeanCopy.jsonProperties(data, CompanyInfoDTO.class), companylogo))
						.build());
	}

	@GetMapping
	public ResponseEntity<SuccessResponse> getCompanyInfoDetails() {
		log.info("Fetch Company Information with respect to id: " + getCompanyId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Fetch Company Information")
						.data(companyInfoService.getCompanyInfoDetails(getCompanyId())).build());

	}

}
