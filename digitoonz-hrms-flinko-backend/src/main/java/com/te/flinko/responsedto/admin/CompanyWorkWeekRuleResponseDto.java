package com.te.flinko.responsedto.admin;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyWorkWeekRuleResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long workWeekRuleId;

	private String ruleName;

	private Boolean isDefault;
	
	private Boolean isSubmited;

	private List<WorkOffDetailsResponseDto> workOffDetailsList;

	public CompanyWorkWeekRuleResponseDto(Long workWeekRuleId, String ruleName, Boolean isDefault) {
		this.workWeekRuleId = workWeekRuleId;
		this.ruleName = ruleName;
		this.isDefault = isDefault;
	}
}
