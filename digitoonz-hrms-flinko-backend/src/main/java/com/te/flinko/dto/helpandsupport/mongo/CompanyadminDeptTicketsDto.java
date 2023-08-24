package com.te.flinko.dto.helpandsupport.mongo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CompanyadminDeptTicketsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String objectTicketId;

	private Long adminTicketId;

	private String category;

	private String description;

	private String employeeId;

	private String attachmentsUrl;

	private String reportingManagerId;
	
	private String department;

	private List<TicketHistroyDto> ticketHistroys;

	private String feedback;

	private Integer rating;
	
	private String status;

	private Long companyId;
}
