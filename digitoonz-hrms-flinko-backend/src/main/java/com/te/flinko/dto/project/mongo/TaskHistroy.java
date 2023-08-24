package com.te.flinko.dto.project.mongo;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskHistroy implements Serializable{
	
	private String id;
	
	@Field("unassigned_employee_id")
    private String employeeId;
	
    @Field("assigned_employee_id")
    private String assignedEmployeeId;
    
    @Field("unassigning_reason")
    private String unassigningReason;
    
    @Field("unassigning_date")
    private LocalDate unassigningDate;
}