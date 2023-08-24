package com.te.flinko.dto.admindept;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(value = Include.NON_DEFAULT)
@Data
public class ProductNameDTO implements Serializable{

	private Long productId;
	private String productName;
	private Long saleItemId;
	private Long purchaseItemId;
	private Boolean isActive;
	private String reason;
}
