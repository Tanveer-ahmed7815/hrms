package com.te.flinko.dto.admindept;


/**
 * @author Tapas
 *
 */

import java.math.BigDecimal;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOtherStockGroupItemDto {
	private Long stockGroupItemId;
	private String subject;		
	private Long purchaseOrderId;		
	private Long salesOrderId;		
	private String productName;
	private Long productId;
	private String inOut;
	private int quantity;
	private BigDecimal amount;
	private int free;
	private int inUse;
	private int working;
	private String stockGroupName;	
	private Long stockGroupId;		
	private int inCount;
	private int outCount;
	
}
