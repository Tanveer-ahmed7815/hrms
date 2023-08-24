package com.te.flinko.entity.employee.mongo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.te.flinko.audit.Audit;
import com.te.flinko.dto.employee.mongo.Timesheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("fa_employee_timesheet_details")
public class EmployeeTimesheetDetails extends Audit implements Serializable {
	
	@Id
	private String timesheetObjectId; 
	
	@Field("ets_timesheet_id")
	private Long timesheetId;
	
	@Field("ets_employee_id")
    private String employeeId;
	
	@Field("ets_company_id")
    private Long companyId;
	
	@Field("ets_timesheet")
    private List<Timesheet> timesheets;
	
	@Field("ets_approval_date")
	@JsonFormat(shape = Shape.STRING,pattern = "MM-dd-yyyy")
    private LocalDate approvalDate;
	
	@Field("ets_is_approved")
    private Boolean isApproved;
	
	@Field("ets_approved_by")
    private Map<String, String> approvedBy;
	
	@Field("ets_rejected_by")
    private String rejectedBy;
	
	@Field("ets_rejection_reason")
    private String rejectionReason;
}
