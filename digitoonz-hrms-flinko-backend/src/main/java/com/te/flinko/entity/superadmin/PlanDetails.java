package com.te.flinko.entity.superadmin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_plan_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "planId")
public class PlanDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pd_plan_id", unique = true, nullable = false, precision = 19)
	private Long planId;
	@Column(name = "pd_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "pd_duration", precision = 2, scale = 2)
	private BigDecimal duration;
	@Column(name = "pd_plan_name", length = 50)
	private String planName;
	@Column(name = "pd_plan_description", length = 999)
	private String planDescription;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "planDetails")
	private List<PaymentDetails> paymentDetailsList;

}
