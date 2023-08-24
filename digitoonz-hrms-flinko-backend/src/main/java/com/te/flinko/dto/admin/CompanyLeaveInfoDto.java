package com.te.flinko.dto.admin;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Brunda
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)

public class CompanyLeaveInfoDto {
	
	private Long leaveId;
	private String leaveTitle;
	private BigDecimal noOfLeave;
}
