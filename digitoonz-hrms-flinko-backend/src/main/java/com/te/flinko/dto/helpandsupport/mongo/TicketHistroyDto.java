package com.te.flinko.dto.helpandsupport.mongo;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TicketHistroyDto implements Serializable {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	@JsonFormat(pattern = "dd-MM-yyyy" , timezone = "Asia/kolkata")
	    private LocalDate date;
	    private Long by;
	    private String department;
}
