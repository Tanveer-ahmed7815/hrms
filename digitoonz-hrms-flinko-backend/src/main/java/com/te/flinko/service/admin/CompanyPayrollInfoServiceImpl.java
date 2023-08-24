package com.te.flinko.service.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.beancopy.BeanCopy;
import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admin.CompanyPayrollDeductionDTO;
import com.te.flinko.dto.admin.CompanyPayrollEarningDTO;
import com.te.flinko.dto.admin.CompanyPayrollInfoDTO;
import com.te.flinko.dto.admin.CompanyPayrollInfoResponseDTO;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyPayrollDeduction;
import com.te.flinko.entity.admin.CompanyPayrollEarning;
import com.te.flinko.entity.admin.CompanyPayrollInfo;
import com.te.flinko.exception.admin.NoCompanyPresentException;
import com.te.flinko.exception.admin.NoPayrollAvailableException;
import com.te.flinko.exception.admin.PayrollDeductionTitleSameException;
import com.te.flinko.exception.admin.PayrollEarningSalaryComponentSameException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.CompanyPayRollInfoRepository;
import com.te.flinko.repository.admin.CompanyPayrollDeductionRepository;
import com.te.flinko.repository.admin.CompanyPayrollEarningRepository;

import lombok.extern.slf4j.Slf4j;

//@author Rakesh Kumar Nayak

@Slf4j
@Service
public class CompanyPayrollInfoServiceImpl implements CompanyPayrollInfoService {

	@Autowired
	CompanyPayRollInfoRepository companyPayRollInfoRepository;
	@Autowired
	CompanyInfoRepository companyInfoRepository;
	@Autowired
	CompanyPayrollEarningRepository companyPayrollEarningRepository;
	@Autowired
	CompanyPayrollDeductionRepository companyPayrollDeductionRepository;

	@Override
	public Boolean createPayrollInfo(CompanyPayrollInfoDTO companyPayrollInfoDto, Long companyId) {

		log.info("createPayrollInfo() method execution started");

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		duplicatePayrollDeductionTitle(companyPayrollInfoDto);

		duplicatePayrollEarningSalaryComponent(companyPayrollInfoDto);

		CompanyPayrollInfo companyPayrollInfo = BeanCopy.objectProperties(companyPayrollInfoDto,
				CompanyPayrollInfo.class);
		companyPayrollInfo.setCompanyInfo(companyInfo);
		companyPayrollInfo.getCompanyPayrollEarningList().stream()
				.forEach(companyPayrollEarning -> companyPayrollEarning.setCompanyPayrollInfo(companyPayrollInfo));
		companyPayrollInfo.getCompanyPayrollDeductionList().stream()
				.forEach(companyPayrollDeduction -> companyPayrollDeduction.setCompanyPayrollInfo(companyPayrollInfo));

		log.info("createPayrollInfo() method execution ended");
		return Optional.ofNullable(companyPayRollInfoRepository.save(companyPayrollInfo)).isPresent();

	}

