package com.te.flinko.dto.hr;

import java.util.List;

import com.te.flinko.dto.employee.EmployeeAnnualSalaryDTO;
import com.te.flinko.dto.employee.EmployeeDocumentDTO;

import lombok.Data;

@Data
public class EmployeeAllDetialsDTO {
	
	private Long personalInfoId;
	private GeneralInformationDTO generalInformation;
	private WorkInformationDTO workInformation;
	private AdditionalWorkInformationDTO additionalWorkInformation;
	private ReportingInformationDTO reportingInformation;
	private PersonalInformationDTO personalInformation;
	private List<DependentInformationDTO> dependentInformation;
	private List<EmployeeEmploymentDTO> employeeEmployment;
	private List<EmployeeEducationDetailsDTO> educationInformation;
	private List<BankInformationDTO> bankInformation;
	private RefrencePersonInfoDTO refrencePersonInfo;
	private PassAndVisaDTO passAndVisa;
	private List<InterviewInformationDTO> interviewInformation;
	private EmployeeAnnualSalaryDTO employeeAnnualSalary;
	private List<EmployeeNoticePeriodDTO> employeeNoticePeriod;
	private AddEmployeeTerminationDetailsDTO terminationInformation;
	private List<EmployeeDocumentDTO> documents;
	
}
