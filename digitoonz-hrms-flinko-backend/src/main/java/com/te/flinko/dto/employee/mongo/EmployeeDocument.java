package com.te.flinko.dto.employee.mongo;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Builder
public class EmployeeDocument implements Serializable{
	@Field("offer_letter")
    private String offerLetter;
	
	@Field("releaving_letter")
    private String releavingLetter;
}