package com.te.flinko.controller.hr;

import static com.te.flinko.common.hr.HrConstants.EMPLOYEES_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.SALARY_DETAILS_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.SALARY_RECORDS_NOT_FOUND;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.hr.AddEmployeeTerminationDetailsDTO;
import com.te.flinko.dto.hr.ApproveRequestDTO;
import com.te.flinko.dto.hr.CandidateDetailsDTO;
import com.te.flinko.dto.hr.CandidatesDisplayDetailsDTO;
import com.te.flinko.dto.hr.EmployeeAllDetialsDTO;
import com.te.flinko.dto.hr.EmployeeDisplayDetailsDTO;
import com.te.flinko.dto.hr.EmployeeSalaryAllDetailsDTO;
import com.te.flinko.dto.hr.EmployeeSalaryDetailsDTO;
import com.te.flinko.dto.hr.EmployeeSalaryDetailsListDTO;
import com.te.flinko.dto.hr.ExitEmployeeDetailsDTO;
import com.te.flinko.dto.hr.RejectCandidateRequestDTO;
import com.te.flinko.dto.hr.ResendLinkDTO;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.hr.EmployeeManagementService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/hr/employee")
public class EmployeeManagementController extends BaseConfigController {

	@Autowired
	EmployeeManagementService managementService;

