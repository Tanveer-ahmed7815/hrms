package com.te.flinko.entity.sales;

import java.io.Serializable;
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
import com.te.flinko.entity.account.CompanySalesOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_client_contact_person_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "contactPersonId")
public class ClientContactPersonDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ccpd_contact_person_id", unique = true, nullable = false, precision = 19)
	private Long contactPersonId;
	@Column(name = "ccpd_first_name", length = 50)
	private String firstName;
	@Column(name = "ccpd_last_name", length = 25)
	private String lastName;
	@Column(name = "ccpd_designation", length = 50)
	private String designation;
	@Column(name = "ccpd_email_id", nullable = false, length = 100)
	private String emailId;
	@Column(name = "ccpd_mobile_number", nullable = false, length = 15)
	private String mobileNumber;
	@ManyToOne
	@JoinColumn(name = "ccpd_client_id")
	private CompanyClientInfo companyClientInfo;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clientContactPersonDetails")
	private List<CompanySalesOrder> companySalesOrderList;

}
