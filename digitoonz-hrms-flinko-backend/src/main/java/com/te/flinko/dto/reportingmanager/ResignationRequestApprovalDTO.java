package com.te.flinko.dto.reportingmanager;

import java.util.List;

import com.te.flinko.entity.project.ProjectDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResignationRequestApprovalDTO {

	private Long resignationId;
	private Long employeeInfoId;
	private String employeeId;
	private String fullName;	
	private String officialEmailId;	
	private Long mobileNumber;	
	private String department;	
	private String designation;	
	private List<String> allocatedProjectList;	
	private String resignationReason;
	private String status;
	private String reportingManager;
//	private Boolean isActionRequired;
//	private String pendingAt;
}
