package com.te.flinko.entity.helpandsupport.mongo;

import java.io.Serializable;
import java.util.List;

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
@Document("fa_company_account_tickets")
public class CompanyEmailIdCardTickets implements Serializable {
	@Field("ceict_email_ticket_id")
	private Long emailTicketId;
	
	@Field("ceict_category")
	private String category;
	
	@Field("ceict_description")
	private String description;
	
	@Field("ceict_employee_id")
	private String employeeId;
	
	@Field("ceict_attachments_url")
	private String attachmentsUrl;
	
	@Field("ceict_reporting_manager_id")
	private String reportingManagerId;
    
    @Field("ceict_histroy")
	private List<TicketHistroy> ticketHistroys;

	@Field("ceict_feedback")
	private String feedback;

	@Field("ceict_rating")
	private Integer rating;

	@Field("ceict_company_id")
	private Long companyId;
}
