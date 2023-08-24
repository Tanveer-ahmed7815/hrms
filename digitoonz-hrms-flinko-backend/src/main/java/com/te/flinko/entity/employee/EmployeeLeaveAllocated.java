package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "fa_employee_leave_allocated")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "employeeLeaveId")
public class EmployeeLeaveAllocated extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cla_employee_leave_id", unique = true, nullable = false, precision = 19)
	private Long employeeLeaveId;
	@Column(name = "cla_leaves_details", length = 255)
	@Convert(converter = MapToStringConverter.class)
	private Map<String,String> leavesDetails;
	@OneToOne
	@JoinColumn(name = "cla_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
