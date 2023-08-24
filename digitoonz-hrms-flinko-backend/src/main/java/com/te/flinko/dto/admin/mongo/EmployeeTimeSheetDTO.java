package com.te.flinko.dto.admin.mongo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.flinko.dto.employee.mongo.Timesheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeTimeSheetDTO {
	private String employeeId;
	private String timesheetObjectId;
	private Long timesheetId;
	private Long employeeInfoId;
	private String employeeName;
	private String branch;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastModifiedDate;
	private List<Timesheet> timesheets;
	private String department;
	private String designation;
	private LocalDate startDate;
	private LocalDate endDate;
	private String timeSheetName;
	private String status;
	private Boolean isActionRequired;
	private String pendingAt;
	private String rejectedBy;
	private String reason;
	private String reportingManagerName;
}
