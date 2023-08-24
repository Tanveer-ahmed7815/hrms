package com.te.flinko.dto.admin;


import java.time.LocalDate;
/**
 * @author Tapas
 *
 */
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeOfficialInfoDTO {
                

	private Long officialId; 
	
	private String firstName;
	
	private String lastName;
	
	private String employeeId;
	
	private String officialEmailId;          

	private Long mobileNumber;
	
	private String branchName;              

	private Long branchId;	
	
	@JsonFormat(shape = Shape.STRING,pattern = "MM-dd-yyyy")
	private LocalDate doj;
	
	private String ruleName;
	
	private Long departmentId;				
	
	private Long workWeekRuleId;			
	
	private Long designationId;				
	
	private String department;              
	
	private String designation;
	
	private Boolean isActive;
	
	private List<String> roles;

}
