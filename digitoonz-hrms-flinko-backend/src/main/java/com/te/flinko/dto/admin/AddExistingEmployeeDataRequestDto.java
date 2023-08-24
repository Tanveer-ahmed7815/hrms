package com.te.flinko.dto.admin;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.OptBoolean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Tapas
 *
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
@Builder
public class AddExistingEmployeeDataRequestDto {
	
	private Long officialId; 
	
	private String firstName;   

	private String lastName;     
	
	private String officialEmailId;  
	
	@JsonFormat(shape = Shape.STRING,pattern = "MM-dd-yyyy")
	private LocalDate doj;
	
	private Long mobileNumber;              

	private Long workWeekRuleId;			
	
	private String ruleName;				
	
	private Map<String, String> status;    
	
	private String employeeId;            

	private Long branchId;					
	
	private String branchName;             

	private Long departmentId;				
	
	private String department;               
	
	private Long designationId;				
	
	
	private String designation;             
	private List<String> roles;            
	
}

// 