package com.te.flinko.dto.hr.mongo;

import java.time.LocalDate;
import java.util.List;

import com.te.flinko.dto.helpandsupport.mongo.TicketHistroy;
import com.te.flinko.dto.hr.EmployeeInformationDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyHrTicketsDTO {
	
	private String ticketObjectId;

	private Long hrTicketId;

	private String category;
	
	private EmployeeInformationDTO ticketRaisedby;

	//private LocalDateTime raisedDateTime;
	
	private LocalDate raisedDate;
	
	private EmployeeInformationDTO ticketOwner;
	
	private String employeeId;
	
	private String employeeName;

	private String status;
	
	private List<TicketHistoryDTO> histroyList;
	
	private List<TicketHistroy> historys;
	
	private Integer ratings;
	
	private String attachments;
	
	private String description;
	
	private EmployeeInformationDTO reportingManagerId;
	
	//private LocalDateTime ticketRaisedDateString;
	
	private LocalDate ticketRaisedDate;

}
