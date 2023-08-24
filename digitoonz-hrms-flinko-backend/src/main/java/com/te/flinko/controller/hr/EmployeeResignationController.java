package com.te.flinko.controller.hr;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.EmployeeManagementService;

@RestController
@RequestMapping("api/v1/resignation")
public class EmployeeResignationController extends BaseConfigController{
	
	@Autowired
	EmployeeManagementService employeeManagementService;
	
	@GetMapping("/accessory/{employeeId}")
	public ResponseEntity<SuccessResponse> getAccessorySubmitted(@PathVariable  Long employeeId){
		Map<String,Boolean> submittedAccessory = employeeManagementService.getSubmittedAccessory(getCompanyId(),employeeId);
		if(submittedAccessory==null) 
			return ResponseEntity.ok().body(SuccessResponse.builder().error(false).message("Data not found").build());
		return ResponseEntity.ok().body(SuccessResponse.builder().error(false).message("Successfully Fetched the Details").data(submittedAccessory).build());
	}
	
	@PostMapping("/accessory/{employeeId}")
	public ResponseEntity<SuccessResponse> addAccessory(@RequestBody List<String> accessory,@PathVariable Long employeeId){
		List<String> addAccessorySubmitted = employeeManagementService.addAccessorySubmitted(accessory,employeeId,getCompanyId());
		if(addAccessorySubmitted.isEmpty())
			return ResponseEntity.ok().body(SuccessResponse.builder().error(false).message("Data not found").build());
		return ResponseEntity.ok().body(SuccessResponse.builder().error(false).message("Successfully Accessory Submitted").data(addAccessorySubmitted).build());
	}

}
