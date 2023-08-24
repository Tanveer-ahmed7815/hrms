package com.te.flinko.dto.hr;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyHrTicketsDTO {
	
	private Long hrTicketId;
	
	private String category;
	
	private LocalDate raisedDate;
	
	private String status;

}
