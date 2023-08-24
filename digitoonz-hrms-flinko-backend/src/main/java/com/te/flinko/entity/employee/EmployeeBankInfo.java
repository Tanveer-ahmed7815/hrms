package com.te.flinko.entity.employee;

import java.io.Serializable;

import javax.persistence.Column;
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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_bank_info")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "bankId")
public class EmployeeBankInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ebi_bank_id", unique = true, nullable = false, precision = 19)
	private Long bankId;
	@Column(name = "ebi_bank_name", length = 50)
	private String bankName;
	@Column(name = "ebi_bank_branch", length = 50)
	private String bankBranch;
	@Column(name = "ebi_account_type", length = 50)
	private String accountType;
	@Column(name = "ebi_account_number", precision = 19)
	private Long accountNumber;
	@Column(name = "ebi_ifsc_code", nullable = false, length = 15)
	private String ifscCode;
	@Column(name = "ebi_account_holder_name", length = 50)
	private String accountHolderName;
	@ManyToOne
	@JoinColumn(name = "ebi_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
