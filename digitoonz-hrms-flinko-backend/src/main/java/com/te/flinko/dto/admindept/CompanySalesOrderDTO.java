package com.te.flinko.dto.admindept;

import java.io.Serializable;
import java.util.List;

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
public class CompanySalesOrderDTO implements Serializable {

	private Long salesOrderId;

	private String subject;

	private List<CompanyHardwareItemsDTO> companyHardwareItemsListDTO;

	private List<SalesOrderItemsDTO> salesOrderItemsListDto;

}