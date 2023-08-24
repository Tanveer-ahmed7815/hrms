package com.te.flinko.service.helpandsupport.mongo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.helpandsupport.mongo.CompanyAdminDeptTicketsResponseDto;
import com.te.flinko.dto.helpandsupport.mongo.CompanyadminDeptTicketsDto;
import com.te.flinko.dto.helpandsupport.mongo.TicketHistroy;
import com.te.flinko.dto.helpandsupport.mongo.TicketHistroyDto;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.helpandsupport.mongo.CompanyAdminDeptTickets;
import com.te.flinko.exception.admin.NoEmployeePresentException;
import com.te.flinko.exception.admin.NoTicketFoundException;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.helpandsupport.mongo.CompanyAdminDeptTicketsRepo;

@Service
public class CompanyAdminDeptTicketsServiceImpl implements CompanyAdminDeptTicketsService {

	@Autowired
	CompanyAdminDeptTicketsRepo companyAdminDeptTicketsRepo;

	@Autowired
	EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Override
	public boolean createTickets(CompanyadminDeptTicketsDto companyAdminDeptTicketsDto) {

		CompanyAdminDeptTickets companyAdminDeptTickets = new CompanyAdminDeptTickets();
		BeanUtils.copyProperties(companyAdminDeptTicketsDto, companyAdminDeptTickets);
		List<TicketHistroy> ticketHistoryList = new ArrayList<>();
		for (TicketHistroyDto ticketHistroyDto : companyAdminDeptTicketsDto.getTicketHistroys()) {
			TicketHistroy ticketHistroy = new TicketHistroy();
			BeanUtils.copyProperties(ticketHistroyDto, ticketHistroy);
			ticketHistoryList.add(ticketHistroy);
		}
		companyAdminDeptTickets.setTicketHistroys(ticketHistoryList);

		CompanyAdminDeptTickets save = companyAdminDeptTicketsRepo.save(companyAdminDeptTickets);
		return Optional.ofNullable(save).isPresent();

	}

	@Override
	public List<CompanyAdminDeptTicketsResponseDto> getAllTickets(Long companyId) {

		List<CompanyAdminDeptTickets> allTickets = companyAdminDeptTicketsRepo.findByCompanyId(companyId);
		if (allTickets.isEmpty()) {
			throw new NoTicketFoundException(AdminConstants.NO_TICKETS_FOUND);
		}

		return duplicateCode(allTickets, companyId);
	}

	@Override
	public List<CompanyAdminDeptTicketsResponseDto> getAllTicketsAccordingStatus(Long companyId, String status) {

		List<CompanyAdminDeptTickets> allTickets = companyAdminDeptTicketsRepo
				.findByCompanyIdAndTicketHistroysStatusIgnoreCase(companyId, status);

		Map<String, Set<Long>> ownerIds = new HashMap<>();
		for (CompanyAdminDeptTickets companyAdminDeptTickets : allTickets) {
			Set<Long> idset = new HashSet<>();
			for (TicketHistroy ticketHistroy : companyAdminDeptTickets.getTicketHistroys()) {
				if (ticketHistroy.getStatus().equalsIgnoreCase(status)) {
					idset.add(ticketHistroy.getBy());
				}
			}
			ownerIds.put(companyAdminDeptTickets.getEmployeeId(), idset);
		}

		if (allTickets.isEmpty())
			return new ArrayList<>();

		List<Long> collect = ownerIds.entrySet().stream().map(Entry::getValue).flatMap(Collection::stream)
				.collect(Collectors.toList());

		List<EmployeePersonalInfo> employees = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeIdIn(companyId,
						ownerIds.entrySet().stream().map(Entry::getKey).collect(Collectors.toList()))
				.orElseGet(Collections::emptyList);

		List<EmployeePersonalInfo> ownersInfo = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeInfoIdIn(companyId, collect).orElseGet(Collections::emptyList);

		return allTickets.stream().filter(xcc -> xcc.getTicketHistroys() != null
				&& xcc.getTicketHistroys().get(xcc.getTicketHistroys().size() - 1).getStatus().equalsIgnoreCase(status))
				.map(x -> {
					EmployeePersonalInfo employeePersonalInfo = employees.stream()
							.filter(id -> id.getEmployeeOfficialInfo().getEmployeeId().equals(x.getEmployeeId()))
							.findFirst().orElseGet(null);
					List<TicketHistroy> histories = ownersInfo.stream()
							.filter(ownerId -> collect.contains(ownerId.getEmployeeInfoId()))
							.map(y -> x.getTicketHistroys().stream()
									.filter(ticket -> ticket.getStatus().equalsIgnoreCase(status))
									.map(history -> TicketHistroy.builder()
											.ownerName(y.getFirstName() + " " + y.getLastName()).date(history.getDate())
											.status(history.getStatus()).build())
									.collect(Collectors.toList()))
							.filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
					return CompanyAdminDeptTicketsResponseDto.builder().objectTicketId(x.getObjectTicketId())
							.category(x.getCategory())
							.employeeName(
									employeePersonalInfo.getFirstName() + " " + employeePersonalInfo.getLastName())
							.employeeId(x.getEmployeeId()).adminTicketId(x.getAdminTicketId())
							.ticketHistroies(histories).build();
				}).collect(Collectors.toList());
	}

