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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyLeadCategoriesDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long leadCategoryId;
	private String leadCategoryName;
	private String color;
	private Boolean isSubmited;

}
