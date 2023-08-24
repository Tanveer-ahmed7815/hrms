package com.te.flinko.repository.admin;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.admin.CompanyExpenseCategories;

@Repository
public interface CompanyExpenseCategoriesRepository extends JpaRepository<CompanyExpenseCategories, Long> {

	Optional<CompanyExpenseCategories> findByExpenseCategoryNameAndCompanyInfoCompanyId(String expenseCategoryName,
			Long companyId);

	Optional<List<CompanyExpenseCategories>> findByExpenseCategoryNameInAndCompanyInfoCompanyId(List<String> collect,
			Long companyId);

	Optional<List<CompanyExpenseCategories>> findByCompanyInfoCompanyId(Long companyId);

	String findByExpenseCategoryName(String expenseCategoryName);

	Optional<CompanyExpenseCategories> findByExpenseCategoryIdAndCompanyInfoCompanyId(Long expenseCategoryId,
			Long companyId);
}