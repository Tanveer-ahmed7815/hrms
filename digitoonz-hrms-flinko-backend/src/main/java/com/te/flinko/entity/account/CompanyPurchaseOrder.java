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
import com.google.common.collect.Lists;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.admin.CompanyStockGroup;
import com.te.flinko.entity.admindept.CompanyStockGroupItems;
import com.te.flinko.entity.it.CompanyHardwareItems;
import com.te.flinko.entity.it.CompanyPcLaptopDetails;

import lombok.*;

@Entity
@Table(name = "fa_company_purchase_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "purchaseOrderId")
public class CompanyPurchaseOrder extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpo_purchase_order_id", unique = true, nullable = false, precision = 19)
	private Long purchaseOrderId;
	@Column(name = "cpo_purchase_order_number", length = 50)
	private String purchaseOrderNumber;
	@Column(name = "cpo_type", length = 50)
	private String type;
	@Column(name = "cpo_purchase_order_description", length = 999)
	private String purchaseOrderDescription;
	@Column(name = "cpo_subject", length = 50)
	private String subject;
	@Column(name = "cpo_requisition_number", length = 50)
	private String requisitionNumber;
	@Column(name = "cpo_tracking_number", length = 50)
	private String trackingNumber;
	@Column(name = "cpo_po_date")
	private LocalDate poDate;
	@Column(name = "cpo_due_date")
	private LocalDate dueDate;
	@Column(name = "cpo_carrier", length = 50)
	private String carrier;
	@Column(name = "cpo_excise_duty", length = 50)
	private String exciseDuty;
	@Column(name = "cpo_sales_commission", precision = 10, scale = 2)
	private BigDecimal salesCommission;
	@Column(name = "cpo_status", length = 25)
	private String status;
	@Column(name = "cpo_vendor_id", precision = 19)
	private Long vendorId;
	@Column(name = "cpo_sub_total", precision = 10, scale = 2)
	private BigDecimal subTotal;
	@Column(name = "cpo_discount_total", precision = 10, scale = 2)
	private BigDecimal discountTotal;
	@Column(name = "cpo_tax_total", precision = 10, scale = 2)
	private BigDecimal taxTotal;
	@Column(name = "cpo_adjustment", length = 50)
	private String adjustment;
	@Column(name = "cpo_total_payable_amount", precision = 10, scale = 2)
	private BigDecimal totalPayableAmount;
	@Column(name = "cpo_vendor_contact_person_id", length = 45)
	private String vendorContactPersonId;
	@Column(name = "cpo_isHardware")
	private Integer isHardware;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPurchaseOrder")
	private List<CompanyHardwareItems> companyHardwareItemsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPurchaseOrder")
	private List<CompanyPcLaptopDetails> companyPcLaptopDetailsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPurchaseOrder")
	private List<CompanyPurchaseInvoice> companyPurchaseInvoiceList;
	@ManyToOne
	@JoinColumn(name = "cpo_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "cpo_stock_group_id")
	private CompanyStockGroup companyStockGroup;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPurchaseOrder")
	private List<CompanyStockGroupItems> companyStockGroupItemsList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPurchaseOrder")
	private List<PurchaseBillingShippingAddress> purchaseBillingShippingAddressList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPurchaseOrder")
	private List<PurchaseOrderItems> purchaseOrderItemsList;

}
