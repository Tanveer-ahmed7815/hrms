package com.te.flinko.service.admin;

import static com.te.flinko.common.employee.EmployeeLoginConstants.EMPLYOEE_DOES_NOT_EXIST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admin.AddExistingEmployeeDataRequestDto;
import com.te.flinko.dto.admin.BranchInfoDto;
import com.te.flinko.dto.admin.DepartmentInfoDto;
import com.te.flinko.dto.admin.DesignationInfoDto;
import com.te.flinko.dto.admin.EmployeeDataDto;
import com.te.flinko.dto.admin.EmployeeOfficialInfoDTO;
import com.te.flinko.dto.admin.WorkWeekInfoDto;
import com.te.flinko.dto.admindept.ProductNameDTO;
import com.te.flinko.dto.employee.EmployeeIdDto;
import com.te.flinko.dto.employee.MailDto;
import com.te.flinko.entity.Department;
import com.te.flinko.entity.admin.CompanyBranchInfo;
import com.te.flinko.entity.admin.CompanyDesignationInfo;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyWorkWeekRule;
import com.te.flinko.entity.employee.EmployeeLoginInfo;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.exception.admin.BranchNotFoundException;
import com.te.flinko.exception.admin.CompanyNotFound;
import com.te.flinko.exception.admin.DuplicateEmployeeIdException;
import com.te.flinko.exception.admin.WorkWeekRuleNotFoundException;
import com.te.flinko.exception.employee.EmployeeNotFoundException;
import com.te.flinko.exception.employee.EmployeeOfficialInfoNotFoundException;
import com.te.flinko.repository.admin.CompanyDesignationInfoRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.DepartmentInfoRepository;
import com.te.flinko.repository.employee.EmployeeLoginInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.service.mail.employee.EmailService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tapas
 *
 */
