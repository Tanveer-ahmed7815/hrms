package com.te.flinko.entity.admin;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_workoff_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

  @JsonIdentityInfo( generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "weekId")
 
public class WorkOffDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wd_week_id", unique = true, nullable = false, precision = 19)
	private Long weekId;
	@Column(name = "wd_week_number", precision = 10)
	private Integer weekNumber;
	
	@Convert(converter = ListToStringConverter.class)
	@Column(name = "wd_full_day_workoff", length = 100)
	private List<String> fullDayWorkOff;
	
	@Convert(converter = ListToStringConverter.class)
	@Column(name = "wd_half_day_workoff", length = 100)
	private List<String> halfDayWorkOff;
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "wd_work_week_rule_id")
	private CompanyWorkWeekRule companyWorkWeekRule;
	

}
