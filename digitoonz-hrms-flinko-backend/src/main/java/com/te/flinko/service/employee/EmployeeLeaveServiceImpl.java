package com.te.flinko.service.employee;

import static com.te.flinko.common.employee.EmployeeLeaveConstants.LEAVE_DURATION_FULL;
import static com.te.flinko.common.employee.EmployeeLeaveConstants.LEAVE_DURATION_HALF;
import static com.te.flinko.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_ALL;
import static com.te.flinko.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_APPLIED;
import static com.te.flinko.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_APPROVED;
import static com.te.flinko.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_INVALID;
import static com.te.flinko.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_PENDING;
import static com.te.flinko.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_REJECTED;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AtomicDouble;
import com.te.flinko.dto.employee.EmployeeAllotedLeavesDTO;
import com.te.flinko.dto.employee.EmployeeApplyLeaveDTO;
import com.te.flinko.dto.employee.EmployeeCalanderDetailsDTO;
import com.te.flinko.dto.employee.EmployeeLeaveDTO;
import com.te.flinko.entity.admin.CompanyShiftInfo;
import com.te.flinko.entity.admin.WorkOffDetails;
import com.te.flinko.entity.employee.EmployeeLeaveAllocated;
import com.te.flinko.entity.employee.EmployeeLeaveApplied;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReportingInfo;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.exception.EmployeeNotFoundException;
import com.te.flinko.exception.LeaveIdNotFoundException;
import com.te.flinko.exception.ReportingIdNotFoundException;
import com.te.flinko.exception.employee.InsufficientLeavesException;
import com.te.flinko.repository.employee.EmployeeLeaveAllocatedRepository;
import com.te.flinko.repository.employee.EmployeeLeaveAppliedRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeLeaveServiceImpl implements EmployeeLeaveService {

	@Autowired
	private EmployeeLeaveAppliedRepository leaveAppliedRepository;

	@Autowired
	private EmployeePersonalInfoRepository personalInfoRepository;

	@Autowired
	private EmployeeLeaveAllocatedRepository allocatedRepository;

	// To add new leave request
	@Override
	public EmployeeApplyLeaveDTO saveLeaveApplied(EmployeeApplyLeaveDTO employeeDTO, Long employeeInfoId) {

		if (employeeDTO.getEndDate().isBefore(employeeDTO.getStartDate())) {
			throw new LeaveIdNotFoundException("End date cannot be after Start date");
		}

		EmployeePersonalInfo employeeInfo = personalInfoRepository.findById(employeeInfoId).orElse(null);

		if (employeeInfo == null) {
			throw new EmployeeNotFoundException("Employee not found with id :" + employeeInfoId);
		}

		if (employeeInfo.getEmployeeInfoList() == null) {
			throw new ReportingIdNotFoundException("Reporting Manager not found for current employee:");
		}

		EmployeeReportingInfo optionalReport = employeeInfo.getEmployeeInfoList().get(0);

		CompanyShiftInfo companyShiftInfo = employeeInfo.getEmployeeOfficialInfo().getCompanyShiftInfo();
		double shiftMinutes = companyShiftInfo.getLoginTime().until(companyShiftInfo.getLogoutTime(), MINUTES);
		Double appliedCount = employeeInfo.getEmployeeLeaveAppliedList().stream()
				.filter(e -> e.getLeaveOfType().equals(employeeDTO.getLeaveOfType()))
				.map(EmployeeLeaveApplied::getLeaveDuration).reduce(0.0, (a, b) -> (a + b));
		String allotedLeavecount = employeeInfo.getEmployeeLeaveAllocated().getLeavesDetails()
				.get(employeeDTO.getLeaveOfType());

		if (allotedLeavecount == null) {
			throw new DataNotFoundException("No leaves alloted for this category");
		}
		List<WorkOffDetails> employeeWorkOffDetails = employeeInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule()
				.getWorkOffDetailsList();
		Double currentLeave;
		Double availableLeaves = Double.parseDouble(allotedLeavecount) - appliedCount.doubleValue();
		
		AtomicDouble[] leaveCounts = currentLeaveCount(employeeDTO, employeeWorkOffDetails);

		double leaveCount = leaveCounts[0].doubleValue();

		if (employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_FULL)) {
			currentLeave = 1.0 * leaveCount;
		} else if (employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_HALF)) {
			currentLeave = 0.5 * leaveCount;
		} else {
			double leaveMinutes = employeeDTO.getStartTime().until(employeeDTO.getEndTime(), MINUTES);
			currentLeave = Math.round((leaveMinutes / shiftMinutes) * leaveCount * 100) / 100.0;
		}

		if (currentLeave + leaveCounts[1].doubleValue() > availableLeaves) {
			throw new InsufficientLeavesException("Insufficient Leaves for current type");
		}

		EmployeeLeaveApplied leaveApplied = new EmployeeLeaveApplied();
		BeanUtils.copyProperties(employeeDTO, leaveApplied);
		leaveApplied.setLeaveDuration(currentLeave + leaveCounts[1].doubleValue());
		leaveApplied.setEmployeePersonalInfo(employeeInfo);
		leaveApplied.setEmployeeReportingInfo(optionalReport);
		leaveApplied.setStatus(LEAVE_STATUS_PENDING);
		BeanUtils.copyProperties(leaveAppliedRepository.save(leaveApplied), employeeDTO);
		return employeeDTO;
	}

	@Override
	public EmployeeApplyLeaveDTO editLeave(EmployeeApplyLeaveDTO employeeDTO, Long leaveAppliedId,
			Long employeeInfoId) {
		EmployeeLeaveApplied employeeLeaveApplied = leaveAppliedRepository
				.findByLeaveAppliedIdAndEmployeePersonalInfoEmployeeInfoId(leaveAppliedId, employeeInfoId).orElse(null);
		
		if (employeeLeaveApplied == null) {
			throw new LeaveIdNotFoundException("Leave Request Not Available");
		}
			EmployeePersonalInfo employeeInfo = employeeLeaveApplied.getEmployeePersonalInfo();
			if (!employeeLeaveApplied.getStatus().equalsIgnoreCase(LEAVE_STATUS_PENDING)) {
				throw new LeaveIdNotFoundException("Unable to edit leave request!!! Kindly rasie a new request.");
			}
				
				Double appliedCount = employeeInfo.getEmployeeLeaveAppliedList().stream()
						.filter(e -> e.getLeaveOfType().equals(employeeDTO.getLeaveOfType()))
						.map(EmployeeLeaveApplied::getLeaveDuration).reduce(0.0, (a, b) -> (a + b))-employeeLeaveApplied.getLeaveDuration();
				
				CompanyShiftInfo companyShiftInfo = employeeInfo.getEmployeeOfficialInfo().getCompanyShiftInfo();
				double shiftMinutes = companyShiftInfo.getLoginTime().until(companyShiftInfo.getLogoutTime(), MINUTES);
				String allotedLeavecount = employeeInfo.getEmployeeLeaveAllocated().getLeavesDetails()
						.get(employeeDTO.getLeaveOfType());
				List<WorkOffDetails> employeeWorkOffDetails = employeeInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule()
						.getWorkOffDetailsList();
				Double currentLeave;
				Double availableLeaves = Double.parseDouble(allotedLeavecount) - appliedCount.doubleValue();

				AtomicDouble[] leaveCounts = currentLeaveCount(employeeDTO, employeeWorkOffDetails);

				double leaveCount = leaveCounts[0].doubleValue();

				if (employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_FULL)) {
					currentLeave = 1.0 * leaveCount;
				} else if (employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_HALF)) {
					currentLeave = 0.5 * leaveCount;
				} else {
					double leaveMinutes = employeeDTO.getStartTime().until(employeeDTO.getEndTime(), MINUTES);
					currentLeave = Math.round((leaveMinutes / shiftMinutes) * leaveCount * 100) / 100.0;
				}

				if (currentLeave + leaveCounts[1].doubleValue() > availableLeaves) {
					throw new InsufficientLeavesException("Insufficient Leaves for current type");
				}
				
				BeanUtils.copyProperties(employeeDTO, employeeLeaveApplied);
				employeeLeaveApplied.setLeaveDuration(currentLeave + leaveCounts[1].doubleValue());
				employeeLeaveApplied.setStatus(LEAVE_STATUS_PENDING);
				BeanUtils.copyProperties(leaveAppliedRepository.save(employeeLeaveApplied), employeeDTO);
				return employeeDTO;
	}
	
	private AtomicDouble[] currentLeaveCount(EmployeeApplyLeaveDTO employeeDTO, List<WorkOffDetails> employeeWorkOffDetails) {
		AtomicDouble atomicLeaveCount = new AtomicDouble(0.0);
		AtomicDouble extraLeaveCount = new AtomicDouble(0.0);

		if (!employeeDTO.getStartDate().isAfter(employeeDTO.getEndDate())) {
			employeeDTO.getStartDate().datesUntil(employeeDTO.getEndDate().plusDays(1))
					.forEach(leaveDate -> employeeWorkOffDetails.stream().forEach(e -> {
						if (e.getWeekNumber() == leaveDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH)) {
							if (!(e.getFullDayWorkOff().stream().anyMatch(
									day -> day.equalsIgnoreCase(leaveDate.getDayOfWeek().toString().substring(0, 3))))
									&& !(e.getHalfDayWorkOff().stream().anyMatch(day -> day
											.equalsIgnoreCase(leaveDate.getDayOfWeek().toString().substring(0, 3))))) {
								atomicLeaveCount.getAndAdd(1.0);
							} else if (e.getHalfDayWorkOff().stream().anyMatch(
									day -> day.equalsIgnoreCase(leaveDate.getDayOfWeek().toString().substring(0, 3)))
									&& employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_FULL)) {
								extraLeaveCount.getAndAdd(0.5);
							}
						}
					}));
		}
		return new AtomicDouble[] {atomicLeaveCount, extraLeaveCount};
	}

	@Override
	public Boolean deleteLeave(Long leaveAppliedId, Long employeeInfoId) {
		EmployeeLeaveApplied employeeLeaveApplied = leaveAppliedRepository
				.findByLeaveAppliedIdAndEmployeePersonalInfoEmployeeInfoId(leaveAppliedId, employeeInfoId).orElse(null);
		if (employeeLeaveApplied != null) {
			if (employeeLeaveApplied.getStatus().equalsIgnoreCase(LEAVE_STATUS_PENDING)) {
				leaveAppliedRepository.deleteById(leaveAppliedId);
				return Boolean.TRUE;
			} else {
				throw new LeaveIdNotFoundException("Unable to delete leave request!!!");
			}
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public List<EmployeeApplyLeaveDTO> getLeavesList(String status, Long employeeInfoId, Integer year,
			List<Integer> months) {

		List<EmployeeApplyLeaveDTO> employeeLeaveDTOList = new ArrayList<>();

		List<EmployeeLeaveApplied> employeeLeaveAppliedList = null;

		switch (status.toUpperCase()) {
		case LEAVE_STATUS_ALL:
			employeeLeaveAppliedList = leaveAppliedRepository
					.findByEmployeePersonalInfoEmployeeInfoIdOrderByStartDateDesc(employeeInfoId);
			break;
		case LEAVE_STATUS_APPLIED:
			employeeLeaveAppliedList = leaveAppliedRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndStatusOrderByStartDateDesc(employeeInfoId,
							LEAVE_STATUS_PENDING);
			break;
		case LEAVE_STATUS_APPROVED:
			employeeLeaveAppliedList = leaveAppliedRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndStatusOrderByStartDateDesc(employeeInfoId,
							LEAVE_STATUS_APPROVED);
			break;
		case LEAVE_STATUS_REJECTED:
			employeeLeaveAppliedList = leaveAppliedRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndStatusOrderByStartDateDesc(employeeInfoId,
							LEAVE_STATUS_REJECTED);
			break;
		default:
			throw new DataNotFoundException(LEAVE_STATUS_INVALID);
		}
		if (employeeLeaveAppliedList == null) {
			return employeeLeaveDTOList;
		}
		if (months.contains(0)) {
			employeeLeaveAppliedList.stream().forEach(leaveApplied -> {
				if (leaveApplied.getStartDate().getYear() == year) {
					EmployeeApplyLeaveDTO applyLeaveDto = new EmployeeApplyLeaveDTO();
					BeanUtils.copyProperties(leaveApplied, applyLeaveDto);
					employeeLeaveDTOList.add(applyLeaveDto);
				}
			});
		} else {
			employeeLeaveAppliedList.stream().forEach(leaveApplied -> {
				if (leaveApplied.getStartDate().getYear() == year
						&& months.contains(leaveApplied.getStartDate().getMonthValue())) {
					EmployeeApplyLeaveDTO applyLeaveDto = new EmployeeApplyLeaveDTO();
					BeanUtils.copyProperties(leaveApplied, applyLeaveDto);
					employeeLeaveDTOList.add(applyLeaveDto);
				}
			});
		}
		return employeeLeaveDTOList;
	}

	@Override
	public List<EmployeeAllotedLeavesDTO> getAllotedLeavesList(Long employeeInfoId) {
		List<EmployeeAllotedLeavesDTO> employeeLeaveDTOList = new ArrayList<>();
		EmployeeLeaveAllocated employeeLeaveAllocated = allocatedRepository
				.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId).orElse(null);
		if (employeeLeaveAllocated == null) {
			return employeeLeaveDTOList;
		}
		EmployeePersonalInfo employeePersonalInfo = employeeLeaveAllocated.getEmployeePersonalInfo();
		List<EmployeeLeaveApplied> employeeLeaveAppliedList = employeeLeaveAllocated.getEmployeePersonalInfo()
				.getEmployeeLeaveAppliedList().stream()
				.filter(e -> e.getStartDate().getYear() == LocalDate.now().getYear()).collect(Collectors.toList());

		if (employeePersonalInfo.getEmployeeOfficialInfo() == null
				|| employeePersonalInfo.getEmployeeOfficialInfo().getCompanyShiftInfo() == null)
			throw new DataNotFoundException("No shift information found!!!");
		Map<String, String> leavesDetails = employeeLeaveAllocated.getLeavesDetails();
		for (Map.Entry<String, String> entry : leavesDetails.entrySet()) {
			EmployeeAllotedLeavesDTO allotedLeavesDTO = new EmployeeAllotedLeavesDTO();
			allotedLeavesDTO.setLeaveType(entry.getKey());
			allotedLeavesDTO.setAllottedLeave(Double.parseDouble(entry.getValue()));
			Double appliedCount = employeeLeaveAppliedList.stream()
					.filter(e -> e.getLeaveOfType().equals(entry.getKey())).map(EmployeeLeaveApplied::getLeaveDuration)
					.reduce(0.0, (a, b) -> (a + b));
			allotedLeavesDTO.setRemainingLeave((Double.parseDouble(entry.getValue()) - appliedCount));
			employeeLeaveDTOList.add(allotedLeavesDTO);
		}
		return employeeLeaveDTOList;
	}

	@Override
	public EmployeeCalanderDetailsDTO getAllCalenderDetails(Long employeeInfoId, Integer year, List<Integer> months) {

		EmployeeCalanderDetailsDTO calanderDetailsDTO = new EmployeeCalanderDetailsDTO();

		List<EmployeeAllotedLeavesDTO> allotedLeaveList = getAllotedLeavesList(employeeInfoId);

		calanderDetailsDTO.setAllotedLeaves(
				allotedLeaveList.stream().map(EmployeeAllotedLeavesDTO::getAllottedLeave).reduce(0.0, (a, b) -> a + b));
		calanderDetailsDTO.setRemainingLeaves(allotedLeaveList.stream().map(EmployeeAllotedLeavesDTO::getRemainingLeave)
				.reduce(0.0, (a, b) -> a + b));

		List<Integer> allMonths = new ArrayList<>();
		allMonths.add(0);
		List<EmployeeApplyLeaveDTO> employeeLeaveDTO = getLeavesList(LEAVE_STATUS_ALL, employeeInfoId, year, allMonths);

		calanderDetailsDTO.setApprovedLeaves(
				employeeLeaveDTO.stream().filter(e -> e.getStatus().equalsIgnoreCase(LEAVE_STATUS_APPROVED))
						.map(EmployeeApplyLeaveDTO::getLeaveDuration).reduce(0.0, (a, b) -> a + b));
		calanderDetailsDTO.setAppliedLeaves(
				employeeLeaveDTO.stream().filter(e -> e.getStatus().equalsIgnoreCase(LEAVE_STATUS_PENDING))
						.map(EmployeeApplyLeaveDTO::getLeaveDuration).reduce(0.0, (a, b) -> a + b));
		calanderDetailsDTO.setRejectedLeaves(
				employeeLeaveDTO.stream().filter(e -> e.getStatus().equalsIgnoreCase(LEAVE_STATUS_REJECTED))
						.map(EmployeeApplyLeaveDTO::getLeaveDuration).reduce(0.0, (a, b) -> a + b));
		calanderDetailsDTO.setCalenderLeaves(employeeLeaveDTO.stream()
				.filter(e -> months.contains(e.getStartDate().getMonthValue())).collect(Collectors.toList()));
		return calanderDetailsDTO;
	}

	@Override
	public EmployeeLeaveDTO getLeaveById(Long leaveAppliedId, Long employeeInfoId) {
		EmployeeLeaveApplied leaveApplied = leaveAppliedRepository
				.findByLeaveAppliedIdAndEmployeePersonalInfoEmployeeInfoId(leaveAppliedId, employeeInfoId).orElse(null);
		System.out.println(leaveApplied);
		if (leaveApplied == null) {
			return null;
		}
		EmployeeLeaveDTO employeeLeaveDTO = new EmployeeLeaveDTO();
		BeanUtils.copyProperties(leaveApplied, employeeLeaveDTO);
		if(leaveApplied.getEmployeeReportingInfo() != null ) {
			employeeLeaveDTO.setReportingManagerId(leaveApplied.getEmployeeReportingInfo().getReportingManager().getEmployeeInfoId());
			employeeLeaveDTO.setReportingManagerName(leaveApplied.getEmployeeReportingInfo().getReportingManager().getFirstName()+" "+leaveApplied.getEmployeeReportingInfo().getReportingManager().getLastName());
		}
		return employeeLeaveDTO;
	}

	@Override
	public List<String> getLeaveTypesDropdown(Long employeeInfoId) {
		Optional<EmployeeLeaveAllocated> allocatedLeaves = allocatedRepository
				.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId);
		if (allocatedLeaves.isEmpty()) {
			return new ArrayList<>();
		}
		return allocatedLeaves.get().getLeavesDetails().keySet().stream().collect(Collectors.toList());
	}

}