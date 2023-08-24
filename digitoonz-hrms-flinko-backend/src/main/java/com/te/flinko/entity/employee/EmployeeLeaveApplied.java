package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;

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
import com.te.flinko.util.LinkedHashMapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "fa_employee_leave_applied")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "leaveAppliedId")
public class EmployeeLeaveApplied extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ela_leave_applied_id", unique = true, nullable = false, precision = 19)
	private Long leaveAppliedId;
	@Column(name = "leave_of_type", length = 50)
	private String leaveOfType;
	@Column(name = "ela_leave_duration")
	private Double leaveDuration;
	@Column(name = "ela_start_date")
	private LocalDate startDate;
	@Column(name = "ela_end_date")
	private LocalDate endDate;
	@Column(name = "ela_start_time")
	private LocalTime startTime;
	@Column(name = "ela_end_time")
	private LocalTime endTime;
	@Column(name = "ela_reason", length = 999)
	private String reason;
	@Column(name = "ela_status", length = 50)
	private String status;
	@Column(name = "ela_approved_by", length = 255)
	@Convert(converter = LinkedHashMapToStringConverter.class)
	private LinkedHashMap<String, String> approvedBy;
	@Column(name = "ela_rejected_by", length = 50)
	private String rejectedBy;
	@Column(name = "ela_rejection_reason", length = 999)
	private String rejectionReason;
	@ManyToOne
	@JoinColumn(name = "ela_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;
	@ManyToOne
	@JoinColumn(name = "ela_reporting_manager_id")
	private EmployeeReportingInfo employeeReportingInfo;

}