	@Override
	public CompanyAdminDeptTicketsResponseDto getTicketById(String objectTicketId, String status) {
		CompanyAdminDeptTickets adminDeptTickets = companyAdminDeptTicketsRepo
				.findByObjectTicketIdAndTicketHistroysStatusIgnoreCase(objectTicketId, status)
				.orElseThrow(() -> new NoTicketFoundException(AdminConstants.NO_TICKETS_FOUND));
		if (adminDeptTickets.getTicketHistroys().isEmpty())
			return CompanyAdminDeptTicketsResponseDto.builder().build();
		List<TicketHistroy> list = adminDeptTickets.getTicketHistroys();
		List<EmployeePersonalInfo> employees = employeePersonalInfoRepository
				.findAllById(list.stream().map(TicketHistroy::getBy).collect(Collectors.toSet()));
		List<TicketHistroy> ticketHistroys = list.stream()
				.filter(xyz -> xyz.getStatus().equalsIgnoreCase(status == null ? "" : status))
				.map(x -> employees.stream().filter(y -> y.getEmployeeInfoId().equals(x.getBy())).map(xy -> {
					x.setOwnerName(xy.getFirstName() + " " + xy.getLastName());
					x.setEmployeeId(xy.getEmployeeOfficialInfo().getEmployeeId());
					return x;
				}).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());

		EmployeePersonalInfo createdInfo = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(adminDeptTickets.getCompanyId(),
						adminDeptTickets.getEmployeeId())
				.filter(x -> !x.isEmpty()).map(y -> y.get(0)).orElse(null);
		EmployeePersonalInfo ownerInfo = employeePersonalInfoRepository.findById(ticketHistroys.get(0).getBy())
				.orElse(null);
		EmployeePersonalInfo resolvedTicket = employeePersonalInfoRepository
				.findById(ticketHistroys.get(ticketHistroys.size() - 1).getBy()).orElse(null);
		if (createdInfo == null || ownerInfo == null || resolvedTicket == null)
			return CompanyAdminDeptTicketsResponseDto.builder().build();
		return CompanyAdminDeptTicketsResponseDto.builder().category(adminDeptTickets.getCategory())
				.objectTicketId(objectTicketId).ticketOwner(ownerInfo.getFirstName() + " " + ownerInfo.getLastName())
				.employeeId(adminDeptTickets.getEmployeeId()).adminTicketId(adminDeptTickets.getAdminTicketId())
				.date(adminDeptTickets.getTicketHistroys().get(0).getDate())
				.status(status == null ? ticketHistroys.get(ticketHistroys.size() - 1).getStatus() : status)
				.description(adminDeptTickets.getDescription())
				.employeeBy(resolvedTicket.getFirstName() + " " + resolvedTicket.getLastName())
				.lastDate(ticketHistroys.get(ticketHistroys.size() - 1).getDate()).ticketHistroies(list)
				.rating(adminDeptTickets.getRating()).feedback(adminDeptTickets.getFeedback())
				.attachmentsUrl(adminDeptTickets.getAttachmentsUrl())
				.employeeName(createdInfo.getFirstName() + " " + createdInfo.getLastName()).build();
	}

	@Override
	public Boolean updateTickets(Long employeeInfoId, CompanyadminDeptTicketsDto companyadminDeptTicketsDto) {

		CompanyAdminDeptTickets companyAdminDeptTickets = companyAdminDeptTicketsRepo
				.findById(companyadminDeptTicketsDto.getObjectTicketId())
				.orElseThrow(() -> new NoTicketFoundException(AdminConstants.NO_TICKETS_FOUND));

		companyAdminDeptTickets.getTicketHistroys()
				.add(TicketHistroy.builder().date(LocalDate.now()).by(employeeInfoId)
						.department(companyadminDeptTicketsDto.getDepartment() == null
								? employeePersonalInfoRepository.findById(employeeInfoId).get()
										.getEmployeeOfficialInfo().getDepartment()
								: companyadminDeptTicketsDto.getDepartment())
						.status(companyadminDeptTicketsDto.getStatus()).build());
		return Optional.ofNullable(companyAdminDeptTicketsRepo.save(companyAdminDeptTickets)).isPresent();
	}

