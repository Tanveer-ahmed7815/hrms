package com.te.flinko.dto.hr.mongo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.te.flinko.dto.hr.EmployeeInformationDTO;
import com.te.flinko.dto.hr.EventManagementDepartmentNameDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryDTO {
	private String status;
    private LocalDate date;
    private EmployeeInformationDTO by;
   // private String changedBy;
    private EventManagementDepartmentNameDTO department;
}

