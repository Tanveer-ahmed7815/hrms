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
@Document("fa_company_it_tickets")
public class CompanyItTickets implements Serializable {

	@Field("cit_it_ticket_id")
	private Long itTicketId;

	@Field("cit_category")
	private String category;

	@Field("cit_description")
	private String description;

	@Field("cit_employee_id")
	private String employeeId;

	@Field("cit_attachments_url")
	private String attachmentsUrl;

	@Field("cit_product_name")
	private String productName;

	@Field("cit_identification_number")
	private String identificationNumber;

	@Field("cit_reporting_manager_id")
	private String reportingManagerId;

	@Field("cit_histroy")
	private List<TicketHistroy> ticketHistroys;

	@Field("cit_feedback")
	private String feedback;

	@Field("cit_rating")
	private Integer rating;

	@Field("cit_company_id")
	private Long companyId;
}
