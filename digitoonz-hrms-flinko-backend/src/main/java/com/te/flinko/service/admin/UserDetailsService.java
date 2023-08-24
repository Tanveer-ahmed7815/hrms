package com.te.flinko.service.admin;

import java.util.List;

import com.te.flinko.dto.admin.AddExistingEmployeeDataRequestDto;
import com.te.flinko.dto.admin.BranchInfoDto;
import com.te.flinko.dto.admin.DepartmentInfoDto;
import com.te.flinko.dto.admin.DesignationInfoDto;
import com.te.flinko.dto.admin.EmployeeDataDto;
import com.te.flinko.dto.admin.EmployeeOfficialInfoDTO;
import com.te.flinko.dto.admin.WorkWeekInfoDto;
import com.te.flinko.dto.admindept.ProductNameDTO;
import com.te.flinko.dto.employee.EmployeeIdDto;
/**
 * @author Tapas
 *
 */

public interface UserDetailsService {

	/*
	 * method to find all the employee
	 */
	public List<EmployeeDataDto> userDetails(long companyId);

	/*
	 * API for User Management Details (Find by ID)
	 */
	public EmployeeOfficialInfoDTO userManagementDetails(Long companyId, Long officialId);

	/*
	 * API for status active/inactive
	 * */
	public String updateStatus(Long companyId, Long officialId,String employeeId, ProductNameDTO employeeStatusUpdateDTO);
	
	public String updateUserDetails(Long companyId, Long officialId,String employeeId,EmployeeOfficialInfoDTO employeeDataDto); 
	//get all data on add existing employee
	

	
	public List<BranchInfoDto> getAllBranchInfo(Long companyId);
	
	public List<DepartmentInfoDto> getAllDepartmentInfo(Long companyId);
	
	public List<DesignationInfoDto> getAllDesignationInfo(Long companyId,String department);
	
	public List<WorkWeekInfoDto> getAllWorkInfo(Long companyId);
	
	public List<EmployeeIdDto> getAllEmployeeName(Long companyId);
	
	//add all details of existing employee
	
	public String addExistingEmployee(Long companyId,String employeeId, AddExistingEmployeeDataRequestDto addExistingEmployeeDataRequestDto);
	


	

}
