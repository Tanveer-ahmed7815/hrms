package com.te.flinko.dto.employee.mongo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timesheet implements Serializable{
	

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
    
    private String project;
    
    @Field("project_id")
    private Long projectId;
    
    private String task;
    @Field("task_id")
    private String taskId;
       
}