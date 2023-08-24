package com.te.flinko.service.project;

import static com.te.flinko.common.hr.HrConstants.COMPANY_INFORMATION_FETCHED_SUCCESSFULLY;
import static com.te.flinko.common.hr.HrConstants.COMPANY_INFORMATION_NOT_PRESENT;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.te.flinko.dto.employee.EmployeeDropdownDTO;
import com.te.flinko.dto.project.EmployeeDetailsForProjectDTO;
import com.te.flinko.dto.project.MapEmployeeProjectDTO;
import com.te.flinko.dto.project.MilestoneDTO;
import com.te.flinko.dto.project.MilestoneIDNameDTO;
import com.te.flinko.dto.project.ProjectDetailsDTO;
import com.te.flinko.dto.project.ProjectEstimationDTO;
import com.te.flinko.dto.project.SubMilestoneDetailsDTO;
import com.te.flinko.dto.project.SubMilestoneIDNameDTO;
import com.te.flinko.dto.project.mongo.ProjectListDTO;
import com.te.flinko.dto.project.mongo.SubMilestone;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.project.ProjectDetails;
import com.te.flinko.entity.project.ProjectEstimationDetails;
import com.te.flinko.entity.project.mongo.ProjectMilestoneDeliverables;
import com.te.flinko.exception.InavlidInputException;
import com.te.flinko.exception.InvalidInputException;
import com.te.flinko.exception.admin.CompanyNotFound;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.exception.project.EmployeeAlreadyMappedToProjectException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.project.ProjectDetailsRepository;
import com.te.flinko.repository.project.ProjectEstimationDetailsRepository;
import com.te.flinko.repository.project.mongo.MilestoneRepository;
import com.te.flinko.repository.project.mongo.ProjectTaskDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
/**
 *
 * @author Ravindra
 *
 */
public class ProjectManagementServiceImpl implements ProjectManagementService {

	@Autowired
	private ProjectTaskDetailsRepository projectTaskDetailsRepo;
	@Autowired
	private CompanyInfoRepository companyInfoRepo;
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepo;
	@Autowired
	private ProjectEstimationDetailsRepository projectEstimationDetailsRepo;
	@Autowired
	private MilestoneRepository milestoneRepository;

	@Override
	public ArrayList<ProjectListDTO> onGoingProjects(Long companyId) {
		companyInfoRepo.findById(companyId).orElseThrow(() -> new CompanyNotFound(COMPANY_INFORMATION_NOT_PRESENT));
		log.info(COMPANY_INFORMATION_FETCHED_SUCCESSFULLY);
		ArrayList<ProjectListDTO> projectNameList = new ArrayList<>();
		List<ProjectEstimationDetails> projectDetails = projectEstimationDetailsRepo
				.findByProjectDetailsCompanyInfoCompanyId(companyId);
		if (projectDetails == null || projectDetails.isEmpty()) {
			return projectNameList;
		}
		ArrayList<Long> projectIds = new ArrayList<>();

		for (ProjectEstimationDetails projectEstimationDetails : projectDetails) {
			LocalDate now = LocalDate.now();
			if (now.isAfter(projectEstimationDetails.getStartDate())
					&& now.isBefore(projectEstimationDetails.getEndDate())) {
				projectIds.add(projectEstimationDetails.getProjectDetails().getProjectId());

			}
		}

		List<ProjectDetails> findAllById = projectDetailsRepository.findAllById(projectIds);
		for (ProjectDetails projectInfo : findAllById) {
			projectNameList
					.add(new ProjectListDTO(projectInfo.getProjectId(), projectInfo.getProjectName(), companyId));
		}
		log.info("Ongoing projects fetched successfully");
		return projectNameList;

	}

	@Override
	public MapEmployeeProjectDTO mapEmployeeWithProject(MapEmployeeProjectDTO mapEmployeeProjectDTO) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(mapEmployeeProjectDTO.getProjectId())
				.orElseThrow(() -> new DataNotFoundException(
						"Project Details with id " + mapEmployeeProjectDTO.getProjectId() + " not found."));
		List<Long> listOfPresentId = new ArrayList<>();
		projectDetails.getEmployeePersonalInfoList().forEach(i -> listOfPresentId.add(i.getEmployeeInfoId()));
		mapEmployeeProjectDTO.getListOfEmployeePersonalInfoId().removeAll(listOfPresentId);
		List<EmployeePersonalInfo> listOfEmployees = employeePersonalInfoRepo
				.findAllById(mapEmployeeProjectDTO.getListOfEmployeePersonalInfoId());
		if (listOfEmployees.isEmpty()) {
			if (!projectDetails.getEmployeePersonalInfoList().isEmpty()) {
				throw new EmployeeAlreadyMappedToProjectException("Employee is already mapped");
			}
			throw new DataNotFoundException("No employee details found with following Id's");
		}
		// mapping employee to project
		List<EmployeePersonalInfo> filteredEmployee = listOfEmployees.stream()
				.filter(i -> i.getCompanyInfo().getCompanyId().equals(projectDetails.getCompanyInfo().getCompanyId()))
				.collect(Collectors.toList());

