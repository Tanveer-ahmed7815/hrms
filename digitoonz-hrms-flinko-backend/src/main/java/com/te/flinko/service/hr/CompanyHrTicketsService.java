package com.te.flinko.service.hr;

import java.util.List;

import com.te.flinko.dto.hr.mongo.CompanyHrTicketsDTO;

public interface CompanyHrTicketsService {
	
	public List<CompanyHrTicketsDTO> hrTicketsInfoList(Long companyId);
	
	public CompanyHrTicketsDTO hrTicketsDTOInfo(String hrTicketObjectId);
	
	public CompanyHrTicketsDTO updateTicketHistory(String status, Long employeeInfoId,String hrTicketObjectId);
	
	

}