	@GetMapping("/candidates")
	public ResponseEntity<SuccessResponse> getAllCandidateDetails() {
		List<CandidatesDisplayDetailsDTO> candidatesDetails = managementService.getCandidatesDetails(getCompanyId());
		if (candidatesDetails.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(true, "Employee details are not found", null),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(
				new SuccessResponse(false, "All candidates Detials fetched successfully", candidatesDetails),
				HttpStatus.OK);
	}

	@GetMapping("/candidates/info/{id}")
	public ResponseEntity<SuccessResponse> getCandidateDetails(@PathVariable Long id) {
		CandidateDetailsDTO candidateDetails = managementService.getCandidateDetails(id);
		if (candidateDetails == null) {
			return new ResponseEntity<>(new SuccessResponse(true, "Employee details are not found", null),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(
				new SuccessResponse(false, "Fetched the Employee Details Successfully", candidateDetails),
				HttpStatus.OK);
	}

	@PostMapping("/approve")
	public ResponseEntity<SuccessResponse> approveCandidates(@RequestBody ApproveRequestDTO approveRequestDTO) {
		boolean approveCandidates = managementService.approveCandidates(approveRequestDTO, getUserId(),getCompanyId());
		if (!approveCandidates) {
			return new ResponseEntity<>(new SuccessResponse(true, "Account not created", null), HttpStatus.CREATED);
		}
		return new ResponseEntity<>(new SuccessResponse(false, "Employee is approved", approveCandidates),
				HttpStatus.OK);
	}

	@PostMapping("/reject")
	public ResponseEntity<SuccessResponse> rejectCandidate(@RequestBody RejectCandidateRequestDTO candidateRequestDTO) {
		boolean rejectCandidate = managementService.rejectCandidate(candidateRequestDTO);
		if (!rejectCandidate) {
			return new ResponseEntity<>(new SuccessResponse(true, "Account not created", null), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new SuccessResponse(false, "Employee is rejected", rejectCandidate), HttpStatus.OK);
	}

	@PostMapping("/resend")
	public ResponseEntity<SuccessResponse> resendLink(@RequestBody ResendLinkDTO resendLinkDTO) {
		boolean resendLink = managementService.resendLink(resendLinkDTO,getCompanyId(),getUserId());
		if (!resendLink) {
			return new ResponseEntity<>(new SuccessResponse(true, "Account not created", null), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new SuccessResponse(false, "Resent the link", resendLink), HttpStatus.OK);
	}

	@GetMapping("/employee")
	public ResponseEntity<SuccessResponse> getCurrentEmployees() {
		List<EmployeeDisplayDetailsDTO> currentEmployees = managementService.getCurrentEmployees(getCompanyId());
		if (currentEmployees.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(true, "No Employee Details Are Added", null),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new SuccessResponse(false, "Employees Records Fetched", currentEmployees),
				HttpStatus.OK);
	}

	@GetMapping("/status/{employeeId}")
	public ResponseEntity<SuccessResponse> changeActiveStatus(@PathVariable Long employeeId) {
		boolean changeStatus = managementService.changeStatus(employeeId);
		if (!changeStatus) {
			return new ResponseEntity<>(new SuccessResponse(true, "Status not changed", null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new SuccessResponse(false, "Status Changed Successfully", changeStatus),
				HttpStatus.OK);
	}

	@GetMapping("/exitemployees")
	public ResponseEntity<SuccessResponse> getExitEmployees() {
		List<EmployeeDisplayDetailsDTO> exitEmployees = managementService.getExitEmployees(getCompanyId());
		if(exitEmployees==null) {
			return new ResponseEntity<>(new SuccessResponse(true, "Operations to fetch exit employees failed", exitEmployees), HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(false, "Successfully fetched exit employees", exitEmployees),
				HttpStatus.OK);
	}

	@GetMapping("/exitemployees/{id}")
	public ResponseEntity<SuccessResponse> getExitEmployeeDetails(@PathVariable Long id) {
		ExitEmployeeDetailsDTO exitEmployeeDetails = managementService.getExitEmployeeDetails(id);
		if (exitEmployeeDetails == null) {
			return new ResponseEntity<>(new SuccessResponse(true, "Fetching employee Details Failed", null),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(
				new SuccessResponse(false, "Employee Record fetched successfully", exitEmployeeDetails), HttpStatus.OK);
	}

	@PostMapping("/employeeSalarydetails")
	public ResponseEntity<SuccessResponse> employeeSalarydetails(
			@RequestBody EmployeeSalaryDetailsDTO employeeSalaryDetailsDTO) {
		List<EmployeeSalaryDetailsListDTO> employeeSalarydetails = managementService
				.employeeSalarydetails(employeeSalaryDetailsDTO);
		if (employeeSalarydetails.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, SALARY_RECORDS_NOT_FOUND, employeeSalarydetails),HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(
					new SuccessResponse(false, SALARY_DETAILS_FETCHED_SUCCESSFULLY, employeeSalarydetails),
					HttpStatus.OK);
		}
	}

	@GetMapping("/employeeSalarydetailsFindById")
	public ResponseEntity<SuccessResponse> employeeSalarydetailsFindById(@RequestParam Long employeeSalaryId) {
		EmployeeSalaryAllDetailsDTO salaryAllDetailsDTO = managementService
				.employeeSalarydetailsFindById(employeeSalaryId, getCompanyId());
		return new ResponseEntity<>(
				new SuccessResponse(false, SALARY_DETAILS_FETCHED_SUCCESSFULLY, salaryAllDetailsDTO), HttpStatus.OK);
	}

	@GetMapping("/employeedetails/{employeeId}")
	public ResponseEntity<SuccessResponse> getEmployeeAllDetails(@PathVariable Long employeeId) {
		EmployeeAllDetialsDTO allEmployeeDetials = managementService.getAllEmployeeDetials(employeeId);
		if (allEmployeeDetials == null) {
			return new ResponseEntity<>(new SuccessResponse(true, "Fetching employee Details Failed", null),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new SuccessResponse(false, "Employee Record fetched successfully", allEmployeeDetials),HttpStatus.OK);
		
	}
	
	@PostMapping("/employeeTerminationDetails")
	public ResponseEntity<SuccessResponse> employeeTerminationDetails(@RequestBody AddEmployeeTerminationDetailsDTO terminationDetailsDto){
		AddEmployeeTerminationDetailsDTO employeeTerminationDetails = managementService.employeeTerminationDetails(terminationDetailsDto);
		
		return new ResponseEntity<>(new SuccessResponse(false,EMPLOYEES_FETCHED_SUCCESSFULLY, employeeTerminationDetails),
				HttpStatus.OK);

	}
	
	
}

