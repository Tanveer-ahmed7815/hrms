package com.te.flinko.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.te.flinko.entity.admin.CompanyExpenseCategories;
import com.te.flinko.util.LinkedHashMapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_reimbursement_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "reimbursementId")
public class EmployeeReimbursementInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eri_reimbursement_id", unique = true, nullable = false, precision = 19)
	private Long reimbursementId;
	@Column(name = "eri_expense_date")
	private LocalDate expenseDate;
	@Column(name = "eri_description", length = 999)
	private String description;
	@Column(name = "eri_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "eri_attachment_url", length = 50)
	private String attachmentUrl;
	@Column(name = "eri_status", length = 25)
	private String status;
	@Column(name = "eri_approved_by", length = 255)
	@Convert(converter = LinkedHashMapToStringConverter.class)
	private LinkedHashMap<String, String> approvedBy;
	@Column(name = "eri_rejected_by", length = 50)
	private String rejectedBy;
	@Column(name = "eri_rejected_reason", length = 999)
	private String rejectedReason;
	@Column(name = "eri_is_paid", precision = 3)
	private Boolean isPaid;
	@ManyToOne
	@JoinColumn(name = "eri_expense_category_id")
	private CompanyExpenseCategories companyExpenseCategories;
	@ManyToOne
	@JoinColumn(name = "eri_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
