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
public class CompanyAccountTickets implements Serializable{
	@Field("cat_account_ticket_id")
	private Long accountTicketId;
	
	@Field("cat_category")
	private String category;
	
	@Field("cat_description")
	private String description;
	
	@Field("cat_employee_id")
	private String employeeId;
	
	@Field("cat_attachments_url")
	private String attachmentsUrl;
	
	@Field("cat_reporting_manager_id")
	private String reportingManagerId;
    
    @Field("cat_histroy")
	private List<TicketHistroy> ticketHistroys;

	@Field("cat_feedback")
	private String feedback;

	@Field("cat_rating")
	private Integer rating;

	@Field("cat_company_id")
	private Long companyId;
}
