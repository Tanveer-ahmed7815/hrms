package com.te.flinko.entity.employee;

import java.io.Serializable;

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
@Table(name = "fa_employee_notification")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "employeeNotificationId")
public class EmployeeNotification extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "en_employee_notification_id", unique = true, nullable = false, precision = 19)
	private Long employeeNotificationId;
	@Column(name = "en_description", length = 999)
	private String description;
	@ManyToOne
	@JoinColumn(name = "en_sender_id")
	private EmployeePersonalInfo senderEmployeePersonalInfo;
	@ManyToOne
	@JoinColumn(name = "en_receiver_id")
	private EmployeePersonalInfo receiverEmployeePersonalInfo;

}
