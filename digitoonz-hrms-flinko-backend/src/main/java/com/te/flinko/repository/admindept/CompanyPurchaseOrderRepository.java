package com.te.flinko.repository.admindept;

import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.PurchaseBillingShippingAddress;
import com.te.flinko.entity.admin.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyPurchaseOrderRepository extends JpaRepository<CompanyPurchaseOrder, Long> {

	Optional<CompanyInfo> findByPurchaseOrderIdAndCompanyInfoCompanyId(Long purchaseOrderId, Long companyId);

	List<CompanyPurchaseOrder> findBySubject(String subject);

	// Optional<PurchaseBillingShippingAddress> findByPurchaseBillingShippingAddressPurchaseAddressIdAndAddressType(Long purchaseAddressId, String addressType);
}
