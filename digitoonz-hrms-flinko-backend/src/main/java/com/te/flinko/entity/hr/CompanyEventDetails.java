package com.te.flinko.entity.hr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_event_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "eventId")
public class CompanyEventDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ced_event_id", unique = true, nullable = false, precision = 19)
	private Long eventId;
	@Column(name = "ced_event_date")
	private LocalDate eventDate;
	@Column(name = "ced_start_time")
	private LocalTime startTime;
	@Column(name = "ced_end_time")
	private LocalTime endTime;
	@Column(name = "ced_winners", length = 50)
	@Convert(converter = ListToStringConverter.class)
	private List<String> winners;
	@Column(name = "ced_location", length = 255)
	private String location;
	@Column(name = "ced_photo_url", length = 50)
	private String photoUrl;
	@Column(name = "ced_event_title", length = 50)
	private String eventTitle;
	@Column(name = "ced_event_description", length = 50)
	private String eventDescription;
	@Column(name = "ced_employees", length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> employees;
	@Column(name = "ced_departments", length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> departments;
	@Column(name = "ced_is_mail_required", precision = 3)
	private Boolean isMailRequired;
	@Column(name = "ced_event_visible_duration", precision = 2, scale = 2)
	private BigDecimal eventVisibleDuration;
	@ManyToOne
	@JoinColumn(name = "ced_company_id")
	private CompanyInfo companyInfo;

}
