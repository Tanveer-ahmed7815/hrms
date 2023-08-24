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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_purchase_invoice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "purchaseInvoiceId")
public class CompanyPurchaseInvoice extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpi_purchase_invoice_id", unique = true, nullable = false, precision = 19)
	private Long purchaseInvoiceId;
	@Column(name = "cpi_invoice_date")
	private LocalDate invoiceDate;
	@Column(name = "cpi_excise_duty", length = 50)
	private String exciseDuty;
	@Column(name = "cpi_due_date")
	private LocalDate dueDate;
	@Column(name = "cpi_status", length = 25)
	private String status;
	@Column(name = "cpi_sales_commission", precision = 10, scale = 2)
	private BigDecimal salesCommission;
	@Column(name = "cpi_attachment", length = 100)
	private String attachment;
	@Column(name = "cpi_description", length = 999)
	private String description;
	@Column(name = "cpi_sub_total", precision = 10, scale = 2)
	private BigDecimal subTotal;
	@Column(name = "cpi_discount_total", precision = 10, scale = 2)
	private BigDecimal discountTotal;
	@Column(name = "cpi_tax_total", precision = 10, scale = 2)
	private BigDecimal taxTotal;
	@Column(name = "cpi_adjustment", precision = 10, scale = 2)
	private BigDecimal adjustment;
	@Column(name = "cpi_total_payable_amount", precision = 10, scale = 2)
	private BigDecimal totalPayableAmount;
	@ManyToOne
	@JoinColumn(name = "cpi_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "cpi_purchase_order_id")
	private CompanyPurchaseOrder companyPurchaseOrder;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyPurchaseInvoice")
	private List<PurchaseInvoiceItems> purchaseInvoiceItemsList;

}
