package com.te.flinko.entity.admin;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.employee.EmployeeReimbursementInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_expense_categories")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "expenseCategoryId")
public class CompanyExpenseCategories extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cec_expense_category_id", unique = true, nullable = false, precision = 19)
	private Long expenseCategoryId;
	@Column(name = "cec_expense_category_name", length = 50)
	private String expenseCategoryName;
	@ManyToOne
	@JoinColumn(name = "cec_company_id")
	private CompanyInfo companyInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyExpenseCategories")
	private List<EmployeeReimbursementInfo> employeeReimbursementInfoList;

}
