package com.te.flinko.controller.admin.mongo;

import static com.te.flinko.common.admin.mongo.VendorFormConfigurationConstants.FETCH_VENDOR_FORM_CONFIGURATION_SUCCESSFULLY;

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
import com.te.flinko.dto.admin.mongo.VendorFormConfigurationDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.mongo.VendorFormConfigurationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@RequestMapping("api/v1/admin")
@RestController
@Slf4j
@RequiredArgsConstructor
public class VendorFormConfigurationController extends BaseConfigController {

	private final VendorFormConfigurationService formConfigurationService;

	@PostMapping("vendor")
	public ResponseEntity<SuccessResponse> addVendorFormConfiguration(
			@RequestBody @Valid VendorFormConfigurationDto vendorFormConfigurationDto) {
		log.info("The addVendorFormConfiguration Controller Method Begins");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(
						formConfigurationService.addVendorFormConfiguration(vendorFormConfigurationDto, getCompanyId()))
						.build());
	}

	@GetMapping("vendor")
	public ResponseEntity<SuccessResponse> getVendorFormConfiguration() {
		log.info("The getVendorFormConfiguration Controller Method Begins");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(FETCH_VENDOR_FORM_CONFIGURATION_SUCCESSFULLY)
						.data(formConfigurationService.getVendorFormConfiguration(getCompanyId())).build());
	}
}
