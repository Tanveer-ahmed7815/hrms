package com.te.flinko.entity.employee.mongo;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
@Document("fa_employee_document_details")
public class EmployeeDocumentDetails implements Serializable {

	@Id
	private String documentObjectId;

	@Field("edd_document_id")
	private Long documentId;

	@Field("edd_employee_id")
	private String employeeId;

	@Field("edd_company_id")
	private Long companyId;

	@Field("edd_document")
	private Map<String, String> documents;
}