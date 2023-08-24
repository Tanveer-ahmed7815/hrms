package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
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
import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_login_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "loginId")
public class EmployeeLoginInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eli_login_id", unique = true, nullable = false, precision = 19)
	private Long loginId;
	@Column(name = "eli_employee_id", unique = true, nullable = false, length = 25)
	private String employeeId;
	@Column(name = "eli_old_password", length = 50)
	private String oldPassword;
	@Column(name = "eli_current_password", nullable = false, length = 50)
	private String currentPassword;
	@Column(name = "eli_roles", nullable = false, length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> roles;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "eli_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
