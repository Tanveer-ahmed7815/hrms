package com.te.flinko.repository.admin;
/**
 * @author Tapas
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.admin.CompanyStockCategories;
import com.te.flinko.entity.admin.CompanyStockUnits;

public interface CompanyStockUnitsRepository extends JpaRepository<CompanyStockUnits, Long> {

}
