package com.te.flinko.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.dto.admin.CompanyLeadCategoriesDTO;
import com.te.flinko.entity.admin.CompanyLeadCategories;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public interface CompanyLeadCategoriesRepository extends JpaRepository<CompanyLeadCategories, Long> {

	Optional<CompanyLeadCategoriesDTO> findByLeadCategoryNameAndColor(CompanyLeadCategoriesDTO companyLeadCategoriesDto,
			Long companyId);

	Optional<CompanyLeadCategoriesDTO> save(CompanyLeadCategoriesDTO companyLeadCategoriesDto);

	Optional<CompanyLeadCategories> findByLeadCategoryNameAndCompanyInfoCompanyId(String leadCategoryName,
			Long companyId);

	Optional<CompanyLeadCategories> findByColorAndCompanyInfoCompanyId(String color, Long companyId);

	boolean findByLeadCategoryName(String leadCategoryName);

	Optional<CompanyLeadCategories> findByLeadCategoryIdAndCompanyInfoCompanyId(Long leadCategoryId, Long companyId);

}
