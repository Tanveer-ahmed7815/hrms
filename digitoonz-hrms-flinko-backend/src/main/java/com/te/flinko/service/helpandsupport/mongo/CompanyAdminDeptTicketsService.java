package com.te.flinko.service.helpandsupport.mongo;


import java.util.List;

import com.te.flinko.dto.helpandsupport.mongo.CompanyAdminDeptTicketsResponseDto;
import com.te.flinko.dto.helpandsupport.mongo.CompanyadminDeptTicketsDto;

public interface CompanyAdminDeptTicketsService {

	public boolean createTickets(CompanyadminDeptTicketsDto companyAdminDeptTicketsDto);
	
	public List<CompanyAdminDeptTicketsResponseDto> getAllTickets(Long companyId);
	
	public List<CompanyAdminDeptTicketsResponseDto> getAllTicketsAccordingStatus(Long companyId,String status);
	
	public CompanyAdminDeptTicketsResponseDto getTicketById(String objectTicketId,String status);
	
	public Boolean updateTickets(Long employeeInfoId,CompanyadminDeptTicketsDto companyadminDeptTicketsDto);
	
	public List<CompanyAdminDeptTicketsResponseDto> getAllTicketsAccordingCategory(Long companyId,String category);
}
