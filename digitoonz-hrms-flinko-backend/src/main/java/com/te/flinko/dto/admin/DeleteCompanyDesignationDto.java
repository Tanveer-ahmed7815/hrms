package com.te.flinko.dto.admin;

import lombok.Data;

/**
 * @author Tanveer Ahmed
 *
 */
@Data
public class DeleteCompanyDesignationDto {

	private Long companyId;
	private Long designationId;
	private String designationName;
}
