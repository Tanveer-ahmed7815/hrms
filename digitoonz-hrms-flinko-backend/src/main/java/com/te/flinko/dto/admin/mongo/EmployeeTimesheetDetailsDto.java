package com.te.flinko.dto.admin.mongo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.te.flinko.dto.employee.mongo.Timesheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeTimesheetDetailsDto implements Serializable{
	
	private Long timesheetId;
	
    private String employeeId;
	
    private Long companyId;
	
    private List<Timesheet> timesheets;
	
    private LocalDate approvalDate;
	
    private Boolean isApproved;
	
    private Map<String, String> approvedBy;
	
    private String rejectedBy;
	
    private String rejectionReason;
}
