package com.te.flinko.dto.employee;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetDTO {
	
	private String id;
	
	@JsonFormat(shape = Shape.STRING,pattern = "MM-dd-yyyy")
    private LocalDate date;
    
    @Field("login_time")
    @JsonFormat(shape = Shape.STRING,pattern = "HH:mm:ss")
    private LocalTime loginTime;
    
    @Field("logout_time")
    @JsonFormat(shape = Shape.STRING,pattern = "HH:mm:ss")
    private LocalTime logoutTime;
    
    @Field("break_duration")
    private int breakDuration;
    
    private Long projectId;
    
    private String project;
    
    private Long taskId;
    
    private String task;
}
