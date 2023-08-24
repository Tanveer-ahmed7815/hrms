package com.te.flinko.service.hr;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.te.flinko.dto.admin.CompanyPayrollDropdownInfoDTO;
import com.te.flinko.dto.employee.AddEmployeeDocumentDTO;
import com.te.flinko.dto.employee.EmployeeAnnualSalaryDTO;
import com.te.flinko.dto.employee.EmployeeDocumentDTO;
import com.te.flinko.dto.hr.AddEmployeeTerminationDetailsDTO;
import com.te.flinko.dto.hr.AdditionalWorkInformationDTO;
import com.te.flinko.dto.hr.ApproveRequestDTO;
import com.te.flinko.dto.hr.BankInformationDTO;
import com.te.flinko.dto.hr.CandidateDetailsDTO;
import com.te.flinko.dto.hr.CandidatesDisplayDetailsDTO;
import com.te.flinko.dto.hr.DependentInformationDTO;
import com.te.flinko.dto.hr.EmployeeAllDetialsDTO;
import com.te.flinko.dto.hr.EmployeeDisplayDetailsDTO;
import com.te.flinko.dto.hr.EmployeeEducationDetailsDTO;
import com.te.flinko.dto.hr.EmployeeEmploymentDTO;
import com.te.flinko.dto.hr.EmployeeNoticePeriodDTO;
import com.te.flinko.dto.hr.EmployeeReportingResponseDTO;
import com.te.flinko.dto.hr.EmployeeSalaryAllDetailsDTO;
import com.te.flinko.dto.hr.EmployeeSalaryDetailsDTO;
import com.te.flinko.dto.hr.EmployeeSalaryDetailsListDTO;
import com.te.flinko.dto.hr.EmployementInformationDTO;
import com.te.flinko.dto.hr.ExitEmployeeDetailsDTO;
import com.te.flinko.dto.hr.GeneralInformationDTO;
import com.te.flinko.dto.hr.InterviewInformationDTO;
import com.te.flinko.dto.hr.PassAndVisaDTO;
import com.te.flinko.dto.hr.PersonalInformationDTO;
import com.te.flinko.dto.hr.RefrencePersonInfoDTO;
import com.te.flinko.dto.hr.RejectCandidateRequestDTO;
import com.te.flinko.dto.hr.ReportingInformationDTO;
import com.te.flinko.dto.hr.ResendLinkDTO;
import com.te.flinko.dto.hr.ShiftDropDownDTO;
import com.te.flinko.dto.hr.WorkInformationDTO;

public interface EmployeeManagementService {
	
	public List<CandidatesDisplayDetailsDTO> getCandidatesDetails(Long companyId);

	public CandidateDetailsDTO getCandidateDetails(Long id);

	public boolean approveCandidates(ApproveRequestDTO approveRequestDTO,Long userId,Long companyId);

	public boolean rejectCandidate(RejectCandidateRequestDTO candidateRequestDTO);

	public boolean resendLink(ResendLinkDTO resendLinkDTO,Long companyId,Long userId);

	public List<EmployeeDisplayDetailsDTO> getCurrentEmployees(Long componyId);
	
	public GeneralInformationDTO addEmployeePersonalInfo(GeneralInformationDTO object,Long companyId);

	public WorkInformationDTO addWorkInformation(WorkInformationDTO workInformation, Long employeeInfoId,Long companyId);

	public EmployeeReportingResponseDTO mapReportingInformation(ReportingInformationDTO reportingInformation);

	public PersonalInformationDTO addPersonalInformation(PersonalInformationDTO information);

	public List<DependentInformationDTO> addDependentInformation(List<DependentInformationDTO> dependentInformation,Long employeeId, Long companyId);

	public List<EmployeeEmploymentDTO> addEmploymentInformation(List<EmployementInformationDTO> information, Long employeeId);

	public List<EmployeeEducationDetailsDTO> addEducaitonInformation(List<EmployeeEducationDetailsDTO> information, Long employeeId);

	public List<BankInformationDTO> addBankDetailsInfo(List<BankInformationDTO> information, Long employeeId);

	public RefrencePersonInfoDTO addReferenceInfo(RefrencePersonInfoDTO information);

	public PassAndVisaDTO addPassandVisaInfo(PassAndVisaDTO information, Long employeeId);

	//public List<CandidateInterviewInfo> addInterviewInformation(List<InterviewInformationDTO> information, Long employeeId);

	public EmployeeNoticePeriodDTO addNoticePeriodInformation(EmployeeNoticePeriodDTO noticePeriodInformation, Long employeeId,Long companyId);

	public boolean changeStatus(Long employeeId);

	public List<EmployeeDisplayDetailsDTO> getExitEmployees(Long companyId);

	public ExitEmployeeDetailsDTO getExitEmployeeDetails(Long employeeId);

	public List<EmployeeSalaryDetailsListDTO> employeeSalarydetails(EmployeeSalaryDetailsDTO employeeSalaryDetailsDTO);

	public EmployeeSalaryAllDetailsDTO employeeSalarydetailsFindById(Long employeeSalaryId, Long companyId);

//	public List<EmployeeDisplayDetailsDTO> getExitEmployees(Long companyId);

	public EmployeeAllDetialsDTO getAllEmployeeDetials(Long employeeId);
	
	public Boolean addAnnualSalaryInformation(EmployeeAnnualSalaryDTO employeeAnnualSalaryDTO);
	public List<ShiftDropDownDTO> getCompanyShifts(Long companyId);
	
	public AdditionalWorkInformationDTO additionalWorkInformation(
			AdditionalWorkInformationDTO additionalWorkInforamtionDTO) ;

	public AddEmployeeTerminationDetailsDTO employeeTerminationDetails(AddEmployeeTerminationDetailsDTO terminationDetailsDto);

	
	
	public List<CompanyPayrollDropdownInfoDTO> getPayrollInfo(Long companyId);

	/*
	 * AddEmployeeDocumentDTO addEmployeeDocuments(AddEmployeeDocumentDTO
	 * addEmployeeDocumentDTO, Long companyId, Long employeeInfoId);
	 */
	
	List<EmployeeDocumentDTO> addEmployeeDocuments(List<EmployeeDocumentDTO> employeeDocumentDTOList,
			Long companyId, Long employeeInfoId, MultipartFile[] files);
	
	List<InterviewInformationDTO> addInterviewInformation(List<InterviewInformationDTO> informationList,
			Long candidateId);

	AddEmployeeDocumentDTO addEmployeeDocuments(AddEmployeeDocumentDTO addEmployeeDocumentDTO, Long companyId,
			Long employeeInfoId);

	public Map<String, Boolean> getSubmittedAccessory(Long companyId,Long employeeId);

	public List<String> addAccessorySubmitted(List<String> accessory,Long employeeId, Long companyId);
}
