package com.te.flinko.dto.helpandsupport.mongo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class CompanyAdminDeptTicketsResponseDto implements Serializable {

	private String objectTicketId;
	private Long adminTicketId;
	private String category;
	private String status;
	private String description;
	private Long by;
	private String ticketOwnerEmployeeId;
	private String ticketOwner;
	private String employeeId;
	private String employeeName;
	private String employeeBy;
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/kolkata")
	private LocalDate lastDate;
	private Integer rating;
	private List<TicketHistroy> ticketHistroies;
	private String attachmentsUrl;
	private String feedback;
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/kolkata")
	private LocalDate date;
}
