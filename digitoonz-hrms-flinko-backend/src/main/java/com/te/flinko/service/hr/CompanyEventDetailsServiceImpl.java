package com.te.flinko.service.hr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.employee.MultipleMailDTO;
import com.te.flinko.dto.hr.BranchNameFetchDTO;
import com.te.flinko.dto.hr.EmployeeInformationDTO;
import com.te.flinko.dto.hr.EventManagementAddEventDTO;
import com.te.flinko.dto.hr.EventManagementAddWinnerDTO;
import com.te.flinko.dto.hr.EventManagementDepartmentNameDTO;
import com.te.flinko.dto.hr.EventManagementDisplayEventDTO;
import com.te.flinko.dto.hr.EventManagementEditEventDTO;
import com.te.flinko.dto.hr.EventManagementNameFetchDTO;
import com.te.flinko.dto.hr.EventManagementParticipantslistDTO;
import com.te.flinko.entity.Department;
import com.te.flinko.entity.admin.CompanyBranchInfo;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.hr.CompanyEventDetails;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.exception.PermissionDeniedException;
import com.te.flinko.exception.admin.NoDataPresentException;
import com.te.flinko.repository.DepartmentRepository;
import com.te.flinko.repository.admin.CompanyBranchInfoRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.EmployeeOfficialInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.hr.CompanyEventDetailsRepository;
import com.te.flinko.service.mail.employee.EventMailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyEventDetailsServiceImpl implements CompanyEventDetailsService {

	private CompanyEventDetails companyEventDetails = new CompanyEventDetails();

	@Autowired
	private CompanyEventDetailsRepository companyEventDetailsRepository;

	@Autowired
	private DepartmentRepository departmentInfoRepository;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonelInfoRepository;

	@Autowired
	private CompanyBranchInfoRepository branchInfoRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	EmployeeOfficialInfoRepository employeeOfficialInfoRepository;

	@Autowired
	private EventMailService eventMailService;

	// AddEvent Api...

	@Override
	public EventManagementAddEventDTO adddetails(EventManagementAddEventDTO eventdto, Long companyId) {
		Optional<CompanyInfo> companyInfoDetails = companyInfoRepository.findById(companyId);
		if (!companyInfoDetails.isPresent()) {
			throw new DataNotFoundException("Company is not Present");
		} else {
			CompanyEventDetails companyEventDetails = new CompanyEventDetails();

			if (eventdto.getEventDate().isBefore(LocalDate.now())) {
				throw new NoDataPresentException("Previous Events Cannot Be Added");
			}
			BeanUtils.copyProperties(eventdto, companyEventDetails);

			companyEventDetails.setCompanyInfo(companyInfoDetails.get());

			BeanUtils.copyProperties(companyEventDetailsRepository.save(companyEventDetails), eventdto);

			Boolean isMailRequired = eventdto.getIsMailRequired();
			if (isMailRequired) {
				log.info("sending mail");
				sendMail(eventdto.getEmployees(), companyId, eventdto);
			}

			return eventdto;

		}

	}

	// Department names getting Api..

	@Override
	public List<EventManagementDepartmentNameDTO> fetchDepartmentNames() {

		List<Department> departmentsList = departmentInfoRepository.findAll();
		if (departmentsList.isEmpty()) {

		}

		List<EventManagementDepartmentNameDTO> eventManagementDepartmentNamedtoList = new ArrayList<>();

		for (Department department : departmentsList) {

			eventManagementDepartmentNamedtoList.add(
					new EventManagementDepartmentNameDTO(department.getDepartmentId(), department.getDepartmentName()));
		}
		return eventManagementDepartmentNamedtoList;
	}

	// Eventlist displaying api based on EventId..
	@Override
	public EventManagementDisplayEventDTO displayEventDetails(Long eventId) {

		CompanyEventDetails eventDetails = companyEventDetailsRepository.findByEventId(eventId);

		if (eventDetails != null) {
			CompanyInfo companyInfo = eventDetails.getCompanyInfo();
			Long companyId = companyInfo != null ? companyInfo.getCompanyId() : null;

			EventManagementDisplayEventDTO eventManagementDisplayEventdto = new EventManagementDisplayEventDTO();

			BeanUtils.copyProperties(eventDetails, eventManagementDisplayEventdto);
			eventManagementDisplayEventdto.setPhotoUrl(eventDetails.getPhotoUrl());
			eventManagementDisplayEventdto.setLocation(eventDetails.getLocation());

			List<EventManagementAddWinnerDTO> winnerlist = new ArrayList<>();
			List<EmployeeInformationDTO> employeelist = new ArrayList<>();

			List<String> winnerIdlist = eventDetails.getWinners();
			List<String> employeeIdlist = eventDetails.getEmployees();

			List<String> departmentsList = eventDetails.getDepartments();

			List<EventManagementDepartmentNameDTO> departmentListDTO = new ArrayList<>();

			List<Department> alldepartmentList = departmentInfoRepository.findAll();

			for (Department department : alldepartmentList) {
				for (String dName : departmentsList) {
					if (dName.equals(department.getDepartmentName()))
						departmentListDTO.add(new EventManagementDepartmentNameDTO(department.getDepartmentId(),
								department.getDepartmentName()));
				}
			}
			eventManagementDisplayEventdto.setDepartments(departmentListDTO);

			for (String winnerId : winnerIdlist) {

				EventManagementAddWinnerDTO addWinnerDTO = new EventManagementAddWinnerDTO();
				List<EmployeePersonalInfo> winnerInfo = employeePersonelInfoRepository
						.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(winnerId, companyId);
				if (!winnerInfo.isEmpty()) {

					addWinnerDTO.setEmployeeId(winnerInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
					addWinnerDTO.setEmployeeInfoId(winnerInfo.get(0).getEmployeeInfoId());
					addWinnerDTO.setWinnerName((winnerInfo.get(0).getFirstName() + " " + winnerInfo.get(0).getLastName()));
					addWinnerDTO.setDepartment(winnerInfo.get(0).getEmployeeOfficialInfo().getDepartment());

					winnerlist.add(addWinnerDTO);
				} else {
					throw new DataNotFoundException("Employee Personal id not Found");
				}

			}
			for (String employeeId : employeeIdlist) {
				EmployeeInformationDTO employeeInformationDTO = new EmployeeInformationDTO();
				List<EmployeePersonalInfo> employeeInfo = employeePersonelInfoRepository
						.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(employeeId, companyId);
				if (!employeeInfo.isEmpty()) {

					employeeInformationDTO.setEmployeeId(employeeInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
					employeeInformationDTO.setEmployeeInfoId(employeeInfo.get(0).getEmployeeInfoId());
					employeeInformationDTO.setFullname(employeeInfo.get(0).getFirstName() + " " + employeeInfo.get(0).getLastName());
					employeelist.add(employeeInformationDTO);
				} else {
					throw new DataNotFoundException("Employee Personal id not Found");
				}

			}
			eventManagementDisplayEventdto.setWinners(winnerlist);
			eventManagementDisplayEventdto.setEmployees(employeelist);

			return eventManagementDisplayEventdto;
		} else {
			throw new DataNotFoundException("Event Not Found");
		}

	}

//for fetching the EventLIst based on companyId and Month and Year

	@Override
	public List<EventManagementDisplayEventDTO> displayEventList(Long companyId, int year, int month) {

		Optional<CompanyInfo> optionaldetails = companyInfoRepository.findById(companyId);
		if (optionaldetails.isPresent()) {

			List<EventManagementDisplayEventDTO> eventdtoList = new ArrayList<>();

			List<CompanyEventDetails> companyEventDetails = companyEventDetailsRepository
					.findByCompanyInfoCompanyId(companyId);
			for (CompanyEventDetails eventlistDetails : companyEventDetails) {
				EventManagementDisplayEventDTO eventdto = new EventManagementDisplayEventDTO();
				if (eventlistDetails.getEventDate().getMonthValue() == month) {

					if (eventlistDetails.getEventDate().getYear() == year) {
						eventdto.setEventId(eventlistDetails.getEventId());
						eventdto.setEventDate(eventlistDetails.getEventDate());
						eventdto.setStartTime(eventlistDetails.getStartTime());
						eventdto.setEndTime(eventlistDetails.getEndTime());
						eventdto.setEventTitle(eventlistDetails.getEventTitle());
						eventdto.setEventDescription(eventlistDetails.getEventDescription());
						eventdto.setPhotoUrl(eventlistDetails.getPhotoUrl());
						eventdto.setLocation(eventlistDetails.getLocation());
						List<EventManagementAddWinnerDTO> winnerlist = new ArrayList<>();
						List<String> winnerIdlist = eventlistDetails.getWinners();

						for (String winnerId : winnerIdlist) {

							EventManagementAddWinnerDTO addWinnerDTO = new EventManagementAddWinnerDTO();
							List<EmployeePersonalInfo> winnerInfo = employeePersonelInfoRepository
									.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(winnerId, companyId);
							if (!winnerInfo.isEmpty()) {

								addWinnerDTO.setEmployeeId(winnerInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
								addWinnerDTO.setEmployeeInfoId(winnerInfo.get(0).getEmployeeInfoId());
								addWinnerDTO
										.setWinnerName((winnerInfo.get(0).getFirstName() + " " + winnerInfo.get(0).getLastName()));
								addWinnerDTO.setDepartment(winnerInfo.get(0).getEmployeeOfficialInfo().getDepartment());
								winnerlist.add(addWinnerDTO);
							} else {
								log.error("Winner's personal employee id not found for event id: "
										+ eventlistDetails.getEventId());

							}

						}

						eventdto.setWinners(winnerlist);
						eventdtoList.add(eventdto);
					}

				}

			}

			List<EventManagementDisplayEventDTO> unsortedeventdtoList = eventdtoList;
			Comparator<EventManagementDisplayEventDTO> basedOnDate = (date1, date2) -> date1.getEventDate()
					.compareTo(date2.getEventDate());
			Comparator<EventManagementDisplayEventDTO> basedOnStartTime = (time1, time2) -> time1.getStartTime()
					.compareTo(time2.getStartTime());

			return unsortedeventdtoList.stream().sorted(basedOnDate.thenComparing(basedOnStartTime))
					.collect(Collectors.toList());
		} else {

			throw new DataNotFoundException("Company Id not present");
		}

	}

// for uploading the winner Api
	@Override
	public EventManagementAddWinnerDTO addWinners(EventManagementAddWinnerDTO addWinnerdto, Long eventId) {

		Optional<CompanyEventDetails> eventDetails = companyEventDetailsRepository.findById(eventId);

		if (eventDetails.isPresent()) {

			CompanyEventDetails companyEventDetails2 = eventDetails.get();
			if ((companyEventDetails2.getEventDate().isAfter(LocalDate.now())
					|| (companyEventDetails2.getEventDate().isEqual(LocalDate.now())
							&& companyEventDetails2.getStartTime().isAfter(LocalTime.now())))) {
				throw new PermissionDeniedException("Event Not Yet Started");
			}
			BeanUtils.copyProperties(addWinnerdto, companyEventDetails2);
			if (!(addWinnerdto.getPhotoUrl().endsWith(".jpeg") || addWinnerdto.getPhotoUrl().endsWith(".jpg"))) {
				throw new PermissionDeniedException("Jpeg Format Only Accepted");
			}
			BeanUtils.copyProperties(companyEventDetailsRepository.save(companyEventDetails2), addWinnerdto);

			return addWinnerdto;
		}

		else {
			throw new DataNotFoundException("Event Not Found");

		}

	}
// for fetching the employee from EmployeePersonelInfo Api..

	@Override
	public List<EventManagementNameFetchDTO> fetchEmployeeNames(Long companyId) {
		Optional<CompanyInfo> optionalCompanyDetails = companyInfoRepository.findById(companyId);
		if (optionalCompanyDetails.isPresent()) {

			List<EmployeePersonalInfo> employeePersonalInfoList = employeePersonelInfoRepository
					.findByCompanyInfoCompanyId(companyId);
			if (employeePersonalInfoList.isEmpty()) {
				throw new DataNotFoundException("Employee Not Present");

			} else {
				List<EventManagementNameFetchDTO> eventManagementNameFetchdtoList = new ArrayList<>();
				for (EmployeePersonalInfo employeePersonalInfo : employeePersonalInfoList) {
					if(employeePersonalInfo.getEmployeeOfficialInfo()!=null) {
					eventManagementNameFetchdtoList
							.add(new EventManagementNameFetchDTO(employeePersonalInfo.getEmployeeInfoId(),
									employeePersonalInfo.getFirstName()
											.concat(" " + employeePersonalInfo.getLastName()),
									employeePersonalInfo.getEmployeeOfficialInfo().getEmployeeId()));
					}
				}

				return eventManagementNameFetchdtoList;
			}
		} else {
			throw new DataNotFoundException("Company Id Not Present");
		}
	}

	// for fetching employees from list of Department

	public List<EventManagementNameFetchDTO> fetchMultipleDepartmentEmployees(Long companyId, List<String> department) {
		Optional<CompanyInfo> optionalDetails = companyInfoRepository.findById(companyId);
		if (optionalDetails.isPresent()) {
			List<EmployeePersonalInfo> employeePersonalInfoList;
			if (department.isEmpty()) {
				employeePersonalInfoList = employeePersonelInfoRepository.findByCompanyInfoCompanyId(companyId);
			} else {
				employeePersonalInfoList = employeePersonelInfoRepository
						.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoDepartmentIn(companyId, department);

			}

			if (employeePersonalInfoList.isEmpty()) {
				throw new DataNotFoundException("Employees Not Present");
			} else {
				List<EventManagementNameFetchDTO> eventManagementNameFetchdtoList = new ArrayList<>();
				for (EmployeePersonalInfo employeePersonalInfo : employeePersonalInfoList) {
					eventManagementNameFetchdtoList
							.add(new EventManagementNameFetchDTO(employeePersonalInfo.getEmployeeInfoId(),
									employeePersonalInfo.getFirstName()
											.concat(" " + employeePersonalInfo.getLastName()),
									employeePersonalInfo.getEmployeeOfficialInfo().getEmployeeId()));

				}

				return eventManagementNameFetchdtoList;
			}
		} else {
			throw new DataNotFoundException("Company Id Not Present");
		}
	}

	// for fetching branchId from company
	@Override
	public List<BranchNameFetchDTO> fetchBranchNames(Long companyId) {

		Optional<CompanyInfo> optionalDetails = companyInfoRepository.findById(companyId);
		if (optionalDetails.isPresent()) {

			List<BranchNameFetchDTO> fetchDtoList = new ArrayList<>();

			List<CompanyBranchInfo> companyBranchInfoList = branchInfoRepository.findByCompanyInfoCompanyId(companyId);

			if (!companyBranchInfoList.isEmpty()) {

				for (CompanyBranchInfo companyBranchInfo : companyBranchInfoList) {
					BranchNameFetchDTO fetchDto = new BranchNameFetchDTO();

					fetchDto.setBranchId(companyBranchInfo.getBranchId());
					fetchDto.setBranchName(companyBranchInfo.getBranchName());
					fetchDtoList.add(fetchDto);

				}

				return fetchDtoList;
			} else {
				throw new DataNotFoundException("Branches not Present");
			}
		} else {
			throw new DataNotFoundException("CompanyId Not Present");
		}
	}

// For Deleting the Event Api..

	@Override
	public void deleteEvent(Long eventId) {

		Optional<CompanyEventDetails> eventDetails = companyEventDetailsRepository.findById(eventId);

		if (eventDetails.isPresent()) {
			companyEventDetailsRepository.deleteById(eventId);

		} else {
			throw new DataNotFoundException("Event Not FOund");
		}

	}

// For Editing the Event
	@Override
	public EventManagementEditEventDTO editEvent(EventManagementEditEventDTO editEventdto, Long eventId) {

		Optional<CompanyEventDetails> companyEventDetails = companyEventDetailsRepository.findById(eventId);

		if (companyEventDetails.isPresent()) {

			CompanyEventDetails companyEventDetailsInfo = companyEventDetails.get();
			if ((companyEventDetailsInfo.getEventDate().isAfter(LocalDate.now())
					|| (companyEventDetailsInfo.getEventDate().isEqual(LocalDate.now())
							&& companyEventDetailsInfo.getStartTime().isAfter(LocalTime.now())))
					&& (!companyEventDetailsInfo.getStartTime().isBefore(LocalTime.now()))) {
				BeanUtils.copyProperties(editEventdto, companyEventDetailsInfo);
				BeanUtils.copyProperties(companyEventDetailsRepository.save(companyEventDetailsInfo), editEventdto);

				return editEventdto;

			} else {
				throw new PermissionDeniedException(
						"Once the Event is Started or Completed,the Event cannot be Edited");
			}

		}

		else {
			throw new DataNotFoundException("Event Not Found");

		}

	}

	// Fetching participants list for selecting Winners Api..

	@Override
	public List<EventManagementParticipantslistDTO> getParticipantsList(Long eventId) {

		Optional<CompanyEventDetails> eventInfoforId = companyEventDetailsRepository.findById(eventId);

		if (eventInfoforId.isPresent()) {
			CompanyEventDetails eventDetails = eventInfoforId.get();
			CompanyInfo companyInfo = eventDetails.getCompanyInfo();
			Long companyId = companyInfo != null ? companyInfo.getCompanyId() : null;
			List<EventManagementParticipantslistDTO> employeelist = new ArrayList<>();
			List<String> employeeIdlist = eventDetails.getEmployees();

			for (String employeeId : employeeIdlist) {
				List<EmployeePersonalInfo> employeeInfo = employeePersonelInfoRepository
						.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(employeeId, companyId);
				if (!employeeInfo.isEmpty()) {

					EventManagementParticipantslistDTO eventManagementParticipantslistDTO = new EventManagementParticipantslistDTO();
					eventManagementParticipantslistDTO
							.setEmployeeId(employeeInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
					eventManagementParticipantslistDTO.setEmployeeInfoId(employeeInfo.get(0).getEmployeeInfoId());
					eventManagementParticipantslistDTO
							.setEmployees(employeeInfo.get(0).getFirstName() + " " + employeeInfo.get(0).getLastName());
					employeelist.add(eventManagementParticipantslistDTO);
				} else {
					throw new DataNotFoundException("Employee Personal id not Found");
				}

			}

			return employeelist;
		} else {
			throw new DataNotFoundException("Event Id nor Present");
		}

	}

	public void sendMail(List<String> list, Long companyId, EventManagementAddEventDTO eventdto) {
		List<EmployeeOfficialInfo> employees = employeeOfficialInfoRepository
				.findByCompanyBranchInfoCompanyInfoCompanyIdAndEmployeeIdIn(companyId, list);
		if (!employees.isEmpty()) {
			MultipleMailDTO multipleMailDTO = new MultipleMailDTO();
			multipleMailDTO.setBody("Dear Employee, HR Team invites you to participate in " + eventdto.getEventTitle()
					+ "that’s going to be held on " + eventdto.getEventDate()
					+ ". More Information’s about the event can be found below." + "\n" + "EVENT"
					+ eventdto.getEventTitle() + "\n" + "DESCRIPTION" + eventdto.getEventDescription() + "\n"
					+ "EVENT DATE IS" + eventdto.getEventDate() + "\n" + "EVENT START AT" + eventdto.getStartTime()
					+ "\n" + "EVENT END IN " + eventdto.getEndTime() + "\n" + "Thanks & Regards" + "\n"
					+ "Team Flinko");
			multipleMailDTO.setSubject("Event Details");
			ArrayList<String> arrayList = new ArrayList<>();

			List<String> mails = employees.stream()
					.filter(EmployeeOfficialInfo -> EmployeeOfficialInfo.getOfficialEmailId() != null)
					.map(EmployeeOfficialInfo::getOfficialEmailId).collect(Collectors.toList());
			multipleMailDTO.setTo(mails);
			eventMailService.sendMultipleMail(multipleMailDTO);
			log.info("successfully mail send");

		}

	}

}
