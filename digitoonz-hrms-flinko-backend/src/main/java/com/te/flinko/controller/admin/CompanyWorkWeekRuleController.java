package com.te.flinko.controller.admin;

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
import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admin.CompanyWorkWeekRuleDTO;
import com.te.flinko.dto.admin.UpdateAllEmpWorkWeekRuleDTO;
import com.te.flinko.dto.admin.UpdateEmployeeWorkWeekDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.CompanyWorkWeekRuleService;

//@author Rakesh Kumar Nayak

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin-company")
public class CompanyWorkWeekRuleController extends BaseConfigController {

	@Autowired
	CompanyWorkWeekRuleService companyWorkWeekRuleService;

	/*
	 * api for creating company work week rule
	 */
	@PostMapping("/work-week-rule")
	public ResponseEntity<SuccessResponse> createCompanyWorkWeekRule(
			@RequestBody(required = false) CompanyWorkWeekRuleDTO companyWorkWeekRuleDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(companyWorkWeekRuleService.createCompWorkWeek(companyWorkWeekRuleDto, getCompanyId())
								.booleanValue() ? AdminConstants.WORK_WEEK_RULE_CREATED_SUCCESS
										: AdminConstants.WORK_WEEK_RULE_CREATED_FAIL)
						.build());
	}
	/*
	 * api for fetching company work week rule
	 */

	@GetMapping("/work-week-rule")
	public ResponseEntity<SuccessResponse> getCompanyWorkWeekRule() {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(AdminConstants.WORK_WEEK_RULE_FETCH_SUCCESS)
						.data(companyWorkWeekRuleService.getAllWorkWeekRule(getCompanyId())).build());

	}

	/*
	 * api for updating company work week rule
	 */
	@PutMapping("/work-week-rule")
	public ResponseEntity<SuccessResponse> updateCompanyWorkWeekRule(
			@RequestBody CompanyWorkWeekRuleDTO companyWorkWeekRuleDto) {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(SuccessResponse.builder().error(Boolean.FALSE).message(
				companyWorkWeekRuleService.updateCompanyWorkweek(companyWorkWeekRuleDto, getCompanyId()).booleanValue()
						? companyWorkWeekRuleDto.getWorkWeekRuleId() == null
								? AdminConstants.WORK_WEEK_RULE_CREATED_SUCCESS
								: AdminConstants.WORK_WEEK_RULE_UPDATE_SUCCESS
						: AdminConstants.WORK_WEEK_RULE_UPDATE_FAIL)
				.build());
	}

	/*
	 * api for deleting company work week rule
	 */
	@DeleteMapping("work-week-rule/{workWeekRuleId}")
	public ResponseEntity<SuccessResponse> deleteCompanyWorkWeekRule(
			@PathVariable(value = "workWeekRuleId") Long workWeekRuleId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(companyWorkWeekRuleService.deleteComanyWorkWeekRule(getCompanyId(), workWeekRuleId)
								.booleanValue() ? AdminConstants.WORK_WEEK_RULE_DELETE_SUCCESS
										: AdminConstants.WORK_WEEK_RULE_DELETE_FAIL)
						.build());
	}

	/*
	 * api for getting all employee personal information
	 */
	@GetMapping("/employees")
	public ResponseEntity<SuccessResponse> getAllEmployeePersonalInfo() {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(AdminConstants.FETCH_ALL_EMPLOYEE_INFO_SUCCESS)
						.data(companyWorkWeekRuleService.getAllEmployeeDetails(getCompanyId())).build());

	}

	/*
	 * api for getting all work week rule name
	 */
	@GetMapping("/work-week-rule/name")
	public ResponseEntity<SuccessResponse> getAllWorkWeekRuleName() {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(AdminConstants.FETCH_ALL_WORK_WEEK_RULE_NAME_SUCCESS)
						.data(companyWorkWeekRuleService.getAllWorkWeekRuleName(getCompanyId())).build());
	}

	@GetMapping("/work-week-rule/employee/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getEmployeeDetailWithId(
			@PathVariable(value = "employeeInfoId") Long employeeInfoId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(AdminConstants.EMPLOYEE_FETCH_SUCCESS)
						.data(companyWorkWeekRuleService.findEmployeeWithId(getCompanyId(), employeeInfoId)).build());
	}
	/*
	 * api for updating employee work week
	 */

	@PutMapping("/work-week-rule/employee/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> updateEmployeeWorkWeek(
			@RequestBody UpdateEmployeeWorkWeekDTO employeeOfficialInfoResponseDto,
			@PathVariable(value = "employeeInfoId") Long employeeInfoId) {

		return ResponseEntity
				.status(HttpStatus.ACCEPTED).body(
						SuccessResponse.builder().error(Boolean.FALSE)
								.message(companyWorkWeekRuleService.updateEmployeeWorkWeek(
										employeeOfficialInfoResponseDto, getCompanyId(), employeeInfoId).booleanValue()
												? AdminConstants.EMPLOYEE_WORK_WEEK_UPDATE_SUCCESS
												: AdminConstants.EMPLOYEE_WORK_WEEK_UPDATE_FAIL)
								.build());
	}
	/*
	 * api for updating all employee work week rule
	 */

	@PutMapping("/work-week-rule/employees")
	public ResponseEntity<SuccessResponse> updateAllEmployeeWorkWeekRule(
			@RequestBody UpdateAllEmpWorkWeekRuleDTO updateAllEmpWorkWeekRuleDto) {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(companyWorkWeekRuleService.updateAllEmpWorkWeek(updateAllEmpWorkWeekRuleDto, getCompanyId())
						.booleanValue() ? AdminConstants.ALL_EMPLOYEE_WORK_WEEK_UPDATE_SUCCESS
								: AdminConstants.ALL_EMPLOYEE_WORK_WEEK_UPDATE_FAIL)
				.build());
	}

}