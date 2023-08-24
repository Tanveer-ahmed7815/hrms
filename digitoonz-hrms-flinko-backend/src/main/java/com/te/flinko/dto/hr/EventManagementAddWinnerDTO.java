package com.te.flinko.dto.hr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventManagementAddWinnerDTO {

	
	private List<String> winners;
	
	private String employeeId;
	
	private Long employeeInfoId;
	
	private String department;
	
	private String winnerName;

	private String location;

	private String photoUrl;

}
