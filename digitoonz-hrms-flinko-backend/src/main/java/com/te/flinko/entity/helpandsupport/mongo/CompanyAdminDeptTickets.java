package com.te.flinko.entity.helpandsupport.mongo;

import java.io.Serializable;
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
@Document("fa_company_admin_dept_tickets")
public class CompanyAdminDeptTickets implements Serializable{
	@Id
	private String objectTicketId;
	
	@Field("cadt_admin_ticket_id")
	private Long adminTicketId;
	
	@Field("cadt_category")
	private String category;
	
	@Field("cadt_description")
	private String description;
	
	@Field("cadt_employee_id")
	private String employeeId;
	
	@Field("cadt_attachments_url")
	private String attachmentsUrl;
	
	@Field("cadt_reporting_manager_id")
	private String reportingManagerId;
    
    @Field("cadt_histroy")
	private List<TicketHistroy> ticketHistroys;

	@Field("cadt_feedback")
	private String feedback;

	@Field("cadt_rating")
	private Integer rating;

	@Field("cadt_company_id")
	private Long companyId;
}
