package com.te.flinko.entity.helpandsupport.mongo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.te.flinko.dto.helpandsupport.mongo.Conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("fa_company_chat_details")
public class CompanyChatDetails implements Serializable{
	@Field("chd_chat_id")
	private Long chatId;
	
	@Field("chd_company_id")
	private Long companyId;
	
	@Field("chd_employee_one")
	private String employeeOne;
	
	@Field("chd_employee_two")
	private String employeeTwo;
	
	@Field("chd_conversation")
    private List<Conversation> conversations;
}
