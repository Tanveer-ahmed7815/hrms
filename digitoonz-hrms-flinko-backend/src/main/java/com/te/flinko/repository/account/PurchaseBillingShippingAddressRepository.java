package com.te.flinko.repository.account;

import com.te.flinko.entity.account.PurchaseBillingShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseBillingShippingAddressRepository extends JpaRepository<PurchaseBillingShippingAddress, Long> {
    void deleteByCompanyPurchaseOrderPurchaseOrderId(Long purchaseOrderId);
}