	@Override
	public CompanyPayrollInfoResponseDTO getAllCompanyPayrollInfo(Long companyId) {
		log.info("getAllCompanyPayrollInfo() method execution started ,taking input companyId :" + companyId);
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		List<CompanyPayrollInfoResponseDTO> companyPayrollInfoResponseDTO = companyPayRollInfoRepository
				.findByCompanyInfoCompanyId(companyInfo.getCompanyId()).filter(x -> !x.isEmpty())
				.orElseThrow(() -> new NoPayrollAvailableException(AdminConstants.NO_PAYROLL_AVAILABLE));

		CompanyPayrollInfoResponseDTO companyPayrollInfoResponseDTO2 = companyPayrollInfoResponseDTO.get(0);

		List<CompanyPayrollEarningDTO> companyPayrollEarningDTO = companyPayrollEarningRepository
				.findEarningByPayrollInfoId(companyPayrollInfoResponseDTO2.getPayrollId()).filter(x -> !x.isEmpty())
				.orElseThrow(() -> new NoPayrollAvailableException("No Earning Details!!!"));

		List<CompanyPayrollDeductionDTO> companyPayrollDeductionDTO = companyPayrollDeductionRepository
				.findDeductionByPayrollInfoId(companyPayrollInfoResponseDTO2.getPayrollId()).filter(x -> !x.isEmpty())
				.orElseThrow(() -> new NoPayrollAvailableException("No Deduction Details!!!"));

		companyPayrollInfoResponseDTO2.setCompanyPayrollEarningList(companyPayrollEarningDTO);
		companyPayrollInfoResponseDTO2.setCompanyPayrollDeductionList(companyPayrollDeductionDTO);

		log.info("getAllCompanyPayrollInfo() method execution enede , returns companyPayrollInfoResponseDtoList :"
				+ companyPayrollInfoResponseDTO2);
		companyPayrollInfoResponseDTO2.setIsSubmited(companyInfo.getIsSubmited());
		return companyPayrollInfoResponseDTO2;
	}

	@Override
	public Boolean updateCompanyPayrollInfo(CompanyPayrollInfoDTO companyPayrollInfoDto, Long companyId) {

		if (companyPayrollInfoDto.getPayrollId() == null || companyPayrollInfoDto.getPayrollId() == 0) {
			return createPayrollInfo(companyPayrollInfoDto, companyId);
		}

		log.info("updateCompanyPayrollInfo() method started taking inputd :CompanyPayrollInfoDto :"
				+ companyPayrollInfoDto + " and companyId :" + companyId);

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		duplicatePayrollDeductionTitle(companyPayrollInfoDto);

		duplicatePayrollEarningSalaryComponent(companyPayrollInfoDto);

		CompanyPayrollInfo companyPayrollInfo = companyPayRollInfoRepository
				.findByPayrollIdAndCompanyInfoCompanyId(companyPayrollInfoDto.getPayrollId(),
						companyInfo.getCompanyId())
				.orElseThrow(() -> new NoPayrollAvailableException(AdminConstants.NO_PAYROLL_AVAILABLE));

		BeanUtils.copyProperties(companyPayrollInfoDto, companyPayrollInfo);

		List<CompanyPayrollEarning> list1 = new ArrayList<>();
		List<Long> payrollEarningDtoId = new ArrayList<>();
		List<Long> payrollEarningId = new ArrayList<>();

		companyPayrollInfoDto.getCompanyPayrollEarningList().forEach(companyPayrollEarningDto -> {

			payrollEarningDtoId.add(companyPayrollEarningDto.getEarningId());

			if (companyPayrollEarningDto.getEarningId() == null) {
				CompanyPayrollEarning companyPayrollEarning1 = new CompanyPayrollEarning();
				BeanUtils.copyProperties(companyPayrollEarningDto, companyPayrollEarning1);
				companyPayrollEarning1.setCompanyPayrollInfo(companyPayrollInfo);
				list1.add(companyPayrollEarning1);
			}
			companyPayrollInfo.getCompanyPayrollEarningList().forEach(companyPayrollEarning -> {
				payrollEarningId.add(companyPayrollEarning.getEarningId());

				if (Objects.equals(companyPayrollEarningDto.getEarningId(), companyPayrollEarning.getEarningId())) {
					BeanUtils.copyProperties(companyPayrollEarningDto, companyPayrollEarning);
				}
			});

		});

		List<Long> duplicateValueEarning = duplicateValue(payrollEarningDtoId, payrollEarningId);
		companyPayrollInfo.setCompanyPayrollEarningList(list1);

		List<CompanyPayrollDeduction> list2 = new ArrayList<>();
		List<Long> payrollDeductionDtoId = new ArrayList<>();
		List<Long> payrolldeductionId = new ArrayList<>();

		companyPayrollInfoDto.getCompanyPayrollDeductionList().forEach(companyPayrollDeductionDto -> {

			payrollDeductionDtoId.add(companyPayrollDeductionDto.getDeductionId());
			if (companyPayrollDeductionDto.getDeductionId() == null) {
				CompanyPayrollDeduction companyPayrollDeduction1 = new CompanyPayrollDeduction();
				BeanUtils.copyProperties(companyPayrollDeductionDto, companyPayrollDeduction1);
				companyPayrollDeduction1.setCompanyPayrollInfo(companyPayrollInfo);
				list2.add(companyPayrollDeduction1);
			}
			companyPayrollInfo.getCompanyPayrollDeductionList().forEach(companyPayrollDeduction -> {

				payrolldeductionId.add(companyPayrollDeduction.getDeductionId());

				if (Objects.equals(companyPayrollDeductionDto.getDeductionId(),
						companyPayrollDeduction.getDeductionId())) {
					BeanUtils.copyProperties(companyPayrollDeductionDto, companyPayrollDeduction);
				}
			});
		});

		List<Long> duplicateValueDeduction = duplicateValue(payrollDeductionDtoId, payrolldeductionId);
		companyPayrollInfo.setCompanyPayrollDeductionList(list2);

		companyPayrollEarningRepository.deleteAllByIdInBatch(duplicateValueEarning);
		companyPayrollDeductionRepository.deleteAllByIdInBatch(duplicateValueDeduction);

		log.info("updateCompanyPayrollInfo() method execution ended");
		return Optional.ofNullable(companyPayRollInfoRepository.save(companyPayrollInfo)).isPresent();

	}

