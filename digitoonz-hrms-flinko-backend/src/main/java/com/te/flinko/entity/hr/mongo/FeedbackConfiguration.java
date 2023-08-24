package com.te.flinko.entity.hr.mongo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.persistence.Id;

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
@Document("fa_feedback_configuration")
public class FeedbackConfiguration implements Serializable {
	
	@Id
	private String id;
	
	@Field("fc_feedback_configuration_id")
	private Long feedbackConfigurationId;
	
	@Field("fc_company_id")
	private Long companyId;
	
	@Field("fc_feedback_type")
	private String feedbackType;
	
	@Field("fc_feedback_factor")
	private List<String> feedbackFactor;
}
