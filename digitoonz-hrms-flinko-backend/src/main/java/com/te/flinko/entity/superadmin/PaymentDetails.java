package com.te.flinko.entity.superadmin;

import java.io.Serializable;
import java.math.BigDecimal;

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
import com.te.flinko.entity.admin.CompanyInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_payment_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "paymentId")
public class PaymentDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pd_payment_id", unique = true, nullable = false, precision = 19)
	private Long paymentId;
	@Column(name = "pd_amount_payed", precision = 10, scale = 2)
	private BigDecimal amountPayed;
	@Column(name = "pd_mode_of_payment", length = 25)
	private String modeOfPayment;
	@ManyToOne
	@JoinColumn(name = "pd_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "pd_plan_id")
	private PlanDetails planDetails;

}