	public static List<Long> duplicateValue(List<Long> dtoId, List<Long> dbId) {
		log.info("duplicateValue() method started");
		Set<Long> set1 = new HashSet<>(dbId);
		Set<Long> set2 = new HashSet<>(dtoId);
		List<Long> duplicateList = new ArrayList<>();
		set1.forEach(long1 -> {
			boolean contains = set2.contains(long1);
			if (!contains) {
				duplicateList.add(long1);
			}
		});
		log.info("duplicateValue() method execution ended");
		return duplicateList;
	}

	public void duplicatePayrollEarningSalaryComponent(CompanyPayrollInfoDTO companyPayrollInfoDto) {
		log.info("duplicatePayrollEarningSalaryComponent() method execution started");
		Boolean earning = true;
		List<CompanyPayrollEarningDTO> companyPayrollEarningList = companyPayrollInfoDto.getCompanyPayrollEarningList();

		Set<String> salartComponentSet = new HashSet<>();
		for (CompanyPayrollEarningDTO companyPayrollEarning : companyPayrollEarningList) {
			String salaryComponent = companyPayrollEarning.getSalaryComponent();
			earning = salartComponentSet.add(salaryComponent);
			if (!earning.booleanValue()) {

				throw new PayrollEarningSalaryComponentSameException(
						AdminConstants.PAYROLL_EARNING_SALARY_COMPONENT_SAME);

			}
		}

		log.info("duplicatePayrollEarningSalaryComponent() method execution ended");

	}

	public void duplicatePayrollDeductionTitle(CompanyPayrollInfoDTO companyPayrollInfoDto) {

		log.info("duplicatePayrollDeductionTitle() method execution started");

		Boolean deduction = true;
		List<CompanyPayrollDeductionDTO> companyPayrollDeductionList = companyPayrollInfoDto
				.getCompanyPayrollDeductionList();
		Set<String> titleSet = new HashSet<>();
		for (CompanyPayrollDeductionDTO companyPayrollDeduction : companyPayrollDeductionList) {
			String title = companyPayrollDeduction.getTitle();
			deduction = titleSet.add(title);
			if (!deduction.booleanValue()) {
				throw new PayrollDeductionTitleSameException(AdminConstants.PAYROLL_DEDUCTION_TITLE_SAME);
			}
		}

		log.info("duplicatePayrollDeductionTitle() method execution ended");
	}
}
