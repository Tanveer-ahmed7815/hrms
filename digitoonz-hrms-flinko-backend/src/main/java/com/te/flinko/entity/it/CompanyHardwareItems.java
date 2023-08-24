package com.te.flinko.entity.it;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.util.MapOfMapToStringConverter;
import com.te.flinko.util.MapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_hardware_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "indentificationNumber")
public class CompanyHardwareItems extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "chi_indentification_number", unique = true, nullable = false, precision = 19)
	private String indentificationNumber;
	@Column(name = "chi_in_out", length = 25)
	private String inOut;
	@Column(name = "chi_product_name", length = 50)
	private String productName;
	@Column(name = "chi_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "chi_allocated_by", length = 50)
	private String allocatedBy;
	@Column(name = "chi_allocated_date")
	@JsonFormat(shape = Shape.STRING, pattern = "MM-DD-YYYY")
	private LocalDate allocatedDate;
	@Column(name = "chi_history", length = 999)
	@Convert(converter = MapOfMapToStringConverter.class)
	private Map<String, Map<String, String>> chiHistory;
	@Column(name = "chi_is_working", precision = 3)
	private Boolean isWorking;
	@ManyToOne
	@JoinColumn(name = "chi_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "chi_purchase_order_id")
	private CompanyPurchaseOrder companyPurchaseOrder;
	@ManyToOne
	@JoinColumn(name = "chi_sales_order_id")
	private CompanySalesOrder companySalesOrder;
	@ManyToOne
	@JoinColumn(name = "chi_allocated_to")
	private EmployeePersonalInfo employeePersonalInfo;

}
