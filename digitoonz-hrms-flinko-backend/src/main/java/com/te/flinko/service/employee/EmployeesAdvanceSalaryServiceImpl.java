package com.te.flinko.service.employee;

import static com.te.flinko.common.admin.EmployeeReimbursementInfoConstants.EMPLOYEE_NOT_FOUND;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.employee.EmployeeAdvanceSalaryDTO;
import com.te.flinko.dto.hr.EmployeeInformationDTO;
import com.te.flinko.entity.employee.EmployeeAdvanceSalary;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.AdvanceSalaryApprovalDeleteException;
import com.te.flinko.exception.FormInformationNotFilledException;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;
import com.te.flinko.repository.employee.EmployeeAdvanceSalaryRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;

@Service
public class EmployeesAdvanceSalaryServiceImpl implements EmployeesAdvanceSalaryService {

	@Autowired
	private EmployeeAdvanceSalaryRepository repository;

	@Autowired
	private EmployeePersonalInfoRepository personalInfoRepository;
	
	@Autowired
	private LevelsOfApprovalRepository approvalRepository;

	@Override
	public EmployeeAdvanceSalaryDTO saveAdvanceSalaryRequest(Long companyId, EmployeeAdvanceSalaryDTO advanceSalaryDTO,
			Long employeeInfoId) {
		EmployeeAdvanceSalary advanceSalary = new EmployeeAdvanceSalary();
		EmployeePersonalInfo employeePersonalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId).get(0);
		if (employeePersonalInfo == null) {
			throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
		} else {

		
			if (advanceSalaryDTO != null) {			

					BeanUtils.copyProperties(advanceSalaryDTO, advanceSalary);
					advanceSalary.setEmployeePersonalInfo(employeePersonalInfo);
					advanceSalary.setStatus("Pending");
					BeanUtils.copyProperties(repository.save(advanceSalary), advanceSalaryDTO);

					return advanceSalaryDTO;
			} else {
				throw new FormInformationNotFilledException("Form informations are not filled properly");
			}
		}
	}

	@Override
	public List<EmployeeAdvanceSalaryDTO> getAdvanceSalaryDTOList(Long employeeInfoId, Long companyId) {

		List<EmployeePersonalInfo> personalInfoList = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);
		if (!personalInfoList.isEmpty()) {

			List<EmployeeAdvanceSalary> employeeAdvanceSalaryList = personalInfoList.get(0)
					.getEmployeeAdvanceSalaryList();

			List<EmployeeAdvanceSalaryDTO> advanceSalaryList = new ArrayList<>();

			for (EmployeeAdvanceSalary employeeAdvanceSalary : employeeAdvanceSalaryList) {
				EmployeeAdvanceSalaryDTO tempDTO = new EmployeeAdvanceSalaryDTO();
				tempDTO.setRequestedOn(employeeAdvanceSalary.getCreatedDate());
				
				LinkedHashMap<String, String> approvedBy = employeeAdvanceSalary.getApprovedBy();
				Set<String> keySet = approvedBy.keySet();
				HashMap<String, String> resultMap = new HashMap<>();
				for (String keyValue : keySet) {				
					resultMap.put( keyValue,"Approved");
				}
				if(employeeAdvanceSalary.getRejectedBy()!=null) {
					List<String> advanceSalaryApproval = approvalRepository.findByCompanyInfoCompanyId(companyId).get().getAdvanceSalary();
					if(keySet.size()==0)
						resultMap.put(advanceSalaryApproval.get(0),"Rejected");
					else if(keySet.size()==1)
						resultMap.put(advanceSalaryApproval.get(1), "Rejected");
					else
						resultMap.put(advanceSalaryApproval.get(2), "Rejected");
				}
				BeanUtils.copyProperties(employeeAdvanceSalary, tempDTO);
				tempDTO.setApprovedBy(resultMap);
				advanceSalaryList.add(tempDTO);
			}
			
			return advanceSalaryList;
		} else {
			throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
		}
	}

	@Override
	public EmployeeAdvanceSalaryDTO getAdvanceSalary(Long advanceSalaryId, Long companyId) {

		Optional<EmployeeAdvanceSalary> advanceSalary = repository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(advanceSalaryId, companyId);
		EmployeeAdvanceSalary employeeAdvanceSalary = advanceSalary.get();
		
		if (advanceSalary.isPresent()) {
			EmployeeAdvanceSalaryDTO advanceSalaryDTO = new EmployeeAdvanceSalaryDTO();

			LinkedHashMap<String, String> approvedBy = employeeAdvanceSalary.getApprovedBy();
			Set<String> keySet = approvedBy.keySet();
			HashMap<String, String> resultMap = new HashMap<>();
			for (String keyValue : keySet) {				
				resultMap.put( keyValue,"Approved");
			}
			if(employeeAdvanceSalary.getRejectedBy()!=null) {
				List<String> advanceSalaryApproval = approvalRepository.findByCompanyInfoCompanyId(companyId).get().getAdvanceSalary();
				if(keySet.size()==0)
					resultMap.put(advanceSalaryApproval.get(0),"Rejected");
				else if(keySet.size()==1)
					resultMap.put(advanceSalaryApproval.get(1), "Rejected");
				else
					resultMap.put(advanceSalaryApproval.get(2), "Rejected");
			}
			
			BeanUtils.copyProperties(advanceSalary.get(), advanceSalaryDTO);
			advanceSalaryDTO.setApprovedBy(resultMap);
			return advanceSalaryDTO;
		} else {
			throw new DataNotFoundException("Data is Not Present");
		}
	}

	@Override
	public void deleteAdvanceSalaryRequest(Long advanceSalaryId, Long companyId) {

		Optional<EmployeeAdvanceSalary> employeeAdvanceSalary = repository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(advanceSalaryId, companyId);

		if (employeeAdvanceSalary.isPresent()) {

			EmployeeAdvanceSalary advanceSalary = employeeAdvanceSalary.get();

			if (advanceSalary.getApprovedBy().isEmpty() && advanceSalary.getRejectedBy() == null) {

				repository.deleteById(advanceSalaryId);
			} else {
				throw new AdvanceSalaryApprovalDeleteException("Request Can't be Deleted");
			}
		} else {
			throw new DataNotFoundException("Request Not Found");
		}
	}

	@Override
	public EmployeeAdvanceSalaryDTO editAdvanceSalaryRequest(EmployeeAdvanceSalaryDTO advanceSalaryDTO,
			Long advanceSalaryId, Long companyId) {

		Optional<EmployeeAdvanceSalary> employeeAdvanceSalary = repository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(advanceSalaryId, companyId);
		if (employeeAdvanceSalary.isPresent()) {

			EmployeeAdvanceSalary emplAdvanceSalary = employeeAdvanceSalary.get();

			if (emplAdvanceSalary.getApprovedBy().isEmpty() && emplAdvanceSalary.getRejectedBy() == null) {

				advanceSalaryDTO.setAdvanceSalaryId(advanceSalaryId);
				BeanUtils.copyProperties(advanceSalaryDTO, emplAdvanceSalary);

				BeanUtils.copyProperties(repository.save(emplAdvanceSalary), advanceSalaryDTO);

				return advanceSalaryDTO;
			} else {
				throw new AdvanceSalaryApprovalDeleteException("Request Can't Be Edited");
			}
		} else {
			throw new DataNotFoundException("Request Not Found");
		}
	}
}