@Slf4j
@Service

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private DepartmentInfoRepository departmentInfoRepository;

	@Autowired
	private CompanyDesignationInfoRepository companyDesignationInfoRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepo;

	@Autowired
	private EmployeeLoginInfoRepository employeeLoginInfoRepo;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	private EmailService emailService;

	@Override
	// API for Showing All User based on companyId

	public List<EmployeeDataDto> userDetails(long companyId) {
		log.info("service method userDetails of UserDetailsServiceImpl class company id is : {}", companyId);
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		List<EmployeePersonalInfo> listOfEmployeePersonalInfo1 = companyInfo.getEmployeePersonalInfoList();

		List<EmployeePersonalInfo> listOfEmployeePersonalInfo = listOfEmployeePersonalInfo1.stream()
				.sorted(Comparator.comparing(EmployeePersonalInfo::getLastModifiedDate).reversed())
				.collect(Collectors.toList());

		List<EmployeeDataDto> employeeDataDtos = new ArrayList<>();

		for (EmployeePersonalInfo employeePersonalInfo : listOfEmployeePersonalInfo) {
			EmployeeDataDto employeeDataDto = new EmployeeDataDto();
			BeanUtils.copyProperties(employeePersonalInfo, employeeDataDto);

			if (employeePersonalInfo.getEmployeeOfficialInfo() == null) {
				continue;
			}

			if (employeePersonalInfo.getEmployeeOfficialInfo() != null) {
				BeanUtils.copyProperties(employeePersonalInfo.getEmployeeOfficialInfo(), employeeDataDto);
			}

			if (employeeDataDto.getDesignation() != null && !companyInfo.getCompanyDesignationInfoList().isEmpty()) {

				Optional<Long> findFirst = companyInfo.getCompanyDesignationInfoList().stream()
						.filter(x -> x.getDesignationName().equals(employeeDataDto.getDesignation()))
						.map(CompanyDesignationInfo::getDesignationId).findFirst();

				if (!findFirst.isEmpty()) {
					employeeDataDto.setDesignationId(findFirst.get());
				}

			}

			employeeDataDto.setFirstName(employeePersonalInfo.getFirstName());
			employeeDataDto.setLastName(employeePersonalInfo.getLastName());
			employeeDataDtos.add(employeeDataDto);

		}

		log.info("service method returned values--- list of employees : {}", employeeDataDtos);
		return employeeDataDtos;
	}

	/*
	 * API for User Management Details (Find by CompanyId and officialId)
	 */
	@Override
	public EmployeeOfficialInfoDTO userManagementDetails(Long companyId, Long officialId) {
		log.info(
				"service method userManagementDetails of UserDetailsServiceImpl class company id is : {},official id is : {}",
				companyId, officialId);

		Optional<CompanyInfo> optionalCompanyInfo = companyInfoRepo.findById(companyId);
		if (!optionalCompanyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		EmployeePersonalInfo personalInfo = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoOfficialId(companyId, officialId)
				.orElseThrow(() -> new EmployeeNotFoundException(AdminConstants.EMPLOYEE_NOT_FOUND));

		EmployeeOfficialInfoDTO employeeOfficialInfoDTO1 = new EmployeeOfficialInfoDTO();

		employeeOfficialInfoDTO1.setFirstName(personalInfo.getFirstName());
		employeeOfficialInfoDTO1.setLastName(personalInfo.getLastName());

		BeanUtils.copyProperties(personalInfo, employeeOfficialInfoDTO1);

		if (personalInfo.getEmployeeOfficialInfo() == null) {
			throw new EmployeeOfficialInfoNotFoundException("employee official info not found");
		}

		BeanUtils.copyProperties(personalInfo.getEmployeeOfficialInfo(), employeeOfficialInfoDTO1);

		if (personalInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule() != null) {

			BeanUtils.copyProperties(personalInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule(),
					employeeOfficialInfoDTO1);

		}

		if (personalInfo.getEmployeeOfficialInfo().getCompanyBranchInfo() != null) {

			BeanUtils.copyProperties(personalInfo.getEmployeeOfficialInfo().getCompanyBranchInfo(),
					employeeOfficialInfoDTO1);
		}

		if (employeeOfficialInfoDTO1.getDesignation() != null
				&& !optionalCompanyInfo.get().getCompanyDesignationInfoList().isEmpty()) {

			Optional<Long> findFirst = optionalCompanyInfo.get().getCompanyDesignationInfoList().stream()
					.filter(x -> x.getDesignationName().equals(employeeOfficialInfoDTO1.getDesignation()))
					.map(CompanyDesignationInfo::getDesignationId).findFirst();

			if (!findFirst.isEmpty()) {
				employeeOfficialInfoDTO1.setDesignationId(findFirst.get());
			}

		}

		if (employeeOfficialInfoDTO1.getDepartment() != null) {
			Optional<Department> department = departmentInfoRepository
					.findByDepartmentName(employeeOfficialInfoDTO1.getDepartment());
			if (!department.isEmpty()) {
				employeeOfficialInfoDTO1.setDepartmentId(department.get().getDepartmentId());
			}

		}

		EmployeeLoginInfo findByEmployeeId = employeeLoginInfoRepo
				.findByEmployeeId(employeeOfficialInfoDTO1.getEmployeeId()).filter(y -> !y.isEmpty()).map(x -> x.get(0))
				.orElseThrow(() -> new EmployeeNotFoundException(EMPLYOEE_DOES_NOT_EXIST));

		BeanUtils.copyProperties(findByEmployeeId, employeeOfficialInfoDTO1);

		log.info("service method returned values--- employee details : {}", employeeOfficialInfoDTO1);
		return employeeOfficialInfoDTO1;
	}

	/*
	 * API for status active/inactive
	 */
	@Override
	@Transactional
	public String updateStatus(Long companyId, Long officialId, String employeeId,
			ProductNameDTO employeeStatusUpdateDTO) {
		log.info(
				"service method updateStatus of UserDetailsServiceImpl class company id is : {},official id is : {},eployee details update request : {}",
				companyId, officialId, employeeStatusUpdateDTO);

		if (!(boolean) employeeStatusUpdateDTO.getIsActive() && employeeStatusUpdateDTO.getReason() == null) {
			throw new DataNotFoundException("please provide reason for Inactive");
		}

		Optional<CompanyInfo> companyInfo = companyInfoRepo.findById(companyId);
		if (!companyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		EmployeeLoginInfo employeeLoginInfo = employeeLoginInfoRepo
				.findByEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeOfficialInfoOfficialId(
						companyId, officialId)
				.orElseThrow(() -> new EmployeeNotFoundException(AdminConstants.EMPLOYEE_NOT_FOUND));

		EmployeePersonalInfo employeePersonalInfo = employeeLoginInfo.getEmployeePersonalInfo();

		if (employeeStatusUpdateDTO.getReason() != null) {
			employeePersonalInfo.setStatus(Map.of(employeeId, employeeStatusUpdateDTO.getReason()));

		} else {
			employeePersonalInfo.setStatus(Map.of(AdminConstants.APPROVED_BY, employeeId));
		}

		employeePersonalInfo.setIsActive(employeeStatusUpdateDTO.getIsActive());

		return null;
	}

	// push all data on add existing employee

	@Override
	@Transactional
	public String addExistingEmployee(Long companyId, String employeeId,
			AddExistingEmployeeDataRequestDto addExistingEmployeeDataRequestDto) {
		log.info(
				"service method of UserDetailsServiceImpl class company id is : {},eployee details update request : {}",
				companyId, addExistingEmployeeDataRequestDto);
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		Optional<EmployeeLoginInfo> findByEmployeeId = employeeLoginInfoRepo
				.findByEmployeeIdAndEmployeePersonalInfoCompanyInfoCompanyId(
						addExistingEmployeeDataRequestDto.getEmployeeId(), companyId);

		if (!findByEmployeeId.isEmpty()) {
			throw new DuplicateEmployeeIdException("Employee id already exists");
		}

		Boolean companyWorkWeekRulePresent = true;

		if (addExistingEmployeeDataRequestDto.getWorkWeekRuleId() == null) {
			if (!companyInfo.getCompanyWorkWeekRuleList().isEmpty()) {
				Optional<CompanyWorkWeekRule> defaultCompanyWorkWeekRule = companyInfo.getCompanyWorkWeekRuleList()
						.stream().filter(CompanyWorkWeekRule::getIsDefault).findFirst();
				if (defaultCompanyWorkWeekRule.isPresent()) {
					addExistingEmployeeDataRequestDto
							.setWorkWeekRuleId(defaultCompanyWorkWeekRule.get().getWorkWeekRuleId());
				}
			} else {
				companyWorkWeekRulePresent = false;
			}
		}

		EmployeeOfficialInfo employeeOfficialInfo = new EmployeeOfficialInfo();
		BeanUtils.copyProperties(addExistingEmployeeDataRequestDto, employeeOfficialInfo);

		employeeOfficialInfo.setCompanyBranchInfo(
				UserDetailsServiceImpl.findBranch(companyInfo, addExistingEmployeeDataRequestDto.getBranchId()));

		if (Boolean.TRUE.equals(companyWorkWeekRulePresent)) {
			employeeOfficialInfo.setCompanyWorkWeekRule(UserDetailsServiceImpl.findWorkWeekRule(companyInfo,
					addExistingEmployeeDataRequestDto.getWorkWeekRuleId()));
		} else {
			employeeOfficialInfo.setCompanyWorkWeekRule(null);
		}

		EmployeePersonalInfo employeePersonalInfo = new EmployeePersonalInfo();
		BeanUtils.copyProperties(addExistingEmployeeDataRequestDto, employeePersonalInfo);
		employeePersonalInfo.setCompanyInfo(companyInfo);
		employeePersonalInfo.setEmployeeOfficialInfo(employeeOfficialInfo);

		employeePersonalInfo.setStatus(Map.of(AdminConstants.APPROVED_BY, employeeId));
		employeePersonalInfo.setIsActive(Boolean.TRUE);
		EmployeePersonalInfo savedEmployeePersonalInfo = employeePersonalInfoRepository.save(employeePersonalInfo);

		EmployeeLoginInfo employeeLoginInfo = new EmployeeLoginInfo();
		BeanUtils.copyProperties(addExistingEmployeeDataRequestDto, employeeLoginInfo);
		employeeLoginInfo.setEmployeePersonalInfo(savedEmployeePersonalInfo);
		employeeLoginInfo.setCurrentPassword(generatePassword());

		EmployeeLoginInfo saveEmployeeLoginInfo = employeeLoginInfoRepo.save(employeeLoginInfo);

		String toMail = saveEmployeeLoginInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId();

		if (toMail != null) {
			MailDto mailDto = new MailDto();
			mailDto.setTo(toMail);
			mailDto.setSubject("Your password For Verification");
			mailDto.setBody("Dear " + saveEmployeeLoginInfo.getEmployeePersonalInfo().getFirstName() + " "
					+ saveEmployeeLoginInfo.getEmployeePersonalInfo().getLastName() + ",\r\n" + "\r\n"
					+ "your official email ID login credentials. Please login and access FLiNKO. If you are unable to login, please share a screenshot of the error page to projectmailer2021@gmail.com"
					+ "\r\n" + "\r\n" + "Your Employee Id :" + saveEmployeeLoginInfo.getEmployeeId() + "\r\n"
					+ " Your Default Password :" + saveEmployeeLoginInfo.getCurrentPassword() + "\r\n" + "\r\n"
					+ "Kindly use the FLINKO official email id for any further communications." + "\r\n"
					+ "Thanks and Regards," + "\r\n" + "Team FLINKO");
			emailService.sendMail(mailDto);

		}

		return null;
	}

	private String generatePassword() {
		return new String(Base64.encodeBase64(("" + ThreadLocalRandom.current().nextLong(100000, 1000000)).getBytes()));
	}

	public static CompanyBranchInfo findBranch(CompanyInfo companyInfo, Long branchId) {
		List<CompanyBranchInfo> companyBranchInfoList = companyInfo.getCompanyBranchInfoList();
		CompanyBranchInfo companyBranchInfo1;

		List<Long> listOfBranch = companyBranchInfoList.stream().map(CompanyBranchInfo::getBranchId)
				.collect(Collectors.toList());

		if (branchId == null) {
			throw new BranchNotFoundException(AdminConstants.COMPANY_BRANCH_NOT_FOUND);
		}

		int indexOfBranch = listOfBranch.indexOf(branchId);

		if (indexOfBranch < 0) {
			throw new BranchNotFoundException(AdminConstants.COMPANY_BRANCH_NOT_FOUND);
		} else {
			companyBranchInfo1 = companyBranchInfoList.get(indexOfBranch);
		}
		return companyBranchInfo1;
	}

	public static CompanyWorkWeekRule findWorkWeekRule(CompanyInfo companyInfo, Long workWeekRuleId) {
		CompanyWorkWeekRule companyWorkWeekRule;

		List<CompanyWorkWeekRule> companyWorkWeekRuleList = companyInfo.getCompanyWorkWeekRuleList();

		List<Long> listOfRule = companyWorkWeekRuleList.stream().map(CompanyWorkWeekRule::getWorkWeekRuleId)
				.collect(Collectors.toList());

		if (workWeekRuleId == null) {
			throw new WorkWeekRuleNotFoundException(AdminConstants.WORK_WEEK_RULE_NOT_FOUND);
		}

		int indexOfRuleName = listOfRule.indexOf(workWeekRuleId);

		if (indexOfRuleName < 0) {
			throw new WorkWeekRuleNotFoundException(AdminConstants.WORK_WEEK_RULE_NOT_FOUND);
		} else {
			companyWorkWeekRule = companyWorkWeekRuleList.get(indexOfRuleName);
		}
		return companyWorkWeekRule;
	}

	/*
	 * API for update user
	 */

	@Override
	@Transactional
	public String updateUserDetails(Long companyId, Long officialId, String employeeId,
			EmployeeOfficialInfoDTO employeeOfficialInfoDTO) {

		log.info(
				"service method of UserDetailsServiceImpl class company id is : {},official id is : {},eployee details update request : {}",
				companyId, officialId, employeeOfficialInfoDTO);

		Optional<CompanyInfo> companyInfo = companyInfoRepo.findById(companyId);
		if (!companyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		employeeOfficialInfoDTO.setOfficialId(officialId);

		EmployeeLoginInfo employeeLoginInfo = employeeLoginInfoRepo
				.findByEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeOfficialInfoOfficialId(
						companyId, officialId)
				.orElseThrow(() -> new EmployeeNotFoundException(AdminConstants.EMPLOYEE_NOT_FOUND));

		boolean sendMail = false;

		if (employeeOfficialInfoDTO.getEmployeeId()
				.equals(employeeLoginInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId())) {
			sendMail = true;
		}

		Boolean companyWorkWeekRulePresent = true;

		if (employeeOfficialInfoDTO.getWorkWeekRuleId() == null) {
			if (!companyInfo.get().getCompanyWorkWeekRuleList().isEmpty()) {
				Optional<CompanyWorkWeekRule> defaultCompanyWorkWeekRule = companyInfo.get()
						.getCompanyWorkWeekRuleList().stream().filter(CompanyWorkWeekRule::getIsDefault).findFirst();
				if (defaultCompanyWorkWeekRule.isPresent()) {
					employeeOfficialInfoDTO.setWorkWeekRuleId(defaultCompanyWorkWeekRule.get().getWorkWeekRuleId());
				}
			} else {
				companyWorkWeekRulePresent = false;
			}
		}

		String password = employeeLoginInfo.getCurrentPassword();

		EmployeePersonalInfo employeePersonalInfo = employeeLoginInfo.getEmployeePersonalInfo();

		Optional<EmployeeLoginInfo> findByEmployeeId = employeeLoginInfoRepo
				.findByEmployeeIdAndEmployeePersonalInfoCompanyInfoCompanyId(employeeOfficialInfoDTO.getEmployeeId(),
						companyId);

		if (!findByEmployeeId.isEmpty() && !findByEmployeeId.get().getEmployeePersonalInfo().getEmployeeOfficialInfo()
				.getOfficialId().equals(employeeOfficialInfoDTO.getOfficialId())) {
			throw new DuplicateEmployeeIdException("Employee id already exists");
		}

		employeeOfficialInfoDTO.setIsActive(employeePersonalInfo.getIsActive());

		EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();

		BeanUtils.copyProperties(employeeOfficialInfoDTO, employeeOfficialInfo);
		employeeOfficialInfo.setCompanyBranchInfo(
				UserDetailsServiceImpl.findBranch(companyInfo.get(), employeeOfficialInfoDTO.getBranchId()));

		if (Boolean.TRUE.equals(companyWorkWeekRulePresent)) {
			employeeOfficialInfo.setCompanyWorkWeekRule(UserDetailsServiceImpl.findWorkWeekRule(companyInfo.get(),
					employeeOfficialInfoDTO.getWorkWeekRuleId()));
		} else {
			employeeOfficialInfo.setCompanyWorkWeekRule(null);
		}

		BeanUtils.copyProperties(employeeOfficialInfoDTO, employeePersonalInfo);

		employeePersonalInfo.setCompanyInfo(companyInfo.get());
		employeePersonalInfo.setEmployeeOfficialInfo(employeeOfficialInfo);

		employeePersonalInfo.setStatus(Map.of(AdminConstants.APPROVED_BY, employeeId));

		BeanUtils.copyProperties(employeeOfficialInfoDTO, employeeLoginInfo);
		employeePersonalInfo.setIsActive(Boolean.TRUE);
		employeeLoginInfo.setEmployeePersonalInfo(employeePersonalInfo);

		employeeLoginInfo.setCurrentPassword(password);

		String toMail = employeeLoginInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId();

		if (toMail != null && !sendMail) {

			MailDto mailDto = new MailDto();
			mailDto.setTo(toMail);
			mailDto.setSubject("Your password For Verification");
			mailDto.setBody("Dear " + employeeLoginInfo.getEmployeePersonalInfo().getFirstName() + " "
					+ employeeLoginInfo.getEmployeePersonalInfo().getLastName() + ",\r\n" + "\r\n"
					+ "your official email ID login credentials. Please login and access FLiNKO. If you are unable to login, please share a screenshot of the error page to projectmailer2021@gmail.com"
					+ "\r\n" + "\r\n" + "Your Employee Id :" + employeeLoginInfo.getEmployeeId() + "\r\n"
					+ " Your Default Password :" + employeeLoginInfo.getCurrentPassword() + "\r\n" + "\r\n"
					+ "Kindly use the FLINKO official email id for any further communications." + "\r\n"
					+ "Thanks and Regards," + "\r\n" + "Team FLINKO");
			emailService.sendMail(mailDto);

		}
		log.info("service method returned values--- employee details : {}", employeeLoginInfo);
		return null;
	}

	@Override
	public List<BranchInfoDto> getAllBranchInfo(Long companyId) {
		log.info("service method getAllBranchInfo of UserDetailsServiceImpl class company id is : {}", companyId);
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		List<CompanyBranchInfo> companyBranchInfoList = companyInfo.getCompanyBranchInfoList();

		List<BranchInfoDto> branchInfoDtos = new ArrayList<>();
		for (CompanyBranchInfo companyBranchInfo : companyBranchInfoList) {
			BranchInfoDto branchInfoDto = new BranchInfoDto();
			branchInfoDto.setBranchId(companyBranchInfo.getBranchId());
			branchInfoDto.setBranchName(companyBranchInfo.getBranchName());
			branchInfoDtos.add(branchInfoDto);
		}
		return branchInfoDtos;
	}

	@Override
	public List<DepartmentInfoDto> getAllDepartmentInfo(Long companyId) {

		log.info("service method getAllDepartmentInfo of UserDetailsServiceImpl class company id is : {}", companyId);

		Optional<CompanyInfo> optionalCompanyInfo = companyInfoRepo.findById(companyId);
		if (!optionalCompanyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		List<DepartmentInfoDto> departmentInfoDtos = new ArrayList<>();
		for (Department department : departmentInfoRepository.findAll()) {

			DepartmentInfoDto departmentInfoDto = new DepartmentInfoDto();
			departmentInfoDto.setDepartmentId(department.getDepartmentId());
			departmentInfoDto.setDepartmentName(department.getDepartmentName());
			departmentInfoDtos.add(departmentInfoDto);
		}
		return departmentInfoDtos;
	}

	@Override
	public List<DesignationInfoDto> getAllDesignationInfo(Long companyId, String department) {

		log.info("service method getAllDesignationInfo of UserDetailsServiceImpl class company id is : {}", companyId);

		Optional<CompanyInfo> optionalCompanyInfo = companyInfoRepo.findById(companyId);
		if (!optionalCompanyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		List<CompanyDesignationInfo> companyDesignationInfoList = companyDesignationInfoRepository
				.findByDepartmentAndCompanyInfoCompanyId(department, companyId);
		List<DesignationInfoDto> designationInfoDtos = new ArrayList<>();
		for (CompanyDesignationInfo companyDesignationInfo : companyDesignationInfoList) {

			DesignationInfoDto designationInfoDto = new DesignationInfoDto();
			designationInfoDto.setDesignationId(companyDesignationInfo.getDesignationId());
			designationInfoDto.setDesignationName(companyDesignationInfo.getDesignationName());
			designationInfoDto.setRoles(companyDesignationInfo.getRoles());
			designationInfoDtos.add(designationInfoDto);
		}
		return designationInfoDtos;
	}

	@Override
	public List<WorkWeekInfoDto> getAllWorkInfo(Long companyId) {

		log.info("service method getAllWorkInfo of UserDetailsServiceImpl class company id is : {}", companyId);
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		List<CompanyWorkWeekRule> companyWorkWeekRuleList = companyInfo.getCompanyWorkWeekRuleList();
		List<WorkWeekInfoDto> weekInfoDtos = new ArrayList<>();
		for (CompanyWorkWeekRule companyWorkWeekRule : companyWorkWeekRuleList) {

			WorkWeekInfoDto workWeekInfoDto = new WorkWeekInfoDto();
			workWeekInfoDto.setWorkWeekRuleId(companyWorkWeekRule.getWorkWeekRuleId());
			workWeekInfoDto.setRuleName(companyWorkWeekRule.getRuleName());
			weekInfoDtos.add(workWeekInfoDto);
		}
		return weekInfoDtos;
	}

	@Override
	public List<EmployeeIdDto> getAllEmployeeName(Long companyId) {
		return employeePersonalInfoRepository.getEmployeeNames(companyId).orElse(Collections.emptyList());
	}

}
