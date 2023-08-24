package com.te.flinko.controller.hr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.dto.hr.CompanyAddressListDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.CompanyBranchInfoService;

@RestController
@RequestMapping("/hr")
public class CompanyBranchInfoController {

	@Autowired
	CompanyBranchInfoService service;
	
	@GetMapping("/getCompanyAddress/{param}")
	public ResponseEntity<SuccessResponse> getCompanyAddressInfoList(@PathVariable("param") Long branchId ){
		
		List<CompanyAddressListDTO> companyAddressListDTO = service.getCompanyAddressList(branchId);
		
		if(companyAddressListDTO != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(companyAddressListDTO).error(false).message("Data Fetched Succesfully").build(),HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(companyAddressListDTO).error(false).message("Data Fetched Succesfully").build(),HttpStatus.OK);
	}
}
