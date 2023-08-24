package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
/**
 * 
 * @author Rakesh Kumar Nayak
 *
 */

@Data
@Getter
@Setter
@JsonInclude(value = Include.USE_DEFAULTS)
public class CompanyWorkWeekRuleDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long workWeekRuleId;
	
	private String ruleName;
	
	private Boolean isDefault;
	
	
	private List<WorkOffDetailsDTO> workOffDetailsList;

}
