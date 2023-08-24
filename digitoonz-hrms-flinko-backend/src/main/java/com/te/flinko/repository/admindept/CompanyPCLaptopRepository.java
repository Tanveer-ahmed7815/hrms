package com.te.flinko.repository.admindept;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.it.CompanyPcLaptopDetails;

/**
 * 
 * @author Brunda
 *
 */
@Repository
public interface CompanyPCLaptopRepository extends JpaRepository<CompanyPcLaptopDetails, String> {

	Optional<CompanyPcLaptopDetails> findBySerialNumberAndCompanyInfoCompanyId(String serialNumber, Long companyId);

	List<CompanyPcLaptopDetails> findByCompanyInfoCompanyId(Long companyId);

	List<CompanyPcLaptopDetails> findByProductNameAndCompanyPurchaseOrderAndCompanySalesOrder(String productName,
			CompanyPurchaseOrder companyPurchaseOrder, CompanySalesOrder companySalesOrder);

	Optional<CompanyPcLaptopDetails> findByEmployeePersonalInfoEmployeeInfoId(Long employeeInfoId);
}
