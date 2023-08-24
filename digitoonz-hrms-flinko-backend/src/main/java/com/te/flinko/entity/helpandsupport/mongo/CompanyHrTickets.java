package com.te.flinko.entity.helpandsupport.mongo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.te.flinko.dto.helpandsupport.mongo.TicketHistroy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("fa_company_hr_tickets")
public class CompanyHrTickets implements Serializable {
	

	@Id
	private String ticketObjectId;
	
	@Field("cht_hr_ticket_id")
	private Long hrTicketId;
	
	@Field("cht_category")
	private String category;
	
	@Field("cht_description")
	private String description;
	
	@Field("cht_employee_id")
	private String employeeId;
	
	@Field("cht_attachments_url")
	private String attachmentsUrl;
	
	@Field("cht_reporting_manager_id")
	private String reportingManagerId;
    
    @Field("cht_histroy")
	private List<TicketHistroy> ticketHistroys;

	@Field("cht_feedback")
	private String feedback;

	@Field("cht_rating")
	private Integer rating;

	@Field("cht_company_id")
	private Long companyId;
	
	@Field("created_by")
	private Long createdBy;
	
	@Field("created_date")
	private LocalDate createdDate;
	
	
}
