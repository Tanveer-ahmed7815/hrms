package com.te.flinko.dto.admin;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CompanyExpenseCategoriesDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long expenseCategoryId;

	private String expenseCategoryName;
	
	private Boolean isSubmited;

}
