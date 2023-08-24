package com.te.flinko.repository.admindept;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.it.CompanyHardwareItems;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@Repository
public interface CompanyHardwareItemsRepository extends JpaRepository<CompanyHardwareItems, String> {

	String findByInOut(String inOut);

	List<CompanyHardwareItems> findByCompanyInfoCompanyId(Long companyId);

	Optional<CompanyHardwareItems> findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);

	Optional<CompanyHardwareItems> findByIndentificationNumberAndCompanyInfoCompanyId(String indentificationNumber,
			Long companyId);

	Optional<CompanyHardwareItems> findByProductNameAndCompanyInfoCompanyId(String productName, Long companyId);

	List<CompanyHardwareItems> findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrderAndIndentificationNumber(
			String productName, CompanyPurchaseOrder companyPurchaseOrder, CompanySalesOrder companySalesOrder,
			String indentificationNumber);

	Optional<CompanyHardwareItems> findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrderAndIndentificationNumberAndEmployeePersonalInfoEmployeeInfoId(
			String productName, CompanyPurchaseOrder companyPurchaseOrder, CompanySalesOrder companySalesOrder,
			String indentificationNumber, Long employeeInfoId);

}
