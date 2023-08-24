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
import com.te.flinko.dto.admindept.CompanyPCLaptopDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admindept.CompanyPCLaptopService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Brunda
 *
 */
@CrossOrigin(origins = "*")
@Slf4j
@RequestMapping("/api/v1/admindept")
@RequiredArgsConstructor
@RestController
public class CompanyPCLaptopController extends BaseConfigController {

	@Autowired
	CompanyPCLaptopService pcLaptopService;

	@PostMapping
	public ResponseEntity<SuccessResponse> createCompanyPcLaptopInfo(
			@RequestBody CompanyPCLaptopDTO companyPCLaptopDTO) {
		log.info("Create Company PC/Laptop details with respect to company Id: " + getCompanyId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Company PC/Laptop Details Created Successfully")
						.data(pcLaptopService.createCompanyPCLaptop(getCompanyId(), companyPCLaptopDTO)).build());

	}

	@PutMapping("update")
	public ResponseEntity<SuccessResponse> updateCompanyPcLaptopInfo(
			@RequestBody CompanyPCLaptopDTO companyPCLaptopDTO) {
		log.info("Company PC/Laptop Details updated with respect to company id: "+ getCompanyId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message(" Company PC Laptop details Updated")
						.data(pcLaptopService.updateCompanyPCLaptop(getCompanyId(), companyPCLaptopDTO)).build());
	}

	@PutMapping("allocate/employee") 
	public ResponseEntity<SuccessResponse> updateCompanyPcLaptopEmployeeInfo(
			@RequestBody CompanyPCLaptopDTO companyPCLaptopDTO) {
		log.info("Employee is allocated or deallocated for Company PC/Laptop");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Company PC Laptop is allocated to employee")
						.data(pcLaptopService.updateCompanyPcLaptopEmployeeInfo(getCompanyId(), companyPCLaptopDTO))
						.build());
	}
	
	@PutMapping("/markAsNotWorking")
	public ResponseEntity<SuccessResponse> generateIndentification(@RequestBody CompanyPCLaptopDTO companyPCLaptopDTO) {
		log.info("Mark Company PC/Laptop as working or not working");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Check working or not working")
						.data(pcLaptopService.markNotWorking(getCompanyId(), companyPCLaptopDTO)).build());

	}

	@GetMapping("{serialNumber}")
	public ResponseEntity<SuccessResponse> companyPcLaptop(@PathVariable String serialNumber) {
		log.info("Company PC/Laptop details for the given Serial Number");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false)
						.message("Company PC Laptop details with respect to serial number")
						.data(pcLaptopService.getcompanyPCLaptop(getCompanyId(), serialNumber)).build());

	}

	@GetMapping("serialNumber")
	public ResponseEntity<SuccessResponse> serialNumberList() {
		log.info("List of Serial Number");
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message("List of Serial number ").data(pcLaptopService.getAllSerialNumber()).build());
	}

	@GetMapping
	public ResponseEntity<SuccessResponse> companyPCLaptopDetails() {
		log.info("List of Company PC Laptop details");
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false)
						.message("Company PC Laptop details Lists")
						.data(pcLaptopService.companyPCLaptopDetails(getCompanyId())).build());

	}

	@GetMapping("subject/{inOut}/{status}")
	public ResponseEntity<SuccessResponse> getSubjects(@PathVariable String inOut,@PathVariable Integer status) {
		log.info("Subjects List");
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message("List of subjects").data(pcLaptopService.getAllSubjects(getCompanyId(), inOut,status)).build());
	}

	@GetMapping("products/{inOut}/{subjectId}")
	public ResponseEntity<SuccessResponse> getProducts(@PathVariable String inOut, @PathVariable Long subjectId) {
		log.info("Products List");
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message("Products Lists").data(pcLaptopService.getProducts(getCompanyId(), inOut, subjectId)).build());
	}
	
	
}
