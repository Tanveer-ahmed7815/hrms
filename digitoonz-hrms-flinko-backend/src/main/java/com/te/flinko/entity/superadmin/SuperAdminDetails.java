package com.te.flinko.entity.superadmin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.te.flinko.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_super_admin_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sad_super_admin_id", unique = true, nullable = false, precision = 10)
	private int superAdminId;
	@Column(name = "sad_email_id", unique = true, length = 100)
	private String emailId;
	@Column(name = "sad_mobile_number", unique = true, length = 15)
	private String mobileNumber;
	@Column(name = "sad_old_password", length = 25)
	private String oldPassword;
	@Column(name = "sad_new_password", length = 25)
	private String newPassword;

}
