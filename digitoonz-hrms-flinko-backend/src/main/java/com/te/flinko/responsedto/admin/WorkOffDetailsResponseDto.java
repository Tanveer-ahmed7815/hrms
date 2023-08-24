package com.te.flinko.responsedto.admin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@JsonInclude(value = Include.NON_DEFAULT)
//@AllArgsConstructor
@NoArgsConstructor
public class WorkOffDetailsResponseDto  {

	private Long weekId;
	
	private Integer weekNumber;

	private List<String> fullDayWorkOff;
	
	private List<String> halfDayWorkOff;
	
	private Long workWeekRuleId;

	public WorkOffDetailsResponseDto(Long weekId, Integer weekNumber, List<String> fullDayWorkOff,
			List<String> halfDayWorkOff, Long workWeekRuleId) {
		super();
		this.weekId = weekId;
		this.weekNumber = weekNumber;
		this.fullDayWorkOff = fullDayWorkOff;
		this.halfDayWorkOff = halfDayWorkOff;
		this.workWeekRuleId = workWeekRuleId;
	}
	
	
	
}
