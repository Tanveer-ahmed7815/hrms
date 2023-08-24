package com.te.flinko.dto.admindept;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CompanyHardwareItemsDTO implements Serializable {

	private String indentificationNumber;

	private String inOut;
	
	private String productName;

	private BigDecimal amount;

	private String allocatedBy;

	@JsonFormat(shape = Shape.STRING , pattern = "MM-DD-YYYY")
	private LocalDate allocatedDate;
	
	private Long salesOrderId;
	
	private Boolean isWorking;
	
	private Long purchaseOrderId;
	
	private List<IssuesDTO> issuesDTOs;

	private List<HistoryDTO> historyDTOs;

	private EmployeePersonalInfoDTO employeePersonalInfoDto;
	
	private String status;
	
	private String subject; 
	
	private Long subjectId; 
	
	private Long productId;  

}