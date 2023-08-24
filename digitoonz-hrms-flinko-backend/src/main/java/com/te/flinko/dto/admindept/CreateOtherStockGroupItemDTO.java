package com.te.flinko.dto.admindept;

/**
 * @author Tapas
 *
 */

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOtherStockGroupItemDTO {

	private String inOut;       
	private String subject;		
	private Long subjectId;		
	private String productName;	
	private Long productId;
	private int quantity;		
	private String stockGroupName;	
	private Long stockGroupId;		
	private BigDecimal amount;	
	private int free;
	private int inUse;
	private int working;
	private int inCount;
	private int outCount;
}	
