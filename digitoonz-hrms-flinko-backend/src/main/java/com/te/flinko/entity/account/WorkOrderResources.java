package com.te.flinko.entity.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_work_order_resources")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "resourceId")
public class WorkOrderResources extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wor_resource_id", unique = true, nullable = false, precision = 19)
	private Long resourceId;
	@Column(name = "wor_resource_type", length = 100)
	private String resourceType;
	@Column(name = "wor_name", length = 50)
	private String name;
	@Column(name = "wor_start_date")
	private LocalDate startDate;
	@Column(name = "wor_end_date")
	private LocalDate endDate;
	@Column(name = "wor_amount", precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "wor_quantity", precision = 10)
	private Integer quantity;
	@ManyToOne
	@JoinColumn(name = "wor_work_order_id")
	private CompanyWorkOrder companyWorkOrder;

}
