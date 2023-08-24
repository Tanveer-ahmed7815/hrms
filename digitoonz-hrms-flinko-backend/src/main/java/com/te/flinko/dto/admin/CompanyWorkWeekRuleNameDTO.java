package com.te.flinko.dto.admin;

import java.io.Serializable;

import lombok.Data;

//@author Rakesh Kumar Nayak
@Data
public class CompanyWorkWeekRuleNameDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long workWeekRuleId;
	
	private String ruleName;

	public CompanyWorkWeekRuleNameDTO(Long workWeekRuleId, String ruleName) {
		
		this.workWeekRuleId = workWeekRuleId;
		this.ruleName = ruleName;
	}
	
	
}
