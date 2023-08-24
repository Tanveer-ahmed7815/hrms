package com.te.flinko.dto.admin;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
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

public class CompanyLeadCategoriesDTOList  implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<CompanyLeadCategoriesDTO> companyLeadCategoriesDtoList;
}
