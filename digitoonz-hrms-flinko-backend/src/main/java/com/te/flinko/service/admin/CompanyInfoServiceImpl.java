package com.te.flinko.service.admin;

import java.util.ArrayList;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.te.flinko.beancopy.BeanCopy;
import com.te.flinko.common.admin.CompanyInfoConstants;
import com.te.flinko.dto.admin.CompanyAddressInfoDTO;
import com.te.flinko.dto.admin.CompanyBranchInfoDTO;
import com.te.flinko.dto.admin.CompanyInfoDTO;
import com.te.flinko.entity.admin.CompanyAddressInfo;
import com.te.flinko.entity.admin.CompanyBranchInfo;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.ComapnyNameAlreadyExistsException;
import com.te.flinko.exception.CompanyNotFoundException;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.repository.admin.CompanyAddressInfoRepository;
import com.te.flinko.repository.admin.CompanyBranchInfoRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.util.S3UploadFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Brunda
 *
 */

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class CompanyInfoServiceImpl implements CompanyInfoService {

	private final CompanyInfoRepository companyInfoRepository;

	private final CompanyBranchInfoRepository branchInfoRepository;

	private final CompanyAddressInfoRepository companyAddressInfoRepository;

	private final S3UploadFile uploadFileService;

	@Transactional
	@Override
	public CompanyInfoDTO getCompanyInfoDetails(Long companyId) {
		log.info("Service Layer, fetch Company Information");
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(CompanyInfoConstants.COMPANY_NOT_FOUND));
		CompanyInfoDTO companyInfoDto = new CompanyInfoDTO();

		BeanUtils.copyProperties(companyInfo, companyInfoDto);

		List<CompanyBranchInfo> companyBranchInfoList = companyInfo.getCompanyBranchInfoList();
		List<CompanyBranchInfoDTO> branchInfos = new ArrayList<>();

		for (CompanyBranchInfo companyBranchInfo : companyBranchInfoList) {
			CompanyBranchInfoDTO branchInfoDto = new CompanyBranchInfoDTO();
			List<CompanyAddressInfoDTO> collect = companyBranchInfo.getCompanyAddressInfoList().stream().map(x -> {
				CompanyAddressInfoDTO addressInfoDto = new CompanyAddressInfoDTO();
				BeanUtils.copyProperties(x, addressInfoDto);
				return addressInfoDto;
			}).collect(Collectors.toList());
			BeanUtils.copyProperties(companyBranchInfo, branchInfoDto);
			branchInfoDto.setCompanyAddressInfo(collect.isEmpty() ? new CompanyAddressInfoDTO() : collect.get(0));
			branchInfos.add(branchInfoDto);
		}

		companyInfoDto.setCompanyBranchInfoList(branchInfos);
		companyInfoDto.setIsSubmited(companyInfo.getIsSubmited());
		log.info("Service Layer, fetch Company Information: " + companyInfoDto);
		return companyInfoDto;
	}

	CompanyBranchInfo companyBranchInfo;

	@Override
	@Transactional
	public String updateCompanyInfo(CompanyInfoDTO companyInfoDto, MultipartFile companylogo) {
		CompanyInfo companyInfo = companyInfoRepository.findById(companyInfoDto.getCompanyId()).map(company -> {
			Boolean isSubmited = company.getIsSubmited() == null ? Boolean.FALSE
					: company.getIsSubmited().booleanValue();
			return companyInfoRepository.findByCompanyName(companyInfoDto.getCompanyName()).map(com -> {
				Boolean exist = Optional.of(company.getCompanyId().equals(com.getCompanyId())).filter(c -> c)
						.map(cc -> true).orElseThrow(() -> new ComapnyNameAlreadyExistsException(
								CompanyInfoConstants.COMPANY_NAME_ALREADY_PRESENT));
				return Optional.of(isSubmited).filter(x -> !x && exist).map(y -> com).orElseThrow(
						() -> new ComapnyNameAlreadyExistsException(CompanyInfoConstants.COMPANY_ALREADY_CONFIGURED));
			}).orElseGet(() -> Optional.of(isSubmited).filter(x -> !x).map(y -> company).orElseThrow(
					() -> new ComapnyNameAlreadyExistsException(CompanyInfoConstants.COMPANY_ALREADY_CONFIGURED)));
		}).orElseThrow(() -> new ComapnyNameAlreadyExistsException(CompanyInfoConstants.COMPANY_NOT_FOUND));

		String uploadFile = companyInfo.getCompanyLogoUrl();
		BeanUtils.copyProperties(companyInfoDto, companyInfo);
		if (companylogo != null && !companylogo.isEmpty()) {
			if (companyInfo.getCompanyLogoUrl() != null)
				uploadFileService.deleteS3Folder(companyInfo.getCompanyLogoUrl());
			uploadFile = uploadFileService.uploadFile(companylogo);
		}
		companyInfo.setCompanyLogoUrl(uploadFile);

		Set<Long> bIds = new HashSet<>();
		Set<Long> aIds = new HashSet<>();

		Set<Long> branchIds = companyInfo.getCompanyBranchInfoList().stream().map(CompanyBranchInfo::getBranchId)
				.collect(Collectors.toSet());
		Set<Long> addressIds = companyInfo.getCompanyBranchInfoList().stream()
				.filter(branch -> branch.getBranchId() != null)
				.map(x -> x.getCompanyAddressInfoList().stream().filter(z -> z.getCompanyAddressId() != null)
						.map(CompanyAddressInfo::getCompanyAddressId).collect(Collectors.toSet()))
				.flatMap(Collection::stream).collect(Collectors.toSet());
		companyBranchInfo = null;
		for (CompanyBranchInfoDTO companyBranchInfoDto : companyInfoDto.getCompanyBranchInfoList()) {
			if (companyBranchInfoDto.getCompanyAddressInfoList() != null
					&& companyBranchInfoDto.getCompanyAddressInfoList().size() > 1)
				throw new DataNotFoundException("Can Not Add Multiple Address for The Same Branch!!!");
			if (companyBranchInfoDto.getBranchId() != null) {
				companyBranchInfo = companyInfo.getCompanyBranchInfoList().stream()
						.filter(branch -> branch.getBranchId().equals(companyBranchInfoDto.getBranchId())).findFirst()
						.orElseGet(CompanyBranchInfo::new);
				for (CompanyAddressInfoDTO companyAddressInfoDto : companyBranchInfoDto.getCompanyAddressInfoList()) {
					if (companyAddressInfoDto.getCompanyAddressId() != null) {
						CompanyAddressInfo companyAddressInfo = companyBranchInfo.getCompanyAddressInfoList().stream()
								.filter(address -> address.getCompanyAddressId()
										.equals(companyAddressInfoDto.getCompanyAddressId()))
								.findFirst().orElseGet(CompanyAddressInfo::new);
						BeanUtils.copyProperties(companyAddressInfoDto, companyAddressInfo);
						companyBranchInfo.getCompanyAddressInfoList().add(companyAddressInfo);
						companyAddressInfo.setCompanyBranchInfo(companyBranchInfo);
						aIds.add(companyAddressInfoDto.getCompanyAddressId());
					} else {
						CompanyAddressInfo companyAddressInfo = BeanCopy.objectProperties(companyAddressInfoDto,
								CompanyAddressInfo.class);
						companyBranchInfo.getCompanyAddressInfoList().add(companyAddressInfo);
						companyAddressInfo.setCompanyBranchInfo(companyBranchInfo);
					}
				}
				BeanUtils.copyProperties(companyBranchInfoDto, companyBranchInfo);
				companyInfo.getCompanyBranchInfoList().add(companyBranchInfo);
				companyBranchInfo.setCompanyInfo(companyInfo);
				bIds.add(companyBranchInfoDto.getBranchId());
				aIds.addAll(companyBranchInfoDto.getCompanyAddressInfoList().stream()
						.map(CompanyAddressInfoDTO::getCompanyAddressId).collect(Collectors.toSet()));
			} else {
				CompanyBranchInfo companyBranchInfo1 = BeanCopy.objectProperties(companyBranchInfoDto,
						CompanyBranchInfo.class);
				companyBranchInfo1.setCompanyInfo(companyInfo);
				companyBranchInfo = branchInfoRepository.save(companyBranchInfo1);
				List<CompanyAddressInfo> collect = companyBranchInfo.getCompanyAddressInfoList().stream().map(x -> {
					x.setCompanyBranchInfo(companyBranchInfo);
					return x;
				}).collect(Collectors.toList());
				companyBranchInfo.getCompanyAddressInfoList().addAll(collect);
			}
		}
		companyInfo.setIsSubmited(Boolean.FALSE);
		branchIds.removeAll(bIds);
		addressIds.removeAll(aIds);
		companyAddressInfoRepository.deleteAllById(addressIds);
		branchInfoRepository.deleteAllById(branchIds);
		List<EmployeePersonalInfo> collect = companyInfo.getEmployeePersonalInfoList().stream()
				.filter(empPersonal -> empPersonal != null && empPersonal.getEmployeeOfficialInfo() != null)
				.map(personal -> {
					personal.getEmployeeOfficialInfo().setCompanyBranchInfo(companyBranchInfo);
					return personal;
				}).collect(Collectors.toList());
		return collect.isEmpty()?"Somewent wrong!!!":CompanyInfoConstants.UPDATE_COMPANY_INORMATION;
	}
}
