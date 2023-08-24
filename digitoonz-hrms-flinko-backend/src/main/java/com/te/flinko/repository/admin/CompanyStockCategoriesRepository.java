package com.te.flinko.repository.admin;
/**
 * @author Tapas
 *
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.admin.CompanyStockCategories;

public interface CompanyStockCategoriesRepository extends JpaRepository<CompanyStockCategories, Long> {
	

	 List<CompanyStockCategories> findByCompanyStockGroupCompanyInfoCompanyId(Long companyId);

}

