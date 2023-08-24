package com.te.flinko.response.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sahid
 *
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class EmployeeLoginResponse {
	
	private Boolean error;
	private String message;
	private Object data;

}
