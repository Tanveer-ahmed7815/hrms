package com.te.flinko.dto.admindept;

import java.io.Serializable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.Convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.flinko.entity.account.CompanySalesOrder;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.it.PcLaptopSoftwareDetails;
import com.te.flinko.util.MapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Brunda
 *
 */

@SuppressWarnings("serial")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(value = Include.NON_DEFAULT)
public class CompanyPCLaptopDTO implements Serializable {

	private String serialNumber;
	private String inOut; 
	private String subject;
	private Long purchaseOrderId;
	private Long salesOrderId;
	private Long subjectId;
	private Long productItemId; 
	private String productName;
	private String memory;
	private String processor;
	private String os;
	private String storage;
	private BigDecimal amount;
	private LocalDate allocatedDate;
	private String status;
	private String availability;
	private Boolean cpldIsWorking;
	private List<HistoryDTO> historyDTOs;
	private EmployeePersonalInfoDTO employeePersonalInfo; 
	private List<PcLaptopSoftwareDetailsDTO> pcLaptopSoftwareDetailsList;
	private List<IssuesDTO> issuesDTO;
	
}
