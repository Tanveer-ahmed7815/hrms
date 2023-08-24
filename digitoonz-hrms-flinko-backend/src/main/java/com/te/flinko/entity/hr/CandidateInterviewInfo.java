package com.te.flinko.entity.hr;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.util.MapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_candidate_interview_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "interviewId")
public class CandidateInterviewInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cii_interview_id", unique = true, nullable = false, precision = 19)
	private Long interviewId;
	@Column(name = "cii_interview_type", length = 50)
	private String interviewType;
	@Column(name = "cii_interview_details", length = 999)
	private String interviewDetails;
	@Column(name = "cii_round_of_interview", precision = 10)
	private Integer roundOfInterview;
	@Column(name="cii_interview_date")
	private LocalDate interviewDate;
	@Column(name = "cii_start_time")
	private LocalTime startTime;
	@Column(name = "cii_duration", precision = 10)
	private Integer duration;
	@Column(name = "cii_round_name", length = 50)
	private String roundName;
	@Column(name = "cii_feedback", length = 999)
	@Convert(converter = MapToStringConverter.class)
	private Map<String, String> feedback;
	@ManyToOne
	@JoinColumn(name = "cii_candidate_id")
	private CandidateInfo candidateInfo;
	@ManyToOne
	@JoinColumn(name = "cii_interviewer_id")
	private EmployeePersonalInfo employeePersonalInfo;
	@ManyToOne
	@JoinColumn(name = "cii_employee_info_id")
	private EmployeePersonalInfo employeePersonalInterviewInfo;
	

}
