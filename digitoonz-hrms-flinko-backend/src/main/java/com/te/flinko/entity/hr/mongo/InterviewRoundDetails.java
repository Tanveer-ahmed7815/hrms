package com.te.flinko.entity.hr.mongo;

import java.io.Serializable;
import java.util.Map;

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
@Document("fa_interview_round_details")
public class InterviewRoundDetails implements Serializable {
    @Id
	private String id;

	@Field("ird_interview_round_id")
	private Long interviewRoundId;
	

	@Field("ird_company_id")
	private Long companyId;

	@Field("ird_rounds")
	private Map<Integer, String> rounds;

}