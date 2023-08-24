package com.te.flinko.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admin.CompanyWorkWeekRuleDTO;
import com.te.flinko.dto.admin.CompanyWorkWeekRuleNameDTO;
import com.te.flinko.dto.admin.EmployeeOfficialInfoResponseDTO;
import com.te.flinko.dto.admin.UpdateAllEmpWorkWeekRuleDTO;
import com.te.flinko.dto.admin.UpdateEmployeeWorkWeekDTO;
import com.te.flinko.entity.admin.CompanyBranchInfo;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyWorkWeekRule;
import com.te.flinko.entity.admin.WorkOffDetails;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.admin.NoCompanyPresentException;
import com.te.flinko.exception.admin.NoDeleteWorkWeekRuleException;
import com.te.flinko.exception.admin.NoEmployeeOfficialInfoException;
import com.te.flinko.exception.admin.NoEmployeePresentException;
import com.te.flinko.exception.admin.NoWorkWeekRuleException;
import com.te.flinko.exception.admin.RuleNameDefaultException;
import com.te.flinko.exception.admin.RuleNameExitException;
import com.te.flinko.exception.admin.RuleNameSameException;
import com.te.flinko.exception.admin.WorkWeekAssociateWithEmpException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.CompanyWorkWeekRuleRepository;
import com.te.flinko.repository.admin.WorkoffDetailsRepository;
import com.te.flinko.repository.employee.EmployeeOfficialInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.responsedto.admin.CompanyWorkWeekRuleResponseDto;
import com.te.flinko.responsedto.admin.WorkOffDetailsResponseDto;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Rakesh kumar Nayak
 *
 */
@Slf4j
@Service
public class CompanyWorkWeekRuleServiceImpl implements CompanyWorkWeekRuleService {

	@Autowired
	CompanyWorkWeekRuleRepository companyWorkWeekRuleRepository;
	@Autowired
	EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	CompanyInfoRepository companyInfoRepository;

	@Autowired
	EmployeeOfficialInfoRepository employeeOfficialInfoRepository;

	@Autowired
	WorkoffDetailsRepository workoffDetailsRepository;

	// creating work week rule

	@Override
	public Boolean createCompWorkWeek(CompanyWorkWeekRuleDTO companyWorkWeekRuleDto, Long companyId) {

		log.info("createCompWorkweek() method execution started, taking input :" + companyWorkWeekRuleDto + " and "
				+ companyId);

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		if (companyWorkWeekRuleRepository
				.findByRuleNameAndCompanyInfoCompanyId(companyWorkWeekRuleDto.getRuleName(), companyId).isPresent()) {
			throw new RuleNameExitException(AdminConstants.WORK_WEEK_RULE_PRESENT);

		} else {

			CompanyWorkWeekRule companyWorkWeekRule = com.te.flinko.beancopy.BeanCopy
					.objectProperties(companyWorkWeekRuleDto, CompanyWorkWeekRule.class);

			List<CompanyWorkWeekRule> companyWorkWeekRuleList = companyInfo.getCompanyWorkWeekRuleList();
			if (companyWorkWeekRuleList.isEmpty()) {

				companyWorkWeekRule.setIsDefault(true);
			} else {

				defaultCheck(companyWorkWeekRuleDto, companyId);
			}

			companyWorkWeekRule.setCompanyInfo(companyInfo);

			for (WorkOffDetails workoffDetails : companyWorkWeekRule.getWorkOffDetailsList()) {
				workoffDetails.setCompanyWorkWeekRule(companyWorkWeekRule);
			}

			CompanyWorkWeekRule save = companyWorkWeekRuleRepository.save(companyWorkWeekRule);
			log.info("createCompWorkweek() method execution ended");
			return Optional.of(save).isPresent();

		}

	}

	// fetching all work week rule of a company

