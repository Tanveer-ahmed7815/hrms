package com.te.flinko.service.hr;

import static com.te.flinko.common.hr.HrConstants.CANDIDATE_RECORD_NOT_FOUND;
import static com.te.flinko.common.hr.HrConstants.COMPANY_INFORMATION_NOT_PRESENT;
import static com.te.flinko.common.hr.HrConstants.INTERVIEWER_RECORD_NOT_FOUND_IN_DATABASE;
import static com.te.flinko.common.hr.HrConstants.OVERALL_FEEDBACK;
import static com.te.flinko.common.hr.HrConstants.RECORD_DOES_NOT_EXISTS_IN_REFERENCE_TABLE;
import static com.te.flinko.common.hr.HrConstants.REJECTED;
import static com.te.flinko.common.hr.HrConstants.SELECTED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.employee.EmployeeDropdownDTO;
import com.te.flinko.dto.employee.MailDto;
import com.te.flinko.dto.hr.CandidateInfoDTO;
import com.te.flinko.dto.hr.CandidateInfoDTOById;
import com.te.flinko.dto.hr.CandidateInterviewInfoDTO;
import com.te.flinko.dto.hr.CandidateListDTO;
import com.te.flinko.dto.hr.FollowUpDTO;
import com.te.flinko.dto.hr.FollowUpDetailsDTO;
import com.te.flinko.dto.hr.InterviewFeedbackInfoDTO;
import com.te.flinko.dto.hr.RejectedCandidatedetailsDTO;
import com.te.flinko.dto.hr.ScheduledCandidateDTO;
import com.te.flinko.dto.hr.ScheduledCandidateDetailsDTO;
import com.te.flinko.dto.hr.UpdateFeedbackDTO;
import com.te.flinko.entity.Department;
import com.te.flinko.entity.admin.CompanyDesignationInfo;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeeOfficialInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.employee.EmployeeReferenceInfo;
import com.te.flinko.entity.hr.CandidateInfo;
import com.te.flinko.entity.hr.CandidateInterviewInfo;
import com.te.flinko.entity.hr.mongo.FeedbackConfiguration;
import com.te.flinko.entity.hr.mongo.InterviewRoundDetails;
import com.te.flinko.exception.hr.CompanyNotFoundException;
import com.te.flinko.exception.hr.CustomExceptionForHr;
import com.te.flinko.repository.DepartmentRepository;
import com.te.flinko.repository.admin.CompanyDesignationInfoRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.repository.employee.EmployeeReferenceInfoRepository;
import com.te.flinko.repository.hr.CandidateInfoRepository;
import com.te.flinko.repository.hr.CandidateInterviewInfoRepository;
import com.te.flinko.repository.hr.mongo.FeedbackConfigurationRepository;
import com.te.flinko.repository.hr.mongo.InterviewRoundDetailsRepository;
import com.te.flinko.service.mail.employee.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
/**
 * 
 * @author Ravindra
 *
 */
public class CandidateManagementServiceImpl implements CandidateManagementService {
	@Autowired
	private CandidateInfoRepository candidateInfoRepository;
	@Autowired
	private CandidateInterviewInfoRepository candidateInterviewInfoRepo;
	@Autowired
	private CompanyInfoRepository companyInfoRepo;
	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepo;
	@Autowired
	private EmployeeReferenceInfoRepository employeeRefferenceInfoRepo;
	@Autowired
	private DepartmentRepository departmentRepo;
	@Autowired
	private InterviewRoundDetailsRepository interviewRoundDetailsRepo;
	@Autowired
	private CompanyDesignationInfoRepository companyDesignationInfoRepo;
	@Autowired
	private FeedbackConfigurationRepository feedbackConfigurationRepo;
	@Autowired
	private EmailService emailService;

