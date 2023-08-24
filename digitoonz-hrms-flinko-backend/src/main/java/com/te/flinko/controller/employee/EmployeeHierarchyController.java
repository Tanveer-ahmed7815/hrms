package com.te.flinko.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.employee.GetReportingManagerDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.employee.EmployeeHierarchyService;

@RestController
@RequestMapping("api/v1/employee-hierarchy")
@CrossOrigin(origins = "*")
public class EmployeeHierarchyController extends BaseConfigController{
	@Autowired
	private EmployeeHierarchyService hierarchyService;

	@GetMapping("/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getHierarchy(@PathVariable Long employeeInfoId) {
		
		GetReportingManagerDTO hierarchy = hierarchyService.getEmployeeHierarchy(employeeInfoId, getCompanyId());
		if (hierarchy != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(hierarchy).error(false).message(null).build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(hierarchy).error(true).message(null).build(), HttpStatus.NOT_FOUND);

		}
	}
}
