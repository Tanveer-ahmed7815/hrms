package com.te.flinko.service.admin;
import java.util.List;

import com.te.flinko.dto.admin.CompanyWorkWeekRuleDTO;
import com.te.flinko.dto.admin.CompanyWorkWeekRuleNameDTO;
import com.te.flinko.dto.admin.EmployeeOfficialInfoResponseDTO;
import com.te.flinko.dto.admin.UpdateAllEmpWorkWeekRuleDTO;
import com.te.flinko.dto.admin.UpdateEmployeeWorkWeekDTO;
import com.te.flinko.responsedto.admin.CompanyWorkWeekRuleResponseDto;

//@author Rakesh Kumar Nayak
public interface CompanyWorkWeekRuleService {
	
	

	public Boolean createCompWorkWeek(CompanyWorkWeekRuleDTO companyWorkWeekRuleDto,Long companyId);
	
	public List<CompanyWorkWeekRuleResponseDto> getAllWorkWeekRule(Long companyId);
	
	public Boolean updateCompanyWorkweek(CompanyWorkWeekRuleDTO companyWorkWeekRuleDto,Long companyId);
	
	public Boolean deleteComanyWorkWeekRule(Long companyId,Long workWeekRuleId);
	
	public List<EmployeeOfficialInfoResponseDTO> getAllEmployeeDetails(Long companyId);
	
	public List<CompanyWorkWeekRuleNameDTO> getAllWorkWeekRuleName(Long companyId);
	
	public EmployeeOfficialInfoResponseDTO findEmployeeWithId(Long companyId,Long employeeInfoId);
	
	public Boolean updateEmployeeWorkWeek(UpdateEmployeeWorkWeekDTO employeeOfficialInfoResponseDto,Long companyId,Long employeeInfoId);


	public Boolean updateAllEmpWorkWeek(UpdateAllEmpWorkWeekRuleDTO updateAllEmpWorkWeekRuleDto,Long companyId);
	
	
	public void defaultCheck(CompanyWorkWeekRuleDTO companyWorkWeekRuleDto,Long companyId);
	
	
	//public void allEmployeeUpdate(List<EmployeePersonalInfo> employeePersonalInfoList);
	
	

}
