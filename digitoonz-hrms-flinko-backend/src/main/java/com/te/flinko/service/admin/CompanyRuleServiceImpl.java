package com.te.flinko.service.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.te.flinko.common.admin.CompanyInfoConstants;
import com.te.flinko.dto.admin.CompanyLeaveInfoDto;
import com.te.flinko.dto.admin.CompanyRulesDto;
import com.te.flinko.dto.admin.CompanyShiftInfoDTO;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyLeaveInfo;
import com.te.flinko.entity.admin.CompanyRuleInfo;
import com.te.flinko.entity.admin.CompanyShiftInfo;
import com.te.flinko.entity.employee.EmployeePerformanceRule;
import com.te.flinko.exception.ComapnyNameAlreadyExistsException;
import com.te.flinko.exception.CompanyNotFoundException;
import com.te.flinko.exception.CompanyRulesExistsException;
import com.te.flinko.exception.admin.DuplicateShiftNameException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.CompanyLeaveInfoRepository;
import com.te.flinko.repository.admin.CompanyRuleRepository;
import com.te.flinko.repository.admin.CompanyShiftInfoRepository;
import com.te.flinko.repository.employee.EmployeePerformanceRuleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Brunda
 *
 */

@Slf4j
@Service
public class CompanyRuleServiceImpl implements CompanyRuleService {

	@Autowired
	private EmployeePerformanceRuleRepository employeePerformanceRuleRepository;

	@Autowired
	private CompanyRuleRepository companyRuleRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private CompanyLeaveInfoRepository companyLeaveInfoRepository;

	@Autowired
	private CompanyShiftInfoRepository companyShiftInfoRepository;

	@Override
	public CompanyRulesDto getCompanyRules(Long companyId) {
		log.info("Service Layer, Fetch company rules with respect to company id: " + companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(CompanyInfoConstants.COMPANY_NOT_FOUND));
		CompanyRuleInfo companyRuleInfo = companyInfo.getCompanyRuleInfo();
		CompanyRulesDto companyRulesDto = new CompanyRulesDto();
		BeanUtils.copyProperties(companyRuleInfo, companyRulesDto);

		companyRulesDto
				.setIsCasualLeaveEarnedLeave(companyRuleInfo.getIsCasualLeaveEarnedLeave() == null ? Boolean.FALSE
						: companyRuleInfo.getIsCasualLeaveEarnedLeave().booleanValue());

		EmployeePerformanceRule employeePerformanceRule = companyRuleInfo.getEmployeePerformanceRule();
		BeanUtils.copyProperties(employeePerformanceRule, companyRulesDto);

		List<CompanyLeaveInfo> companyLeaveInfoList = companyRuleInfo.getCompanyLeaveInfoList();
		List<CompanyLeaveInfoDto> companyLeaveInfoDtos = new ArrayList<>();

		companyLeaveInfoList.forEach(companyLeaveInfo -> {
			CompanyLeaveInfoDto companyLeaveInfoDto = new CompanyLeaveInfoDto();
			BeanUtils.copyProperties(companyLeaveInfo, companyLeaveInfoDto);
			companyLeaveInfoDtos.add(companyLeaveInfoDto);
		});

		companyRulesDto.setCompanyLeaveInfoList(companyLeaveInfoDtos);

		List<CompanyShiftInfo> companyShiftInfos = companyRuleInfo.getCompanyShiftInfoList();
		List<CompanyShiftInfoDTO> companyShiftInfoDtos = new ArrayList<>();

		companyShiftInfos.forEach(companyShiftInfo -> {
			CompanyShiftInfoDTO companyShiftInfoDto = new CompanyShiftInfoDTO();
			BeanUtils.copyProperties(companyShiftInfo, companyShiftInfoDto);
			companyShiftInfoDtos.add(companyShiftInfoDto);
		});

		companyRulesDto.setCompanyShiftInfoList(companyShiftInfoDtos);
		companyRulesDto.setIsSubmited(companyInfo.getIsSubmited());
		log.info("Service Layer, company rules: " + companyRulesDto);
		return companyRulesDto;
	}

