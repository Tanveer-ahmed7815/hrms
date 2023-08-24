package com.te.flinko.repository.admindept;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.te.flinko.entity.account.PurchaseOrderItems;

/**
 * 
 * @author Brunda
 *
 */

public interface PurchaseOrderItemsRepository extends JpaRepository<PurchaseOrderItems, Long> {


	PurchaseOrderItems findByProductNameAndCompanyPurchaseOrderPurchaseOrderId(String productName, Long purchaseOrderId);

	List<PurchaseOrderItems> findByCompanyPurchaseOrderPurchaseOrderId(Long purchaseOrderId);

}
