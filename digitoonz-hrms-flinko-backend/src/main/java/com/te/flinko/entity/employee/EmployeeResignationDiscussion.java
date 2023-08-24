package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.util.MapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_resignation_discussion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "duscussionId")
public class EmployeeResignationDiscussion extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "erd_duscussion_id", unique = true, nullable = false, precision = 19)
	private Long duscussionId;
	@Column(name = "erd_discussion_type", length = 25)
	private String discussionType;
	@Column(name="erd_discussion_date")
	private LocalDate discussionDate;
	@Column(name = "erd_start_time")
	private LocalTime startTime;
	@Column(name = "erd_duration", precision = 10)
	private Integer duration;
	@Column(name = "erd_discussion_details", length = 999)
	private String discussionDetails;
	@Column(name = "erd_status", length = 25)
	private String status;
	@Column(name = "erd_feedback", length = 999)
	@Convert(converter = MapToStringConverter.class)
	private Map<String, String> feedback;
	@ManyToMany(mappedBy = "employeeResignationDiscussionList")
	private List<EmployeePersonalInfo> employeePersonalInfoList;
	@ManyToOne
	@JoinColumn(name = "erd_resignation_id")
	private CompanyEmployeeResignationDetails companyEmployeeResignationDetails;

}
