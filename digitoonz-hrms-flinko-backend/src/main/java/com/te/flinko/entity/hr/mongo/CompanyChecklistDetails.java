package com.te.flinko.entity.hr.mongo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

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
@Document("fa_company_checklist_details")
public class CompanyChecklistDetails implements Serializable {

	@Id
	private String id;

	@Field("ccd_checklist_id")
	private Long checklistId;

	@Field("ccd_checklist_factor")
	private List<String> checklistFactor;

	@Field("ccd_company_id")
	private Long companyId;
}