	@Override
	public List<CompanyWorkWeekRuleResponseDto> getAllWorkWeekRule(Long companyId) {

		log.info("getAllWorkWeekRule() method execution started , taking parameter companyId :" + companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		List<CompanyWorkWeekRuleResponseDto> allWorkWeekRule = companyWorkWeekRuleRepository
				.getAllWorkWeekRule(companyInfo.getCompanyId()).filter(x -> !x.isEmpty())
				.orElseGet(ArrayList::new);
		if (allWorkWeekRule.isEmpty()) 
			return allWorkWeekRule;

		List<Long> ids = allWorkWeekRule.stream().map(CompanyWorkWeekRuleResponseDto::getWorkWeekRuleId)
				.collect(Collectors.toList());
		List<WorkOffDetails> list = workoffDetailsRepository.findByCompanyWorkWeekRuleWorkWeekRuleIdIn(ids);

		List<WorkOffDetailsResponseDto> list1 = new ArrayList<>();

		list.forEach(workOffDetails -> {

			WorkOffDetailsResponseDto workOffDetailsResponseDto = new WorkOffDetailsResponseDto();

			workOffDetailsResponseDto.setFullDayWorkOff(workOffDetails.getFullDayWorkOff());
			workOffDetailsResponseDto.setHalfDayWorkOff(workOffDetails.getHalfDayWorkOff());
			workOffDetailsResponseDto.setWeekId(workOffDetails.getWeekId());
			workOffDetailsResponseDto.setWeekNumber(workOffDetails.getWeekNumber());
			workOffDetailsResponseDto.setWorkWeekRuleId(workOffDetails.getCompanyWorkWeekRule().getWorkWeekRuleId());

			list1.add(workOffDetailsResponseDto);
		});

		Map<Long, List<WorkOffDetailsResponseDto>> collect = list1.stream()
				.collect(Collectors.groupingBy(WorkOffDetailsResponseDto::getWorkWeekRuleId));

		List<CompanyWorkWeekRuleResponseDto> companyWorkWeekRuleResponseDtoList = new ArrayList<>();

		allWorkWeekRule.forEach(companyWorkWeekRuleResponseDto -> {
			companyWorkWeekRuleResponseDto
					.setWorkOffDetailsList(collect.get(companyWorkWeekRuleResponseDto.getWorkWeekRuleId()));

			companyWorkWeekRuleResponseDtoList.add(companyWorkWeekRuleResponseDto);
		});

		log.info("getAllWorkWeekRule() method execution ended, returning  CompanyWorkWeekRuleResponseDto :"
				+ companyWorkWeekRuleResponseDtoList);
		return companyWorkWeekRuleResponseDtoList;

	}

	// updating work week rule

	@Override
	public Boolean updateCompanyWorkweek(CompanyWorkWeekRuleDTO companyWorkWeekRuleDto, Long companyId) {

		log.info("updateCompanyWorkweek() method started, taking input CompanyWorkWeekRuleDto:" + companyWorkWeekRuleDto
				+ " and companyId " + companyId);
		Boolean ruleNameSame = false;

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));
		if (companyWorkWeekRuleDto.getWorkWeekRuleId() == null) {
			return createCompWorkWeek(companyWorkWeekRuleDto, companyId);
		} else {
			CompanyWorkWeekRule companyWorkWeekRule = companyWorkWeekRuleRepository
					.findByWorkWeekRuleIdAndCompanyInfoCompanyId(companyWorkWeekRuleDto.getWorkWeekRuleId(), companyId)
					.orElseThrow(() -> new NoWorkWeekRuleException(AdminConstants.NO_WORK_WEEK_RULE_PRESENT));

			List<EmployeeOfficialInfo> employeeOfficialInfoList = companyWorkWeekRule.getEmployeeOfficialInfoList();
			if (!employeeOfficialInfoList.isEmpty()) {

				defaultCheck(companyWorkWeekRuleDto, companyId);
				if (!companyWorkWeekRuleDto.getIsDefault().booleanValue()) {
					throw new WorkWeekAssociateWithEmpException(
							AdminConstants.WORK_WEEK_RULE_ASSOCIATED_WITH_EMPLOYEE_NOUPDATE);
				}

			}

			List<CompanyWorkWeekRule> companyWorkWeekRuleList = companyInfo.getCompanyWorkWeekRuleList();

			for (CompanyWorkWeekRule companyWorkWeekRule1 : companyWorkWeekRuleList) {

				if (companyWorkWeekRule1.getRuleName().equalsIgnoreCase(companyWorkWeekRuleDto.getRuleName())) {

					if (companyWorkWeekRuleDto.getRuleName().equalsIgnoreCase(companyWorkWeekRule.getRuleName())) {
						continue;
					}
					ruleNameSame = true;
					break;

				}
			}

			if (ruleNameSame.booleanValue()) {
				throw new RuleNameSameException(AdminConstants.WORK_WEEK_RULE_NAME_SAME);
			} else {

				defaultCheck(companyWorkWeekRuleDto, companyId);

				companyWorkWeekRule.setRuleName(companyWorkWeekRuleDto.getRuleName());
				companyWorkWeekRule.setIsDefault(companyWorkWeekRuleDto.getIsDefault());
				List<WorkOffDetails> list1 = new ArrayList<>();

				companyWorkWeekRuleDto.getWorkOffDetailsList().forEach(workoffDetailsDto -> {
					if (workoffDetailsDto.getWeekId() == null) {
						WorkOffDetails workoffDetails = new WorkOffDetails();
						BeanUtils.copyProperties(workoffDetailsDto, workoffDetails);
						workoffDetails.setCompanyWorkWeekRule(companyWorkWeekRule);
						list1.add(workoffDetails);

					}

					companyWorkWeekRule.getWorkOffDetailsList().forEach(workoffDetails -> {

						if (Objects.equal(workoffDetailsDto.getWeekId(), workoffDetails.getWeekId())) {
							BeanUtils.copyProperties(workoffDetailsDto, workoffDetails);

						}
					});
				});

				companyWorkWeekRule.setWorkOffDetailsList(list1);

				log.info("updateCompanyWorkweek() method ended");

				return Optional.ofNullable(companyWorkWeekRuleRepository.save(companyWorkWeekRule)).isPresent();

			}
		}
	}

	// delete to work week rule
	@Override
	public Boolean deleteComanyWorkWeekRule(Long companyId, Long workWeekRuleId) {
		log.info("deleteComanyWorkWeekRule() method started taking input : companyId " + companyId
				+ " and workWeekRuleId " + workWeekRuleId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		CompanyWorkWeekRule companyWorkWeekRule = companyWorkWeekRuleRepository
				.findByWorkWeekRuleIdAndCompanyInfoCompanyId(workWeekRuleId, companyInfo.getCompanyId())
				.orElseThrow(() -> new NoWorkWeekRuleException(AdminConstants.NO_WORK_WEEK_RULE_PRESENT));

		Boolean isDefault = companyWorkWeekRule.getIsDefault() == null ? Boolean.FALSE
				: companyWorkWeekRule.getIsDefault();

		if (isDefault.booleanValue()) {
			throw new RuleNameDefaultException(AdminConstants.RULE_NAME_DEFAULT);
		}

		List<EmployeeOfficialInfo> employeeOfficialInfoList = companyWorkWeekRule.getEmployeeOfficialInfoList();
		if (employeeOfficialInfoList.isEmpty() && !isDefault.booleanValue()) {
			companyWorkWeekRuleRepository.deleteById(workWeekRuleId);
			log.info("deleteComanyWorkWeekRule() execution ended");
			return true;
		} else {
			log.info(
					"throwing user defined exception NoDeleteWorkWeekRuleException(work week rule associated with employee, cann't delete)");
			throw new NoDeleteWorkWeekRuleException(AdminConstants.WORK_WEEK_RULE_ASSOCIATED_WITH_EMPLOYEE);
		}
	}

	// fetching all employee details
	@Override
	public List<EmployeeOfficialInfoResponseDTO> getAllEmployeeDetails(Long companyId) {

		log.info("getAllEmployeeDetails() method started , taking input :companyId " + companyId);
		CompanyInfo companyInfo2 = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));
		CompanyInfo companyInfo = companyInfoRepository
				.findByCompanyIdAndEmployeePersonalInfoListIsActive(companyInfo2.getCompanyId(), true);

		if (companyInfo == null) {
			throw new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT);
		} else {

			List<EmployeePersonalInfo> employeePersonalInfoList = companyInfo.getEmployeePersonalInfoList();

			List<EmployeeOfficialInfoResponseDTO> employeeOfficialInfoResponseDtoList = new ArrayList<>();

			for (EmployeePersonalInfo employeePersonalInfo : employeePersonalInfoList) {

				if (employeePersonalInfo.getIsActive() != null && employeePersonalInfo.getIsActive() != false) {

					String firstName = employeePersonalInfo.getFirstName();
					String lastName = employeePersonalInfo.getLastName();
					String name = firstName + " " + lastName;
					EmployeeOfficialInfoResponseDTO employeeOfficialInfoResponseDto = new EmployeeOfficialInfoResponseDTO();

					employeeOfficialInfoResponseDto.setEmployeeName(name);
					employeeOfficialInfoResponseDto.setEmployeeInfoId(employeePersonalInfo.getEmployeeInfoId());

					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					if (employeeOfficialInfo != null) {

						if (employeeOfficialInfo.getOfficialId() != null) {

							employeeOfficialInfoResponseDto.setOfficialId(employeeOfficialInfo.getOfficialId());
							employeeOfficialInfoResponseDto.setDepartment(employeeOfficialInfo.getDepartment());
							employeeOfficialInfoResponseDto.setDesignation(employeeOfficialInfo.getDesignation());
							employeeOfficialInfoResponseDto.setEmployeeId(employeeOfficialInfo.getEmployeeId());
							CompanyWorkWeekRule companyWorkWeekRule = employeeOfficialInfo.getCompanyWorkWeekRule();
							if (companyWorkWeekRule != null) {
								employeeOfficialInfoResponseDto
										.setWorkWeekRuleId(companyWorkWeekRule.getWorkWeekRuleId());
								employeeOfficialInfoResponseDto.setRuleName(companyWorkWeekRule.getRuleName());
							}

							CompanyBranchInfo companyBranchInfo = employeeOfficialInfo.getCompanyBranchInfo();
							if (companyBranchInfo != null) {
								employeeOfficialInfoResponseDto.setBranchId(companyBranchInfo.getBranchId());
								employeeOfficialInfoResponseDto.setBranchName(companyBranchInfo.getBranchName());
							}

						}

						employeeOfficialInfoResponseDtoList.add(employeeOfficialInfoResponseDto);

					}

				}

			}
			log.info("getAllEmployeeDetails() method ended , returns employeeOfficialInfoResponseDtoList "
					+ employeeOfficialInfoResponseDtoList);
			return employeeOfficialInfoResponseDtoList;

		}

	}

	// fetching all work week rule of a company
	@Override
	public List<CompanyWorkWeekRuleNameDTO> getAllWorkWeekRuleName(Long companyId) {

		log.info("getAllWorkWeekRuleName() method started , taking input companyId" + companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		return companyWorkWeekRuleRepository.getWorkWeekByCompanyId(companyInfo.getCompanyId())
				.filter(x -> !x.isEmpty()).orElseGet(() -> new ArrayList<CompanyWorkWeekRuleNameDTO>());

	}

	// fetching employee details with id
	@Override
	public EmployeeOfficialInfoResponseDTO findEmployeeWithId(Long companyId, Long employeeInfoId) {

		log.info("findEmployeeWithId() method started taking input :companyId " + companyId + " and  employeeInfoId"
				+ employeeInfoId);

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		EmployeePersonalInfo employeePersonalInfo = Optional
				.ofNullable(employeePersonalInfoRepository.findByEmployeeInfoIdAndIsActiveAndCompanyInfoCompanyId(
						employeeInfoId, true, companyInfo.getCompanyId()))
				.orElseThrow(() -> new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT));
		EmployeeOfficialInfoResponseDTO employeeOfficialInfoResponseDto = new EmployeeOfficialInfoResponseDTO();

		if (employeePersonalInfo != null) {

			String firstName = employeePersonalInfo.getFirstName();
			String lastName = employeePersonalInfo.getLastName();
			String name = firstName + " " + lastName;

			employeeOfficialInfoResponseDto.setEmployeeName(name);
			employeeOfficialInfoResponseDto.setEmployeeInfoId(employeePersonalInfo.getEmployeeInfoId());

			EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();

			if (employeeOfficialInfo != null) {

				employeeOfficialInfoResponseDto.setOfficialId(employeeOfficialInfo.getOfficialId());
				employeeOfficialInfoResponseDto.setDepartment(employeeOfficialInfo.getDepartment());
				employeeOfficialInfoResponseDto.setDesignation(employeeOfficialInfo.getDesignation());
				employeeOfficialInfoResponseDto.setEmployeeId(employeeOfficialInfo.getEmployeeId());
				CompanyWorkWeekRule companyWorkWeekRule = employeeOfficialInfo.getCompanyWorkWeekRule();
				CompanyBranchInfo companyBranchInfo = employeeOfficialInfo.getCompanyBranchInfo();

				if (companyWorkWeekRule != null) {
					employeeOfficialInfoResponseDto.setWorkWeekRuleId(companyWorkWeekRule.getWorkWeekRuleId());
					employeeOfficialInfoResponseDto.setRuleName(companyWorkWeekRule.getRuleName());
				}
				if (companyBranchInfo != null) {
					employeeOfficialInfoResponseDto.setBranchId(companyBranchInfo.getBranchId());
					employeeOfficialInfoResponseDto.setBranchName(companyBranchInfo.getBranchName());
				}
			}

		}

		log.info("findEmployeeWithId() method ended , returns employeeOfficialInfoResponseDto "
				+ employeeOfficialInfoResponseDto);

		return employeeOfficialInfoResponseDto;

	}

	@Override
	public Boolean updateEmployeeWorkWeek(UpdateEmployeeWorkWeekDTO employeeOfficialInfoResponseDto, Long companyId,
			Long employeeInfoId) {

		log.info("updateEmployeeWorkWeek() method started taking input EmployeeOfficialInfoResponseDto :"
				+ employeeOfficialInfoResponseDto + " and companyId" + companyId + " and employeeInfoId"
				+ employeeInfoId);
		CompanyInfo companyInfo1 = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		EmployeePersonalInfo employeePersonalInfo = Optional
				.ofNullable(employeePersonalInfoRepository.findByEmployeeInfoIdAndIsActiveAndCompanyInfoCompanyId(
						employeeInfoId, true, companyInfo1.getCompanyId()))
				.orElseThrow(() -> new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT));

		EmployeeOfficialInfo employeeOfficialInfo = Optional.ofNullable(employeePersonalInfo.getEmployeeOfficialInfo())
				.orElseThrow(() -> new NoEmployeeOfficialInfoException(AdminConstants.NO_OFFICIAL_INFO_PRESENT));

		if (employeeOfficialInfo.getOfficialId().equals(employeeOfficialInfoResponseDto.getOfficialId())) {
			CompanyWorkWeekRule companyWorkWeekRule2 = companyWorkWeekRuleRepository
					.findByWorkWeekRuleIdAndCompanyInfoCompanyId(employeeOfficialInfoResponseDto.getWorkWeekRuleId(),
							companyId)
					.orElseThrow(() -> new NoWorkWeekRuleException(AdminConstants.NO_WORK_WEEK_RULE_PRESENT));
			employeeOfficialInfo.setCompanyWorkWeekRule(companyWorkWeekRule2);

			employeePersonalInfoRepository.save(employeePersonalInfo);

			log.info("updateEmployeeWorkWeek() method execution ended");
			return Optional.ofNullable(employeePersonalInfoRepository.save(employeePersonalInfo)).isPresent();

		}

		return false;
	}

	@Override
	public Boolean updateAllEmpWorkWeek(UpdateAllEmpWorkWeekRuleDTO updateAllEmpWorkWeekRuleDto, Long companyId) {

		log.info("updateAllEmpWorkWeek() method started , taking input UpdateAllEmpWorkWeekRuleDto :"
				+ updateAllEmpWorkWeekRuleDto + " and companyId " + companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		List<EmployeePersonalInfo> employeePersonalInfoList = companyInfo.getEmployeePersonalInfoList();

		List<Long> officialIdList = updateAllEmpWorkWeekRuleDto.getOfficialIdList();

		CompanyWorkWeekRule companyWorkWeekRule = companyWorkWeekRuleRepository
				.findByWorkWeekRuleIdAndCompanyInfoCompanyId(updateAllEmpWorkWeekRuleDto.getWorkWeekRuleId(), companyId)
				.orElseThrow(() -> new NoWorkWeekRuleException(AdminConstants.NO_WORK_WEEK_RULE_PRESENT));

		List<EmployeeOfficialInfo> employeeOfficialInfoList = new ArrayList<>();
		if (!employeePersonalInfoList.isEmpty()) {

			employeePersonalInfoList.stream().forEach(employeePersonalInfo -> {
				if (employeePersonalInfo != null) {
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					if (employeeOfficialInfo != null) {

						officialIdList.stream().forEach(officialId -> {

							if (officialId.equals(employeeOfficialInfo.getOfficialId())) {
								employeeOfficialInfo.setCompanyWorkWeekRule(companyWorkWeekRule);
							}

						});

						employeeOfficialInfoList.add(employeeOfficialInfo);
					}

				}
			});
		}

		log.info("updateAllEmpWorkWeek() method execution ended");
		return Optional.ofNullable(employeeOfficialInfoRepository.saveAll(employeeOfficialInfoList)).isPresent();

	}

	@Override
	public void defaultCheck(CompanyWorkWeekRuleDTO companyWorkWeekRuleDto, Long companyId) {

		log.info("defaultCheck() method started");

		if (companyWorkWeekRuleDto.getIsDefault().booleanValue()) {
			Optional<List<CompanyWorkWeekRule>> findByIsDefaultAndCompanyInfoCompanyId = companyWorkWeekRuleRepository
					.findByIsDefaultAndCompanyInfoCompanyId(true, companyId);
			if (findByIsDefaultAndCompanyInfoCompanyId.isPresent()) {

				List<CompanyWorkWeekRule> companyWorkWeekRulelist = new ArrayList<>();
				for (CompanyWorkWeekRule companyWorkWeekRule2 : findByIsDefaultAndCompanyInfoCompanyId.get()) {
					companyWorkWeekRule2.setIsDefault(false);
					companyWorkWeekRulelist.add(companyWorkWeekRule2);
				}
				companyWorkWeekRuleRepository.saveAll(companyWorkWeekRulelist);
			}

		}
		log.info("defaultCheck() method execution ended");
	}

}