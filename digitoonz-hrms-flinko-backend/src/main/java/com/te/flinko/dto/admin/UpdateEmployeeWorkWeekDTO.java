package com.te.flinko.dto.admin;

import java.io.Serializable;

import lombok.Data;

@Data

public class UpdateEmployeeWorkWeekDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long employeeInfoId;
	
	private Long officialId;
	
	private Long workWeekRuleId;
	private String ruleName;
	

}
