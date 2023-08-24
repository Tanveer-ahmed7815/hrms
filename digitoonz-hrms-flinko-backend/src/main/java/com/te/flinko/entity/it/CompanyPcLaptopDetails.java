package com.te.flinko.entity.it;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.account.CompanyPurchaseOrder;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.util.MapOfMapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_pc_laptop_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "serialNumber")
public class CompanyPcLaptopDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "cpld_serial_number", unique = true, nullable = false, length = 50)
	private String serialNumber;
	@Column(name = "cpld_in_out", length = 5)
	private String inOut;
	@Column(name = "cpld_product_name", length = 100)
	private String productName;
	@Column(name = "cpld_memory", length = 25)
	private String memory;
	@Column(name = "cpld_processor", length = 25)
	private String processor;
	@Column(name = "cpld_os", length = 25)
	private String os;
	@Column(name = "cpld_storage", length = 25)
	private String storage;
	@Column(name = "cpld_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "cpld_allocated_by", length = 50)
	private String allocatedBy;
	@Column(name = "cpld_allocated_date")
	private LocalDate allocatedDate;
	@Column(name = "cpld_history", length = 999)
	@Convert(converter = MapOfMapToStringConverter.class)
	private Map<String, Map<String,String>> cpldHistory;
	@Column(name = "cpld_is_working", precision = 3)
	private Boolean cpldIsWorking;
	@ManyToOne
	@JoinColumn(name = "cpld_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "cpld_purchase_order_id")
	private CompanyPurchaseOrder companyPurchaseOrder;
	@ManyToOne
	@JoinColumn(name = "cpld_sales_order_id")
	private CompanySalesOrder companySalesOrder;
	@ManyToOne
	@JoinColumn(name = "cpld_allocated_to")
	private EmployeePersonalInfo employeePersonalInfo;
	@OneToMany(mappedBy = "companyPcLaptopDetails")
	private List<PcLaptopSoftwareDetails> pcLaptopSoftwareDetailsList;

}
