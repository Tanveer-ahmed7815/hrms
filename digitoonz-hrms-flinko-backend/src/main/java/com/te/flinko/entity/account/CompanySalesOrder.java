package com.te.flinko.entity.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyStockGroup;
import com.te.flinko.entity.admindept.CompanyStockGroupItems;
import com.te.flinko.entity.it.CompanyHardwareItems;
import com.te.flinko.entity.it.CompanyPcLaptopDetails;
import com.te.flinko.entity.sales.ClientContactPersonDetails;
import com.te.flinko.entity.sales.CompanyClientInfo;

import lombok.*;

@Entity
@Table(name = "fa_company_sales_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "salesOrderId")
public class CompanySalesOrder extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cso_sales_order_id", unique = true, nullable = false, precision = 19)
	private Long salesOrderId;
	@Column(name = "cso_type", length = 100)
	private String type;
	@Column(name = "cso_sale_order_description", length = 999)
	private String saleOrderDescription;
	@Column(name = "cso_subject", length = 50)
	private String subject;
	@Column(name = "cso_purchase_order", length = 50)
	private String purchaseOrder;
	@Column(name = "cso_customer_number", precision = 19)
	private Long customerNumber;
	@Column(name = "cso_due_date")
	private LocalDate dueDate;
	@Column(name = "cso_pending", length = 25)
	private String pending;
	@Column(name = "cso_excise_duty", length = 50)
	private String exciseDuty;
	@Column(name = "cso_carrier", length = 50)
	private String carrier;
	@Column(name = "cso_status", length = 25)
	private String status;
	@Column(name = "cso_sales_commission", precision = 10, scale = 2)
	private BigDecimal salesCommission;
	@Column(name = "cpo_sub_total", precision = 10, scale = 2)
	private BigDecimal subTotal;
	@Column(name = "cpo_discount_total", precision = 10, scale = 2)
	private BigDecimal discountTotal;
	@Column(name = "cpo_tax_total", precision = 10, scale = 2)
	private BigDecimal taxTotal;
	@Column(name = "cpo_adjustment", length = 50)
	private String adjustment;
	@Column(name = "cso_isHardware")
	private Integer isHardware;
	@Column(name = "cpo_total_receivable_amount", precision = 10, scale = 2)
	private BigDecimal totalReceivableAmount;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companySalesOrder")
	private List<CompanyHardwareItems> companyHardwareItemsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companySalesOrder")
	private List<CompanyPcLaptopDetails> companyPcLaptopDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companySalesOrder")
	private List<CompanySalesInvoice> companySalesInvoiceList;
	@ManyToOne
	@JoinColumn(name = "cpo_contact_person_id")
	private ClientContactPersonDetails clientContactPersonDetails;
	@ManyToOne
	@JoinColumn(name = "cpo_client_id")
	private CompanyClientInfo companyClientInfo;
	@ManyToOne
	@JoinColumn(name = "cpo_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "cpo_stock_group_id")
	private CompanyStockGroup companyStockGroup;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companySalesOrder")
	private List<CompanyStockGroupItems> companyStockGroupItemsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companySalesOrder")
	private List<SalesBillingShippingAddress> salesBillingShippingAddressList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companySalesOrder")
	private List<SalesOrderItems> salesOrderItemsList;

}
