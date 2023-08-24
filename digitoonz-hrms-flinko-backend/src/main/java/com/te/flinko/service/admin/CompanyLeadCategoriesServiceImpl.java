package com.te.flinko.service.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admin.CompanyLeadCategoriesDTO;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyLeadCategories;
import com.te.flinko.exception.admin.CompanyNotExistException;
import com.te.flinko.exception.admin.DuplicateColorException;
import com.te.flinko.exception.admin.DuplicateLeadNameException;
import com.te.flinko.exception.admin.LeadNotExistsException;
import com.te.flinko.exception.admin.NoDataPresentException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.CompanyLeadCategoriesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyLeadCategoriesServiceImpl implements CompanyLeadCategoriesService {

	@Autowired
	private final CompanyLeadCategoriesRepository companyLeadCategoriesRepository;
	@Autowired
	private final CompanyInfoRepository companyInfoRepository;

	@Override
	public List<CompanyLeadCategoriesDTO> getLead(Long companyId) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotExistException(AdminConstants.COMPANY_NOT_EXIST));

		List<CompanyLeadCategories> leadCategories = Optional.ofNullable(companyInfo.getCompanyLeadCategoriesList())
				.orElseThrow(() -> new LeadNotExistsException(AdminConstants.LEAD_NOT_EXIST));

		List<CompanyLeadCategoriesDTO> companyLeadCategoriesDtoList = new ArrayList<>();

		for (CompanyLeadCategories companyLeadCategories2 : leadCategories) {

			CompanyLeadCategoriesDTO companyLeadCategoriesDto = new CompanyLeadCategoriesDTO();

			companyLeadCategoriesDto.setLeadCategoryId(companyLeadCategories2.getLeadCategoryId());
			companyLeadCategoriesDto.setLeadCategoryName(companyLeadCategories2.getLeadCategoryName());
			companyLeadCategoriesDto.setColor(companyLeadCategories2.getColor());
			companyLeadCategoriesDto.setIsSubmited(companyInfo.getIsSubmited());
			companyLeadCategoriesDtoList.add(companyLeadCategoriesDto);
		}

		if (leadCategories == null || leadCategories.isEmpty()) {
			throw new NoDataPresentException(AdminConstants.DATA_NOT_PRESENT);

		}

		return companyLeadCategoriesDtoList;
	}

	@Override
	public String updateLead(List<CompanyLeadCategoriesDTO> companyLeadCategoriesDtoList, Long companyId) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.map(com -> Optional.of(com.getIsSubmited() == null ? Boolean.FALSE : com.getIsSubmited())
						.filter(x -> !x).map(y -> com)
						.orElseThrow(() -> new CompanyNotExistException("Company Already Configured!!!")))
				.orElseThrow(() -> new CompanyNotExistException("No Company Present With This ID"));

		List<Long> leadCategoryIdDto = new ArrayList<>();
		List<String> leadCategoryNameDto = new ArrayList<>();
		List<String> leadCategoryColorDto = new ArrayList<>();
		Set<String> leadCategoryNameDtoSet = new HashSet<>();
		Set<String> leadCategoryColorDtoSet = new HashSet<>();

		companyLeadCategoriesDtoList.forEach(lead -> {
			if (!leadCategoryNameDtoSet.add(lead.getLeadCategoryName())) {

				throw new DuplicateLeadNameException(AdminConstants.DUPLICATE_LEAD);
			}
			if (!leadCategoryColorDtoSet.add(lead.getColor())) {

				throw new DuplicateColorException(AdminConstants.DUPLICATE_COLOR);
			}

			leadCategoryNameDto.add(lead.getLeadCategoryName());
			leadCategoryIdDto.add(lead.getLeadCategoryId());
			leadCategoryColorDto.add(lead.getColor());
		});

		List<Long> leadCategoryIdDB = new ArrayList<>();
		List<String> leadCategoryNameDB = new ArrayList<>();
		List<String> leadCategoryColorDB = new ArrayList<>();

		companyInfo.getCompanyLeadCategoriesList().forEach(lead -> {

			leadCategoryNameDB.add(lead.getLeadCategoryName());
			leadCategoryIdDB.add(lead.getLeadCategoryId());
			leadCategoryColorDB.add(lead.getColor());

		});

		List<CompanyLeadCategories> companyLeadCategoriesList = new ArrayList<>();
		companyLeadCategoriesDtoList.forEach(lead -> {
			CompanyLeadCategories companyLeadCategories = new CompanyLeadCategories();
			BeanUtils.copyProperties(lead, companyLeadCategories);
			companyLeadCategories.setCompanyInfo(companyInfo);
			companyLeadCategoriesList.add(companyLeadCategories);
		});

		List<Long> duplicateValue = duplicateValue(leadCategoryIdDB, leadCategoryIdDto);
		companyLeadCategoriesRepository.deleteAllByIdInBatch(duplicateValue);
		companyLeadCategoriesRepository.saveAll(companyLeadCategoriesList);
		String message = leadCategoryIdDB.isEmpty() ? "Add Company Lead Categories Successfully!!!"
				: "Update Company Lead Categories Successfully!!!";
		log.info(message);
		return message;
	}

	public static List<Long> duplicateValue(List<Long> leadCategoryIdDB, List<Long> leadCategoryIdDto) {
		Set<Long> set1 = new HashSet<>(leadCategoryIdDB);
		Set<Long> set2 = new HashSet<>(leadCategoryIdDto);
		List<Long> duplicateList = new ArrayList<>();
		for (Long long1 : set1) {
			boolean contains = set2.contains(long1);
			if (!contains) {
				duplicateList.add(long1);
			}
		}
		return duplicateList;

	}

}
