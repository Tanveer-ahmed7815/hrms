package com.te.flinko.service.employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.employee.CompanyEmployeeResignationDetailsDTO;
import com.te.flinko.dto.employee.EmployeeResignationDiscussionDTO;
import com.te.flinko.dto.hr.NotificationExitInterviewDropdownDTO;
import com.te.flinko.entity.employee.CompanyEmployeeResignationDetails;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeResignationDiscussion;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeResignationRequestRepository;

@Service
public class EmployeeResignationRequestServiceImpl implements EmployeeResignationRequestService {

	@Autowired
	EmployeeResignationRequestRepository resignationRepository;

	@Autowired
	private EmployeePersonalInfoRepository personalInfoRepository;

	@Override
	public CompanyEmployeeResignationDetailsDTO saveEmployeeResignation(
			CompanyEmployeeResignationDetailsDTO resignationDetailsDTO, Long employeeInfoId, Long companyId) {

		 List<EmployeePersonalInfo> OptionalPersonalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);
		 if(OptionalPersonalInfo.isEmpty()) {
			 throw new DataNotFoundException("PersonalInfo Not Found");
		 }
		 EmployeePersonalInfo personalInfo = OptionalPersonalInfo.get(0);
		CompanyEmployeeResignationDetailsDTO newDTO = new CompanyEmployeeResignationDetailsDTO();
		CompanyEmployeeResignationDetails resignationDetails = new CompanyEmployeeResignationDetails();
		BeanUtils.copyProperties(resignationDetailsDTO, resignationDetails);

		resignationDetails.setAppliedDate(LocalDate.now());
		resignationDetails.setStatus("Pending");
		resignationDetails.setCompanyInfo(personalInfo.getCompanyInfo());
		resignationDetails.setEmployeePersonalInfo(personalInfo);
		BeanUtils.copyProperties(resignationRepository.save(resignationDetails), newDTO);

		return newDTO;
	}


	@Override
	public CompanyEmployeeResignationDetailsDTO getEmployeeResignation(Long employeeInfoId,Long companyId) {

		CompanyEmployeeResignationDetailsDTO resignationDto = new CompanyEmployeeResignationDetailsDTO();

		 List<CompanyEmployeeResignationDetails> resignationRequestList = resignationRepository
				.findByEmployeePersonalInfoEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId,companyId);
		 if(resignationRequestList.isEmpty()) {
			 throw new DataNotFoundException("Data Not Found");
		 }
		 int size = resignationRequestList.size();
		 CompanyEmployeeResignationDetails resignationRequest = resignationRequestList.get(size-1);
		 
		List<EmployeeResignationDiscussion> employeeResignationDiscussionList = resignationRequest.getEmployeeResignationDiscussionList();

		EmployeeResignationDiscussionDTO discussionDTO = new EmployeeResignationDiscussionDTO();

		List<EmployeeResignationDiscussionDTO> DTOList = new ArrayList();

		List<LocalDate> discussionDate = new ArrayList();
		
		for (EmployeeResignationDiscussion employeeResignationDiscussion : employeeResignationDiscussionList) {

			BeanUtils.copyProperties(employeeResignationDiscussion, discussionDTO);

			List<NotificationExitInterviewDropdownDTO> organizerDTOList = new ArrayList<>();

			List<EmployeePersonalInfo> organizers = employeeResignationDiscussion.getEmployeePersonalInfoList();

			for (EmployeePersonalInfo personalInfo : organizers) {
				NotificationExitInterviewDropdownDTO organizerDTO = new NotificationExitInterviewDropdownDTO();

				organizerDTO.setEmployeeId(personalInfo.getEmployeeOfficialInfo().getEmployeeId());
				organizerDTO.setEmployeeInfoId(personalInfo.getEmployeeInfoId());
				organizerDTO.setEmployeeName(personalInfo.getFirstName() + " " + personalInfo.getLastName());
				organizerDTOList.add(organizerDTO);
			}
			discussionDTO.setOrganizerDetails(organizerDTOList);

			DTOList.add(discussionDTO);
			discussionDate.add(employeeResignationDiscussion.getDiscussionDate());
		}
		resignationDto.setDiscussion(DTOList);
		resignationDto.setDiscussionDate(discussionDate);
//		resignationDto.setFeedback(null);
		BeanUtils.copyProperties(resignationRequest, resignationDto);

		return resignationDto;
	}

}
