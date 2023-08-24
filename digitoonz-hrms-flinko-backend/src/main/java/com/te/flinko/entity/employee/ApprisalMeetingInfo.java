package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_apprisal_meeting_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "meetingId")
public class ApprisalMeetingInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ami_meeting_id", unique = true, nullable = false, precision = 19)
	private Long meetingId;
	@Column(name = "ami_meeting_type", length = 25)
	private String meetingType;
	@Column(name = "ami_meeting_date")
	private LocalDate meetingDate;
	@Column(name = "ami_start_time")
	private LocalTime startTime;
	@Column(name = "ami_duration", precision = 10)
	private Integer duration;
	@Column(name = "ami_meeting_details", length = 999)
	private String meetingDetails;
	@Column(name = "ami_status", length = 25)
	private String status;
	@ManyToMany(mappedBy = "apprisalMeetingInfoList")
	private List<EmployeePersonalInfo> employeePersonalInfoList;
	@OneToOne
	@JoinColumn(name = "ami_revise_salary_id")
	private EmployeeReviseSalary employeeReviseSalary;

}
