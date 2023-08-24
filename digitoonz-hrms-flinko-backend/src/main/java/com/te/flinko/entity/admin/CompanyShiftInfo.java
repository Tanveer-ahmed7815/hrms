package com.te.flinko.entity.admin;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_shift_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "shiftId")
public class CompanyShiftInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "csi_shift_id", unique = true, nullable = false, precision = 19)
	private Long shiftId;
	@Column(name = "csi_shift_name", length = 50)
	private String shiftName;
	@Column(name = "csi_login_time")
	@JsonFormat(shape = Shape.STRING,pattern = "HH:mm:ss")
	private LocalTime loginTime;
	@Column(name = "csi_logout_time")
	@JsonFormat(shape = Shape.STRING,pattern = "HH:mm:ss")
	private LocalTime logoutTime;
	@ManyToOne
	@JoinColumn(name = "csi_rule_id")
	private CompanyRuleInfo companyRuleInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyShiftInfo")
	private List<EmployeeOfficialInfo> employeeOfficialInfoList;
}
