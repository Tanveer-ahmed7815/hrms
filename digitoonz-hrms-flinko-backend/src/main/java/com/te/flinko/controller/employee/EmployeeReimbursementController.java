package com.te.flinko.controller.employee;

import java.util.List;

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
import com.te.flinko.dto.employee.EmployeeReimbursementDTO;
import com.te.flinko.dto.employee.EmployeeReimbursementExpenseCategoryDTO;
import com.te.flinko.entity.employee.EmployeeReimbursementInfo;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.employee.EmployeeReimbursementService;

@RestController
@RequestMapping("/api/v1/reimbursement")
@CrossOrigin(origins = "*")
public class EmployeeReimbursementController  extends BaseConfigController{

	@Autowired
	EmployeeReimbursementService service;

	@GetMapping("/get-type")
	public ResponseEntity<?> getCategories() {

		List<EmployeeReimbursementExpenseCategoryDTO> categoriesList = service.findByExpenseCategoryId(getCompanyId());
		if (categoriesList != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(categoriesList).error(false)
					.message("data fetched Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(categoriesList).error(true)
					.message("data not available").build(), HttpStatus.OK);
	}

	@PostMapping("/{param}")
	public ResponseEntity<SuccessResponse> saveEmployeeReimbursement(
			@RequestBody EmployeeReimbursementDTO reimbursementDTO, @PathVariable("param") Long employeeInfoId) {
		EmployeeReimbursementDTO employeeReimbursementDTO = service.saveEmployeeReimbursement(reimbursementDTO,
				employeeInfoId,getCompanyId());
		if (employeeReimbursementDTO != null) {

			return new ResponseEntity<>(
					SuccessResponse.builder().data(employeeReimbursementDTO).error(false).message("data saved successfully").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(employeeReimbursementDTO).error(true).message("cannot save the data").build(),
					HttpStatus.OK);
		}
	}

	@GetMapping("/by-status/{employeeInfoId}/{status}")
	public ResponseEntity<SuccessResponse> getReimbursementDTOList(@PathVariable Long employeeInfoId,@PathVariable String status) {

		List<EmployeeReimbursementDTO> reimbursementDTOList = service.getReimbursementDTOList(employeeInfoId,getCompanyId(),status);
		
		if (reimbursementDTOList != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(reimbursementDTOList).error(false)
					.message("Data fetched Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(reimbursementDTOList).error(false)
					.message("Data not available").build(), HttpStatus.OK);
	}
	
	@GetMapping("/{employeeInfoId}/{reimbursementId}")
	public ResponseEntity<SuccessResponse> getReimbursementDetails(@PathVariable Long employeeInfoId,@PathVariable Long reimbursementId) {

		 EmployeeReimbursementDTO employeeReimbursement = service.getEmployeeReimbursement(employeeInfoId, reimbursementId, getCompanyId());
		
		if (employeeReimbursement != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeReimbursement).error(false)
					.message("Data fetched Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeReimbursement).error(false)
					.message("Data not available").build(), HttpStatus.OK);
	}

	@DeleteMapping("/{reimbursementId}")
	public ResponseEntity<SuccessResponse> deleteReimbursementRequest(@PathVariable Long reimbursementId) {

		service.deleteReimbursementRequest(reimbursementId);

		return new ResponseEntity<>(SuccessResponse.builder().data(null).error(false).message("Data Deleted").build(),
				HttpStatus.OK);
	}

	@PutMapping("/{reimbursementId}")
	public ResponseEntity<SuccessResponse> editReimbursement(@RequestBody EmployeeReimbursementDTO reimbursementDTO,
			@PathVariable Long reimbursementId) {
	
		 EmployeeReimbursementDTO reimbursementRequest = service.editReimbursementRequest(reimbursementDTO, reimbursementId);

		if(reimbursementRequest != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(reimbursementRequest).error(false)
					.message("Request edited Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(reimbursementRequest).error(false)
					.message("Cannot edit the request").build(), HttpStatus.OK);
	
	}
}
