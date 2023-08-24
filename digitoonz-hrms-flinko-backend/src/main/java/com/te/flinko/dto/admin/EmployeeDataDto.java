package com.te.flinko.dto.admin;

/**
 * @author Tapas
 *
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDataDto {
	
	private Long officialId;
	

	private String firstName;
	
	private String lastName;

	private Long mobileNumber; 

	private Boolean isActive; 

	private String employeeId; 
	
	private Long designationId; 
	
	private String designation; 

	private String officialEmailId;

}
