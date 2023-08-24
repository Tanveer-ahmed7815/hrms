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
@Table(name = "fa_company_sales_invoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "salesInvoiceId")
public class CompanySalesInvoice extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "csi_sales_invoice_id", unique = true, nullable = false, precision = 19)
	private Long salesInvoiceId;
	@Column(name = "csi_invoice_date")
	private LocalDate invoiceDate;
	@Column(name = "csi_excise_duty", length = 50)
	private String exciseDuty;
	@Column(name = "csi_due_date")
	private LocalDate dueDate;
	@Column(name = "csi_status", length = 25)
	private String status;
	@Column(name = "csi_sales_commission", precision = 10, scale = 2)
	private BigDecimal salesCommission;
	@Column(name = "csi_attachments", length = 100)
	private String attachments;
	@Column(name = "csi_description", length = 999)
	private String description;
	@Column(name = "csi_sub_total", precision = 10, scale = 2)
	private BigDecimal subTotal;
	@Column(name = "csi_discount_total", precision = 10, scale = 2)
	private BigDecimal discountTotal;
	@Column(name = "csi_tax_total", precision = 10, scale = 2)
	private BigDecimal taxTotal;
	@Column(name = "csi_adjustment", precision = 10, scale = 2)
	private BigDecimal adjustment;
	@Column(name = "csi_total_receivable_amount", precision = 10, scale = 2)
	private BigDecimal totalReceivableAmount;
	@ManyToOne
	@JoinColumn(name = "csi_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "csi_sales_order_id")
	private CompanySalesOrder companySalesOrder;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companySalesInvoice")
	private List<SalesInvoiceItems> salesInvoiceItemsList;

}
