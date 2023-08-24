package com.te.flinko.dto.admindept;

import java.io.Serializable;

import com.te.flinko.entity.account.CompanyPurchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class PurchaseOrderItemsDTO implements Serializable{

	private Long purchaseItemId;

	private String productName;

	private CompanyPurchaseOrder companyPurchaseOrder;
}
