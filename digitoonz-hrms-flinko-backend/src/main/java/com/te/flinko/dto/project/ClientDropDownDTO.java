package com.te.flinko.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDropDownDTO {
	
	private Long clientId;
	
	private String clientName;

}