	@Override
	public List<CompanyAdminDeptTicketsResponseDto> getAllTicketsAccordingCategory(Long companyId, String category) {

		List<CompanyAdminDeptTickets> allTickets = companyAdminDeptTicketsRepo.findByCompanyIdAndCategory(companyId,
				category);
		if (allTickets.isEmpty()) {
			throw new NoTicketFoundException(AdminConstants.NO_TICKETS_FOUND);
		}

		return duplicateCode(allTickets, companyId);
	}

	private List<CompanyAdminDeptTicketsResponseDto> duplicateCode(List<CompanyAdminDeptTickets> allTickets,
			Long companyId) {

		List<String> employeeIdList = new ArrayList<>();
		Set<Long> idset = new HashSet<>();
		for (CompanyAdminDeptTickets companyAdminDeptTickets : allTickets) {

			employeeIdList.add(companyAdminDeptTickets.getEmployeeId());
			List<TicketHistroy> ticketHistroys = companyAdminDeptTickets.getTicketHistroys();

			if (ticketHistroys != null && !ticketHistroys.isEmpty()) {
				int size = ticketHistroys.size();
				TicketHistroy ticketHistroy = ticketHistroys.get(size - 1);
				idset.add(ticketHistroy.getBy());
			}

		}

		List<EmployeePersonalInfo> employeePersonalInfo = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeIdIn(companyId, employeeIdList)
				.orElseThrow(() -> new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT));
		if (employeePersonalInfo.isEmpty()) {
			throw new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT);
		}

		List<Long> employeeInfoIdList = new ArrayList<>(idset);

		List<EmployeePersonalInfo> findAllById = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeInfoIdIn(companyId, employeeInfoIdList)
				.orElseThrow(() -> new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT));
		if (findAllById.isEmpty()) {
			throw new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT);
		}

		Map<String, String> map = new HashMap<>();

		for (EmployeePersonalInfo employeePersonalInfo2 : employeePersonalInfo) {
			String name = "";
			String employeeId = employeePersonalInfo2.getEmployeeOfficialInfo().getEmployeeId();
			String firstName = employeePersonalInfo2.getFirstName();
			String lastName = employeePersonalInfo2.getLastName();
			name = firstName + " " + lastName;
			map.put(employeeId, name);
		}

		Map<Long, String> map2 = new HashMap<>();
		Map<Long, String> map3 = new HashMap<>();
		for (EmployeePersonalInfo employeePersonalInfo2 : findAllById) {

			String name = "";
			String firstName = employeePersonalInfo2.getFirstName();
			String lastName = employeePersonalInfo2.getLastName();
			name = firstName + " " + lastName;
			Long employeeInfoId = employeePersonalInfo2.getEmployeeInfoId();
			map2.put(employeeInfoId, name);
			map3.put(employeeInfoId, employeePersonalInfo2.getEmployeeOfficialInfo().getEmployeeId());
		}

		List<CompanyAdminDeptTicketsResponseDto> companyAdminDeptTicketsResponseDtoList = new ArrayList<>();
		for (CompanyAdminDeptTickets companyAdminDeptTickets : allTickets) {
			CompanyAdminDeptTicketsResponseDto companyAdminDeptTicketsResponseDto = new CompanyAdminDeptTicketsResponseDto();
			BeanUtils.copyProperties(companyAdminDeptTickets, companyAdminDeptTicketsResponseDto);
			companyAdminDeptTicketsResponseDto.setEmployeeName(map.get(companyAdminDeptTickets.getEmployeeId()));
			List<TicketHistroy> ticketHistroys = companyAdminDeptTickets.getTicketHistroys();
			if (ticketHistroys != null && !ticketHistroys.isEmpty()) {

				int size = ticketHistroys.size();
				TicketHistroy ticketHistroy = ticketHistroys.get(size - 1);
				BeanUtils.copyProperties(ticketHistroy, companyAdminDeptTicketsResponseDto);
				companyAdminDeptTicketsResponseDto.setTicketOwnerEmployeeId(map3.get(ticketHistroy.getBy()));
				companyAdminDeptTicketsResponseDto.setTicketOwner(map2.get(ticketHistroy.getBy()));

			}
			companyAdminDeptTicketsResponseDto.setEmployeeId(companyAdminDeptTickets.getEmployeeId());
			companyAdminDeptTicketsResponseDtoList.add(companyAdminDeptTicketsResponseDto);
		}
		
		return companyAdminDeptTicketsResponseDtoList;
	}

}
