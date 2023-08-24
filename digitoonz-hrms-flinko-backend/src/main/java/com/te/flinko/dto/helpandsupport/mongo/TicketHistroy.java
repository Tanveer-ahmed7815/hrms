package com.te.flinko.dto.helpandsupport.mongo;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class TicketHistroy implements Serializable{
	
    private String status;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/kolkata")
    private LocalDate date;
    private Long by;
    private String ownerName;
    private String employeeId;
    private String department;
}