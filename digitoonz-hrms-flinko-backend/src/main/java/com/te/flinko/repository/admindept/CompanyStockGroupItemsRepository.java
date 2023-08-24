package com.te.flinko.repository.admindept;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.admin.CompanyStockGroup;
import com.te.flinko.entity.admindept.CompanyStockGroupItems;

public interface CompanyStockGroupItemsRepository extends JpaRepository<CompanyStockGroupItems, Long> {
	List<CompanyStockGroupItems> findByCompanyInfoCompanyId(Long companId);
	
	CompanyStockGroupItems findByCompanyInfoCompanyIdAndStockGroupItemId(Long companId,Long stockGroupItemId);
	
	
	List<CompanyStockGroupItems> findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrderAndCompanyStockGroupAndInOut(String productName,CompanyPurchaseOrder companyPurchaseOrder,CompanySalesOrder companySalesOrder,CompanyStockGroup companyStockGroup,String inOut);
}
