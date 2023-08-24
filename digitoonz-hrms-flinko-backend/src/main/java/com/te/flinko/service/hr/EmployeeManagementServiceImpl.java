package com.te.flinko.service.hr;

import static com.te.flinko.common.hr.HrConstants.COMPANY_INFORMATION_NOT_PRESENT;
import static com.te.flinko.common.hr.HrConstants.COMPANY_PAYROLL_INFORMATION_NOT_PRESENT;
import static com.te.flinko.common.hr.HrConstants.REIMBURSEMENT_PATTERN;
import static com.te.flinko.common.hr.HrConstants.SALARY_RECORDS_NOT_FOUND;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.te.flinko.dto.admin.CompanyPayrollDropdownInfoDTO;
import com.te.flinko.dto.admin.CompanyPayrollInfoResponseDTO;
import com.te.flinko.dto.employee.AddEmployeeDocumentDTO;
import com.te.flinko.dto.employee.EmployeeAnnualSalaryDTO;
import com.te.flinko.dto.employee.EmployeeBonusDTO;
import com.te.flinko.dto.employee.EmployeeDocumentDTO;
import com.te.flinko.dto.employee.EmployeeVariablePayDTO;
import com.te.flinko.dto.employee.MailDto;
import com.te.flinko.dto.hr.AddEmployeeTerminationDetailsDTO;
import com.te.flinko.dto.hr.AddVisaInfoDTO;
import com.te.flinko.dto.hr.AdditionalWorkInformationDTO;
import com.te.flinko.dto.hr.ApproveRequestDTO;
import com.te.flinko.dto.hr.BankInformationDTO;
import com.te.flinko.dto.hr.CandidateDetailsDTO;
import com.te.flinko.dto.hr.CandidatesDisplayDetailsDTO;
import com.te.flinko.dto.hr.DependentInformationDTO;
import com.te.flinko.dto.hr.EmployeeAddressInfoDTO;
import com.te.flinko.dto.hr.EmployeeAllDetialsDTO;
import com.te.flinko.dto.hr.EmployeeDependantInfoDetailsDTO;
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
import com.te.flinko.dto.hr.OrganiserDetialsDTO;
import com.te.flinko.dto.hr.PassAndVisaDTO;
import com.te.flinko.dto.hr.PersonalInformationDTO;
import com.te.flinko.dto.hr.ReferencePersonDTO;
import com.te.flinko.dto.hr.RefrencePersonInfoDTO;
import com.te.flinko.dto.hr.RejectCandidateRequestDTO;
import com.te.flinko.dto.hr.ReportingInformationDTO;
import com.te.flinko.dto.hr.ResendLinkDTO;
import com.te.flinko.dto.hr.ShiftDropDownDTO;
import com.te.flinko.dto.hr.WorkInformationDTO;
import com.te.flinko.entity.Department;
import com.te.flinko.entity.admin.CompanyBranchInfo;
import com.te.flinko.entity.admin.CompanyDesignationInfo;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyPayrollInfo;
import com.te.flinko.entity.admin.CompanyShiftInfo;
import com.te.flinko.entity.employee.CompanyEmployeeResignationDetails;
import com.te.flinko.entity.employee.EmployeeAddressInfo;
import com.te.flinko.entity.employee.EmployeeAnnualSalary;
import com.te.flinko.entity.employee.EmployeeBankInfo;
import com.te.flinko.entity.employee.EmployeeBonus;
import com.te.flinko.entity.employee.EmployeeDependentInfo;
import com.te.flinko.entity.employee.EmployeeEducationInfo;
import com.te.flinko.entity.employee.EmployeeEmploymentInfo;
import com.te.flinko.entity.employee.EmployeeLoginInfo;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReferenceInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.entity.employee.EmployeeResignationDiscussion;
import com.te.flinko.entity.employee.EmployeeSalaryDetails;
import com.te.flinko.entity.employee.EmployeeTerminationDetails;
import com.te.flinko.entity.employee.EmployeeVariablePay;
import com.te.flinko.entity.employee.EmployeeVisaInfo;
import com.te.flinko.entity.employee.mongo.EmployeeDocumentDetails;
import com.te.flinko.entity.hr.CandidateInfo;
import com.te.flinko.entity.hr.CandidateInterviewInfo;
import com.te.flinko.entity.hr.mongo.CompanyChecklistDetails;
import com.te.flinko.exception.CompanyIdNotFoundException;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.exception.hr.CompanyNotFoundException;
import com.te.flinko.exception.hr.CustomExceptionForHr;
import com.te.flinko.exception.hr.OfficalIdInUseException;
import com.te.flinko.repository.DepartmentRepository;
import com.te.flinko.repository.admin.CompanyBranchInfoRepository;
import com.te.flinko.repository.admin.CompanyDesignationInfoRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.CompanyPayRollInfoRepository;
import com.te.flinko.repository.admin.CompanyShiftInfoRepository;
import com.te.flinko.repository.employee.CompanyEmployeeResignationDetailsRepository;
import com.te.flinko.repository.employee.EmployeeAddressInfoRepository;
import com.te.flinko.repository.employee.EmployeeAnnualSalaryRepository;
import com.te.flinko.repository.employee.EmployeeBankInfoRepository;
import com.te.flinko.repository.employee.EmployeeDependentInfoRepository;
import com.te.flinko.repository.employee.EmployeeEducationInfoRepo;
import com.te.flinko.repository.employee.EmployeeEmploymentInfoRepo;
import com.te.flinko.repository.employee.EmployeeLoginInfoRepository;
import com.te.flinko.repository.employee.EmployeeOfficialInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReferenceInfoRepository;
import com.te.flinko.repository.employee.EmployeeReportingInfoRepository;
import com.te.flinko.repository.employee.EmployeeSalaryDetailsRepository;
import com.te.flinko.repository.employee.EmployeeTerminationDetailsRepository;
import com.te.flinko.repository.employee.EmployeeVisaInfoRepository;
import com.te.flinko.repository.employee.mongo.EmployeeDocumentDetailsRepository;
import com.te.flinko.repository.hr.CandidateInfoRepository;
import com.te.flinko.repository.hr.CandidateInterviewInfoRepository;
import com.te.flinko.repository.hr.mongo.CompanyChecklistDetailsRepository;
import com.te.flinko.service.mail.employee.EmailService;
import com.te.flinko.util.S3UploadFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeManagementServiceImpl implements EmployeeManagementService {

	@Autowired
	EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	EmployeeLoginInfoRepository employeeLoginRepository;

	@Autowired
	EmployeeOfficialInfoRepository employeeOfficialInfoRepository;

	@Autowired
	EmployeeReportingInfoRepository employeeReportingInfoRepo;

	@Autowired
	EmployeeAddressInfoRepository employeeddressInfoRepo;

	@Autowired
	EmployeeEmploymentInfoRepo employeeEmploymentInfoRepo;

	@Autowired
	EmployeeVisaInfoRepository employeeVisaInfoRepository;

	@Autowired
	EmployeeDependentInfoRepository dependentInfoRepo;

	@Autowired
	EmployeeEducationInfoRepo employeeEducationInfoRepo;

	@Autowired
	EmployeeBankInfoRepository employeeBankInfoRepository;

	@Autowired
	EmployeeReferenceInfoRepository employeeReferenceInfoRepo;

	@Autowired
	CandidateInterviewInfoRepository candidateInterviewInfoRepo;

	@Autowired
	CandidateInfoRepository candidateInfoRepo;

	@Autowired
	CompanyInfoRepository companyInfoRepo;

	@Autowired
	CompanyEmployeeResignationDetailsRepository resignationDetailsRepo;

	@Autowired
	CompanyBranchInfoRepository branchInfoRepo;

	@Autowired
	EmployeeAnnualSalaryRepository annualSalaryRepo;
	@Autowired
	EmployeeSalaryDetailsRepository salaryDetailsRepo;
	@Autowired
	DepartmentRepository departmentRepo;

	@Autowired
	CompanyPayRollInfoRepository payRollInfoRepo;

	@Autowired
	private S3UploadFile uploadFile;

	@Autowired
	private EmployeeDocumentDetailsRepository employeeDocumentRepository;

	@Autowired
	private CompanyShiftInfoRepository companyShiftInfoRepository;
	@Autowired
	private EmployeeTerminationDetailsRepository terminationDetailsRepo;

	@Autowired
	private CompanyDesignationInfoRepository companyDesignationInfoRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private EmployeeLoginInfoRepository employeeLoginInfoRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CandidateManagementService candidateManagementService;

	@Autowired
	private CompanyChecklistDetailsRepository checklistDetailsRepository;

	@Override
	public List<CandidatesDisplayDetailsDTO> getCandidatesDetails(Long companyId) {
		List<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepository.findByCompanyInfoCompanyId(companyId);
		List<EmployeePersonalInfo> filteredEmployee = employeeInfo.stream().filter(i -> i.getStatus().isEmpty())
				.collect(Collectors.toList());
		List<CandidatesDisplayDetailsDTO> listCandidatesDetailsDTO = new ArrayList<>();
		filteredEmployee.stream().forEach((EmployeePersonalInfo e) -> {
			CandidatesDisplayDetailsDTO candidatesDisplayDetailsDTO = new CandidatesDisplayDetailsDTO();
			candidatesDisplayDetailsDTO.setCandidateId(e.getEmployeeInfoId());
			candidatesDisplayDetailsDTO.setFullName(e.getFirstName() + " " + e.getLastName());
			candidatesDisplayDetailsDTO.setEmailId(e.getEmailId());
			if (e.getEmployeeOfficialInfo() != null)
				candidatesDisplayDetailsDTO.setDesignation(e.getEmployeeOfficialInfo().getDesignation());
			if (e.getCandidateInfo() != null)
				candidatesDisplayDetailsDTO.setYearOfExperience(e.getCandidateInfo().getYearOfExperience());
			candidatesDisplayDetailsDTO.setMobileNumber(e.getMobileNumber());
			listCandidatesDetailsDTO.add(candidatesDisplayDetailsDTO);
		});
		return listCandidatesDetailsDTO.stream()
				.sorted(Comparator.comparingLong(CandidatesDisplayDetailsDTO::getCandidateId).reversed())
				.collect(Collectors.toList());

	}

	@Override
	@Transactional
	public CandidateDetailsDTO getCandidateDetails(Long id) {
		Optional<EmployeePersonalInfo> findById = employeePersonalInfoRepository.findById(id);
		CandidateDetailsDTO candidateDetailsDTO = new CandidateDetailsDTO();

		if (!findById.isPresent()) {
			throw new DataNotFoundException("Candidate Details does not exist");
		}

		EmployeePersonalInfo employeePersonalInfo = findById.get();
		candidateDetailsDTO.setFullName(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName());
		candidateDetailsDTO.setEmployeeId(employeePersonalInfo.getEmployeeInfoId());
		candidateDetailsDTO.setMobileNumber(employeePersonalInfo.getMobileNumber());
		candidateDetailsDTO.setDesignation(employeePersonalInfo.getEmployeeOfficialInfo().getDesignation());
		candidateDetailsDTO.setDepartment(employeePersonalInfo.getEmployeeOfficialInfo().getDepartment());

		List<EmployeeBankInfo> employeeBankInfoList = employeePersonalInfo.getEmployeeBankInfoList();
		List<BankInformationDTO> employeeBankDetailsList = new ArrayList<>();
		employeeBankInfoList.stream().forEach((EmployeeBankInfo b) -> {
			BankInformationDTO employeeBankDTO = new BankInformationDTO();
			BeanUtils.copyProperties(b, employeeBankDTO);
			employeeBankDetailsList.add(employeeBankDTO);
		});
		candidateDetailsDTO.setEmployeeBankInfoList(employeeBankDetailsList);

		List<EmployeeEducationInfo> employeeEducationInfoList = employeePersonalInfo.getEmployeeEducationInfoList();
		List<EmployeeEducationDetailsDTO> educationDetails = new ArrayList<>();
		employeeEducationInfoList.stream().forEach((EmployeeEducationInfo e) -> {
			EmployeeEducationDetailsDTO employeeEducationDetails = new EmployeeEducationDetailsDTO();
			BeanUtils.copyProperties(e, employeeEducationDetails);
			educationDetails.add(employeeEducationDetails);
		});
		candidateDetailsDTO.setEmployeeEducationInfoList(educationDetails);

		List<EmployeeEmploymentInfo> employeeEmploymentInfoList = employeePersonalInfo.getEmployeeEmploymentInfoList();
		List<EmployeeEmploymentDTO> employeeEmploymentList = new ArrayList<>();
		employeeEmploymentInfoList.stream().forEach((e) -> {
			EmployeeEmploymentDTO employeeEmploymentDTO = new EmployeeEmploymentDTO();
			BeanUtils.copyProperties(e, employeeEmploymentDTO);
			employeeEmploymentList.add(employeeEmploymentDTO);
		});
		candidateDetailsDTO.setEmployeeEmployment(employeeEmploymentList);

		List<EmployeeDependentInfo> employeeDependentInfoList = employeePersonalInfo.getEmployeeDependentInfoList();
		List<EmployeeDependantInfoDetailsDTO> dependantInfoDetailsList = new ArrayList<>();
		employeeDependentInfoList.stream().forEach((EmployeeDependentInfo e) -> {
			EmployeeDependantInfoDetailsDTO employeeDependantInfoDetailsDTO = new EmployeeDependantInfoDetailsDTO();
			BeanUtils.copyProperties(e, employeeDependantInfoDetailsDTO);
			dependantInfoDetailsList.add(employeeDependantInfoDetailsDTO);
		});
		candidateDetailsDTO.setEmployeeDependentInfoList(dependantInfoDetailsList);

		return candidateDetailsDTO;
	}

	@Override
	public boolean approveCandidates(ApproveRequestDTO approveRequestDTO, Long userId, Long companyId) {
		Optional<EmployeePersonalInfo> employeeInfoOptional = employeePersonalInfoRepository
				.findById(approveRequestDTO.getPersonalId());
		if (!employeeInfoOptional.isPresent()) {
			throw new DataNotFoundException("Employee Details not found");
		}
		EmployeePersonalInfo employeePersonalInfo = employeeInfoOptional.get();
		boolean empty = employeeOfficialInfoRepository
				.findByEmployeeIdAndCompanyBranchInfoCompanyInfoCompanyId(approveRequestDTO.getEmployeeId(), companyId)
				.isEmpty();

		if (!empty) {
			throw new OfficalIdInUseException("Employee Offical id is already in use");
		}
		if (employeePersonalInfo.getCreatedBy() != null) {
			Optional<CandidateInfo> candidateOptional = candidateInfoRepo.findById(employeePersonalInfo.getCreatedBy());
			if (candidateOptional.isPresent()) {
				CandidateInfo candidateInfo = candidateOptional.get();
				candidateInfo.setIsDocumentVerified(Boolean.TRUE);
				candidateInfoRepo.save(candidateInfo);
			}
		}
		employeePersonalInfo.setIsActive(Boolean.TRUE);
		log.info(employeePersonalInfo + "");
		EmployeeLoginInfo employeeLoginInfo = new EmployeeLoginInfo();
		employeeLoginInfo.setEmployeeId(approveRequestDTO.getEmployeeId());
		employeeLoginInfo.setCurrentPassword(approveRequestDTO.getPassword());
		employeeLoginInfo.setEmployeePersonalInfo(employeePersonalInfo);
		employeeLoginInfo.setRoles(Arrays.asList("ROLE_EMPLOYEE"));
		Map<String, String> status = new HashMap<>();
		status.put("approvedBy", userId + "");
		employeePersonalInfo.setStatus(status);
		employeePersonalInfo.setIsActive(true);

		// Adding details to Official Info table
		EmployeeOfficialInfo employeeOfficialInfo = new EmployeeOfficialInfo();
		employeeOfficialInfo.setEmployeeId(approveRequestDTO.getEmployeeId());
		// Mapping the official data to personal info
		employeePersonalInfo.setEmployeeOfficialInfo(employeeOfficialInfo);
		employeePersonalInfoRepository.save(employeePersonalInfo);
		EmployeeLoginInfo save = employeeLoginRepository.save(employeeLoginInfo);
		String toMail = employeePersonalInfo.getEmployeeOfficialInfo().getOfficialEmailId();
		if (toMail != null) {
			MailDto mailDto = new MailDto();
			mailDto.setTo(toMail);
			mailDto.setSubject("Your password For Verification");
			mailDto.setBody("Dear " + save.getEmployeePersonalInfo().getFirstName() + " "
					+ save.getEmployeePersonalInfo().getLastName() + ",\r\n" + "\r\n"
					+ "your FLINKO Application login credentials. Please login and access FLINKO. If you are unable to login, please share a screenshot of the error page to projectmailer2021@gmail.com"
					+ "\r\n" + "\r\n" + "Your Employee Id :" + save.getEmployeeId() + "\r\n"
					+ " Your Default Password :" + save.getCurrentPassword() + "\r\n" + "\r\n"
					+ "Kindly use the FLINKO official email id for any further communications." + "\r\n"
					+ "Thanks and Regards," + "\r\n" + "Team FLINKO");
			emailService.sendMail(mailDto);

		}
		return true;
	}

	@Override
	@Transactional
	public boolean rejectCandidate(RejectCandidateRequestDTO candidateRequestDTO) {
		Optional<EmployeePersonalInfo> employeeOptional = employeePersonalInfoRepository
				.findById(candidateRequestDTO.getPersonalId());

		if (!employeeOptional.isPresent()) {
			throw new DataNotFoundException("EployeeDetails not Found");
		}
		EmployeePersonalInfo employeePersonalInfo = employeeOptional.get();
		if (employeePersonalInfo.getCreatedBy() != null) {
			Optional<CandidateInfo> candidateOptional = candidateInfoRepo.findById(employeePersonalInfo.getCreatedBy());
			if (candidateOptional.isPresent()) {
				CandidateInfo candidateInfo = candidateOptional.get();
				candidateInfo.setIsDocumentVerified(Boolean.FALSE);
				candidateInfoRepo.save(candidateInfo);
			}
		}
		Map<String, String> status = new HashMap<>();
		status.put("rejectedBy", candidateRequestDTO.getRejectedBy());
		status.put("reason", candidateRequestDTO.getReason());
		employeePersonalInfo.setStatus(status);
		employeePersonalInfoRepository.save(employeePersonalInfo);
		return true;
	}

	@Override
	public boolean resendLink(ResendLinkDTO resendLinkDTO, Long companyId, Long userId) {
		// should call the method to send mail
		EmployeePersonalInfo employeePersonalInfo = employeePersonalInfoRepository
				.findById(resendLinkDTO.getPersonalInfoId())
				.orElseThrow(() -> new DataNotFoundException("EployeeDetails not Found"));
		candidateManagementService.sendLink(resendLinkDTO.getLink(),
				employeePersonalInfo.getCandidateInfo().getCandidateId(), companyId, userId);
		Map<String, String> status = new HashMap<>();
		// call the email api
		status.put("sentby", resendLinkDTO.getResentBy());
		status.put("comment", resendLinkDTO.getComment());
		employeePersonalInfo.setStatus(status);
		employeePersonalInfoRepository.save(employeePersonalInfo);
		return true;
	}

	@Override
	public List<EmployeeDisplayDetailsDTO> getCurrentEmployees(Long companyId) {
		List<EmployeePersonalInfo> employeeList = employeePersonalInfoRepository.findByCompanyInfoCompanyId(companyId);
		if (employeeList.isEmpty()) {
			throw new DataNotFoundException("No Employees to display");
		}
		List<EmployeePersonalInfo> currentEmployees = employeeList.stream()
				.filter(e -> e.getStatus().containsKey("approvedBy")).collect(Collectors.toList());

		List<EmployeeDisplayDetailsDTO> currentEmployeeDTOList = new ArrayList<>();
		currentEmployees.stream().forEach((e) -> {
			EmployeeDisplayDetailsDTO employeeDetailsDTO = new EmployeeDisplayDetailsDTO();
			employeeDetailsDTO.setEmployeeId(e.getEmployeeInfoId());
			employeeDetailsDTO.setFullName(e.getFirstName() + " " + e.getLastName());
			employeeDetailsDTO.setIsActive(e.getIsActive());
			if (e.getEmployeeOfficialInfo() != null) {
				employeeDetailsDTO.setOfficialEmailId(e.getEmployeeOfficialInfo().getOfficialEmailId());
				employeeDetailsDTO.setDepartment(e.getEmployeeOfficialInfo().getDepartment());
				employeeDetailsDTO.setDesignation(e.getEmployeeOfficialInfo().getDesignation());
			}
			currentEmployeeDTOList.add(employeeDetailsDTO);
		});
		return currentEmployeeDTOList.stream()
				.sorted(Comparator.comparingLong(EmployeeDisplayDetailsDTO::getEmployeeId).reversed())
				.collect(Collectors.toList());
	}

	@Override
	public GeneralInformationDTO addEmployeePersonalInfo(GeneralInformationDTO information, Long companyId) {
		Optional<CompanyInfo> optionalCompany = companyInfoRepo.findById(companyId);
		if (!optionalCompany.isPresent()) {
			throw new DataNotFoundException("Company not found with id " + companyId);
		}
		EmployeePersonalInfo employeePersonalInfo = new EmployeePersonalInfo();
		if (information.getEmployeeInfoId() == 0) {
			BeanUtils.copyProperties(information, employeePersonalInfo);
		} else {
			Optional<EmployeePersonalInfo> optionalEmployee = employeePersonalInfoRepository
					.findById(information.getEmployeeInfoId());
			if (optionalEmployee.isEmpty()) {
				throw new DataNotFoundException("The employee is not present");
			}
			employeePersonalInfo = optionalEmployee.get();
			employeePersonalInfo.setFirstName(information.getFirstName());
			employeePersonalInfo.setLastName(information.getLastName());
			employeePersonalInfo.setEmailId(information.getEmailId());
			employeePersonalInfo.setMobileNumber(information.getMobileNumber());
		}
		;
		employeePersonalInfo.setCompanyInfo(optionalCompany.get());
		BeanUtils.copyProperties(employeePersonalInfoRepository.save(employeePersonalInfo), information);
		return information;
	}

	@Override
	public WorkInformationDTO addWorkInformation(WorkInformationDTO workInformation, Long employeeInfoId,
			Long companyId) {
		Optional<EmployeePersonalInfo> optionalEmployee = employeePersonalInfoRepository
				.findById(workInformation.getEmployeeInfoId());

		if (optionalEmployee.isEmpty()) {
			throw new DataNotFoundException("The employee is not present");
		}
		EmployeePersonalInfo employeePersonalInfo = optionalEmployee.get();
		boolean empty = employeeOfficialInfoRepository
				.findByEmployeeIdAndCompanyBranchInfoCompanyInfoCompanyId(workInformation.getEmployeeId(), companyId)
				.isEmpty();

		if (!empty) {
			throw new OfficalIdInUseException("Employee Offical id is already in use");
		}
		EmployeeOfficialInfo employeeOfficialInfo = new EmployeeOfficialInfo();
		if (workInformation.getOfficialId() == null || workInformation.getOfficialId() == 0) {
			BeanUtils.copyProperties(workInformation, employeeOfficialInfo);
		} else {
			employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
			employeeOfficialInfo.setEmployeeId(workInformation.getEmployeeId());
			employeeOfficialInfo.setDoj(workInformation.getDoj());
			employeeOfficialInfo.setEmployeeType(workInformation.getEmployeeType());
			employeeOfficialInfo.setDepartment(workInformation.getDepartment());
			employeeOfficialInfo.setDesignation(workInformation.getDesignation());
			employeeOfficialInfo.setEmployeeType(workInformation.getEmployeeType());
			employeeOfficialInfo.setOfficialEmailId(workInformation.getOfficialEmailId());
		}

		Optional<CompanyBranchInfo> branchDetails = branchInfoRepo.findById(workInformation.getBranchId());
		branchDetails.ifPresentOrElse(employeeOfficialInfo::setCompanyBranchInfo, () -> {
			throw new DataNotFoundException("branch details not present");
		});
		employeePersonalInfo.setEmployeeOfficialInfo(employeeOfficialInfo);
		Map<String, String> status = new LinkedHashMap<>();
		status.put("approvedBy", "" + employeeInfoId);
		employeePersonalInfo.setStatus(status);
		employeePersonalInfo.setIsActive(Boolean.TRUE);
		EmployeePersonalInfo save = employeePersonalInfoRepository.save(employeePersonalInfo);
		EmployeeLoginInfo employeeLoginInfo = new EmployeeLoginInfo();
		employeeLoginInfo.setCurrentPassword(generatePassword());
		employeeLoginInfo.setEmployeeId(workInformation.getEmployeeId());
		employeeLoginInfo.setEmployeePersonalInfo(employeePersonalInfo);
		employeeLoginInfo.setCurrentPassword(generatePassword());

		EmployeeLoginInfo saveEmployeeLoginInfo = employeeLoginInfoRepository.save(employeeLoginInfo);

		String toMail = saveEmployeeLoginInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId();

		if (toMail != null) {
			MailDto mailDto = new MailDto();
			mailDto.setTo(toMail);
			mailDto.setSubject("Your password For Verification");
			mailDto.setBody("Dear " + saveEmployeeLoginInfo.getEmployeePersonalInfo().getFirstName() + " "
					+ saveEmployeeLoginInfo.getEmployeePersonalInfo().getLastName() + ",\r\n" + "\r\n"
					+ "your FLINKO Application login credentials. Please login and access FLINKO. If you are unable to login, please share a screenshot of the error page to projectmailer2021@gmail.com"
					+ "\r\n" + "\r\n" + "Your Employee Id :" + saveEmployeeLoginInfo.getEmployeeId() + "\r\n"
					+ " Your Default Password :" + saveEmployeeLoginInfo.getCurrentPassword() + "\r\n" + "\r\n"
					+ "Kindly use the FLINKO official email id for any further communications." + "\r\n"
					+ "Thanks and Regards," + "\r\n" + "Team FLINKO");
			emailService.sendMail(mailDto);

		}
		WorkInformationDTO workInformationDTO = new WorkInformationDTO();
		BeanUtils.copyProperties(save, workInformationDTO);
		BeanUtils.copyProperties(save.getEmployeeOfficialInfo(), workInformationDTO);
		workInformationDTO.setBranchName(save.getEmployeeOfficialInfo().getCompanyBranchInfo().getBranchName());
		return workInformationDTO;
	}

	@Override
	@Transactional
	public EmployeeReportingResponseDTO mapReportingInformation(ReportingInformationDTO reportingInformation) {
		Optional<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepository
				.findById(reportingInformation.getEmployeeInfoId());
		Optional<EmployeePersonalInfo> reportingHr = employeePersonalInfoRepository
				.findById(reportingInformation.getReportingHRid());
		Optional<EmployeePersonalInfo> managerInfo = employeePersonalInfoRepository
				.findById(reportingInformation.getReportingManagerId());
		EmployeeReportingInfo employeeReportingInfo = new EmployeeReportingInfo();
		managerInfo.ifPresentOrElse(employeeReportingInfo::setReportingManager, () -> {
			throw new DataNotFoundException("Manager info is not present");
		});
		reportingHr.ifPresentOrElse(employeeReportingInfo::setReportingHR, () -> {
			throw new DataNotFoundException("Hr information is not present");
		});
		if (!employeeInfo.isPresent()) {
			throw new DataNotFoundException("Employee Record not present");
		}
		employeeReportingInfo.setEmployeePersonalInfo(employeeInfo.get());
		;
		employeeReportingInfo.setReportId(reportingInformation.getReportId());
		EmployeeReportingInfo save = employeeReportingInfoRepo.save(employeeReportingInfo);
		EmployeeReportingResponseDTO employeeReportingResponseDTO = new EmployeeReportingResponseDTO();
		employeeReportingResponseDTO.setReportId(save.getReportId());
		employeeReportingInfoRepo.save(employeeReportingInfo);
		employeeInfo.ifPresentOrElse((i) -> employeeReportingResponseDTO.setEmployeeName(i.getFirstName()), () -> {
			throw new DataNotFoundException("Employee data is not found");
		});
		reportingHr.ifPresentOrElse((i) -> employeeReportingResponseDTO.setReportingHrName(i.getFirstName()), () -> {
			throw new DataNotFoundException("Hr data not found");
		});
		managerInfo.ifPresentOrElse((i) -> employeeReportingResponseDTO.setReportingManagerName(i.getFirstName()),
				() -> {
					throw new DataNotFoundException("Manager Data not found");
				});
		return employeeReportingResponseDTO;
	}

	@Override
	public PersonalInformationDTO addPersonalInformation(PersonalInformationDTO information) {
		Optional<EmployeePersonalInfo> optionalEmployee = employeePersonalInfoRepository
				.findById(information.getEmployeeInfoId());
		if (optionalEmployee.isEmpty()) {
			throw new DataNotFoundException("Employee Not found with id" + information.getEmployeeInfoId());
		}

		EmployeePersonalInfo employeePersonalInfo = optionalEmployee.get();

		BeanUtils.copyProperties(information, employeePersonalInfo);
		employeePersonalInfo.setLanguage(information.getLanguage());

		List<EmployeeAddressInfo> addressList = employeePersonalInfo.getEmployeeAddressInfoList();

		Set<Long> employeeAddress = addressList.stream().map(EmployeeAddressInfo::getAddressId)
				.collect(Collectors.toSet());
		Set<Long> addressDTOs = information.getEmployeeAddressDTO().stream().map(EmployeeAddressInfoDTO::getAddressId)
				.collect(Collectors.toSet());
		employeeAddress.removeAll(addressDTOs);
		employeeddressInfoRepo.deleteAllByIdInBatch(employeeAddress);

		List<EmployeeAddressInfo> addressListToBeSaved = new ArrayList<>();

		information.getEmployeeAddressDTO().stream().forEach(i -> {
			EmployeeAddressInfo employeeAddressInfo = new EmployeeAddressInfo();
			BeanUtils.copyProperties(i, employeeAddressInfo);
			employeeAddressInfo.setEmployeePersonalInfo(employeePersonalInfo);
			addressListToBeSaved.add(employeeAddressInfo);
		});
		employeePersonalInfo.setEmployeeAddressInfoList(addressListToBeSaved);

		EmployeePersonalInfo save = employeePersonalInfoRepository.save(employeePersonalInfo);
		PersonalInformationDTO personalInformationDTO = new PersonalInformationDTO();
		List<EmployeeAddressInfoDTO> listOfAddress = new ArrayList<>();
		BeanUtils.copyProperties(save, personalInformationDTO);
		save.getEmployeeAddressInfoList().forEach(i -> {
			EmployeeAddressInfoDTO employeeAddressInfoDTO = new EmployeeAddressInfoDTO();
			BeanUtils.copyProperties(i, employeeAddressInfoDTO);
			listOfAddress.add(employeeAddressInfoDTO);
		});
		personalInformationDTO.setEmployeeAddressDTO(listOfAddress);
		return personalInformationDTO;
	}

//	@Override
//	public boolean addDependentInformation(List<DependentInformationDTO> dependentInformation, Long employeeId,
//			Long companyId) {
//		Optional<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepository.findById(employeeId);
////		EmployeePersonalInfo employeePersonalInfo = employeeInfo.get();
////		List<EmployeeDependentInfo> employeeDependentInfoList = employeePersonalInfo.getEmployeeDependentInfoList();
////		log.info(employeeDependentInfoList.get(0).getFullName()  +"");
////		dependentInfoRepo.deleteById(1l);
//
//		if (!employeeInfo.isPresent()) {
//			throw new DataNotFoundException("Employee not found");
//		}
//		EmployeePersonalInfo employeePersonalInfo = employeeInfo.get();
//		List<Long> listOfId = new ArrayList<>();
//		List<EmployeeDependentInfo> dependentInfoList = employeePersonalInfo.getEmployeeDependentInfoList();
//		dependentInfoList.forEach(i -> listOfId.add(i.getDependentId()));
//		dependentInformation.forEach(i -> {
//			EmployeeDependentInfo employeeDependentInfo = new EmployeeDependentInfo();
//			BeanUtils.copyProperties(i, employeeDependentInfo);
//			employeeDependentInfo.setEmployeePersonalInfo(employeePersonalInfo);
//			if (i.getDependentId() == null)
//				dependentInfoList.add(employeeDependentInfo);
//		});
//
//		employeePersonalInfo.setEmployeeDependentInfoList(dependentInfoList);
//		employeePersonalInfoRepository.save(employeePersonalInfo);
//		List<Long> listOfIdToKeep = new ArrayList<>();
//		dependentInfoList.forEach(i -> {
//			dependentInformation.forEach(j -> {
//				if (j.getDependentId() == null || j.getDependentId().equals(i.getDependentId())) {
//					listOfIdToKeep.add(i.getDependentId());
//				}
//			});
//		});
//		listOfIdToKeep.removeAll(listOfId);
////		dependentInfoRepo.deleteAllByIdInBatch(listOfId);
//		return true;
//	}

	@Override
	public List<EmployeeEmploymentDTO> addEmploymentInformation(List<EmployementInformationDTO> information,
			Long employeeId) {
		Optional<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepository.findById(employeeId);
		if (!employeeInfo.isPresent()) {
			throw new DataNotFoundException("Employee Data not found");
		}
		EmployeePersonalInfo employeePersonalInfo = employeeInfo.get();
		List<EmployeeEmploymentInfo> employeeEmploymentInfoList = employeePersonalInfo.getEmployeeEmploymentInfoList();

		Set<Long> employmentInfoIdDtoSet = information.stream().map(EmployementInformationDTO::getEmploymentId)
				.collect(Collectors.toSet());
		Set<Long> employmentInfoIdEntity = employeeEmploymentInfoList.stream()
				.map(EmployeeEmploymentInfo::getEmploymentId).collect(Collectors.toSet());

		employmentInfoIdEntity.removeAll(employmentInfoIdDtoSet);
		employeeEmploymentInfoRepo.deleteAllByIdInBatch(employmentInfoIdEntity);

		List<EmployeeEmploymentInfo> employmentInfoList = new ArrayList<>();
		information.forEach(i -> {
			EmployeeEmploymentInfo employeeEmploymentInfo = new EmployeeEmploymentInfo();
			BeanUtils.copyProperties(i, employeeEmploymentInfo);
			LinkedHashMap<String, String> referencePersonDetails = new LinkedHashMap<>();
			i.getReferencePersonDetails().forEach(personDetails -> {
				referencePersonDetails.put(personDetails.getMobileNumber().toString(),
						personDetails.getReferenceName());
			});
			employeeEmploymentInfo.setReferencePersonDetails(referencePersonDetails);
			employeeEmploymentInfo.setEmployeePersonalInfo(employeePersonalInfo);
			employmentInfoList.add(employeeEmploymentInfo);
		});
		employeePersonalInfo.setEmployeeEmploymentInfoList(employmentInfoList);
		List<EmployeeEmploymentInfo> employeeEmploymentInfoList2 = employeePersonalInfoRepository
				.save(employeePersonalInfo).getEmployeeEmploymentInfoList();

		List<EmployeeEmploymentDTO> responseEmploymentInformationList = new ArrayList<>();
		employeeEmploymentInfoList2.forEach(i -> {
			EmployeeEmploymentDTO employeeEmploymentDTO = new EmployeeEmploymentDTO();
			BeanUtils.copyProperties(i, employeeEmploymentDTO);
			List<ReferencePersonDTO> referencePersonDetailsList = new ArrayList<>();
			if (i.getReferencePersonDetails() != null) {
				for (Entry<String, String> personDetails : i.getReferencePersonDetails().entrySet()) {
					referencePersonDetailsList.add(
							new ReferencePersonDTO(personDetails.getValue(), Long.parseLong(personDetails.getKey())));
				}
			}
			employeeEmploymentDTO.setReferencePersonDetails(referencePersonDetailsList);
			responseEmploymentInformationList.add(employeeEmploymentDTO);
		});
		return responseEmploymentInformationList;
	}

	@Override
	public List<EmployeeEducationDetailsDTO> addEducaitonInformation(List<EmployeeEducationDetailsDTO> information,
			Long employeeId) {
		Optional<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepository.findById(employeeId);
		if (employeeInfo.isEmpty()) {
			throw new DataNotFoundException("Employee Data not found");
		}
		EmployeePersonalInfo employeePersonalInfo = employeeInfo.get();
		List<EmployeeEducationInfo> employmentInfoList = employeePersonalInfo.getEmployeeEducationInfoList();

		Set<Long> educaitonInfoIdDTOSet = information.stream().map(EmployeeEducationDetailsDTO::getEducationId)
				.collect(Collectors.toSet());
		Set<Long> educationInfoSet = employmentInfoList.stream().map(EmployeeEducationInfo::getEducationId)
				.collect(Collectors.toSet());

		educationInfoSet.removeAll(educaitonInfoIdDTOSet);

		employeeEducationInfoRepo.deleteAllByIdInBatch(educationInfoSet);
		List<EmployeeEducationInfo> listOfEducationDeatils = new ArrayList<>();
		information.forEach(i -> {
			EmployeeEducationInfo employeeEducationInfo = new EmployeeEducationInfo();
			BeanUtils.copyProperties(i, employeeEducationInfo);
			employeeEducationInfo.setEmployeePersonalInfo(employeePersonalInfo);
			listOfEducationDeatils.add(employeeEducationInfo);
		});
		employeePersonalInfo.setEmployeeEducationInfoList(listOfEducationDeatils);
		List<EmployeeEducationInfo> employeeEducationInfoList = employeePersonalInfoRepository
				.save(employeePersonalInfo).getEmployeeEducationInfoList();
		List<EmployeeEducationDetailsDTO> response = new ArrayList<>();
		employeeEducationInfoList.forEach(i -> {
			EmployeeEducationDetailsDTO employeeEducationDetailsDTO = new EmployeeEducationDetailsDTO();
			BeanUtils.copyProperties(i, employeeEducationDetailsDTO);
			response.add(employeeEducationDetailsDTO);
		});
		return response;
	}

	@Override
	public List<BankInformationDTO> addBankDetailsInfo(List<BankInformationDTO> information, Long employeeId) {
		Optional<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepository.findById(employeeId);
		if (employeeInfo.isEmpty()) {
			throw new DataNotFoundException("Employee Data not found");
		}
		EmployeePersonalInfo employeePersonalInfo = employeeInfo.get();
		List<EmployeeBankInfo> bankInfoList = employeePersonalInfo.getEmployeeBankInfoList();
		Set<Long> listOfBankIdEntitySet = bankInfoList.stream().map(EmployeeBankInfo::getBankId)
				.collect(Collectors.toSet());
		Set<Long> listOfBankIdDto = information.stream().map(BankInformationDTO::getBankId).collect(Collectors.toSet());
		listOfBankIdEntitySet.removeAll(listOfBankIdDto);

		employeeBankInfoRepository.deleteAllByIdInBatch(listOfBankIdEntitySet);

		List<EmployeeBankInfo> listOfBankInfo = new ArrayList<>();
		information.forEach(i -> {
			EmployeeBankInfo employeeBankInfo = new EmployeeBankInfo();
			BeanUtils.copyProperties(i, employeeBankInfo);
			employeeBankInfo.setEmployeePersonalInfo(employeePersonalInfo);
			listOfBankInfo.add(employeeBankInfo);
		});
		employeePersonalInfo.setEmployeeBankInfoList(listOfBankInfo);
		List<EmployeeBankInfo> employeeBankInfoList = employeePersonalInfoRepository.save(employeePersonalInfo)
				.getEmployeeBankInfoList();
		List<BankInformationDTO> response = new ArrayList<>();
		employeeBankInfoList.forEach(i -> {
			BankInformationDTO bankInformationDTO = new BankInformationDTO();
			BeanUtils.copyProperties(i, bankInformationDTO);
			response.add(bankInformationDTO);
		});
		return response;
	}

	@Override
	public RefrencePersonInfoDTO addReferenceInfo(RefrencePersonInfoDTO information) {
		Optional<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepository
				.findById(information.getEmployeeId());
		Optional<EmployeePersonalInfo> referedEmployeeInfo = employeePersonalInfoRepository
				.findById(information.getReferedEmployeeId());
		EmployeeReferenceInfo employeeReferenceInfo = new EmployeeReferenceInfo();
		employeeReferenceInfo.setReferenceId(information.getReferenceId());
		employeeInfo.ifPresentOrElse(employeeReferenceInfo::setEmployeePersonalInfo, () -> {
			throw new DataNotFoundException("No employee data found");
		});
		referedEmployeeInfo.ifPresentOrElse(employeeReferenceInfo::setRefferalEmployeePersonalInfo, () -> {
			throw new DataNotFoundException("No employee data found");
		});
		employeeReferenceInfo.setReferralName(information.getReferralName());
		EmployeeReferenceInfo save = employeeReferenceInfoRepo.save(employeeReferenceInfo);
		RefrencePersonInfoDTO refrencePersonInfoDTO = new RefrencePersonInfoDTO();
		refrencePersonInfoDTO.setReferenceId(save.getReferenceId());
		refrencePersonInfoDTO.setEmployeeId(save.getRefferalEmployeePersonalInfo().getEmployeeInfoId());
		refrencePersonInfoDTO.setReferralName(save.getReferralName());
		refrencePersonInfoDTO.setReferedEmployeeId(save.getRefferalEmployeePersonalInfo().getEmployeeInfoId());
		refrencePersonInfoDTO.setReferedEmployeeOfficialId(
				save.getRefferalEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId());
		return refrencePersonInfoDTO;
	}

	// Need to discuss
	@Override
	public PassAndVisaDTO addPassandVisaInfo(PassAndVisaDTO information, Long employeeId) {
		Optional<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepository.findById(employeeId);
		if (!employeeInfo.isPresent()) {
			throw new DataNotFoundException("Emplpyee Data not found");
		}
		EmployeePersonalInfo employeePersonalInfo = employeeInfo.get();
		List<EmployeeVisaInfo> visaInfoList = employeePersonalInfo.getEmployeeVisaInfoList();

		Set<Long> visaInfoIdEntitySet = visaInfoList.stream().map(EmployeeVisaInfo::getVisaId)
				.collect(Collectors.toSet());
		Set<Long> visaInfoIdDtoSet = information.getVisaInfo().stream().map(AddVisaInfoDTO::getVisaId)
				.collect(Collectors.toSet());

		visaInfoIdEntitySet.removeAll(visaInfoIdDtoSet);

		employeeVisaInfoRepository.deleteAllByIdInBatch(visaInfoIdEntitySet);

		List<EmployeeVisaInfo> listOfVisaInfo = new ArrayList<>();
		information.getVisaInfo().forEach((AddVisaInfoDTO i) -> {
			EmployeeVisaInfo employeeVisaInfo = new EmployeeVisaInfo();
			BeanUtils.copyProperties(i, employeeVisaInfo);
			employeeVisaInfo.setEmployeePersonalInfo(employeePersonalInfo);
			listOfVisaInfo.add(employeeVisaInfo);
		});
		BeanUtils.copyProperties(information, employeePersonalInfo);
		;
		employeePersonalInfo.setEmployeeVisaInfoList(listOfVisaInfo);
		EmployeePersonalInfo save = employeePersonalInfoRepository.save(employeePersonalInfo);
		PassAndVisaDTO passAndVisaDTO = new PassAndVisaDTO();
		BeanUtils.copyProperties(save, passAndVisaDTO);
		List<EmployeeVisaInfo> employeeVisaInfoList = save.getEmployeeVisaInfoList();
		ArrayList<AddVisaInfoDTO> listOfVisaDTO = new ArrayList<>();
		employeeVisaInfoList.forEach(i -> {
			AddVisaInfoDTO addVisaInfoDTO = new AddVisaInfoDTO();
			BeanUtils.copyProperties(i, addVisaInfoDTO);
			listOfVisaDTO.add(addVisaInfoDTO);
		});
		passAndVisaDTO.setVisaInfo(listOfVisaDTO);
		return passAndVisaDTO;
	}

	@Override
	public List<InterviewInformationDTO> addInterviewInformation(List<InterviewInformationDTO> informationList,
			Long employeeInfoId) {
		List<InterviewInformationDTO> interviewInformationDTOList = new ArrayList<>();
		Optional<EmployeePersonalInfo> employeeInfoOptional = employeePersonalInfoRepository.findById(employeeInfoId);
		if (employeeInfoOptional.isPresent()) {
			EmployeePersonalInfo employeePersonalInfo = employeeInfoOptional.get();
			List<CandidateInterviewInfo> interviewList = employeePersonalInfo.getEmployeeInterviewInfoList();

			Set<Long> interviewInfoIdEntitySet = interviewList.stream().map(CandidateInterviewInfo::getInterviewId)
					.collect(Collectors.toSet());
			Set<Long> interviewInfoIdDtoSet = informationList.stream().map(InterviewInformationDTO::getInterviewId)
					.collect(Collectors.toSet());

			interviewInfoIdEntitySet.removeAll(interviewInfoIdDtoSet);

			candidateInterviewInfoRepo.deleteAllByIdInBatch(interviewInfoIdEntitySet);

			List<CandidateInterviewInfo> candidateInterviewInfoList = new ArrayList<>();
			informationList.forEach((InterviewInformationDTO interviewInformationDTO) -> {
				Optional<EmployeePersonalInfo> optionalInterviewer = employeePersonalInfoRepository
						.findById(interviewInformationDTO.getInterviewerId());
				if (optionalInterviewer.isPresent()
						&& !interviewInfoIdEntitySet.contains(interviewInformationDTO.getInterviewerId())) {
					CandidateInterviewInfo candidateInterviewInfo = new CandidateInterviewInfo();
					BeanUtils.copyProperties(interviewInformationDTO, candidateInterviewInfo);
					candidateInterviewInfo.setEmployeePersonalInfo(optionalInterviewer.get());
					candidateInterviewInfo.setEmployeePersonalInterviewInfo(employeePersonalInfo);
					candidateInterviewInfoList.add(candidateInterviewInfo);
				}
			});
			employeePersonalInfo.setEmployeeInterviewInfoList(candidateInterviewInfoList);
			EmployeePersonalInfo saved = employeePersonalInfoRepository.save(employeePersonalInfo);
			List<CandidateInterviewInfo> savedInterviewList = saved.getEmployeeInterviewInfoList();
			savedInterviewList.forEach(i -> {
				InterviewInformationDTO interviewDTO = new InterviewInformationDTO();
				BeanUtils.copyProperties(i, interviewDTO);
				interviewInformationDTOList.add(interviewDTO);
			});
		}
		return interviewInformationDTOList;
	}

	@Override
	public EmployeeNoticePeriodDTO addNoticePeriodInformation(EmployeeNoticePeriodDTO noticePeriodInformation,
			Long employeeId, Long companyId) {
		log.info("This is from employee Notice period");
		Optional<CompanyInfo> optionalCompanyInfo = companyInfoRepo.findById(companyId);
		if (!optionalCompanyInfo.isPresent()) {
			throw new DataNotFoundException("Invalid company id is sent");
		}
		CompanyInfo companyInfo = optionalCompanyInfo.get();
		Optional<EmployeePersonalInfo> optionalEmployeeInfo = employeePersonalInfoRepository.findById(employeeId);
		if (!optionalEmployeeInfo.isPresent()) {
			throw new DataNotFoundException("Employee is not present with following id" + employeeId);
		}
		EmployeePersonalInfo employeePersonalInfo = optionalEmployeeInfo.get();
		LocalDate noticePeriodStartDate = noticePeriodInformation.getNoticePeriodStartDate();
		LocalDate noticePeriodEndDate = noticePeriodStartDate
				.plusDays(noticePeriodInformation.getNoticePeriodDuration().longValue());
		CompanyEmployeeResignationDetails resignatoinInfo = new CompanyEmployeeResignationDetails();
		resignatoinInfo.setNoticePeriodDuration(BigDecimal.valueOf(noticePeriodInformation.getNoticePeriodDuration()));
		resignatoinInfo.setNoticePeriodStartDate(noticePeriodStartDate);
		resignatoinInfo.setCompanyInfo(companyInfo);
		resignatoinInfo.setEmployeePersonalInfo(employeePersonalInfo);
		resignatoinInfo.setResignationId(noticePeriodInformation.getResignationId());
		EmployeeNoticePeriodDTO employeeNoticePeriod = new EmployeeNoticePeriodDTO();
		CompanyEmployeeResignationDetails savedData = resignationDetailsRepo.save(resignatoinInfo);
		BeanUtils.copyProperties(savedData, employeeNoticePeriod);
		employeeNoticePeriod.setNoticePeriodEndDate(noticePeriodEndDate);
		employeeNoticePeriod.setNoticePeriodDuration(savedData.getNoticePeriodDuration().doubleValue());
		return employeeNoticePeriod;
	}

	@Override
	@Transactional
	public boolean changeStatus(Long employeeId) {
		Optional<EmployeePersonalInfo> optionalEmployeeInfo = employeePersonalInfoRepository.findById(employeeId);
		if (!optionalEmployeeInfo.isPresent()) {
			throw new DataNotFoundException("Employee not found with id " + employeeId);
		}
		EmployeePersonalInfo employeePersonalInfo = optionalEmployeeInfo.get();
		employeePersonalInfo.setIsActive(!employeePersonalInfo.getIsActive());
		employeePersonalInfoRepository.save(employeePersonalInfo);
		return true;
	}

	@Override
	public List<EmployeeDisplayDetailsDTO> getExitEmployees(Long companyId) {
		List<CompanyEmployeeResignationDetails> listOfExitEmployees = resignationDetailsRepo
				.findByCompanyInfoCompanyId(companyId);
		if (listOfExitEmployees.isEmpty()) {
			return new ArrayList<>();
		}
		List<EmployeeDisplayDetailsDTO> currentEmployeeDTOList = new ArrayList<>();
		listOfExitEmployees.stream().forEach((CompanyEmployeeResignationDetails e) -> {
			if (!e.getEmployeeResignationDiscussionList().isEmpty()) {
				EmployeePersonalInfo employeePersonalInfo = e.getEmployeePersonalInfo();
				EmployeeDisplayDetailsDTO employeeDetailsDTO = new EmployeeDisplayDetailsDTO();
				employeeDetailsDTO.setEmployeeId(employeePersonalInfo.getEmployeeInfoId());
				employeeDetailsDTO
						.setFullName(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName());
				employeeDetailsDTO.setIsActive(employeePersonalInfo.getIsActive());
				employeeDetailsDTO
						.setOfficialEmailId(employeePersonalInfo.getEmployeeOfficialInfo().getOfficialEmailId());
				employeeDetailsDTO.setDepartment(employeePersonalInfo.getEmployeeOfficialInfo().getDepartment());
				employeeDetailsDTO.setDesignation(employeePersonalInfo.getEmployeeOfficialInfo().getDesignation());
				currentEmployeeDTOList.add(employeeDetailsDTO);
			}
		});
		return currentEmployeeDTOList;
	}

	@Override
	public ExitEmployeeDetailsDTO getExitEmployeeDetails(Long employeeId) {
		Optional<EmployeePersonalInfo> optionalEmployee = employeePersonalInfoRepository.findById(employeeId);
		if (!optionalEmployee.isPresent()) {
			throw new DataNotFoundException("Emplyee details not found");
		}
		EmployeePersonalInfo employeeInfo = optionalEmployee.get();
		ExitEmployeeDetailsDTO exitEmployeeDetailsDTO = new ExitEmployeeDetailsDTO();
		exitEmployeeDetailsDTO.setEmployeeInfoId(employeeInfo.getEmployeeInfoId());
		exitEmployeeDetailsDTO.setFullName(employeeInfo.getFirstName() + " " + employeeInfo.getLastName());
		exitEmployeeDetailsDTO.setMobileNumber(employeeInfo.getMobileNumber());
		exitEmployeeDetailsDTO.setOfficialId(employeeInfo.getEmployeeOfficialInfo().getOfficialEmailId());
		exitEmployeeDetailsDTO.setDesignation(employeeInfo.getEmployeeOfficialInfo().getDesignation());
		if (employeeInfo.getEmployeeReportingInfoList().isEmpty()) {
			throw new DataNotFoundException("Employee Does not have Reporting manager");
		}

		exitEmployeeDetailsDTO.setReportingManager(employeeInfo.getEmployeeReportingInfoList()
				.get(employeeInfo.getEmployeeReportingInfoList().size() - 1).getReportingManager().getFirstName());

		// setting the organiser details
		if (employeeInfo.getEmployeeResignationDiscussionList().isEmpty()) {
			throw new DataNotFoundException("Employee Does not have any Discussion scheduled");
		}
		EmployeeResignationDiscussion employeeResignationDiscussion = employeeInfo
				.getEmployeeResignationDiscussionList()
				.get(employeeInfo.getEmployeeResignationDiscussionList().size() - 1);
		List<EmployeePersonalInfo> oragniserDetailsList = employeeResignationDiscussion.getEmployeePersonalInfoList();
		List<OrganiserDetialsDTO> organiserList = new ArrayList<>();
		oragniserDetailsList.forEach(s -> {
			OrganiserDetialsDTO organiserDetialsDTO = new OrganiserDetialsDTO();
			organiserDetialsDTO.setEmployeeName(s.getFirstName());
			organiserDetialsDTO.setStartTime(employeeResignationDiscussion.getStartTime());
			organiserDetialsDTO.setDuration(employeeResignationDiscussion.getDuration());
			organiserDetialsDTO.setStatus(employeeResignationDiscussion.getStatus());
			organiserDetialsDTO.setFeedback(employeeResignationDiscussion.getFeedback());
			organiserList.add(organiserDetialsDTO);
		});
		exitEmployeeDetailsDTO.setOrganiser(organiserList);
		return exitEmployeeDetailsDTO;
	}

	@Override
	public List<EmployeeSalaryDetailsListDTO> employeeSalarydetails(EmployeeSalaryDetailsDTO employeeSalaryDetailsDTO) {
		CompanyInfo companyInfo = companyInfoRepo.findById(employeeSalaryDetailsDTO.getCompanyId())
				.orElseThrow(() -> new CompanyIdNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		log.info("company info verify");
		List<Long> departmentIdList = employeeSalaryDetailsDTO.getDepartment().stream().map(Long::parseLong)
				.collect(Collectors.toList());
		List<EmployeeSalaryDetails> salaryDetails;
		if (departmentIdList.get(0) == 0L) {
			salaryDetails = salaryDetailsRepo.findByCompanyInfoCompanyIdAndMonthIn(companyInfo.getCompanyId(),
					employeeSalaryDetailsDTO.getMonth());
		} else {
			List<Department> findAllById = departmentRepo.findAllById(departmentIdList);

			List<String> departmentNameList = findAllById.stream().map(Department::getDepartmentName)
					.collect(Collectors.toList());
			log.info("department" + departmentNameList);
			salaryDetails = salaryDetailsRepo
					.findByCompanyInfoCompanyIdAndMonthInAndEmployeePersonalInfoEmployeeOfficialInfoDepartmentIn(
							companyInfo.getCompanyId(), employeeSalaryDetailsDTO.getMonth(),
							departmentNameList);
		}

		ArrayList<EmployeeSalaryDetailsListDTO> dropDownlist = new ArrayList<>();

		for (EmployeeSalaryDetails employeeSalaryDetails : salaryDetails) {
			Map<String, String> deduction = employeeSalaryDetails.getDeduction();

			EmployeePersonalInfo employeePersonalInfo = employeeSalaryDetails.getEmployeePersonalInfo();
			EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
			if ((employeePersonalInfo != null) && (employeeOfficialInfo != null)) {
				String dummyStatus;
				if (Boolean.TRUE.equals(employeeSalaryDetails.getIsPayslipGenerated())) {
					dummyStatus = "Payslip Generated";
				} else if (Boolean.TRUE.equals(employeeSalaryDetails.getIsPaid())) {
					dummyStatus = "Paid";
				} else if (Boolean.TRUE.equals(employeeSalaryDetails.getIsFinalized())) {
					dummyStatus = "Finalised";
				} else {
					dummyStatus = "Pending";
				}

				String lop = null;
				if ((deduction != null)) {

					for (Map.Entry<String, String> entry : deduction.entrySet()) {
						if (entry.getKey().equalsIgnoreCase("lop")) {
							lop = entry.getValue();
							break;
						}
					}
				}
				lop = (lop == null) ? Integer.toString(0) : lop;
				dropDownlist.add(new EmployeeSalaryDetailsListDTO(employeeSalaryDetails.getEmployeeSalaryId(),
						employeeOfficialInfo.getEmployeeId(),
						employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName(),
						employeeSalaryDetails.getTotalSalary(), employeeSalaryDetails.getNetPay(), lop, dummyStatus));

			}
		}
		log.info("employee Salary details fetch successfully");
		return dropDownlist;
	}

	@Override
	public EmployeeSalaryAllDetailsDTO employeeSalarydetailsFindById(Long employeeSalaryId, Long companyId) {

		companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyIdNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		log.info("company info verify");
		EmployeeSalaryDetails salaryDetails = salaryDetailsRepo.findById(employeeSalaryId)
				.orElseThrow(() -> new CustomExceptionForHr(SALARY_RECORDS_NOT_FOUND));
		Map<String, String> deduction = salaryDetails.getDeduction();

		Map<String, String> reimbursemt = new HashMap<>();
		Map<String, String> additionalDto = salaryDetails.getAdditional();

		EmployeeSalaryAllDetailsDTO salaryDto = new EmployeeSalaryAllDetailsDTO();

		Map<String, String> newList = new HashMap<>(additionalDto);

		if (additionalDto != null) {
			for (Map.Entry<String, String> entry : additionalDto.entrySet()) {
				if (Pattern.matches(REIMBURSEMENT_PATTERN, entry.getKey())) {

					reimbursemt.put(entry.getKey(), entry.getValue());
					newList.remove(entry.getKey());
				}
			}
		}
		String lop = null;
		if ((deduction != null)) {

			for (Map.Entry<String, String> entry : deduction.entrySet()) {
				if (entry.getKey().equalsIgnoreCase("lop")) {
					lop = entry.getValue();
					break;
				}
			}
		}
		lop = (lop == null) ? Integer.toString(0) : lop;
		BeanUtils.copyProperties(salaryDetails, salaryDto);
		salaryDto.setAdditional(newList);
		salaryDto.setReimbursements(reimbursemt);
		salaryDto.setEmployeeId(salaryDetails.getEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId());
		salaryDto.setEmployeeName(salaryDetails.getEmployeePersonalInfo().getFirstName() + " "
				+ salaryDetails.getEmployeePersonalInfo().getLastName());
		salaryDto.setLop(lop);
		salaryDto.setGrossPay(salaryDetails.getTotalSalary());
		log.info("employee salary detail fetch successfully");
		return salaryDto;
	}

	public List<DependentInformationDTO> addDependentInformation(List<DependentInformationDTO> dependentInformation,
			Long employeeId, Long companyId) {

		Optional<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepository.findById(employeeId);
		if (employeeInfo.isEmpty()) {
			throw new DataNotFoundException("Employee Data not found");
		}
		EmployeePersonalInfo employeePersonalInfo = employeeInfo.get();
		List<EmployeeDependentInfo> dependentInfoList = employeePersonalInfo.getEmployeeDependentInfoList();

		Set<Long> dependentInfoIdDtoSet = dependentInformation.stream().map(DependentInformationDTO::getDependentId)
				.collect(Collectors.toSet());
		Set<Long> dependentInfoIdEntitySet = dependentInfoList.stream().map(EmployeeDependentInfo::getDependentId)
				.collect(Collectors.toSet());

		dependentInfoIdEntitySet.removeAll(dependentInfoIdDtoSet);
		dependentInfoRepo.deleteAllByIdInBatch(dependentInfoIdEntitySet);

		List<EmployeeDependentInfo> departmentList = new ArrayList<>();
		dependentInformation.forEach(i -> {
			if (!dependentInfoIdEntitySet.contains(i.getDependentId())) {
				EmployeeDependentInfo employeeDependentInfo = new EmployeeDependentInfo();
				BeanUtils.copyProperties(i, employeeDependentInfo);
				employeeDependentInfo.setEmployeePersonalInfo(employeePersonalInfo);
				departmentList.add(employeeDependentInfo);
			}
		});
		employeePersonalInfo.setEmployeeDependentInfoList(departmentList);
		List<EmployeeDependentInfo> employeeDependentInfoList = employeePersonalInfoRepository
				.save(employeePersonalInfo).getEmployeeDependentInfoList();
		List<DependentInformationDTO> responseDependentInformationList = new ArrayList<>();
		employeeDependentInfoList.forEach(i -> {
			DependentInformationDTO dependentInformationDTO = new DependentInformationDTO();
			BeanUtils.copyProperties(i, dependentInformationDTO);
			responseDependentInformationList.add(dependentInformationDTO);
		});
		return responseDependentInformationList;
	}

	@Override
	public EmployeeAllDetialsDTO getAllEmployeeDetials(Long employeeId) {
		Optional<EmployeePersonalInfo> findById = employeePersonalInfoRepository.findById(employeeId);
		if (!findById.isPresent()) {
			throw new DataNotFoundException("Employee details not found");
		}
		EmployeePersonalInfo employeePersonalInfo = findById.get();
		GeneralInformationDTO generalInformationDTO = new GeneralInformationDTO();
		BeanUtils.copyProperties(employeePersonalInfo, generalInformationDTO);
		WorkInformationDTO workInformationDTO = new WorkInformationDTO();
		if (employeePersonalInfo.getEmployeeOfficialInfo() != null) {
			BeanUtils.copyProperties(employeePersonalInfo.getEmployeeOfficialInfo(), workInformationDTO);
			Optional<Department> departmentDetails = departmentRepository
					.findByDepartmentName(employeePersonalInfo.getEmployeeOfficialInfo().getDepartment());
			departmentDetails.ifPresent(i -> {
				workInformationDTO.setDepartmentId(i.getDepartmentId());
				workInformationDTO.setDepartment(i.getDepartmentName());
			});
			List<CompanyDesignationInfo> findByDesignationName = companyDesignationInfoRepository
					.findByDesignationNameAndCompanyInfoCompanyId(
							employeePersonalInfo.getEmployeeOfficialInfo().getDesignation(),
							employeePersonalInfo.getCompanyInfo().getCompanyId());
			findByDesignationName.forEach(i -> {
				workInformationDTO.setDesignation(i.getDesignationName());
				workInformationDTO.setDesignationId(i.getDesignationId());
			});

			CompanyBranchInfo companyBranchInfo = employeePersonalInfo.getEmployeeOfficialInfo().getCompanyBranchInfo();
			if (companyBranchInfo != null) {
				workInformationDTO.setBranchName(companyBranchInfo.getBranchName());
				workInformationDTO.setBranchId(companyBranchInfo.getBranchId());
			}
		}
		AdditionalWorkInformationDTO additionalWorkInformationDTO = new AdditionalWorkInformationDTO();
		EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
		if (employeeOfficialInfo != null) {
			BeanUtils.copyProperties(employeeOfficialInfo, additionalWorkInformationDTO);
			CompanyShiftInfo companyShiftInfo = employeeOfficialInfo.getCompanyShiftInfo();
			if (companyShiftInfo != null) {
				additionalWorkInformationDTO.setShiftId(companyShiftInfo.getShiftId());
				additionalWorkInformationDTO.setShiftName(companyShiftInfo.getShiftName());
				additionalWorkInformationDTO.setProbationStartDate(employeeOfficialInfo.getProbationStartDate());
				additionalWorkInformationDTO.setProbationEndDate(employeeOfficialInfo.getProbationEndDate());
			}
		}
		ReportingInformationDTO reportingInformationDTO = new ReportingInformationDTO();
		List<EmployeeReportingInfo> employeeReportingInfoList = employeePersonalInfo.getEmployeeInfoList();
		if (!employeeReportingInfoList.isEmpty()) {
			EmployeeReportingInfo employeeReportingInfo = employeeReportingInfoList
					.get(employeeReportingInfoList.size() - 1);
			EmployeePersonalInfo reportingManager = employeeReportingInfo.getReportingManager();
			EmployeePersonalInfo reportingHR = employeeReportingInfo.getReportingHR();
			reportingInformationDTO
					.setReportingManagerName(reportingManager.getFirstName() + " " + reportingManager.getLastName());
			reportingInformationDTO.setReportingHrName(reportingHR.getFirstName() + " " + reportingHR.getLastName());
			reportingInformationDTO.setReportId(employeeReportingInfo.getReportId());
			reportingInformationDTO.setReportingManagerId(reportingManager.getEmployeeInfoId());
			reportingInformationDTO.setEmployeeInfoId(employeePersonalInfo.getEmployeeInfoId());
			reportingInformationDTO.setReportingHRid(reportingHR.getEmployeeInfoId());
		}
		PersonalInformationDTO personalInformationDTO = new PersonalInformationDTO();
		BeanUtils.copyProperties(employeePersonalInfo, personalInformationDTO);
		personalInformationDTO.setLanguage(employeePersonalInfo.getLanguage());
		List<EmployeeAddressInfo> employeeAddressInfoList = employeePersonalInfo.getEmployeeAddressInfoList();
		List<EmployeeAddressInfoDTO> listOfEmployeeAddressDTO = new ArrayList<>();
		employeeAddressInfoList.stream().forEach(i -> {
			EmployeeAddressInfoDTO employeeAddressInfoDTO = new EmployeeAddressInfoDTO();
			BeanUtils.copyProperties(i, employeeAddressInfoDTO);
			listOfEmployeeAddressDTO.add(employeeAddressInfoDTO);
		});
		personalInformationDTO.setEmployeeAddressDTO(listOfEmployeeAddressDTO);

		List<EmployeeDependentInfo> employeeDependentInfoList = employeePersonalInfo.getEmployeeDependentInfoList();
		List<DependentInformationDTO> dependentInformationDTOs = new ArrayList<>();
		employeeDependentInfoList.stream().forEach(i -> {
			DependentInformationDTO dependentInformationDTO = new DependentInformationDTO();
			BeanUtils.copyProperties(i, dependentInformationDTO);
			dependentInformationDTOs.add(dependentInformationDTO);
		});
		List<EmployeeEmploymentInfo> employeeEmploymentInfoList = employeePersonalInfo.getEmployeeEmploymentInfoList();
		List<EmployeeEmploymentDTO> employeeEmploymentDTOs = new ArrayList<>();
		employeeEmploymentInfoList.stream().forEach(i -> {
			EmployeeEmploymentDTO employeeEmploymentDTO = new EmployeeEmploymentDTO();
			BeanUtils.copyProperties(i, employeeEmploymentDTO);
			List<ReferencePersonDTO> referencePersonDetailsList = new ArrayList<>();
			if (i.getReferencePersonDetails() != null) {
				for (Entry<String, String> personDetails : i.getReferencePersonDetails().entrySet()) {
					referencePersonDetailsList.add(
							new ReferencePersonDTO(personDetails.getValue(), Long.parseLong(personDetails.getKey())));
				}
			}
			employeeEmploymentDTO.setReferencePersonDetails(referencePersonDetailsList);
			employeeEmploymentDTOs.add(employeeEmploymentDTO);
		});
		List<EmployeeEducationInfo> employeeEducationInfoList = employeePersonalInfo.getEmployeeEducationInfoList();
		List<EmployeeEducationDetailsDTO> educationDetailsDTOs = new ArrayList<>();
		employeeEducationInfoList.stream().forEach(i -> {
			EmployeeEducationDetailsDTO educationInformationDTO = new EmployeeEducationDetailsDTO();
			BeanUtils.copyProperties(i, educationInformationDTO);
			educationDetailsDTOs.add(educationInformationDTO);
		});
		List<EmployeeBankInfo> employeeBankInfoList = employeePersonalInfo.getEmployeeBankInfoList();
		List<BankInformationDTO> bankInformationDTOs = new ArrayList<>();
		employeeBankInfoList.stream().forEach(i -> {
			BankInformationDTO bankInformationDTO = new BankInformationDTO();
			BeanUtils.copyProperties(i, bankInformationDTO);
			bankInformationDTOs.add(bankInformationDTO);
		});

		Optional<EmployeeReferenceInfo> optionalRefrenceEmployee = employeeReferenceInfoRepo
				.findByEmployeePersonalInfoEmployeeInfoId(employeeId);
		RefrencePersonInfoDTO refrencePersonInfoDTO = new RefrencePersonInfoDTO();
		optionalRefrenceEmployee.ifPresentOrElse((e) -> {
			refrencePersonInfoDTO.setReferenceId(e.getReferenceId());
			refrencePersonInfoDTO.setEmployeeId(e.getRefferalEmployeePersonalInfo().getEmployeeInfoId());
			refrencePersonInfoDTO.setReferralName(e.getReferralName());
			refrencePersonInfoDTO.setReferedEmployeeId(e.getRefferalEmployeePersonalInfo().getEmployeeInfoId());
			refrencePersonInfoDTO.setReferedEmployeeOfficialId(
					e.getRefferalEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId());
		}, () -> new DataNotFoundException("Reference Details not found"));

		PassAndVisaDTO passAndVisaDTO = new PassAndVisaDTO();
		BeanUtils.copyProperties(employeePersonalInfo, passAndVisaDTO);
		List<EmployeeVisaInfo> employeeVisaInfoList = employeePersonalInfo.getEmployeeVisaInfoList();
		List<AddVisaInfoDTO> addVisaInfoDTOs = new ArrayList<>();
		employeeVisaInfoList.stream().forEach(i -> {
			AddVisaInfoDTO addVisaInfoDTO = new AddVisaInfoDTO();
			BeanUtils.copyProperties(i, addVisaInfoDTO);
			addVisaInfoDTOs.add(addVisaInfoDTO);
		});
		passAndVisaDTO.setVisaInfo(addVisaInfoDTOs);
		CandidateInfo candidateInfo = employeePersonalInfo.getCandidateInfo();
		List<InterviewInformationDTO> interviewInformationDTOs = new ArrayList<>();
		List<CandidateInterviewInfo> employeeInterviewInfoList = (candidateInfo == null)
				? employeePersonalInfo.getEmployeeInterviewInfoList()
				: candidateInfo.getCandidateInterviewInfoList();
		employeeInterviewInfoList.stream().forEach(i -> {
			InterviewInformationDTO interviewInformationDTO = new InterviewInformationDTO();
			BeanUtils.copyProperties(i, interviewInformationDTO);
			Map<String, String> feedbackDetails = new LinkedHashMap<>();
			for (Entry<String, String> feedbackFactors : i.getFeedback().entrySet()) {
				if (feedbackFactors.getKey().equalsIgnoreCase("overall Feedback")) {
					interviewInformationDTO.setOverallFeedback(feedbackFactors.getValue());
				} else if (feedbackFactors.getKey().equalsIgnoreCase("detailed Feedback")) {
					interviewInformationDTO.setDetailedFeedback(feedbackFactors.getValue());
				} else {
					feedbackDetails.put(feedbackFactors.getKey(), feedbackFactors.getValue());
				}
			}
			interviewInformationDTO.setFeedback(feedbackDetails);
			if (i.getEmployeePersonalInfo() != null) {
				interviewInformationDTO.setInterviewerId(i.getEmployeePersonalInfo().getEmployeeInfoId());
				interviewInformationDTO.setInterviewerName(
						i.getEmployeePersonalInfo().getFirstName() + " " + i.getEmployeePersonalInfo().getLastName());
			}
			interviewInformationDTOs.add(interviewInformationDTO);
		});
		List<CompanyEmployeeResignationDetails> companyEmployeeResignationDetailsList = employeePersonalInfo
				.getCompanyEmployeeResignationDetailsList();
		List<EmployeeNoticePeriodDTO> employeeNoticePeriodDTOs = new ArrayList<>();
		companyEmployeeResignationDetailsList.stream().forEach(i -> {
			EmployeeNoticePeriodDTO employeeNoticePeriodDTO = new EmployeeNoticePeriodDTO();
			BeanUtils.copyProperties(i, employeeNoticePeriodDTO);
			employeeNoticePeriodDTO.setNoticePeriodDuration(i.getNoticePeriodDuration().doubleValue());
			employeeNoticePeriodDTOs.add(employeeNoticePeriodDTO);
		});
		List<EmployeeAnnualSalary> annualSalaryDetails = annualSalaryRepo
				.findByEmployeePersonalInfoEmployeeInfoId(employeeId);
		EmployeeAnnualSalaryDTO employeeAnnualSalaryDTO = new EmployeeAnnualSalaryDTO();

		ArrayList<EmployeeBonusDTO> bonusList = new ArrayList<>();
		if (!annualSalaryDetails.isEmpty()) {
			EmployeeAnnualSalary employeeAnnualSalary = annualSalaryDetails.get(0);
			BeanUtils.copyProperties(employeeAnnualSalary, employeeAnnualSalaryDTO);
			List<EmployeeBonus> employeeBonusList = employeeAnnualSalary.getEmployeeBonusList();

			employeeBonusList.stream().forEach(i -> {
				EmployeeBonusDTO employeeBonusDTO = new EmployeeBonusDTO();
				BeanUtils.copyProperties(i, employeeBonusDTO);
				bonusList.add(employeeBonusDTO);
			});

			CompanyPayrollInfo companyPayrollInfo = employeeAnnualSalary.getCompanyPayrollInfo();
			if (companyPayrollInfo != null) {
				employeeAnnualSalaryDTO.setCompanyPayrollInfoId(companyPayrollInfo.getPayrollId());
				employeeAnnualSalaryDTO.setCompanyPayrollName(companyPayrollInfo.getPayrollName());
			}
			employeeAnnualSalaryDTO.setEmployeeBonusDTOList(bonusList);
			EmployeeVariablePayDTO employeeVariablePayDTO = new EmployeeVariablePayDTO();
			EmployeeVariablePay employeeVariablePay = annualSalaryDetails.get(0).getEmployeeVariablePay();
			if (employeeVariablePay != null) {
				BeanUtils.copyProperties(employeeVariablePay, employeeVariablePayDTO);
			}
			employeeAnnualSalaryDTO.setEmployeeVariablePayDTO(employeeVariablePayDTO);

		}
		AddEmployeeTerminationDetailsDTO addEmployeeTerminationDetailsDTO = new AddEmployeeTerminationDetailsDTO();
		List<EmployeeTerminationDetails> employeeTerminationDetailsList = terminationDetailsRepo
				.findByEmployeePersonalInfoEmployeeInfoId(employeeId);
		if (!employeeTerminationDetailsList.isEmpty()) {
			EmployeeTerminationDetails employeeTerminationDetails = employeeTerminationDetailsList.get(0);
			BeanUtils.copyProperties(employeeTerminationDetails, addEmployeeTerminationDetailsDTO);
		}
		List<EmployeeDocumentDetails> employeeDocument = employeeDocumentRepository.findByEmployeeIdAndCompanyId(
				employeePersonalInfo.getEmployeeInfoId() + "", employeePersonalInfo.getCompanyInfo().getCompanyId());
		ArrayList<EmployeeDocumentDTO> documentList = new ArrayList<>();
		if (!employeeDocument.isEmpty()) {
			EmployeeDocumentDetails employeeDocumentDetails = employeeDocument.get(0);
			Map<String, String> documents = employeeDocumentDetails.getDocuments();

			for (Map.Entry<String, String> entry : documents.entrySet()) {
				EmployeeDocumentDTO employeeDocumentDTO = new EmployeeDocumentDTO();
				employeeDocumentDTO.setDocumentObjectId(employeeDocumentDetails.getDocumentObjectId());
				employeeDocumentDTO.setFileType(entry.getKey());
				employeeDocumentDTO.setUrl(entry.getValue());
				documentList.add(employeeDocumentDTO);
			}
		}
		EmployeeAllDetialsDTO employeeAllDetialsDTO = new EmployeeAllDetialsDTO();
		employeeAllDetialsDTO.setPersonalInfoId(employeePersonalInfo.getEmployeeInfoId());
		employeeAllDetialsDTO.setGeneralInformation(generalInformationDTO);
		employeeAllDetialsDTO.setWorkInformation(workInformationDTO);
		employeeAllDetialsDTO.setAdditionalWorkInformation(additionalWorkInformationDTO);
		employeeAllDetialsDTO.setReportingInformation(reportingInformationDTO);
		employeeAllDetialsDTO.setPersonalInformation(personalInformationDTO);
		employeeAllDetialsDTO.setDependentInformation(dependentInformationDTOs);
		employeeAllDetialsDTO.setEmployeeEmployment(employeeEmploymentDTOs);
		employeeAllDetialsDTO.setEducationInformation(educationDetailsDTOs);
		employeeAllDetialsDTO.setBankInformation(bankInformationDTOs);
		employeeAllDetialsDTO.setRefrencePersonInfo(refrencePersonInfoDTO);
		employeeAllDetialsDTO.setPassAndVisa(passAndVisaDTO);
		employeeAllDetialsDTO.setInterviewInformation(interviewInformationDTOs);
		employeeAllDetialsDTO.setEmployeeNoticePeriod(employeeNoticePeriodDTOs);
		employeeAllDetialsDTO.setEmployeeAnnualSalary(employeeAnnualSalaryDTO);
		employeeAllDetialsDTO.setTerminationInformation(addEmployeeTerminationDetailsDTO);
		employeeAllDetialsDTO.setDocuments(documentList);
		return employeeAllDetialsDTO;

	}

	@Override
	public Boolean addAnnualSalaryInformation(EmployeeAnnualSalaryDTO employeeAnnualSalaryDTO) {

		EmployeePersonalInfo employeePersonalInfo = employeePersonalInfoRepository
				.findById(employeeAnnualSalaryDTO.getEmployeePersonalId()).orElse(null);

		if (employeePersonalInfo == null) {
			return Boolean.FALSE;
		}

		CompanyPayrollInfo companyPayrollInfo = employeePersonalInfo
				.getCompanyInfo().getCompanyPayrollInfoList().stream().filter(payroll -> Objects
						.equals(payroll.getPayrollId(), employeeAnnualSalaryDTO.getCompanyPayrollInfoId()))
				.findFirst().orElse(null);

		if (companyPayrollInfo == null) {
			throw new CustomExceptionForHr(COMPANY_PAYROLL_INFORMATION_NOT_PRESENT);
		}

		EmployeeAnnualSalary employeeAnnualSalary = new EmployeeAnnualSalary();
		BeanUtils.copyProperties(employeeAnnualSalaryDTO, employeeAnnualSalary);

		employeeAnnualSalary.setCompanyPayrollInfo(companyPayrollInfo);
		employeeAnnualSalary.setEmployeePersonalInfo(employeePersonalInfo);

		if (Boolean.TRUE.equals(employeeAnnualSalaryDTO.getIsPayEnabled())) {
			EmployeeVariablePay employeeVariablePay = new EmployeeVariablePay();
			BeanUtils.copyProperties(employeeAnnualSalaryDTO.getEmployeeVariablePayDTO(), employeeVariablePay);
			employeeAnnualSalary.setEmployeeVariablePay(employeeVariablePay);
		} else {
			employeeAnnualSalary.setEmployeeVariablePay(null);
		}

		List<EmployeeBonus> employeeBonusList = new ArrayList<>();
		if (Boolean.TRUE.equals(employeeAnnualSalaryDTO.getIsBonusEnabled())) {
			employeeAnnualSalaryDTO.getEmployeeBonusDTOList().forEach(b -> {
				EmployeeBonus employeeBonus = new EmployeeBonus();
				BeanUtils.copyProperties(b, employeeBonus);
				employeeBonus.setEmployeeAnnualSalary(employeeAnnualSalary);
				employeeBonusList.add(employeeBonus);
			});
		}
		employeeAnnualSalary.setEmployeeBonusList(employeeBonusList);
		annualSalaryRepo.save(employeeAnnualSalary);
		return Boolean.TRUE;
	}

	public List<ShiftDropDownDTO> getCompanyShifts(Long companyId) {
		List<CompanyShiftInfo> shifts = companyShiftInfoRepository.findByCompanyRuleInfoCompanyInfoCompanyId(companyId);
		List<ShiftDropDownDTO> dropDownList = new ArrayList<>();
		for (CompanyShiftInfo companyShiftInfo : shifts) {
			dropDownList.add(new ShiftDropDownDTO(companyShiftInfo.getShiftId(), companyShiftInfo.getShiftName()));
		}
		return dropDownList;
	}

	@Override
	public AdditionalWorkInformationDTO additionalWorkInformation(
			AdditionalWorkInformationDTO additionalWorkInforamtionDTO) {
		Optional<EmployeePersonalInfo> findById = employeePersonalInfoRepository
				.findById(additionalWorkInforamtionDTO.getEmployeeId());
		if (!findById.isPresent()) {
			throw new DataNotFoundException("Employee Details not present");
		}
		Optional<CompanyShiftInfo> optionalCompanyShift = companyShiftInfoRepository
				.findById(additionalWorkInforamtionDTO.getShiftId());
		if (!optionalCompanyShift.isPresent()) {
			throw new DataNotFoundException("company shift details not found");
		}
		EmployeePersonalInfo employeePersonalInfo = findById.get();
		CompanyShiftInfo companyShiftInfo = optionalCompanyShift.get();
		EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
		BeanUtils.copyProperties(additionalWorkInforamtionDTO, employeeOfficialInfo);
		employeeOfficialInfo.setCompanyShiftInfo(companyShiftInfo);
		employeePersonalInfo.setEmployeeOfficialInfo(employeeOfficialInfo);
		EmployeePersonalInfo save = employeePersonalInfoRepository.save(employeePersonalInfo);
		AdditionalWorkInformationDTO response = new AdditionalWorkInformationDTO();

		response.setShiftId(save.getEmployeeOfficialInfo().getCompanyShiftInfo().getShiftId());
		response.setEmployeeId(save.getEmployeeInfoId());
		response.setProbationEndDate(save.getEmployeeOfficialInfo().getProbationEndDate());
		response.setProbationStartDate(save.getEmployeeOfficialInfo().getProbationStartDate());
		return additionalWorkInforamtionDTO;
	}

	@Override
	public AddEmployeeTerminationDetailsDTO employeeTerminationDetails(
			AddEmployeeTerminationDetailsDTO terminationDetailsDto) {
		CompanyInfo companyInfo = companyInfoRepo.findById(terminationDetailsDto.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		List<EmployeeTerminationDetails> employeeInfoId = terminationDetailsRepo
				.findByEmployeePersonalInfoEmployeeInfoIdAndCompanyInfoCompanyId(
						terminationDetailsDto.getEmployeeInfoId(), terminationDetailsDto.getCompanyId());
		if (!employeeInfoId.isEmpty()) {
			log.info("employee already terminated");
			throw new CustomExceptionForHr("employee already terminated");
		}

		EmployeeTerminationDetails employeeTerminationDetails = new EmployeeTerminationDetails();
		List<EmployeePersonalInfo> employeePersonalDetails = employeePersonalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(terminationDetailsDto.getEmployeeInfoId(),
						companyInfo.getCompanyId());
		if (employeePersonalDetails.isEmpty()) {
			log.error("employee record not found");
			throw new CustomExceptionForHr("employee personal information not found");
		}
		EmployeePersonalInfo employeePersonalInfo = employeePersonalDetails.get(0);
		employeePersonalInfo.setIsActive(false);
		EmployeePersonalInfo personalInfo = employeePersonalInfoRepository.save(employeePersonalInfo);
		employeeTerminationDetails.setCompanyInfo(companyInfo);
		employeeTerminationDetails.setEmployeePersonalInfo(personalInfo);
		BeanUtils.copyProperties(terminationDetailsDto, employeeTerminationDetails);

		EmployeeTerminationDetails terminationDetails = terminationDetailsRepo.save(employeeTerminationDetails);
		AddEmployeeTerminationDetailsDTO addEmployeeTerminationDetailsDTO = new AddEmployeeTerminationDetailsDTO();
		BeanUtils.copyProperties(terminationDetails, addEmployeeTerminationDetailsDTO);
		addEmployeeTerminationDetailsDTO.setCompanyId(companyInfo.getCompanyId());
		addEmployeeTerminationDetailsDTO
				.setEmployeeInfoId(terminationDetails.getEmployeePersonalInfo().getEmployeeInfoId());
		return addEmployeeTerminationDetailsDTO;

	}

	public List<CompanyPayrollDropdownInfoDTO> getPayrollInfo(Long companyId) {

		List<CompanyPayrollInfoResponseDTO> companyPayrollInfos = payRollInfoRepo.findByCompanyInfoCompanyId(companyId)
				.orElseThrow(() -> new CustomExceptionForHr(COMPANY_PAYROLL_INFORMATION_NOT_PRESENT));

		List<CompanyPayrollDropdownInfoDTO> companyPayrollInfoDTOList = new ArrayList<>();

		companyPayrollInfos.stream().forEach(payrollInfo -> {
			CompanyPayrollDropdownInfoDTO companyPayrollInfoDTO = new CompanyPayrollDropdownInfoDTO();
			companyPayrollInfoDTO.setPayrollId(payrollInfo.getPayrollId());
			companyPayrollInfoDTO.setPayrollName(payrollInfo.getPayrollName());
			companyPayrollInfoDTOList.add(companyPayrollInfoDTO);
		});
		return companyPayrollInfoDTOList;
	}

	@Override
	public AddEmployeeDocumentDTO addEmployeeDocuments(AddEmployeeDocumentDTO addEmployeeDocumentDTO, Long companyId,
			Long employeeInfoId) {
		Optional<EmployeePersonalInfo> employeePersonalInfo = employeePersonalInfoRepository.findById(employeeInfoId);
		if (!employeePersonalInfo.isPresent()) {
			throw new DataNotFoundException("Employee Data Not Found");
		}
		EmployeeDocumentDetails employeeDocument = null;
		List<String> deletedURLs = addEmployeeDocumentDTO.getDeletedURLs();
		for (String url : deletedURLs) {
			uploadFile.deleteS3Folder(url);
		}
		if (addEmployeeDocumentDTO.getDocumentObjectId() != null) {
			employeeDocument = employeeDocumentRepository.findById(addEmployeeDocumentDTO.getDocumentObjectId())
					.map(emp -> {
						emp.setDocuments(addEmployeeDocumentDTO.getDocuments());
						return emp;
					})
					.orElseGet(() -> EmployeeDocumentDetails.builder().companyId(companyId)
							.employeeId(employeePersonalInfo.get().getEmployeeInfoId() + "")
							.documents(addEmployeeDocumentDTO.getDocuments()).build());
		} else {
			employeeDocument = EmployeeDocumentDetails.builder().companyId(companyId)
					.employeeId(employeePersonalInfo.get().getEmployeeInfoId() + "")
					.documents(addEmployeeDocumentDTO.getDocuments()).build();
		}
		employeeDocument = employeeDocumentRepository.save(employeeDocument);
		BeanUtils.copyProperties(employeeDocument, addEmployeeDocumentDTO);
		return addEmployeeDocumentDTO;
	}

	@Override
	public List<EmployeeDocumentDTO> addEmployeeDocuments(List<EmployeeDocumentDTO> employeeDocumentDTOList,
			Long companyId, Long employeeInfoId, MultipartFile[] files) {
		Optional<EmployeePersonalInfo> employeePersonalInfo = employeePersonalInfoRepository.findById(employeeInfoId);
		if (!employeePersonalInfo.isPresent()) {
			throw new DataNotFoundException("Employee Data Not Found");
		}

		Map<String, String> documents = getDocuments(employeeDocumentDTOList, files);

		EmployeeDocumentDetails employeeDocument = null;

		List<EmployeeDocumentDetails> employeeDocumentList = employeeDocumentRepository
				.findByEmployeeIdAndCompanyId(employeePersonalInfo.get().getEmployeeInfoId() + "", companyId);
		if (employeeDocumentList.size() > 0) {
			employeeDocument = employeeDocumentList.get(0);
			employeeDocument.setDocuments(documents);
		} else {
			employeeDocument = EmployeeDocumentDetails.builder().companyId(companyId)
					.employeeId(employeePersonalInfo.get().getEmployeeInfoId() + "").documents(documents).build();
		}

		EmployeeDocumentDetails save = employeeDocumentRepository.save(employeeDocument);
		Map<String, String> savedDocument = save.getDocuments();
		List<EmployeeDocumentDTO> employeeSaveDocumentDTOList = new ArrayList<>();
		for (Entry<String, String> employeeDocumentDetails : savedDocument.entrySet()) {
			employeeSaveDocumentDTOList.add(EmployeeDocumentDTO.builder().fileType(employeeDocumentDetails.getKey())
					.url(employeeDocumentDetails.getValue()).build());
		}
		return employeeSaveDocumentDTOList;

	}

	private Map<String, String> getDocuments(List<EmployeeDocumentDTO> employeeDocumentDTOList, MultipartFile[] files) {
		Map<String, String> documents = new LinkedHashMap<>();
		for (EmployeeDocumentDTO employeeDocumentDTO : employeeDocumentDTOList) {
			if (employeeDocumentDTO.getFileName() != null && !employeeDocumentDTO.getFileName().isEmpty()
					&& employeeDocumentDTO.getUrl() != null && !employeeDocumentDTO.getUrl().isEmpty()) {
				uploadFile.deleteS3Folder(employeeDocumentDTO.getUrl());
				List<MultipartFile> matchedFile = Arrays.stream(files).collect(Collectors.toList()).stream()
						.filter(file -> file.getOriginalFilename().equalsIgnoreCase(employeeDocumentDTO.getFileName()))
						.collect(Collectors.toList());
				if (!matchedFile.isEmpty()) {
					documents.put(employeeDocumentDTO.getFileType(), uploadFile.uploadFile(matchedFile.get(0)));
				}
			} else if (employeeDocumentDTO.getFileName() != null && !employeeDocumentDTO.getFileName().isEmpty()) {
				List<MultipartFile> matchedFile = Arrays.stream(files).collect(Collectors.toList()).stream()
						.filter(file -> file.getOriginalFilename().equalsIgnoreCase(employeeDocumentDTO.getFileName()))
						.collect(Collectors.toList());
				if (!matchedFile.isEmpty()) {
					documents.put(employeeDocumentDTO.getFileType(), uploadFile.uploadFile(matchedFile.get(0)));
				}
			} else if (employeeDocumentDTO.getUrl() != null && !employeeDocumentDTO.getFileName().isEmpty()) {
				documents.put(employeeDocumentDTO.getFileType(), employeeDocumentDTO.getUrl());
			}

		}
		return documents;
	}

	private String generatePassword() {
		return new String(Base64.encodeBase64(("" + ThreadLocalRandom.current().nextLong(100000, 1000000)).getBytes()));
	}

	@Override
	public Map<String, Boolean> getSubmittedAccessory(Long companyId, Long employeeId) {
		List<CompanyChecklistDetails> listOfCompanyCheckListDetails = checklistDetailsRepository
				.findByCompanyId(companyId);
		List<String> checklistFactor = listOfCompanyCheckListDetails.get(listOfCompanyCheckListDetails.size() - 1)
				.getChecklistFactor();
		List<CompanyEmployeeResignationDetails> employeeResignationDetailsList = resignationDetailsRepo
				.findByEmployeePersonalInfoEmployeeInfoIdAndCompanyInfoCompanyId(employeeId, companyId);
		if (employeeResignationDetailsList.isEmpty()) {
			throw new DataNotFoundException("Employee Details not Found");
		}
		CompanyEmployeeResignationDetails employeeResignationDetails = employeeResignationDetailsList
				.get(employeeResignationDetailsList.size() - 1);
		Map<String, Boolean> submittedAccessoryCheck = new HashMap<>();
		checklistFactor.forEach((i) -> submittedAccessoryCheck.put(i, false));
		checklistFactor.forEach((companyAccessory) -> {
			employeeResignationDetails.getAccessorySubmission().forEach((employeeAccessory) -> {
				if (employeeAccessory.equals(companyAccessory)) {
					submittedAccessoryCheck.put(companyAccessory, true);
				}
			});
		});
		return submittedAccessoryCheck;
	}

	@Override
	public List<String> addAccessorySubmitted(List<String> accessory, Long employeeId, Long companyId) {
		List<CompanyEmployeeResignationDetails> employeeResignationDetailsList = resignationDetailsRepo
				.findByEmployeePersonalInfoEmployeeInfoIdAndCompanyInfoCompanyId(employeeId, companyId);
		if (employeeResignationDetailsList.isEmpty()) {
			throw new DataNotFoundException("Employee Details not Found");
		}
		CompanyEmployeeResignationDetails employeeResignationDetails = employeeResignationDetailsList
				.get(employeeResignationDetailsList.size() - 1);
		Set<String> accessorySubmission = new HashSet<>(employeeResignationDetails.getAccessorySubmission());
		accessory.stream().forEach(accessorySubmission::add);
		employeeResignationDetails.setAccessorySubmission(new ArrayList<>(accessorySubmission));
		CompanyEmployeeResignationDetails save = resignationDetailsRepo.save(employeeResignationDetails);
		return save.getAccessorySubmission();
	}

}