	@Override
	@Transactional
	/**
	 * these method is use to save the candidate details
	 * 
	 * @param candidateInfoDto
	 * @return candidateInformationDto
	 **/
	public CandidateInfoDTO newCandidate(CandidateInfoDTO candidateInfoDto) {
		CompanyInfo companyInfo = companyInfoRepo.findById(candidateInfoDto.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		log.info("Company information present in Database");

		CandidateInfo candidateInfoObject = new CandidateInfo();
		candidateInfoObject.setCompanyInfo(companyInfo);
		BeanUtils.copyProperties(candidateInfoDto, candidateInfoObject);
		candidateInfoObject.setIsSelected(Boolean.FALSE);
		CandidateInfo candidateInformation = candidateInfoRepository.save(candidateInfoObject);

		CandidateInfoDTO candidateInformationDto = new CandidateInfoDTO();

		BeanUtils.copyProperties(candidateInformation, candidateInformationDto);
		candidateInformationDto.setCompanyId(companyInfo.getCompanyId());
		candidateInformationDto.setOthers(candidateInfoDto.getOthers());

		if ((candidateInfoDto.getEmployeeId().equals("") && candidateInfoDto.getEmployeeName().equals(""))
				|| (candidateInfoDto.getEmployeeId() == null && candidateInfoDto.getEmployeeName() == null)) {
			log.info("Candidate does not take any refference");
			return candidateInformationDto;
		}

		List<EmployeePersonalInfo> firstNameAndlastName = employeePersonalInfoRepo
				.findByEmployeeOfficialInfoEmployeeId(candidateInfoDto.getEmployeeId());
		if ((firstNameAndlastName == null) || (firstNameAndlastName.isEmpty())) {
			throw new CustomExceptionForHr("Employee record not found for reference purpuse");
		}

		EmployeeReferenceInfo employeeRefferenceObject = new EmployeeReferenceInfo();
		employeeRefferenceObject
				.setReferralName(candidateInformation.getFirstName() + " " + candidateInformation.getLastName());
		employeeRefferenceObject.setCandidateInfo(candidateInformation);
		employeeRefferenceObject.setRefferalEmployeePersonalInfo(firstNameAndlastName.get(0));
		employeeRefferenceInfoRepo.save(employeeRefferenceObject);
		candidateInformationDto.setEmployeeId(firstNameAndlastName.get(0).getEmployeeOfficialInfo().getEmployeeId());
		candidateInformationDto.setEmployeeName(
				firstNameAndlastName.get(0).getFirstName() + " " + firstNameAndlastName.get(0).getLastName());
		return candidateInformationDto;
	}

	@Override
	/**
	 * this method created for display all candidates in form of list which has not
	 * scheduled any interview
	 * 
	 * @param companyId
	 * @return list of candidates
	 **/
	public List<CandidateListDTO> candidateDetailsList(Long companyId) {
		CompanyInfo info = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<CandidateInterviewInfo> candidates = candidateInterviewInfoRepo
				.findByCandidateInfoCompanyInfoCompanyId(info.getCompanyId());

		List<CandidateInfo> candidatesinfo;
		if (candidates == null || candidates.isEmpty()) {
			// return empty list
			log.error("No interview scheduled for any candidate of this Company");
			candidatesinfo = candidateInfoRepository.findByCompanyInfoCompanyIdAndIsSelected(companyId, false);
		} else {

			Set<Long> candidateSet = candidates.stream().filter(y -> y.getCandidateInfo() != null)
					.map(x -> x.getCandidateInfo().getCandidateId()).collect(Collectors.toSet());
			if (candidateSet == null || candidateSet.isEmpty()) {
				log.error("Problem while sorting the candidate record");
				throw new CustomExceptionForHr("Error while sorting the candidate record");
			}

			candidatesinfo = candidateInfoRepository
					.findByCandidateIdNotInAndCompanyInfoCompanyIdAndIsSelected(candidateSet, companyId, false);
		}
		if (candidatesinfo == null || candidatesinfo.isEmpty()) {
			return new ArrayList<>();

		}
		List<CandidateListDTO> listOfCandidates = candidatesinfo.stream()
				.sorted(Comparator.comparing(CandidateInfo::getCandidateId).reversed()).map(e -> {
					CandidateListDTO candidateListDto = new CandidateListDTO();
					BeanUtils.copyProperties(e, candidateListDto);
					return candidateListDto;
				}).collect(Collectors.toList());

		if (listOfCandidates.isEmpty()) {
			log.error("Candidate data not found");
		} else
			log.info("Candidate data fetched successfully");
		return listOfCandidates;

	}

	/**
	 * this method used to scheduled the interview for candidate
	 * 
	 * @param candidateInterviewInfodto
	 * @return interview details
	 **/
	@Override
	@Transactional
	public CandidateInterviewInfoDTO scheduleInterview(CandidateInterviewInfoDTO candidateInterviewInfodto) {
		CompanyInfo info = companyInfoRepo.findById(candidateInterviewInfodto.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<CandidateInterviewInfo> candidateInterviewInfo1 = candidateInterviewInfoRepo
				.findByCandidateInfoCandidateIdAndCandidateInfoCompanyInfoCompanyId(
						candidateInterviewInfodto.getCandidateId(), info.getCompanyId());
		// New Candidate
		if (candidateInterviewInfo1 == null || candidateInterviewInfo1.isEmpty()) {
			CandidateInfo candidateInfo = candidateInfoRepository
					.findByCandidateIdAndCompanyInfoCompanyId(candidateInterviewInfodto.getCandidateId(),
							candidateInterviewInfodto.getCompanyId())
					.orElseThrow(() -> new CustomExceptionForHr(CANDIDATE_RECORD_NOT_FOUND));

			CandidateInterviewInfo candidateInterviewInfo = new CandidateInterviewInfo();
			BeanUtils.copyProperties(candidateInterviewInfodto, candidateInterviewInfo);

			candidateInterviewInfo.setCandidateInfo(candidateInfo);
			List<EmployeePersonalInfo> employeedata = employeePersonalInfoRepo
					.findByEmployeeInfoIdAndCompanyInfoCompanyId(candidateInterviewInfodto.getEmployeePersonalInfo(),
							candidateInterviewInfodto.getCompanyId());

			if (employeedata == null || employeedata.isEmpty()) {
				log.error(INTERVIEWER_RECORD_NOT_FOUND_IN_DATABASE);
				throw new CustomExceptionForHr(INTERVIEWER_RECORD_NOT_FOUND_IN_DATABASE);

			}

			EmployeePersonalInfo employeePersonalInfo = employeedata.get(0);
			candidateInterviewInfo.setEmployeePersonalInfo(employeePersonalInfo);

			Map<String, String> feedback = new HashMap<>();

			candidateInterviewInfo.setFeedback(feedback);

			CandidateInterviewInfo interviewInfo = candidateInterviewInfoRepo.save(candidateInterviewInfo);
			CandidateInterviewInfoDTO candidateInterviewInfoDto = new CandidateInterviewInfoDTO();
			BeanUtils.copyProperties(interviewInfo, candidateInterviewInfoDto);
			candidateInterviewInfoDto.setCandidateId(candidateInfo.getCandidateId());
			candidateInterviewInfoDto.setCompanyId(candidateInfo.getCompanyInfo().getCompanyId());
			candidateInterviewInfoDto.setEmployeePersonalInfo(employeePersonalInfo.getEmployeeInfoId());
			sendMail(interviewInfo, candidateInterviewInfodto.getLinkUrlFeedback());
			sendMailToCandidate(interviewInfo);
			return candidateInterviewInfoDto;

		}

		// To reschedule the round i call one method from below code
		List<CandidateInterviewInfo> sortedData = candidateInterviewInfo1.stream()
				.sorted(Comparator.comparing(CandidateInterviewInfo::getRoundOfInterview).reversed())
				.filter(Objects::nonNull).collect(Collectors.toList());
		CandidateInterviewInfo candidateInterviewData = sortedData.get(0);
		if (sortedData.get(0).getFeedback() == null || sortedData.get(0).getFeedback().isEmpty()) {

			List<CandidateInterviewInfo> candidateObject = candidateInterviewInfoRepo
					.findByInterviewIdAndCandidateInfoCandidateId(candidateInterviewData.getInterviewId(),
							candidateInterviewData.getCandidateInfo().getCandidateId());
			if (candidateObject == null || candidateObject.isEmpty()) {
				throw new CustomExceptionForHr("Candidate record not able to search");
			}
			CandidateInterviewInfo candidateinterviewInfo = candidateObject.get(0);
			return rescheduleRound(candidateInterviewInfodto, candidateinterviewInfo);

		}

		// To schedule next round
		if (sortedData.get(0).getFeedback().get(OVERALL_FEEDBACK).equalsIgnoreCase(SELECTED)) {
			if (sortedData.get(0).getRoundOfInterview().equals(candidateInterviewInfodto.getRoundOfInterview())) {
				throw new CustomExceptionForHr("Same interview round as previous");
			}

			return scheduleNextRound(candidateInterviewInfodto);

		} else if (sortedData.get(0).getFeedback().get(OVERALL_FEEDBACK).equalsIgnoreCase(REJECTED)) {
			throw new CustomExceptionForHr(
					"Candidate is rejected in previous round of interview, not able to schedule interview");
		}
		return null;
	}

	// this method is used in Scheduled interview API
	private CandidateInterviewInfoDTO rescheduleRound(CandidateInterviewInfoDTO candidateInfodto,
			CandidateInterviewInfo candidateinterviewInfo) {

		BeanUtils.copyProperties(candidateInfodto, candidateinterviewInfo);

		CandidateInfo candidateData = candidateInfoRepository
				.findByCandidateIdAndCompanyInfoCompanyId(candidateInfodto.getCandidateId(),
						candidateInfodto.getCompanyId())
				.orElseThrow(() -> new CustomExceptionForHr("Does not found candidate details in Database"));

		candidateinterviewInfo.setCandidateInfo(candidateData);

		List<EmployeePersonalInfo> employeeInfo = employeePersonalInfoRepo.findByEmployeeInfoIdAndCompanyInfoCompanyId(
				candidateInfodto.getEmployeePersonalInfo(), candidateInfodto.getCompanyId());

		EmployeePersonalInfo employeePersonalInfo = employeeInfo.get(0);
		if (employeePersonalInfo == null) {
			log.error(INTERVIEWER_RECORD_NOT_FOUND_IN_DATABASE);
			throw new CustomExceptionForHr("Interviewer record not found in Database");
		}
		candidateinterviewInfo.setEmployeePersonalInfo(employeePersonalInfo);

		Map<String, String> feedback = new HashMap<>();
		candidateinterviewInfo.setFeedback(feedback);
		candidateinterviewInfo.setInterviewDate(candidateInfodto.getInterviewDate());
		CandidateInterviewInfo updatedCandidate = candidateInterviewInfoRepo.save(candidateinterviewInfo);
		CandidateInterviewInfoDTO candidateInterviewInfoDto = new CandidateInterviewInfoDTO();

		BeanUtils.copyProperties(updatedCandidate, candidateInterviewInfoDto);
		candidateInterviewInfoDto.setCandidateId(candidateData.getCandidateId());
		candidateInterviewInfoDto.setCompanyId(candidateData.getCompanyInfo().getCompanyId());

		candidateInterviewInfoDto
				.setEmployeePersonalInfo(updatedCandidate.getEmployeePersonalInfo().getEmployeeInfoId());
		sendMail(updatedCandidate, candidateInfodto.getLinkUrlFeedback());
		sendMailToCandidate(updatedCandidate);
		return candidateInterviewInfoDto;

	}

	// this method is used in Scheduled interview API
	private CandidateInterviewInfoDTO scheduleNextRound(CandidateInterviewInfoDTO candidateInfodto) {
		CandidateInterviewInfo candidateInterviewInfoObject = new CandidateInterviewInfo();
		BeanUtils.copyProperties(candidateInfodto, candidateInterviewInfoObject);
		CandidateInfo candidateDetails = candidateInfoRepository
				.findByCandidateIdAndCompanyInfoCompanyId(candidateInfodto.getCandidateId(),
						candidateInfodto.getCompanyId())
				.orElseThrow(() -> new CustomExceptionForHr("Candidate record not found in Database"));
		candidateInterviewInfoObject.setCandidateInfo(candidateDetails);
		List<EmployeePersonalInfo> employeedata = employeePersonalInfoRepo.findByEmployeeInfoIdAndCompanyInfoCompanyId(
				candidateInfodto.getEmployeePersonalInfo(), candidateInfodto.getCompanyId());
		EmployeePersonalInfo employeePersonalInfo = employeedata.get(0);
		if (Objects.equals(employeePersonalInfo, null)) {
			log.error(INTERVIEWER_RECORD_NOT_FOUND_IN_DATABASE);
			throw new CustomExceptionForHr("Interviewer record not found in Database");

		}

		candidateInterviewInfoObject.setEmployeePersonalInfo(employeePersonalInfo);
		Map<String, String> feedback = new HashMap<>();
		candidateInterviewInfoObject.setFeedback(feedback);
		CandidateInterviewInfo interviewInfo = candidateInterviewInfoRepo.save(candidateInterviewInfoObject);
		CandidateInterviewInfoDTO candidateInterviewInfoDto = new CandidateInterviewInfoDTO();
		BeanUtils.copyProperties(interviewInfo, candidateInterviewInfoDto);
		candidateInterviewInfoDto.setCandidateId(interviewInfo.getCandidateInfo().getCandidateId());
		candidateInterviewInfoDto.setCompanyId(interviewInfo.getCandidateInfo().getCompanyInfo().getCompanyId());
		candidateInterviewInfoDto.setEmployeePersonalInfo(interviewInfo.getEmployeePersonalInfo().getEmployeeInfoId());
		sendMail(interviewInfo, candidateInfodto.getLinkUrlFeedback());
		sendMailToCandidate(interviewInfo);
		return candidateInterviewInfoDto;
	}

// these mail is used to send mail to candidate regarding interview
	private void sendMailToCandidate(CandidateInterviewInfo interviewInfo) {
		MailDto mailDtoForCandidate = new MailDto();
		mailDtoForCandidate.setTo(interviewInfo.getCandidateInfo().getEmailId());
		mailDtoForCandidate.setSubject("Interview Invitation");
		mailDtoForCandidate.setBody("Dear" +" "+ interviewInfo.getCandidateInfo().getFirstName() + " "
				+ interviewInfo.getCandidateInfo().getLastName() + ","
				 +"<BR />" + "<BR />" +"We were impressed by your background and would like to invite you to interview.<body>\n"+"<body>\n" +"Please find the details of interview below:"+ "<body>\n" + "<body>\n" + "<body>\n"
				 + "Interview Date :-"+ interviewInfo.getInterviewDate() + "<BR />"
				+ "Interview type :-" + interviewInfo.getInterviewType() + "<BR />" + "Interview details :-"
				+ interviewInfo.getInterviewDetails() + "<BR />" + "Round Number :-"
				+ interviewInfo.getRoundOfInterview() + "<BR />" + "Round name :-" + interviewInfo.getRoundName()
				+ "<BR />" + "Interview start time :-" + interviewInfo.getStartTime() + "<BR />" + "Duration :-"
				+ interviewInfo.getDuration() + "<BR />" + "<BR />" + "<BR />" + "Thanks and Regards" + "<BR />"
				+ "Team " + interviewInfo.getCandidateInfo().getCompanyInfo().getCompanyName() + "</body>\n"
				+ "</html>");
		emailService.sendMailWithLink(mailDtoForCandidate);
		log.info("successfully send the mail to candidate");
	}

	// these method is use to sending mail to interviewer which take the interview
		private void sendMail(CandidateInterviewInfo interviewInfo, String linkurl) {
			MailDto mailDto = new MailDto();
			EmployeePersonalInfo employeePersonalInfo = interviewInfo.getEmployeePersonalInfo();
			if (employeePersonalInfo != null) {
				EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
				if (employeeOfficialInfo != null && employeeOfficialInfo.getOfficialEmailId() != null) {
					mailDto.setTo(employeeOfficialInfo.getOfficialEmailId());
					mailDto.setSubject("Interview Invitation");
					mailDto.setBody("<html>\n" + "<body>\n" + "\n"
							+ "This is to inform that you have to interview a candidate.Please find the candidate details below:"
							+ "<BR />" + "<BR />" + "<BR />" + "Name of candidate :-"
							+ interviewInfo.getCandidateInfo().getFirstName() + " "
							+ interviewInfo.getCandidateInfo().getLastName() + "<BR />" + "Interview type :-"
							+ interviewInfo.getInterviewType() + "<BR />" + "Interview details :-"
							+ interviewInfo.getInterviewDetails() + "<BR />" + "Round of interview :-"
							+ interviewInfo.getRoundOfInterview() + "<BR />" + "Interview Date :-"
							+ interviewInfo.getInterviewDate() + "<BR />" + "Round name :-" + interviewInfo.getRoundName()
							+ "<BR />" + "Interview start time :-" + interviewInfo.getStartTime() + "<BR />" + "Duration :-"
							+ interviewInfo.getDuration() + "<BR />" + "Candidate email id :-"
							+ interviewInfo.getCandidateInfo().getEmailId() + "<BR />" + "Candidate mobile no :-"
							+ interviewInfo.getCandidateInfo().getMobileNumber() + "<BR />" + "Resume :-" + "<a href='"
							+ interviewInfo.getCandidateInfo().getResumeUrl() + "'>" + "Click Here" + "</a>" + "<BR />"
							+ "<BR />" + "<BR />"
							+ "Kindly take it forward. Once the interview is completed, provide the feedback using below form :"
							+ "<BR />" + "<a href='" + linkurl + "/"
							+ interviewInfo.getCandidateInfo().getCompanyInfo().getCompanyId() + "/"
							+ interviewInfo.getInterviewId() + "'>" + "Feedback Form" + "</a>" + "<BR />" + "<BR />"
							+ "Thanks and Regards," + "<BR />" + "Team "
							+ interviewInfo.getCandidateInfo().getCompanyInfo().getCompanyName() + "</body>\n" + "</html>");
					emailService.sendMailWithLink(mailDto);
					log.info("Successfully send the mail to interviewer");
				}
			}
		}


	@Override
	/**
	 * this method is use to show the candidates details which are selected
	 * 
	 * @param company id
	 * @return list of candidate details
	 **/
	public List<FollowUpDTO> followUp(Long companyId) {
		CompanyInfo info = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException("Company information not found"));
		List<CandidateInfo> findByCompanyInfoCompanyId = candidateInfoRepository
				.findByCompanyInfoCompanyIdAndIsSelected(info.getCompanyId(), false);
		if (findByCompanyInfoCompanyId == null || findByCompanyInfoCompanyId.isEmpty()) {
			return new ArrayList<>();
		}

		List<Long> listOfInfo = findByCompanyInfoCompanyId.stream().map(CandidateInfo::getCandidateId)
				.collect(Collectors.toList());
		List<CandidateInterviewInfo> findByCandidateInfoCandidateIdIn = candidateInterviewInfoRepo
				.findByCandidateInfoCandidateIdInAndCandidateInfoCompanyInfoCompanyIdOrderByCandidateInfoCandidateIdDesc(
						listOfInfo, companyId);

		if (findByCandidateInfoCandidateIdIn.isEmpty()) {
			return new ArrayList<>();
		}

		Map<Long, List<CandidateInterviewInfo>> sortedData = findByCandidateInfoCandidateIdIn.stream()
				.sorted(Comparator.comparing(CandidateInterviewInfo::getRoundOfInterview).reversed())
				.filter(Objects::nonNull).collect(Collectors.groupingBy(x -> x.getCandidateInfo().getCandidateId()));

		List<CandidateInterviewInfo> feedbackObject = sortedData.entrySet().stream().map(x -> x.getValue().get(0))
				.filter(z -> z.getFeedback().get(OVERALL_FEEDBACK) != null)
				.filter(y -> y.getFeedback().get(OVERALL_FEEDBACK).equalsIgnoreCase(SELECTED))
				.collect(Collectors.toList());

		Collections.sort(feedbackObject, (s1, s2) -> {
			return s2.getCandidateInfo().getCandidateId().compareTo(s1.getCandidateInfo().getCandidateId());
		});

		return feedbackObject.stream().map(s -> {
			FollowUpDTO followUpDto = new FollowUpDTO();
			BeanUtils.copyProperties(s, followUpDto);
			followUpDto.setCandidateId(s.getCandidateInfo().getCandidateId());
			followUpDto.setFirstName(s.getCandidateInfo().getFirstName());
			followUpDto.setLastName(s.getCandidateInfo().getLastName());
			followUpDto.setEmailId(s.getCandidateInfo().getEmailId());
			followUpDto.setMobileNumber(s.getCandidateInfo().getMobileNumber());
			followUpDto.setDesignationName(s.getCandidateInfo().getDesignationName());
			followUpDto.setYearOfExperience(s.getCandidateInfo().getYearOfExperience());
			followUpDto.setEmployeePersonalInfo(s.getEmployeePersonalInfo().getEmployeeInfoId());
			return followUpDto;
		}).collect(Collectors.toList());

	}

	@Transactional
	@Override
	/**
	 * this method is use to update the candidate record by HR this this method
	 * accept the whole object of candidate
	 * 
	 * @param candidate DTO object
	 * @return updated object of candidateInfoDTO
	 **/
	public CandidateInfoDTO editCandidateInfo(CandidateInfoDTO candidateInfo) {

		EmployeeReferenceInfo employeeReference = null;
		if (Objects.equals(candidateInfo.getCandidateId(), null)
				|| Objects.equals(candidateInfo.getCompanyId(), null)) {
			log.error("Candidate id or company id is missing");
			throw new CustomExceptionForHr("Company id or candidate id not given");
		}
		// Updating existing candidateInformation in Candidate Info Repository
		CandidateInfo candidateDetails = candidateInfoRepository
				.findByCandidateIdAndCompanyInfoCompanyId(candidateInfo.getCandidateId(), candidateInfo.getCompanyId())
				.orElseThrow(() -> {
					log.error("Candidate information not present in system we cant update details");
					return new CustomExceptionForHr("Candidate information not present in Database");
				});
		BeanUtils.copyProperties(candidateInfo, candidateDetails);
		candidateDetails.setOthers(candidateInfo.getOthers());
		CandidateInfo candidateInformation = candidateInfoRepository.save(candidateDetails);
		CandidateInfoDTO candidateInfoDto = new CandidateInfoDTO();
		BeanUtils.copyProperties(candidateInformation, candidateInfoDto);
		candidateInfoDto.setCompanyId(candidateInformation.getCompanyInfo().getCompanyId());

		// If candidate has taken any reference
		List<EmployeeReferenceInfo> employeeReferenceInfoList = employeeRefferenceInfoRepo
				.findByCandidateInfoCandidateId(candidateInfo.getCandidateId());

		if ((candidateInfo.getEmployeeId().equals("")) || (candidateInfo.getEmployeeId() == null)) {
			// Deleting the record if no reference
			log.info("Candidate does not take any refference");

			if (!employeeReferenceInfoList.isEmpty()) {
				EmployeeReferenceInfo employeeReferenceInfo = employeeReferenceInfoList.get(0);
				employeeRefferenceInfoRepo.delete(employeeReferenceInfo);
			}
			log.info("Data updation operation successfully done..!");
			return candidateInfoDto;
		} else {
			List<EmployeePersonalInfo> employeePersonalInfoList = employeePersonalInfoRepo
					.findByEmployeeOfficialInfoEmployeeId(candidateInfo.getEmployeeId());
			if (employeePersonalInfoList == null || employeePersonalInfoList.isEmpty()) {
				log.error(RECORD_DOES_NOT_EXISTS_IN_REFERENCE_TABLE);
				throw new CustomExceptionForHr(RECORD_DOES_NOT_EXISTS_IN_REFERENCE_TABLE);
			}
			if (employeeReferenceInfoList == null || employeeReferenceInfoList.isEmpty()) {
				log.error(RECORD_DOES_NOT_EXISTS_IN_REFERENCE_TABLE);
				EmployeeReferenceInfo employeeReferenceInfo = new EmployeeReferenceInfo();
				employeeReferenceInfo.setReferralName(candidateInfo.getFirstName() + " " + candidateInfo.getLastName());
				employeeReferenceInfo.setCandidateInfo(candidateInformation);

				employeeReferenceInfo.setRefferalEmployeePersonalInfo(employeePersonalInfoList.get(0));

				employeeReference = employeeRefferenceInfoRepo.save(employeeReferenceInfo);
				candidateInfoDto.setEmployeeId(
						employeeReference.getRefferalEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId());

				candidateInfoDto.setEmployeeName(employeeReference.getRefferalEmployeePersonalInfo().getFirstName()
						+ " " + employeeReference.getRefferalEmployeePersonalInfo().getLastName());

				return candidateInfoDto;
			} else {
				EmployeeReferenceInfo employeeReferenceInfo = employeeReferenceInfoList.get(0);
				employeeReferenceInfo
						.setReferralName(candidateDetails.getFirstName() + " " + candidateDetails.getLastName());
				employeeReferenceInfo.setRefferalEmployeePersonalInfo(employeePersonalInfoList.get(0));
				employeeReference = employeeRefferenceInfoRepo.save(employeeReferenceInfo);

				candidateInfoDto.setEmployeeId(
						employeeReference.getRefferalEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId());

				candidateInfoDto.setEmployeeName(employeeReference.getRefferalEmployeePersonalInfo().getFirstName()
						+ " " + employeeReference.getRefferalEmployeePersonalInfo().getLastName());
				log.info("Candidate detail update operation successfully done..!");
				return candidateInfoDto;
			}
		}
	}

	@Override
	/**
	 * this method is used to find the candidate details
	 * 
	 * @param candidate id ,company id
	 * @return candidate object
	 **/
	public CandidateInfoDTOById findCandidateInfoByUsingId(Long candidateId, Long companyId) {
		CompanyInfo info = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		CandidateInfo candidateInfo = candidateInfoRepository
				.findByCandidateIdAndCompanyInfoCompanyId(candidateId, info.getCompanyId()).orElseThrow(() -> {
					log.error("Data fetching issue with candidate id");
					return new CustomExceptionForHr("Candidate information not present in Database");
				});
		CandidateInfoDTOById candidateInfoDtoById = new CandidateInfoDTOById();

		List<EmployeeReferenceInfo> candidatesId = employeeRefferenceInfoRepo
				.findByReferralNameAndCandidateInfoCandidateId(
						candidateInfo.getFirstName() + " " + candidateInfo.getLastName(), candidateId);

		if (!(candidatesId == null || candidatesId.isEmpty())) {

			candidateInfoDtoById.setEmployeeId(
					candidatesId.get(0).getRefferalEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId());
			candidateInfoDtoById.setEmployeeName(candidatesId.get(0).getRefferalEmployeePersonalInfo().getFirstName()
					+ " " + candidatesId.get(0).getRefferalEmployeePersonalInfo().getLastName());
		}

		List<CompanyDesignationInfo> designationinfo = companyDesignationInfoRepo
				.findByDesignationNameAndCompanyInfoCompanyId(candidateInfo.getDesignationName(), info.getCompanyId());

		Department department = departmentRepo.findById(candidateInfo.getDepartmentId())
				.orElseThrow(() -> new CustomExceptionForHr("Department not found in system"));

		if (designationinfo == null || designationinfo.isEmpty()) {
			log.info("designation id not found");
			throw new CustomExceptionForHr("Designation information not found");
		}
		departmentRepo.findById(candidateInfo.getDepartmentId());

		CompanyDesignationInfo companyDesignationInfo = designationinfo.get(0);
		candidateInfoDtoById.setDesignationId(companyDesignationInfo.getDesignationId());
		candidateInfoDtoById.setDepartmentId(department.getDepartmentId());
		BeanUtils.copyProperties(candidateInfo, candidateInfoDtoById);

		candidateInfoDtoById.setDepartment(department.getDepartmentName());
		candidateInfoDtoById.setOthers(candidateInfo.getOthers());
		return candidateInfoDtoById;
	}

	@Override
	public FollowUpDetailsDTO followUpDetails(Long candidateId, Long companyId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<CandidateInterviewInfo> candidate = candidateInterviewInfoRepo
				.findByCandidateInfoCandidateIdAndCandidateInfoCompanyInfoCompanyId(candidateId,
						companyInfo.getCompanyId());

		if (candidate == null || candidate.isEmpty()) {
			log.error(CANDIDATE_RECORD_NOT_FOUND);
			throw new CustomExceptionForHr("Candidate details not found");
		}
		Map<Long, List<CandidateInterviewInfo>> sortedData = candidate.stream()
				.sorted(Comparator.comparing(CandidateInterviewInfo::getRoundOfInterview).reversed())
				.filter(Objects::nonNull).collect(Collectors.groupingBy(x -> x.getCandidateInfo().getCandidateId()));

		List<CandidateInterviewInfo> feedbackObject = sortedData.entrySet().stream().map(x -> x.getValue().get(0))
				.filter(z -> z.getFeedback().get(OVERALL_FEEDBACK) != null)
				.filter(y -> y.getFeedback().get(OVERALL_FEEDBACK).equalsIgnoreCase(SELECTED))
				.collect(Collectors.toList());
		FollowUpDetailsDTO followUpDetailsDto = new FollowUpDetailsDTO();
		if (feedbackObject == null || feedbackObject.isEmpty()) {
			log.info(CANDIDATE_RECORD_NOT_FOUND);
			throw new CustomExceptionForHr(CANDIDATE_RECORD_NOT_FOUND);
		}
		followUpDetailsDto.setFeedback(feedbackObject.get(0).getFeedback());
		followUpDetailsDto.setRoundNumber(feedbackObject.get(0).getRoundOfInterview());
		followUpDetailsDto.setPreviousInterviewername(feedbackObject.get(0).getEmployeePersonalInfo().getFirstName()
				+ " " + (feedbackObject.get(0).getEmployeePersonalInfo().getLastName()));
		followUpDetailsDto.setInterviewId(candidate.get(0).getInterviewId());
		CandidateInfo candidateDetails = candidateInfoRepository
				.findByCandidateIdAndCompanyInfoCompanyId(feedbackObject.get(0).getCandidateInfo().getCandidateId(),
						companyId)
				.orElseThrow(() -> new CustomExceptionForHr("Candidate details not found"));
		followUpDetailsDto.setOthers(candidateDetails.getOthers());
		Department department = departmentRepo.findById(candidateDetails.getDepartmentId())
				.orElseThrow(() -> new CustomExceptionForHr("Department not found"));
		List<InterviewRoundDetails> interviewRoundDetailsList = interviewRoundDetailsRepo.findByCompanyId(companyId);

		BeanUtils.copyProperties(candidateDetails, followUpDetailsDto);
		List<EmployeeReferenceInfo> referenceInfo = employeeRefferenceInfoRepo
				.findByReferralNameAndCandidateInfoCandidateId(
						candidateDetails.getFirstName() + " " + candidateDetails.getLastName(),
						candidateDetails.getCandidateId());
		if (referenceInfo == null || referenceInfo.isEmpty()) {
			followUpDetailsDto.setDepartment(department.getDepartmentName());
			if ((interviewRoundDetailsList != null) && !interviewRoundDetailsList.isEmpty()) {
				InterviewRoundDetails interviewRoundDetails = interviewRoundDetailsList.get(0);
				Map<Integer, String> roundMap = interviewRoundDetails.getRounds();
				String roundName = roundMap.get(feedbackObject.get(0).getRoundOfInterview());
				followUpDetailsDto.setRoundName(roundName);
			}
			return followUpDetailsDto;
		}
		followUpDetailsDto.setReferencePersonName(referenceInfo.get(0).getRefferalEmployeePersonalInfo().getFirstName()
				+ " " + referenceInfo.get(0).getRefferalEmployeePersonalInfo().getLastName());

		followUpDetailsDto.setDepartment(department.getDepartmentName());

		if ((interviewRoundDetailsList != null) && !interviewRoundDetailsList.isEmpty()) {
			InterviewRoundDetails interviewRoundDetails = interviewRoundDetailsList.get(0);
			Map<Integer, String> roundMap = interviewRoundDetails.getRounds();
			String roundName = roundMap.get(feedbackObject.get(0).getRoundOfInterview());
			followUpDetailsDto.setRoundName(roundName);
		}
		return followUpDetailsDto;

	}

	/**
	 * this method created for display all candidates whose interview is scheduled
	 * 
	 * @param companyId
	 * @return list of candidates
	 **/
	@Override
	public List<ScheduledCandidateDTO> scheduledCandidates(Long companyId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<CandidateInterviewInfo> listOfCandidate = candidateInterviewInfoRepo
				.findByCandidateInfoCompanyInfoCompanyId(companyInfo.getCompanyId());
		if (listOfCandidate == null || listOfCandidate.isEmpty()) {
			return new ArrayList<>();
		}

		Map<Long, List<CandidateInterviewInfo>> collect = listOfCandidate.stream()
				.sorted(Comparator.comparing(CandidateInterviewInfo::getRoundOfInterview).reversed())
				.filter(Objects::nonNull).collect(Collectors.groupingBy(x -> x.getCandidateInfo().getCandidateId()));

		List<CandidateInterviewInfo> feedbackObject = collect.entrySet().stream().map(x -> x.getValue().get(0))
				.filter(z -> z.getFeedback().get(OVERALL_FEEDBACK) == null).collect(Collectors.toList());

		Collections.sort(feedbackObject, (s1, s2) -> {
			return s2.getCandidateInfo().getCandidateId().compareTo(s1.getCandidateInfo().getCandidateId());
		});
		return feedbackObject.stream().map(e -> {

			ScheduledCandidateDTO scheduledCandidatedtos = new ScheduledCandidateDTO();

			BeanUtils.copyProperties(e.getCandidateInfo(), scheduledCandidatedtos);
			scheduledCandidatedtos.setInterviewType(e.getInterviewType());
			scheduledCandidatedtos.setInterviewDetails(e.getInterviewDetails());
			scheduledCandidatedtos.setInterviewDate(e.getInterviewDate());
			scheduledCandidatedtos.setDuration(e.getDuration());
			scheduledCandidatedtos.setStartTime(e.getStartTime());
			scheduledCandidatedtos.setEmployeePersonalInfo(e.getEmployeePersonalInfo().getEmployeeInfoId());
			scheduledCandidatedtos.setRoundOfInterview(e.getRoundOfInterview());
			scheduledCandidatedtos.setInterviewerName(
					e.getEmployeePersonalInfo().getFirstName() + " " + e.getEmployeePersonalInfo().getLastName());
			scheduledCandidatedtos.setRoundName(e.getRoundName());
			return scheduledCandidatedtos;
		}).collect(Collectors.toList());

	}

	/**
	 * this method created for display information of candidate whose interview is
	 * scheduled
	 * 
	 * @param companyId,candidate id
	 * @return candidate detail
	 **/
	@Override
	public ScheduledCandidateDetailsDTO scheduledCandidateDetails(Long candidateId, Long companyId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<CandidateInterviewInfo> candidate = candidateInterviewInfoRepo
				.findByCandidateInfoCandidateIdAndCandidateInfoCompanyInfoCompanyId(candidateId,
						companyInfo.getCompanyId());
		if (candidate == null || candidate.isEmpty()) {
			log.error("Candidate record not found in system");
			throw new CustomExceptionForHr("Candidate interview details not found");
		}
		Map<Long, List<CandidateInterviewInfo>> sortedcandidates = candidate.stream()
				.sorted(Comparator.comparing(CandidateInterviewInfo::getRoundOfInterview).reversed())
				.filter(Objects::nonNull).collect(Collectors.groupingBy(x -> x.getCandidateInfo().getCandidateId()));
		List<CandidateInterviewInfo> scheduledCandidates = sortedcandidates.entrySet().stream()
				.map(x -> x.getValue().get(0)).filter(z -> z.getFeedback().get(OVERALL_FEEDBACK) == null)
				.collect(Collectors.toList());
		if (scheduledCandidates == null || scheduledCandidates.isEmpty()) {
			log.info("Candidate not found");
			throw new CustomExceptionForHr("Scheduled candidates not found");
		}

		ScheduledCandidateDetailsDTO detailsDto = new ScheduledCandidateDetailsDTO();
		detailsDto.setInterviewType(scheduledCandidates.get(0).getInterviewType());
		detailsDto.setInterviewDetails(scheduledCandidates.get(0).getInterviewDetails());
		detailsDto.setInterviewDate(scheduledCandidates.get(0).getInterviewDate());
		detailsDto.setInterviewerName(scheduledCandidates.get(0).getEmployeePersonalInfo().getFirstName() + " "
				+ scheduledCandidates.get(0).getEmployeePersonalInfo().getLastName());
		detailsDto.setEmployeePersonalInfo(scheduledCandidates.get(0).getEmployeePersonalInfo().getEmployeeInfoId());
		detailsDto.setStartTime(scheduledCandidates.get(0).getStartTime());
		detailsDto.setDuration(scheduledCandidates.get(0).getDuration());
		detailsDto.setRoundOfInterview(scheduledCandidates.get(0).getRoundOfInterview());
		detailsDto.setInterviewId(scheduledCandidates.get(0).getInterviewId());
		detailsDto.setRoundName(scheduledCandidates.get(0).getRoundName());
		CandidateInfo candidateinfo = candidateInfoRepository
				.findByCandidateIdAndCompanyInfoCompanyId(
						scheduledCandidates.get(0).getCandidateInfo().getCandidateId(), companyId)
				.orElseThrow(() -> new CustomExceptionForHr("Candidate information not found"));
		BeanUtils.copyProperties(candidateinfo, detailsDto);
		detailsDto.setStatus(candidateinfo.getEmployementStatus());
		Department department = departmentRepo.findById(candidateinfo.getDepartmentId())
				.orElseThrow(() -> new CustomExceptionForHr("Department not found"));
		detailsDto.setDepartment(department.getDepartmentName());
		detailsDto.setDepartmentId(department.getDepartmentId());
		detailsDto.setOthers(candidateinfo.getOthers());
		return detailsDto;
	}

	/**
	 * this method created for display details of candidate who is rejected in
	 * interview
	 * 
	 * @param companyId, candidate id
	 * @return candidate details
	 **/
	@Override
	public RejectedCandidatedetailsDTO rejectedCandidateDetails(Long candidateId, Long companyId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<CandidateInterviewInfo> candidate = candidateInterviewInfoRepo
				.findByCandidateInfoCandidateIdAndCandidateInfoCompanyInfoCompanyId(candidateId,
						companyInfo.getCompanyId());
		if (candidate == null || candidate.isEmpty()) {
			log.error("Candidate record not found in system");
			throw new CustomExceptionForHr("Candidate record not found");
		}
		Map<Long, List<CandidateInterviewInfo>> sortedCandidates = candidate.stream()
				.sorted(Comparator.comparing(CandidateInterviewInfo::getRoundOfInterview).reversed())
				.filter(Objects::nonNull).collect(Collectors.groupingBy(x -> x.getCandidateInfo().getCandidateId()));

		List<CandidateInterviewInfo> feedbackBase = sortedCandidates.entrySet().stream().map(x -> x.getValue().get(0))
				.filter(z -> z.getFeedback().get(OVERALL_FEEDBACK) != null)
				.filter(y -> y.getFeedback().get(OVERALL_FEEDBACK).equalsIgnoreCase(REJECTED)
						|| (y.getFeedback().get(OVERALL_FEEDBACK).equalsIgnoreCase(SELECTED)
								&& y.getCandidateInfo().getIsSelected() != null && y.getCandidateInfo().getIsSelected()
								&& y.getCandidateInfo().getIsDocumentVerified() != null
								&& !(y.getCandidateInfo().getIsDocumentVerified())))
				.collect(Collectors.toList());
		if (feedbackBase == null || feedbackBase.isEmpty()) {
			log.error("Rejected candidate not found");
			throw new CustomExceptionForHr("This is not a rejected candidates");
		}
		RejectedCandidatedetailsDTO candidateDetails = new RejectedCandidatedetailsDTO();
		candidateDetails.setRoundNumber(feedbackBase.get(0).getRoundOfInterview());
		candidateDetails.setFeedback(feedbackBase.get(0).getFeedback());
		CandidateInfo candidateInfo = candidateInfoRepository
				.findByCandidateIdAndCompanyInfoCompanyId(feedbackBase.get(0).getCandidateInfo().getCandidateId(),
						companyId)
				.orElseThrow(() -> new CustomExceptionForHr("Candidate details not found "));
		BeanUtils.copyProperties(candidateInfo, candidateDetails);
		Department department = departmentRepo.findById(candidateInfo.getDepartmentId())
				.orElseThrow(() -> new CustomExceptionForHr("Department record not found"));
		candidateDetails.setDepartment(department.getDepartmentName());
		List<EmployeeReferenceInfo> referalName = employeeRefferenceInfoRepo
				.findByReferralNameAndCandidateInfoCandidateId(
						candidateInfo.getFirstName() + " " + candidateInfo.getLastName(),
						candidateInfo.getCandidateId());
		candidateDetails.setOthers(candidateInfo.getOthers());
		if (referalName == null || referalName.isEmpty()) {
			log.info("Candidate does not take any reference");
			return candidateDetails;
		}
		candidateDetails.setReferencePersonName(referalName.get(0).getRefferalEmployeePersonalInfo().getFirstName()
				+ " " + referalName.get(0).getRefferalEmployeePersonalInfo().getLastName());
		return candidateDetails;

	}

	/**
	 * this method created for delete the candidate information in system
	 * 
	 * @param companyId,candidate id
	 * @return message
	 **/
	@Override
	@Transactional
	public String deleteCandidateInfo(Long candidateId, Long companyId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		CandidateInfo candidateInfo = candidateInfoRepository
				.findByCandidateIdAndCompanyInfoCompanyId(candidateId, companyInfo.getCompanyId()).orElseThrow((() -> {
					log.error("Database transaction problem in CompanyInfo Table");
					throw new CompanyNotFoundException("Company details not present in Database");
				}));
		List<EmployeeReferenceInfo> referenceData = employeeRefferenceInfoRepo
				.findByReferralNameAndCandidateInfoCandidateId(
						candidateInfo.getFirstName() + " " + candidateInfo.getLastName(), candidateId);
		if (referenceData == null || referenceData.isEmpty()) {
			candidateInfoRepository.delete(candidateInfo);
			log.info("Candidate does not take reffrence");
			return "Cadidate information successfully deleted...!";

		} else {
			employeeRefferenceInfoRepo.delete(referenceData.get(0));
			log.info("Candidate data from reference table successfully deleted......!");
		}
		candidateInfoRepository.delete(candidateInfo);
		log.info("Candidate data from candidate Info table successfully deleted");
		return "Cadidate information successfully deleted...!";
	}

	/**
	 * this method created for display all candidates whose rejected in last
	 * interview
	 * 
	 * @param companyId
	 * @return list of candidates
	 **/
	@Override
	public List<FollowUpDTO> rejectedCandidates(Long companyId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<CandidateInterviewInfo> candidates = candidateInterviewInfoRepo
				.findByCandidateInfoCompanyInfoCompanyId(companyInfo.getCompanyId());
		if (candidates == null || candidates.isEmpty()) {
			return new ArrayList<>();
		}

		Map<Long, List<CandidateInterviewInfo>> filterObject = candidates.stream()
				.sorted(Comparator.comparing(CandidateInterviewInfo::getRoundOfInterview).reversed())
				.filter(Objects::nonNull).collect(Collectors.groupingBy(x -> x.getCandidateInfo().getCandidateId()));

//		List<CandidateInterviewInfo> feedbackBase = filterObject.entrySet().stream().map(x -> x.getValue().get(0))
//				.filter(z -> z.getFeedback().get(OVERALL_FEEDBACK) != null)
//				.filter(y -> y.getFeedback().get(OVERALL_FEEDBACK).equalsIgnoreCase(REJECTED))
//				.collect(Collectors.toList());
		
		List<CandidateInterviewInfo> feedbackBase = filterObject.entrySet().stream().map(x -> x.getValue().get(0))
				.filter(z -> z.getFeedback().get(OVERALL_FEEDBACK) != null)
				.filter(y -> y.getFeedback().get(OVERALL_FEEDBACK).equalsIgnoreCase(REJECTED)
						|| (y.getFeedback().get(OVERALL_FEEDBACK).equalsIgnoreCase(SELECTED)
								&& y.getCandidateInfo().getIsSelected()!= null && y.getCandidateInfo().getIsSelected()
								&& y.getCandidateInfo().getIsDocumentVerified()!=null && !(y.getCandidateInfo().getIsDocumentVerified())))
				.collect(Collectors.toList());

		List<Long> candidatesId = feedbackBase.stream().map(r -> r.getCandidateInfo().getCandidateId())
				.collect(Collectors.toList());
		List<CandidateInfo> findAllById = candidateInfoRepository.findAllById(candidatesId);
		log.info("rejected candidate fetch successfully");
		Collections.sort(findAllById, (s1, s2) -> s2.getCandidateId().compareTo(s1.getCandidateId()));

		return findAllById.stream().map(s -> {
			FollowUpDTO followUpDto = new FollowUpDTO();
			BeanUtils.copyProperties(s, followUpDto);
			return followUpDto;
		}).collect(Collectors.toList());
	}

	/**
	 * this method created for update the feedback of candidates interview
	 * 
	 * @param interviewId
	 * @param feedbackDto
	 * @return candidate updated feedback info
	 **/
	@Override
	public UpdateFeedbackDTO updateFeedback(Long interviewId, UpdateFeedbackDTO feedbackDto) {

		CandidateInterviewInfo interviewInfo = candidateInterviewInfoRepo.findById(interviewId)
				.orElseThrow(() -> new CustomExceptionForHr("candidate interview details not found "));

		Map<String, String> candidateFeedackDto = feedbackDto.getFeedback();
		Map<String, String> interviewFeedback = interviewInfo.getFeedback();
		if (!interviewInfo.getFeedback().isEmpty()) {
			log.error("Feedback already present");
			throw new CustomExceptionForHr("candidate interview feedback already inserted");
		}
		candidateFeedackDto.putAll(interviewFeedback);

		interviewInfo.setFeedback(candidateFeedackDto);
		CandidateInterviewInfo candidateInterviewInfo = candidateInterviewInfoRepo.save(interviewInfo);
		UpdateFeedbackDTO updateFeedbackDTO = new UpdateFeedbackDTO();
		BeanUtils.copyProperties(candidateInterviewInfo, updateFeedbackDTO);
		updateFeedbackDTO.setCompanyId(interviewInfo.getCandidateInfo().getCompanyInfo().getCompanyId());
		log.info("candidate feedback save successfully");
		return updateFeedbackDTO;

	}

	/**
	 * this method created for getting all employee info in specific company
	 * interview
	 *
	 * @param companyId
	 * @return list of employee
	 **/

	@Override
	public List<EmployeeDropdownDTO> employeeDropdownList(Long companyId) {
		return employeePersonalInfoRepo.getEmployeeNameInfoIdOffId(companyId).filter(emp -> !emp.isEmpty())
				.map(employees -> employees.stream().filter(Objects::nonNull).collect(Collectors.toList()))
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
	}

	/**
	 * this method created for getting information of candidate interview
	 *
	 * @param companyId
	 * @param interviewId
	 * @return list of employee
	 **/
	@Override
	public InterviewFeedbackInfoDTO interviewFeedbackInfo(Long interviewId, Long companyId) {
		CompanyInfo companyInfo = companyInfoRepo.findByCompanyId(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		List<CandidateInterviewInfo> interviewinfo = candidateInterviewInfoRepo
				.findByInterviewIdAndCandidateInfoCompanyInfoCompanyId(interviewId, companyInfo.getCompanyId());
		if (interviewinfo.isEmpty()) {
			log.error("interview information not found");
			throw new CustomExceptionForHr("interview information not found");
		}
		log.info(SELECTED);
		List<FeedbackConfiguration> feedbackList = feedbackConfigurationRepo
				.findByCompanyIdAndFeedbackType(companyInfo.getCompanyId(), "entry");
		if (feedbackList.isEmpty()) {
			log.error("feedback factor not found");
			throw new CustomExceptionForHr("feedback factor not found");
		}
		InterviewFeedbackInfoDTO interviewFeedbackInfo = new InterviewFeedbackInfoDTO();
		interviewFeedbackInfo.setCompanyId(companyInfo.getCompanyId());
		interviewFeedbackInfo.setCandidateFullname(interviewinfo.get(0).getCandidateInfo().getFirstName() + " "
				+ interviewinfo.get(0).getCandidateInfo().getLastName());
		interviewFeedbackInfo.setInterviewId(interviewinfo.get(0).getInterviewId());
		interviewFeedbackInfo.setInterviewDate(interviewinfo.get(0).getInterviewDate());
		interviewFeedbackInfo.setRoundOfInterview(interviewinfo.get(0).getRoundOfInterview());
		interviewFeedbackInfo.setRoundName(interviewinfo.get(0).getRoundName());
		interviewFeedbackInfo.setFeedbackFactor(feedbackList.get(0).getFeedbackFactor());
		interviewFeedbackInfo.setInterviewerId(interviewinfo.get(0).getEmployeePersonalInfo().getEmployeeInfoId());
		interviewFeedbackInfo.setInterviewerFullName(interviewinfo.get(0).getEmployeePersonalInfo().getFirstName() + " "
				+ interviewinfo.get(0).getEmployeePersonalInfo().getLastName());
		log.info("candidate interview info fetch successfully");
		return interviewFeedbackInfo;

	}

	
	@Override
	public String sendLink(String url, Long candidateId, Long companyId, Long userId) {

		CandidateInfo info = candidateInfoRepository.findById(candidateId)
				.orElseThrow(() -> new CompanyNotFoundException(CANDIDATE_RECORD_NOT_FOUND));
		info.setIsSelected(Boolean.TRUE);
		info.setIsDocumentVerified(null);
		candidateInfoRepository.save(info);

		String toMail = info.getEmailId();

		if (toMail != null) {
			MailDto mailDto = new MailDto();
			mailDto.setTo(toMail);
			CompanyInfo company = info.getCompanyInfo();
			String companyName = (company == null) ? null : company.getCompanyName();
			mailDto.setSubject("Your interview with " + companyName + " for " + info.getDesignationName());
			mailDto.setBody("<html>\n" + "<body>\n" + "\n" + "Dear " + info.getFirstName() + " " + info.getLastName()
					+ ",<BR />" + "<BR />" + "Congratulations!!!" + "<BR />" + "<BR />"
					+ "We are pleased to inform you that we would like to offer you the " + info.getDesignationName()
					+ " position." + "<BR />"
					+ "Your next procedure would be filling all the details. Please find the link below to do the same:"
					+ "<BR />" + "<BR />" + "<a href='" + url + "/" + companyId + "/" + candidateId + "'>"
					+ "Employee Details Form" + "</a>" + "<BR />" + "<BR />" + "Thanks and Regards," + "<BR />"
					+ "<BR />" + "Team FLINKO" + "</body>\n" + "</html>");
			emailService.sendMailWithLink(mailDto);

		}
		return "Link Sent Successfully";
	}

}
