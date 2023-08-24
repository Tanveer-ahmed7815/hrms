package com.te.flinko.dto.account;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostEvaluationDTO {
	
	private String type;
	
	private BigDecimal ammount;

}
