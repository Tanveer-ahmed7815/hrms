package com.te.flinko.repository.admin;
/**
 * @author Tapas
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.admin.CompanyStockGroup;

public interface CompanyStockGroupRepository extends JpaRepository<CompanyStockGroup, Long> {
	
	void deleteByStockGroupId(Long stockGroupId);
	
	CompanyStockGroup findByStockGroupName(String stockGroupName);



}
