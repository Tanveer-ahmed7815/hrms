package com.te.flinko.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.employee.GetReportingManagerDTO;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;

@Service
public class EmployeeHierarchyServiceImpl implements EmployeeHierarchyService {

	@Autowired
	private EmployeePersonalInfoRepository personalInfoRepository;

	@Override
	public GetReportingManagerDTO getEmployeeHierarchy(Long employeeInfoId, Long companyId) {

		GetReportingManagerDTO employeeHierarchyDTO = new GetReportingManagerDTO();
		EmployeePersonalInfo employeePersonalInfo = personalInfoRepository.findById(employeeInfoId).orElse(null);
		if (employeePersonalInfo == null) {
			return employeeHierarchyDTO;
		}
		return getHierarchy(employeePersonalInfo);
	}

//	private GetReportingManagerDTO getHierarchy(EmployeePersonalInfo employeePersonalInfo){
//		
//		GetReportingManagerDTO hierarchyelement = new GetReportingManagerDTO();
//		hierarchyelement.setEmployeeInfoId(employeePersonalInfo.getEmployeeInfoId());
//		hierarchyelement.setName(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName());
//		hierarchyelement.setDesignation(employeePersonalInfo.getEmployeeOfficialInfo().getDesignation());
//		
//		if(employeePersonalInfo.getEmployeeInfoList()==null || employeePersonalInfo.getEmployeeInfoList().isEmpty()) {
//			hierarchyelement.setManager(null);
//			return hierarchyelement;
//		}
//		GetReportingManagerDTO employeeHierarchyDTO = getHierarchy(employeePersonalInfo.getEmployeeInfoList().get(0).getReportingManager());
//		
//		hierarchyelement.setManager(employeeHierarchyDTO);
//		return hierarchyelement;
//	}

	private GetReportingManagerDTO getHierarchy(EmployeePersonalInfo employeePersonalInfo) {
		if (employeePersonalInfo.getEmployeeInfoList() == null
				|| employeePersonalInfo.getEmployeeInfoList().isEmpty()) {
			GetReportingManagerDTO managerDTO = new GetReportingManagerDTO();
			managerDTO.setName(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName());
			managerDTO.setEmployeeInfoId(employeePersonalInfo.getEmployeeInfoId());
			managerDTO.setDesignation(employeePersonalInfo.getEmployeeOfficialInfo().getDesignation());
			return managerDTO;
		}
		GetReportingManagerDTO employeeHierarchyDTO = getHierarchy(
				employeePersonalInfo.getEmployeeInfoList().get(0).getReportingManager());
		GetReportingManagerDTO hierarchyelement = new GetReportingManagerDTO();
		hierarchyelement.setEmployeeInfoId(employeePersonalInfo.getEmployeeInfoId());
		hierarchyelement.setName(employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName());
		hierarchyelement.setDesignation(employeePersonalInfo.getEmployeeOfficialInfo().getDesignation());
		employeeHierarchyDTO.setManager(hierarchyelement);
		return employeeHierarchyDTO;
	}
}