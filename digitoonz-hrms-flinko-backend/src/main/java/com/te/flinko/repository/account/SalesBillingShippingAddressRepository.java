package com.te.flinko.repository.account;

import com.te.flinko.entity.account.SalesBillingShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesBillingShippingAddressRepository extends JpaRepository<SalesBillingShippingAddress, Long> {
    void deleteByCompanySalesOrderSalesOrderId(Long purchaseOrderId);
}
