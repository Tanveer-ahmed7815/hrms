package com.te.flinko.dto.admin;

/**
 * @author Tapas
 *
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
@JsonInclude(value = Include.NON_DEFAULT)
@Builder
public class CompanyStockCategoriesDTO {
	private Long stockCategoryId;
	private String stockCategoryName;
	private String symbol;
	private String stockGroupName;
	private Long stockGroupId;
	private Long unitId;
	private Boolean isSubmited;
}
