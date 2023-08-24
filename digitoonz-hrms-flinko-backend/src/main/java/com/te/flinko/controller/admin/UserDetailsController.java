package com.te.flinko.controller.admin;

import java.util.List;

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
import com.te.flinko.dto.admin.AddExistingEmployeeDataRequestDto;
import com.te.flinko.dto.admin.EmployeeDataDto;
import com.te.flinko.dto.admin.EmployeeOfficialInfoDTO;
import com.te.flinko.dto.admindept.ProductNameDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.UserDetailsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tapas
 *
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/user-management")
@Slf4j
public class UserDetailsController extends BaseConfigController {

	@Autowired
	private UserDetailsService userDetailsService;

	/*
	 * API for Showing All User
	 */
	@GetMapping("employees")
	public ResponseEntity<SuccessResponse> userDetails() {
		log.info("controller method of UserDetailsController class, company id is : {}", getCompanyId());
		List<EmployeeDataDto> userDetails = userDetailsService.userDetails(getCompanyId());
		log.info("userdetails is : {}", userDetails);
		return new ResponseEntity<>(new SuccessResponse(false, "user details", userDetails), HttpStatus.OK);
	}

	/*
	 * API for User Management Details (Find by CompanyId and officialId)
	 **/
	@GetMapping("employee/{officialId}")
	public ResponseEntity<SuccessResponse> userManagementDetails(@PathVariable Long officialId) {
		log.info("controller method of UserDetailsController class, company id is : {} ,officialId is : {} ,  ",
				getCompanyId(), officialId);
		EmployeeOfficialInfoDTO employeeOfficialInfoDTo = userDetailsService.userManagementDetails(getCompanyId(),
				officialId);
		log.info("employee official details : {}", employeeOfficialInfoDTo);
		return new ResponseEntity<>(new SuccessResponse(false, "User Management Details", employeeOfficialInfoDTo),
				HttpStatus.OK);

	}

	/*
	 * API for status active/inactive
	 */
	@PutMapping("employee/{officialId}")
	public ResponseEntity<SuccessResponse> status(@PathVariable Long officialId,
			@RequestBody ProductNameDTO employeeStatusUpdateDTO) {
		log.info(
				"controller method of UserDetailsController class, company id is : {} ,officialId is : {} ,employee official info is : {}  ",
				getCompanyId(), officialId, employeeStatusUpdateDTO);

		String updateStatus = userDetailsService.updateStatus(getCompanyId(), officialId, getEmployeeId(),
				employeeStatusUpdateDTO);

		return new ResponseEntity<>(new SuccessResponse(false, "Status changed successfully", updateStatus),
				HttpStatus.OK);

	}

	/*
	 * API for update user
	 */
	@PutMapping("employeeUpdate/{officialId}")
	public ResponseEntity<SuccessResponse> updateUser(@PathVariable Long officialId,
			@RequestBody EmployeeOfficialInfoDTO employeeOfficialInfoDTO) {
		log.info(
				"controller method of UserDetailsController class, company id is : {} ,officialId is : {} ,employee official info is : {}  ",
				getCompanyId(), officialId, employeeOfficialInfoDTO);

		String updateStatus = userDetailsService.updateUserDetails(getCompanyId(), officialId, getEmployeeId(),
				employeeOfficialInfoDTO);

		return new ResponseEntity<>(new SuccessResponse(false, "Employee details edited successfully", updateStatus),
				HttpStatus.OK);

	}

	// get all data on add existing employee in front end

	@GetMapping("branch")
	public ResponseEntity<SuccessResponse> getAllBranchInfo() {
		log.info("controller method of UserDetailsController class, company id is : {}", getCompanyId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("All branches fetched successfully")
						.data(userDetailsService.getAllBranchInfo(getCompanyId())).build());
	}

	@GetMapping("department")
	public ResponseEntity<SuccessResponse> getAllDepartmentInfo() {
		log.info("controller method of UserDetailsController class, company id is : {}", getCompanyId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("All branches fetched successfully")
						.data(userDetailsService.getAllDepartmentInfo(getCompanyId())).build());
	}

	@GetMapping("designation/{department}")
	public ResponseEntity<SuccessResponse> getAllDesignationInfo(@PathVariable String department) {
		log.info("controller method of UserDetailsController class, company id is : {}", getCompanyId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("All branches fetched successfully")
						.data(userDetailsService.getAllDesignationInfo(getCompanyId(), department)).build());
	}

	@GetMapping("workweekrule")
	public ResponseEntity<SuccessResponse> getAllWorkInfo() {
		log.info("controller method of UserDetailsController class, company id is : {}", getCompanyId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("All branches fetched successfully")
						.data(userDetailsService.getAllWorkInfo(getCompanyId())).build());
	}

	// push all data on add existing employee in database

	@PostMapping("employee")
	public ResponseEntity<SuccessResponse> addExistingEmployee(
			@RequestBody AddExistingEmployeeDataRequestDto addExistingEmployeeDataRequestDto) {
		log.info(
				"controller method of UserDetailsController class, company id is : {} ,employee official info is : {}  ",
				getCompanyId(), addExistingEmployeeDataRequestDto);
		String addExistingEmployee = userDetailsService.addExistingEmployee(getCompanyId(), getEmployeeId(),
				addExistingEmployeeDataRequestDto);
		log.info(" employee details  : {}", addExistingEmployeeDataRequestDto);
		return new ResponseEntity<>(new SuccessResponse(false, "Employee added successfully", addExistingEmployee),
				HttpStatus.OK);
	}

	@GetMapping("employee")
	public ResponseEntity<SuccessResponse> getEmployeeName() {
		return new ResponseEntity<>(new SuccessResponse(false, "All Employee Fetch Successfully",
				userDetailsService.getAllEmployeeName(getCompanyId())), HttpStatus.OK);
	}

}
