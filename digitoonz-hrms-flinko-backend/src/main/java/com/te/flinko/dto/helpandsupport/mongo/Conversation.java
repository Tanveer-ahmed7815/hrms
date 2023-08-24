	package com.te.flinko.dto.helpandsupport.mongo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Builder
public class Conversation implements Serializable{
	@Field("chd_sender_employee_id")
    public String senderEmployeeId;
	
	@Field("chd_message")
    public String message;
	
	@Field("chd_date")
    public LocalDateTime date;
	
	@Field("is_read")
    public Boolean read;
}