package com.te.flinko.controller.hr;

import static com.te.flinko.common.hr.HrConstants.SALARY_CALC_FAIL;
import static com.te.flinko.common.hr.HrConstants.SALARY_CALC_SUCCESS;
import static com.te.flinko.common.hr.HrConstants.SALARY_UPDATE_SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.hr.EmployeeSalaryAllDetailsDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.EmployeeSalaryCalcService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/salary")
@CrossOrigin(origins = "*")
public class EmployeeSalaryCalcController extends BaseConfigController {

	@Autowired
	EmployeeSalaryCalcService employeeSalaryCalcService;

	@GetMapping("/all")
	public ResponseEntity<SuccessResponse> calcAllSalaryDetails() {

		Boolean status = employeeSalaryCalcService.calculateAllEmployeeSalary();

		SuccessResponse response = new SuccessResponse();
		if (Boolean.FALSE.equals(status)) {
			response.setError(true);
			response.setMessage(SALARY_CALC_FAIL);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} else {
			log.info(SALARY_CALC_SUCCESS);
			response.setError(false);
			response.setMessage(SALARY_CALC_SUCCESS);
			response.setData("");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
	@PostMapping("/edit")
	public ResponseEntity<SuccessResponse> editSalaryDetails(@RequestBody EmployeeSalaryAllDetailsDTO updatedSalaryDetails) {
	//editEmployeeSalary
		EmployeeSalaryAllDetailsDTO employeeSalaryAllDetailsDTO = employeeSalaryCalcService.editEmployeeSalary(updatedSalaryDetails);		
		return new ResponseEntity<>(new SuccessResponse(false, SALARY_UPDATE_SUCCESS, employeeSalaryAllDetailsDTO), HttpStatus.OK);
	}
	
	@GetMapping("/reviseAll")
	public ResponseEntity<SuccessResponse> reviseAllSalaryDetails() {
	//editEmployeeSalary
		Boolean status = employeeSalaryCalcService.calculateReviseSalary();
		return new ResponseEntity<>(new SuccessResponse(false, SALARY_UPDATE_SUCCESS, ""), HttpStatus.OK);
	}
}
