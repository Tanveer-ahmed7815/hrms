package com.te.flinko.dto.account.mongo;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Builder
public class ContactPerson implements Serializable{
	private String designation;

	@Field("email_id")
	private String emailId;

	@Field("mobile_number")
	private Long mobileNumber;

	@Field("contact_person_name")
	private String contactPersonName;
}