package com.te.flinko.controller.admindept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.admindept.CompanyHardwareItemsDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admindept.CompanyHardwareItemsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author Brunda
 *
 *
 **/

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/api/v1/admindept")
@RequiredArgsConstructor
public class CompanyHardwareItemsController extends BaseConfigController {

	@Autowired
	CompanyHardwareItemsService companyHardwareItemsService;

	@PostMapping("/hardwareItem")
	public ResponseEntity<SuccessResponse> addHardware(@RequestBody CompanyHardwareItemsDTO companyHardwareItemsDTO) {
		log.info("Create Company Hardware Items with respect to company Id: " + getCompanyId());
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false).message("Created successfully")
				.data(companyHardwareItemsService.addHardware(companyHardwareItemsDTO, getCompanyId())).build());

	}

	@PutMapping("/hardware-item")
	public ResponseEntity<SuccessResponse> updateHardware(
			@RequestBody CompanyHardwareItemsDTO companyHardwareItemsDTO) {
		log.info("Update Company Hardware Items with respect to company Id: " + getCompanyId());
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false).message("Updated successfully")
				.data(companyHardwareItemsService.updateHardware(companyHardwareItemsDTO, getCompanyId())).build());

	}
	
	@PutMapping("allocate/employees") 
	public ResponseEntity<SuccessResponse> updateHardwareItemsEmployeeInfo(
			@RequestBody CompanyHardwareItemsDTO companyHardwareItemsDTO) {
		log.info("Create Company Hardware Items with respect to company Id: " + getCompanyId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Employee Allocated/Deallocated").data(
						companyHardwareItemsService.updateHardwareEmployeeInfo(getCompanyId(), companyHardwareItemsDTO))
						.build());
	}

	@PutMapping("/markNotWorking")
	public ResponseEntity<SuccessResponse> generateIndentification(@RequestBody CompanyHardwareItemsDTO companyHardwareItemsDTO) {
		log.info("Mark Company Hardware as working or not working");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Marked as Working/Not Working")
						.data(companyHardwareItemsService.markNotWorking(getCompanyId(), companyHardwareItemsDTO)).build());

	}
	
	@GetMapping("/hardwareItems")
	public ResponseEntity<SuccessResponse> getHardwareItems() {
		log.info("List of Company Hardware Items");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Company Hardware Items Fetched Successfully!!!")
						.data(companyHardwareItemsService.getAllCompanyHardwareDetails(getCompanyId())).build());

	}
	
	@GetMapping("allsubject/{inOut}/{status}")
	public ResponseEntity<SuccessResponse> getSubjects(@PathVariable String inOut,@PathVariable Integer status) {
		log.info("Subjects List");
		log.info("Mark Company PC/Laptop as working or not working");
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message("List of subjects").data(companyHardwareItemsService.getAllSubjects(getCompanyId(), inOut,status)).build());
	}

	@GetMapping("allproducts/{inOut}/{subjectId}")
	public ResponseEntity<SuccessResponse> getProducts(@PathVariable String inOut, @PathVariable Long subjectId) {
		log.info("Products List");
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message("Products Lists").data(companyHardwareItemsService.getAllProducts(getCompanyId(), subjectId, inOut)).build());
	}

	@GetMapping("/hardwareItem/{indentificationNumber}")
	public ResponseEntity<SuccessResponse> getHardwareItem(@PathVariable String indentificationNumber) {
		log.info("Company Hardware item for given Identification number");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Company Hardware Items Fetched Successfully!!!")
						.data(companyHardwareItemsService.getCompanyHardwareDetails(getCompanyId(),indentificationNumber)).build());
	}
	
	
	@GetMapping("allIndentification")
	public ResponseEntity<SuccessResponse> identificationNumberList() {
		log.info("List of identification number");
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message("List of identification number present in database").data(companyHardwareItemsService.getAllIndentification()).build());
	}
}