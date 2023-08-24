package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.util.List;


import lombok.Data;
//@author Rakesh Kumar Nayak
@Data
public class WorkOffDetailsDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long weekId;
	
	private Integer weekNumber;

	private List<String> fullDayWorkOff;
	
	private List<String>  halfDayWorkOff;

}