		projectDetails.setEmployeePersonalInfoList(filteredEmployee);

		// mapping project
		listOfEmployees.forEach(employee -> employee.getAllocatedProjectList().add(projectDetails));
		projectDetailsRepository.flush();
		ProjectDetails savedProjectDetails = projectDetailsRepository.save(projectDetails);
		List<EmployeePersonalInfo> employeePersonalInfoList = savedProjectDetails.getEmployeePersonalInfoList();
		MapEmployeeProjectDTO responseDto = new MapEmployeeProjectDTO();
		responseDto.setProjectId(savedProjectDetails.getProjectId());
		responseDto.setProjectName(savedProjectDetails.getProjectName());
		List<Long> listOfEmployeeId = new ArrayList<>();
		List<String> members = new ArrayList<>();
		employeePersonalInfoList.forEach(i -> {
			listOfEmployeeId.add(i.getEmployeeInfoId());
			members.add(String.format("%s %s", i.getFirstName(), i.getLastName()));
		});
		responseDto.setListOfEmployeePersonalInfoId(listOfEmployeeId);
		responseDto.setMembers(members);
		return responseDto;

	}

	// need to check to on returning of id
	@Override
	public MilestoneDTO addMilestone(MilestoneDTO milestone) {
		if (milestone == null) {
			throw new DataNotFoundException("Milesotne data is not prest");
		}
		ProjectMilestoneDeliverables projectMilestoneDeliverables = new ProjectMilestoneDeliverables();
		BeanUtils.copyProperties(milestone, projectMilestoneDeliverables);
		ProjectEstimationDetails projectEstimationDetails = projectEstimationDetailsRepo
				.findByProjectDetailsProjectId(milestone.getProjectId())
				.orElseThrow(() -> new DataNotFoundException("Project Estimation Details not FOund"));
		List<ProjectMilestoneDeliverables> listOfMilestones = milestoneRepository
				.findByProjectId(milestone.getProjectId());
		Double totalAmountRecieved = listOfMilestones.stream().filter(i -> i.getAmountToBeReceived() != null)
				.mapToDouble(ProjectMilestoneDeliverables::getAmountToBeReceived).sum();
		if (projectEstimationDetails.getTotalAmountToBeReceived().doubleValue() < totalAmountRecieved
				+ milestone.getAmountToBeReceived()) {
			throw new InavlidInputException("Amount Entered to recieve exceeds estimated amount");
		}
		projectMilestoneDeliverables.setAmountToBeReceived(milestone.getAmountToBeReceived());
		List<SubMilestone> emptySubmileStone = new ArrayList<>();
		projectMilestoneDeliverables.setSubMilestones(emptySubmileStone);
		if (milestone.getDeliveredDate() == null) {
			projectMilestoneDeliverables.setStatus("Not Delivered");
		} else {
			projectMilestoneDeliverables.setStatus("Delivered");
		}
		ProjectMilestoneDeliverables savedMilestone = milestoneRepository.save(projectMilestoneDeliverables);
		MilestoneDTO responseMilestone = new MilestoneDTO();
		BeanUtils.copyProperties(savedMilestone, responseMilestone);
		return responseMilestone;
	}

	@Override
	public List<EmployeeDropdownDTO> getListOfEmployeesInProject(Long projectId) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectId)
				.orElseThrow(() -> new DataNotFoundException("Project Details with id " + projectId + " not found."));
		List<EmployeePersonalInfo> employeePersonalInfoList = projectDetails.getEmployeePersonalInfoList();
		List<EmployeeDropdownDTO> response = new ArrayList<>();
		employeePersonalInfoList.forEach(i -> {
			EmployeeDropdownDTO employeeDropdownDTO = new EmployeeDropdownDTO();
			employeeDropdownDTO.setEmployeeId(i.getEmployeeOfficialInfo().getEmployeeId());
			employeeDropdownDTO.setEmployeeInfoId(i.getEmployeeInfoId());
			employeeDropdownDTO.setEmployeeFullName(String.format("%s %s", i.getFirstName(), i.getLastName()));
			response.add(employeeDropdownDTO);
		});
		return response;
	}

	@Override
	public MilestoneDTO getMilestone(String projectObjectId) {
		Optional<ProjectMilestoneDeliverables> findById = milestoneRepository.findByMileStoneObjectId(projectObjectId);
		if (!findById.isPresent()) {
			throw new DataNotFoundException("Data is not present");
		}
		ProjectMilestoneDeliverables projectMilestoneDeliverables = findById.get();
		MilestoneDTO milestoneDTO = new MilestoneDTO();
		BeanUtils.copyProperties(projectMilestoneDeliverables, milestoneDTO);
		List<SubMilestone> listOfSubmileStoneDTO = new ArrayList<>();
		projectMilestoneDeliverables.getSubMilestones().forEach((mileStoneDetails) -> {
			SubMilestone subMilestone = new SubMilestone();
			BeanUtils.copyProperties(mileStoneDetails, subMilestone);
			listOfSubmileStoneDTO.add(subMilestone);
		});
		milestoneDTO.setSubMilestone(listOfSubmileStoneDTO);
		return milestoneDTO;
	}

	@Override
	public List<MilestoneIDNameDTO> getProjectMilestonesIDName(Long projectId) {
		log.info("getProjectMilestonesIDName method execution start, fetching and returning the project milestones");
		return milestoneRepository
				.findByProjectId(projectId).stream().map(pmd -> MilestoneIDNameDTO.builder()
						.mileStoneObjectId(pmd.getMileStoneObjectId()).milestoneName(pmd.getMilestoneName()).build())
				.collect(Collectors.toList());
	}

	@Override
	public List<SubMilestoneIDNameDTO> getSubMilestonesIDName(String mileStoneObjectId) {
		log.info("getSubMilestonesIDName method execution start, checking if the data is available");
		Optional<ProjectMilestoneDeliverables> pmdOp = milestoneRepository.findById(mileStoneObjectId);
		log.info("returning with required data about the sub-milestones");
		return pmdOp.map(
				projectMilestoneDeliverables -> projectMilestoneDeliverables.getSubMilestones().stream().map(sm -> {
					return SubMilestoneIDNameDTO.builder().milestoneId(sm.getMilestoneId())
							.milestoneName(sm.getMilestoneName()).build();
				}).collect(Collectors.toList())).orElse(Lists.newArrayList());
	}

	@Override
	public MilestoneDTO addSubMilestone(MilestoneDTO milestoneDTO) {
		ProjectMilestoneDeliverables mileStoneDetails = milestoneRepository
				.findByMileStoneObjectIdAndProjectId(milestoneDTO.getMileStoneObjectId(), milestoneDTO.getProjectId())
				.orElseThrow(() -> new DataNotFoundException("MileStone Details not present"));
		if (mileStoneDetails.getStatus().equalsIgnoreCase("Delivered")) {
			throw new InavlidInputException("Milestone Already Delivered");
		}
		if (mileStoneDetails.getSubMilestones() == null) {
			SubMilestone subMilestone = new SubMilestone();
			if (mileStoneDetails.getAmountToBeReceived() < milestoneDTO.getAmountToBeReceived())
				throw new InvalidInputException("Amount to be recieved exceeds the limit");
			BeanUtils.copyProperties(milestoneDTO, subMilestone);
			subMilestone.setMilestoneId(1l);
			if (milestoneDTO.getDeliveredDate() == null) {
				subMilestone.setStatus("Not Delivered");
			} else {
				subMilestone.setStatus("Delivered");
			}
			List<SubMilestone> subMilestoneList = new ArrayList<>();
			subMilestoneList.add(subMilestone);
		} else {
			if (milestoneDTO.getMilestoneId() != null) {
				updateSubMilestone(milestoneDTO, mileStoneDetails);
			} else {
				addSubMilestoneHelper(milestoneDTO, mileStoneDetails);
			}
		}
		ProjectMilestoneDeliverables save = milestoneRepository.save(mileStoneDetails);
		MilestoneDTO responseDTO = new MilestoneDTO();
		BeanUtils.copyProperties(save, responseDTO);
		List<SubMilestone> listOfSubmileStoneDTO = new ArrayList<>();
		save.getSubMilestones().forEach((i) -> {
			SubMilestone subMilestone2 = new SubMilestone();
			BeanUtils.copyProperties(i, subMilestone2);
			listOfSubmileStoneDTO.add(subMilestone2);
		});
		responseDTO.setSubMilestone(listOfSubmileStoneDTO);
		return responseDTO;
	}

	private ProjectMilestoneDeliverables addSubMilestoneHelper(MilestoneDTO milestoneDTO,
			ProjectMilestoneDeliverables mileStoneDetails) {
		SubMilestone subMilestone = new SubMilestone();
		Double totalAmount = mileStoneDetails.getSubMilestones().stream().filter(i -> i.getAmountToBeReceived() != null)
				.mapToDouble(SubMilestone::getAmountToBeReceived).sum();
		if (mileStoneDetails.getAmountToBeReceived() < totalAmount + milestoneDTO.getAmountToBeReceived())
			throw new InvalidInputException("Amount to be recieved exceeds the limit");
		BeanUtils.copyProperties(milestoneDTO, subMilestone);
		subMilestone.setMilestoneId((long) mileStoneDetails.getSubMilestones().size() + 1);
		if (milestoneDTO.getDeliveredDate() == null) {
			subMilestone.setStatus("Not Delivered");
		} else {
			subMilestone.setStatus("Delivered");
		}
		mileStoneDetails.getSubMilestones().add(subMilestone);
		return mileStoneDetails;
	}

	private ProjectMilestoneDeliverables updateSubMilestone(MilestoneDTO milestoneDTO,
			ProjectMilestoneDeliverables mileStoneDetails) {
		if (mileStoneDetails.getSubMilestones().size() < milestoneDTO.getMilestoneId().intValue()) {
			throw new DataNotFoundException("Sub-milstone not found");
		}
		mileStoneDetails.getSubMilestones().remove(milestoneDTO.getMilestoneId().intValue() - 1);
		SubMilestone subMilestone = new SubMilestone();
		Double totalAmount = mileStoneDetails.getSubMilestones().stream()
				.filter(i -> i.getAmountToBeReceived() != null
						&& !Objects.equals(i.getMilestoneId(), milestoneDTO.getMilestoneId()))
				.mapToDouble(SubMilestone::getAmountToBeReceived).sum();
		if (mileStoneDetails.getAmountToBeReceived() < totalAmount + milestoneDTO.getAmountToBeReceived())
			throw new InvalidInputException("Amount to be recieved exceeds the limit");
		BeanUtils.copyProperties(milestoneDTO, subMilestone);
		if (milestoneDTO.getDeliveredDate() == null) {
			subMilestone.setStatus("Not Delivered");
		} else {
			subMilestone.setStatus("Delivered");
		}
		mileStoneDetails.getSubMilestones().add(milestoneDTO.getMilestoneId().intValue()-1, subMilestone);
		return mileStoneDetails;
	}

	@Override
	public ProjectEstimationDTO getProjectEstimationDetailsById(Long projectId) {
		ProjectEstimationDetails estimationDetails = projectEstimationDetailsRepo
				.findByProjectDetailsProjectId(projectId)
				.orElseThrow(() -> new DataNotFoundException("Project Details not found"));
		ProjectEstimationDTO projectEstiamtionDTO = new ProjectEstimationDTO();
		BeanUtils.copyProperties(estimationDetails, projectEstiamtionDTO);
		return projectEstiamtionDTO;
	}

	@Override
	public ProjectDetailsDTO getProjectDetailsById(Long projectId) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectId)
				.orElseThrow(() -> new DataNotFoundException("No project present with Id " + projectId));
		List<ProjectMilestoneDeliverables> milestoneDetails = milestoneRepository.findByProjectId(projectId);
		ProjectEstimationDetails estimationDetails = projectDetails.getProjectEstimationDetails();
		ProjectDetailsDTO detailsDTO = new ProjectDetailsDTO();
		BeanUtils.copyProperties(projectDetails, detailsDTO);
		detailsDTO.setReportingManagerId(projectDetails.getReportingManager().getEmployeeInfoId());
		detailsDTO.setReportingManager(projectDetails.getReportingManager().getFirstName());
		detailsDTO.setClientId(projectDetails.getCompanyClientInfo().getClientId());
		detailsDTO.setClientName(projectDetails.getCompanyClientInfo().getClientName());
		detailsDTO.setProjectManagerId(projectDetails.getProjectManager().getEmployeeInfoId());
		detailsDTO.setProjectManager(projectDetails.getProjectManager().getFirstName());

		double amountPaid = milestoneDetails.stream().filter(deliverable -> deliverable.getAmountReceived() != null)
				.mapToDouble(ProjectMilestoneDeliverables::getAmountReceived).sum();
		detailsDTO.setAmountReceived(amountPaid);
		if (estimationDetails == null) {
			detailsDTO.setStatus("Yet To be Estimated");
		} else {
			if (LocalDate.now().isAfter(estimationDetails.getStartDate())
					&& LocalDate.now().isBefore(estimationDetails.getEndDate())) {
				detailsDTO.setStatus("On Going");
			}
			if (LocalDate.now().isAfter(estimationDetails.getEndDate())) {
				detailsDTO.setStatus("Estimation - " + estimationDetails.getStatus());
			}
			// Calculating days
			detailsDTO.setAmountPending(estimationDetails.getTotalAmountToBeReceived().doubleValue() - amountPaid);
			Period between = Period.between(estimationDetails.getStartDate(), estimationDetails.getEndDate());
			int days = between.getDays();
			int months = between.getMonths();
			int years = between.getYears();
			Integer workingDays = days + (months * 30) + (365 * years);
			detailsDTO.setWorkingDays(workingDays);
		}
		return detailsDTO;
	}

	@Override
	public SubMilestoneDetailsDTO getSubMilestoneById(String projectObjectId, Long milestoneId) {
		ProjectMilestoneDeliverables projectMilestoneDeliverables = milestoneRepository.findById(projectObjectId)
				.orElseThrow(() -> new DataNotFoundException("Milestone Details not found"));
		SubMilestone subMilestone = projectMilestoneDeliverables.getSubMilestones().get(milestoneId.intValue() - 1);
		SubMilestoneDetailsDTO subMilestoneDetailsDTO = new SubMilestoneDetailsDTO();
		BeanUtils.copyProperties(subMilestone, subMilestoneDetailsDTO);
		subMilestoneDetailsDTO.setParentMilestoneObjectId(projectMilestoneDeliverables.getMileStoneObjectId());
		subMilestoneDetailsDTO.setParentMilestoneName(projectMilestoneDeliverables.getMilestoneName());
		return subMilestoneDetailsDTO;
	}

	@Override
	public List<MilestoneDTO> getAllMilestone(Long projectId) {
		List<ProjectMilestoneDeliverables> listOfMilestone = milestoneRepository.findByProjectId(projectId);
		List<MilestoneDTO> listOfMilestoneDTO = new ArrayList<>();
		listOfMilestone.forEach((i) -> {
			MilestoneDTO milestoneDTO = new MilestoneDTO();
			BeanUtils.copyProperties(i, milestoneDTO);
			milestoneDTO.setSubMilestone(i.getSubMilestones());
			listOfMilestoneDTO.add(milestoneDTO);
		});
		return listOfMilestoneDTO;
	}

	@Override
	public List<EmployeeDetailsForProjectDTO> getEmployeesInProject(Long projectId) {
		List<EmployeePersonalInfo> employeePersonalInfos = projectDetailsRepository.findById(projectId)
				.orElseThrow(() -> new DataNotFoundException("Project Details Not Found"))
				.getEmployeePersonalInfoList();

		List<EmployeeDetailsForProjectDTO> employeeInfoDTOs = new ArrayList<>();
		employeePersonalInfos.forEach((i) -> {
			EmployeeDetailsForProjectDTO employeeDetailsForProjectDTO = new EmployeeDetailsForProjectDTO();
			employeeDetailsForProjectDTO.setEmployeeInfoId(i.getEmployeeInfoId());
			employeeDetailsForProjectDTO.setEmployeeName(i.getFirstName());
			employeeDetailsForProjectDTO.setOfficialEmailId(i.getEmployeeOfficialInfo().getOfficialEmailId());
			employeeDetailsForProjectDTO.setAssignedTask(projectTaskDetailsRepo
					.findByAssignedEmployeeAndProjectId(i.getEmployeeOfficialInfo().getEmployeeId(), projectId).size());
			employeeInfoDTOs.add(employeeDetailsForProjectDTO);
		});
		return employeeInfoDTOs;
	}

}