	@Transactional
	@Override
	public String updateCompanyRule(Long companyId, CompanyRulesDto companyRulesDto) {
		log.info("Service Layer, Update company rules with respect to company id: " + companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.map(company -> Optional.of(company.getIsSubmited() == null || company.getIsSubmited()).filter(x -> !x)
						.map(y -> company).orElseThrow(
								() -> new CompanyRulesExistsException(CompanyInfoConstants.COMPANY_ALREADY_CONFIGURED)))
				.orElseThrow(() -> new ComapnyNameAlreadyExistsException(CompanyInfoConstants.COMPANY_NOT_FOUND));

		CompanyRuleInfo companyRuleInfo = Optional.ofNullable(companyInfo.getCompanyRuleInfo())
				.orElseGet(CompanyRuleInfo::new);
		if (companyRulesDto.getRuleId() == null || companyRulesDto.getRuleId() == 0)
			companyRulesDto.setRuleId(companyRuleInfo.getRuleId());
		BeanUtils.copyProperties(companyRulesDto, companyRuleInfo);

		EmployeePerformanceRule employeePerformanceRule2 = Optional
				.ofNullable(companyRuleInfo.getEmployeePerformanceRule()).orElseGet(EmployeePerformanceRule::new);
		BeanUtils.copyProperties(companyRulesDto, employeePerformanceRule2);
		employeePerformanceRule2.setCompanyRuleInfo(companyRuleInfo);

		List<CompanyShiftInfo> companyShiftInfos = new ArrayList<>();
		List<Long> shiftId = new ArrayList<>();
		List<Long> shiftDtoId = new ArrayList<>();

		List<String> shiftNameLists = companyRulesDto.getCompanyShiftInfoList().stream().map(CompanyShiftInfoDTO::getShiftName)
				.collect(Collectors.toList());

		Set<String> shiftNameSetDto = new HashSet<>(shiftNameLists);

		if (shiftNameSetDto.size() < shiftNameLists.size()) {
			throw new DuplicateShiftNameException("Duplicate shift name is present");
		}
		companyRulesDto.getCompanyShiftInfoList().forEach(companyShiftInfoDto -> {
			shiftDtoId.add(companyShiftInfoDto.getShiftId());

			if (companyShiftInfoDto.getShiftId() == null) {
				CompanyShiftInfo companyShiftInfo = new CompanyShiftInfo();

				BeanUtils.copyProperties(companyShiftInfoDto, companyShiftInfo);
				companyShiftInfo.setCompanyRuleInfo(companyRuleInfo);
				companyShiftInfos.add(companyShiftInfo);
			}
			companyRuleInfo.setCompanyInfo(companyInfo);
			Optional.ofNullable(companyRuleInfo.getCompanyShiftInfoList()).orElseGet(ArrayList::new)
					.forEach(companyShiftInfo -> {
						shiftId.add(companyShiftInfo.getShiftId());
						if (com.google.common.base.Objects.equal(companyShiftInfoDto.getShiftId(),
								companyShiftInfo.getShiftId())) {
							BeanUtils.copyProperties(companyShiftInfoDto, companyShiftInfo);
						}
					});
		});

		List<Long> shiftDuplicateValues = duplicateValue(shiftDtoId, shiftId);
		companyRuleInfo.setCompanyShiftInfoList(companyShiftInfos);

		List<CompanyLeaveInfo> companyLeaveInfos = new ArrayList<>();
		List<Long> leaveId = new ArrayList<>();
		List<Long> leaveDtoId = new ArrayList<>();

		companyRulesDto.getCompanyLeaveInfoList().forEach(companyLeaveInfoDto -> {
			leaveDtoId.add(companyLeaveInfoDto.getLeaveId());
			if (companyLeaveInfoDto.getLeaveId() == null) {
				CompanyLeaveInfo companyLeaveInfo = new CompanyLeaveInfo();
				BeanUtils.copyProperties(companyLeaveInfoDto, companyLeaveInfo);
				companyLeaveInfo.setCompanyRuleInfo(companyRuleInfo);
				companyLeaveInfos.add(companyLeaveInfo);
			}
			Optional.ofNullable(companyRuleInfo.getCompanyLeaveInfoList()).orElseGet(ArrayList::new)
					.forEach(companyLeaveInfo -> {
						leaveId.add(companyLeaveInfo.getLeaveId());
						if (com.google.common.base.Objects.equal(companyLeaveInfoDto.getLeaveId(),
								companyLeaveInfo.getLeaveId())) {
							BeanUtils.copyProperties(companyLeaveInfoDto, companyLeaveInfo);
						}
					});
		});

		List<Long> leaveDuplicateValues = duplicateValue(leaveDtoId, leaveId);
		companyRuleInfo.setCompanyLeaveInfoList(companyLeaveInfos);

		EmployeePerformanceRule saveEmployeePerformanceRule = employeePerformanceRuleRepository
				.save(employeePerformanceRule2);
		companyRuleInfo.setEmployeePerformanceRule(saveEmployeePerformanceRule);
		CompanyRuleInfo saveCompanyRuleInfo = companyRuleRepository.save(companyRuleInfo);
		saveEmployeePerformanceRule.setCompanyRuleInfo(saveCompanyRuleInfo);

		companyShiftInfoRepository.deleteAllByIdInBatch(shiftDuplicateValues);
		companyLeaveInfoRepository.deleteAllByIdInBatch(leaveDuplicateValues);
		log.info("Service Layer, Updated company rules: " + companyRulesDto);
		return Objects.nonNull(companyRulesDto.getRuleId()) ? "Update Company Rule Successfully!!!"
				: "Add Company Rule Successfully!!!";
	}

	public static List<Long> duplicateValue(List<Long> dtoId, List<Long> id) {
		Set<Long> set = new HashSet<>(id);
		Set<Long> set1 = new HashSet<>(dtoId);
		List<Long> duplicateList = new ArrayList<>();
		for (Long databaseId : set) {
			boolean contains = set1.contains(databaseId);
			if (!contains) {
				duplicateList.add(databaseId);
			}
		}
		return duplicateList;
	}
}