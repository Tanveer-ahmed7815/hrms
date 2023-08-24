package com.te.flinko.entity.project;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_project_estimation_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "estimationId")
public class ProjectEstimationDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ped_estimation_id", unique = true, nullable = false, precision = 19)
	private Long estimationId;
	@Column(name = "ped_start_date")
	private LocalDate startDate;
	@Column(name = "ped_end_date")
	private LocalDate endDate;
	@Column(name = "ped_no_of_emp", precision = 10)
	private Integer noOfEmp;
	@Column(name = "ped_total_amount_estimated", precision = 10, scale = 2)
	private BigDecimal totalAmountEstimated;
	@Column(name = "ped_total_amount_to_be_received", precision = 10, scale = 2)
	private BigDecimal totalAmountToBeReceived;
	@Column(name = "ped_file_url", length = 250)
	private String fileURL;
	@Column(name = "ped_status", length = 50)
	private String status;
	@OneToOne
	@JoinColumn(name = "ped_project_id")
	private ProjectDetails projectDetails;

}
