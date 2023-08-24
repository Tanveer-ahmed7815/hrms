package com.te.flinko.dto.employee.mongo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.te.flinko.dto.employee.TimesheetDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTimesheetDetailsDTO {

	private String id;

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
