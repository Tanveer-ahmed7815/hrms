package com.te.flinko.service.admindept;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admindept.CompanySoftwareDetailsDto;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admindept.CompanySoftwareDetails;
import com.te.flinko.exception.SoftwareAlreadyAvailableException;
import com.te.flinko.exception.admin.NoCompanyPresentException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admindept.CompanySoftwareDetailsRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Manish Kumar
 *
 */

@Slf4j
@Service
public class CompanySoftwareDetailsServiceImplimentation implements CompanySoftwareDetailsService {

	@Autowired
	private CompanySoftwareDetailsRepository companySoftwareDetailsRepository;
	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Override
	public Boolean createSoftware(CompanySoftwareDetailsDto companySoftwareDetailsDto, Long companyId) {
		log.info("createSoftware() method execution started");

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		List<CompanySoftwareDetails> companySoftwareDetailsList = companyInfo.getCompanySoftwareDetailsList();
		List<String> softwareName = companySoftwareDetailsList.stream().map(x -> x.getSoftwareName().toUpperCase())
				.collect(Collectors.toList());
		boolean contains = softwareName.contains(companySoftwareDetailsDto.getSoftwareName().toUpperCase());
		if (contains) {
			throw new SoftwareAlreadyAvailableException(AdminConstants.SOFTWARE_ALREADY_AVAILABLE);
		}
		CompanySoftwareDetails companySoftwareDetails = new CompanySoftwareDetails();
		BeanUtils.copyProperties(companySoftwareDetailsDto, companySoftwareDetails);
		companySoftwareDetails.setCompanyInfo(companyInfo);
		log.info("createSoftware() method execution ended");

		return Optional.ofNullable(companySoftwareDetailsRepository.save(companySoftwareDetails)).isPresent();

	}

	@Override
	public List<CompanySoftwareDetailsDto> getAllSoftware(Long companyId) {
		log.info("getAllSoftware() method execution started ,taking input companyId :" + companyId);

		return companyInfoRepository.findById(companyId)
				.map(xx -> companySoftwareDetailsRepository.findByCompanyInfoCompanyId(companyId).stream().map(e -> {
					CompanySoftwareDetailsDto companySoftwareDetailsDto = new CompanySoftwareDetailsDto();
					BeanUtils.copyProperties(e, companySoftwareDetailsDto);
					return companySoftwareDetailsDto;
				}).collect(Collectors.toList()))
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

	}

	// updating software details
	@Transactional
	@Override
	public Boolean updatesoftware(CompanySoftwareDetailsDto companySoftwareDetailsDto, Long companyId,
			Long softwareId) {

		boolean present=false;
		log.info("updatesoftware() method executation started taking inputd :" + companyId + "and" + softwareId);

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new NoCompanyPresentException(AdminConstants.NO_COMPANY_PRESENT_WITH_ID));

		List<String> softwareNameList = new ArrayList<>();
		for (CompanySoftwareDetails companySoftwareDetails1 : companyInfo.getCompanySoftwareDetailsList()) {
			if (companySoftwareDetails1.getSoftwareId().equals(companySoftwareDetailsDto.getSoftwareId())) {
				continue;
			}
			softwareNameList.add(companySoftwareDetails1.getSoftwareName());
		}
		for (CompanySoftwareDetails companySoftwareDetails : companyInfo.getCompanySoftwareDetailsList()) {
			if (companySoftwareDetails.getSoftwareId().equals(companySoftwareDetailsDto.getSoftwareId())) {

				if (softwareNameList.contains(companySoftwareDetailsDto.getSoftwareName())) {
					throw new SoftwareAlreadyAvailableException(AdminConstants.SOFTWARE_NAME_ALREADY_EXIST);
				}
				BeanUtils.copyProperties(companySoftwareDetailsDto, companySoftwareDetails);
			 present = Optional.ofNullable(companySoftwareDetailsRepository.save(companySoftwareDetails)).isPresent();
				break;
			}
		}
		

		log.info("updatesoftware() method executation ended ,returning boolean value :"
				+ present);

		return present;

	}
 
}
