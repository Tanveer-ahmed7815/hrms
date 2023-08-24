package com.te.flinko.service.hr;

import static com.te.flinko.common.hr.HrConstants.CHECKLIST_FACTOR_NOT_FOUND;
import static com.te.flinko.common.hr.HrConstants.FACTOR_ALREADY_EXISTS;
import static com.te.flinko.common.hr.HrConstants.COMPANY_INFORMATION_NOT_PRESENT;
import static com.te.flinko.common.hr.HrConstants.INTERVIEW_ROUND_INFORMATION_NOT_FOUND;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.hr.AddFeedbackDTO;
import com.te.flinko.dto.hr.AddInterviewRoundDto;
import com.te.flinko.dto.hr.CompanyChecklistDTO;
import com.te.flinko.dto.hr.ConfigurationDto;
import com.te.flinko.dto.hr.EditInterviewRoundDto;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.hr.mongo.CompanyChecklistDetails;
import com.te.flinko.entity.hr.mongo.FeedbackConfiguration;
import com.te.flinko.entity.hr.mongo.InterviewRoundDetails;
import com.te.flinko.exception.hr.CompanyNotFoundException;
import com.te.flinko.exception.hr.CustomExceptionForHr;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.hr.mongo.CompanyChecklistDetailsRepository;
import com.te.flinko.repository.hr.mongo.FeedbackConfigurationRepository;
import com.te.flinko.repository.hr.mongo.InterviewRoundDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HrConfigurationServiceImpl implements HrConfigurationService {

	@Autowired
	private CompanyInfoRepository companyInfoRepo;

	@Autowired
	private InterviewRoundDetailsRepository interviewRoundDetailsRepo;
	@Autowired
	private FeedbackConfigurationRepository feedbackConfigurationRepo;
	@Autowired
	private CompanyChecklistDetailsRepository companyChecklistDetailsRepo;

	@Override
	public AddInterviewRoundDto addInterviewRounds(AddInterviewRoundDto interviewdetails) {
		CompanyInfo companyInfo = companyInfoRepo.findById(interviewdetails.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<InterviewRoundDetails> interviewRoundDetailsList = interviewRoundDetailsRepo
				.findByCompanyId(companyInfo.getCompanyId());

		if (interviewRoundDetailsList == null || interviewRoundDetailsList.isEmpty()) {
			InterviewRoundDetails interviewRoundDetails = new InterviewRoundDetails();
			BeanUtils.copyProperties(interviewdetails, interviewRoundDetails);
			InterviewRoundDetails roundDetails = interviewRoundDetailsRepo.save(interviewRoundDetails);
			AddInterviewRoundDto addInterviewRoundDto = new AddInterviewRoundDto();
			BeanUtils.copyProperties(roundDetails, addInterviewRoundDto);
			return addInterviewRoundDto;
		}
		InterviewRoundDetails details = interviewRoundDetailsList.get(0);

		Map<Integer, String> roundMap = interviewdetails.getRounds();
		Map<Integer, String> dataRoundMap = details.getRounds();
		boolean anyMatch = dataRoundMap.entrySet().stream().anyMatch(x -> roundMap.entrySet().stream()
				.anyMatch(y -> y.getKey().equals(x.getKey()) || y.getValue().equalsIgnoreCase(x.getValue())));

		if (anyMatch) {
			throw new CustomExceptionForHr("You are tryinig to add round of interview which is already exist");
		}

		roundMap.entrySet().forEach(x -> {
			dataRoundMap.put(x.getKey(), x.getValue());
		});

		details.setRounds(dataRoundMap);

		InterviewRoundDetails roundDetails = interviewRoundDetailsRepo.save(details);
		AddInterviewRoundDto addInterviewRoundDto = new AddInterviewRoundDto();
		BeanUtils.copyProperties(roundDetails, addInterviewRoundDto);
		return addInterviewRoundDto;
	}

	@Override
	public AddFeedbackDTO addFeedback(AddFeedbackDTO feedback) {
		CompanyInfo companyInfo = companyInfoRepo.findById(feedback.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<FeedbackConfiguration> findByCompanyIdAndFeedbackType = feedbackConfigurationRepo
				.findByCompanyIdAndFeedbackType(companyInfo.getCompanyId(), feedback.getFeedbackType());

		if (findByCompanyIdAndFeedbackType == null || findByCompanyIdAndFeedbackType.isEmpty()) {
			log.info("feedback not found for these company");
			FeedbackConfiguration feedbackConfig = new FeedbackConfiguration();
			BeanUtils.copyProperties(feedback, feedbackConfig);

			feedbackConfig.setFeedbackFactor(feedback.getFeedbackFactor());

			FeedbackConfiguration saveFeedbackConfiguration = feedbackConfigurationRepo.save(feedbackConfig);
			AddFeedbackDTO addFeedbackDto = new AddFeedbackDTO();
			BeanUtils.copyProperties(saveFeedbackConfiguration, addFeedbackDto);
			return addFeedbackDto;
		}

		FeedbackConfiguration feedbackConfiguration = findByCompanyIdAndFeedbackType.get(0);
		List<String> feedbackConfigurationFactor = feedbackConfiguration.getFeedbackFactor();

		Set<String> feedbackConfigurationFactorSet = feedbackConfigurationFactor.stream().collect(Collectors.toSet());
		Set<String> feedbackFactorsSet = feedback.getFeedbackFactor().stream().filter(ele -> !ele.equals(""))
				.collect(Collectors.toSet());
		Set<String> duplicateFactorsSet = feedbackConfigurationFactorSet;

		duplicateFactorsSet.retainAll(feedbackFactorsSet);
		feedbackFactorsSet.removeAll(feedbackConfigurationFactorSet);

		if (feedbackFactorsSet.isEmpty()) {
			throw new CustomExceptionForHr(FACTOR_ALREADY_EXISTS);
		}

		feedbackFactorsSet.stream().forEach(feedbackConfigurationFactor::add);
		feedbackConfiguration.setFeedbackFactor(feedbackConfigurationFactor);

		FeedbackConfiguration saveFeedbackConfiguration = feedbackConfigurationRepo.save(feedbackConfiguration);
		AddFeedbackDTO addFeedbackDto = new AddFeedbackDTO();
		BeanUtils.copyProperties(saveFeedbackConfiguration, addFeedbackDto);
		addFeedbackDto.setFeedbackFactor(saveFeedbackConfiguration.getFeedbackFactor());
		addFeedbackDto.setDuplicatedFactors(duplicateFactorsSet.stream().collect(Collectors.toList()));
		return addFeedbackDto;
	}

	@Override
	public CompanyChecklistDTO checklistFactor(CompanyChecklistDTO checklistFactor) {

		CompanyInfo companyInfo = companyInfoRepo.findById(checklistFactor.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<CompanyChecklistDetails> findByCompanyId = companyChecklistDetailsRepo
				.findByCompanyId(companyInfo.getCompanyId());
		if (findByCompanyId == null || findByCompanyId.isEmpty()) {
			CompanyChecklistDetails checklistDetails = new CompanyChecklistDetails();
			BeanUtils.copyProperties(checklistFactor, checklistDetails);
			CompanyChecklistDetails companyChecklistDetails = companyChecklistDetailsRepo.save(checklistDetails);
			CompanyChecklistDTO companyChecklistDto = new CompanyChecklistDTO();

			BeanUtils.copyProperties(companyChecklistDetails, companyChecklistDto);
			return companyChecklistDto;
		}
		CompanyChecklistDetails checklistDetail = findByCompanyId.get(0);
		Set<String> checklistDetailSet = checklistDetail.getChecklistFactor().stream().collect(Collectors.toSet());
		Set<String> checklistFactorSet = checklistFactor.getChecklistFactor().stream().filter(e -> !e.equals(""))
				.collect(Collectors.toSet());
		Set<String> duplicateChecklistFactorSet = checklistDetailSet;
		duplicateChecklistFactorSet.retainAll(checklistFactorSet);
		checklistFactorSet.removeAll(checklistDetailSet);
		if (checklistFactorSet.isEmpty()) {
			throw new CustomExceptionForHr(FACTOR_ALREADY_EXISTS);
		}
		checklistDetail.setChecklistFactor(checklistDetail.getChecklistFactor());
		checklistFactorSet.stream().forEach(checklistDetail.getChecklistFactor()::add);
		CompanyChecklistDetails companyChecklistDetails = companyChecklistDetailsRepo.save(checklistDetail);
		CompanyChecklistDTO companyChecklistDto = new CompanyChecklistDTO();
		BeanUtils.copyProperties(companyChecklistDetails, companyChecklistDto);
		companyChecklistDto
				.setDuplicateChecklistFactors(duplicateChecklistFactorSet.stream().collect(Collectors.toList()));
		return companyChecklistDto;

	}

	@Override
	public AddInterviewRoundDto editInterviewRoundDetails(EditInterviewRoundDto interviewdetails) {
		CompanyInfo companyInfo = companyInfoRepo.findById(interviewdetails.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<InterviewRoundDetails> interviewRoundDetailsList = interviewRoundDetailsRepo
				.findByCompanyId(companyInfo.getCompanyId());
		if (interviewRoundDetailsList == null || interviewRoundDetailsList.isEmpty()) {
			log.error(INTERVIEW_ROUND_INFORMATION_NOT_FOUND);
			throw new CustomExceptionForHr(INTERVIEW_ROUND_INFORMATION_NOT_FOUND);
		}
		InterviewRoundDetails details = interviewRoundDetailsList.get(0);

		Integer oldRoundNo = interviewdetails.getOldInterviewRoundId();
		Integer roundNo = interviewdetails.getNewInterviewRoundId();
		String roundName = interviewdetails.getNewInterviewRoundName();

		Map<Integer, String> dataRoundMap = details.getRounds();

		if (dataRoundMap.containsValue(roundName)&& !roundName.equals(interviewdetails.getOldInterviewRoundName())) {
			throw new CustomExceptionForHr("The round name you are trying add already exists");
		} else if (!dataRoundMap.containsKey(oldRoundNo)) {
			throw new CustomExceptionForHr("The round number you are trying to update does not exists");
		} else if (dataRoundMap.containsKey(roundNo) && !Objects.equals(roundNo, oldRoundNo)) {
			throw new CustomExceptionForHr("The round number you are trying add already exists");
		} else {
			dataRoundMap.remove(oldRoundNo);
			dataRoundMap.put(roundNo, roundName);
			details.setRounds(dataRoundMap);
			InterviewRoundDetails roundDetails = interviewRoundDetailsRepo.save(details);

			AddInterviewRoundDto addInterviewRoundDto = new AddInterviewRoundDto();
			BeanUtils.copyProperties(roundDetails, addInterviewRoundDto);

			return addInterviewRoundDto;
		}
	}

	@Override
	public AddInterviewRoundDto deleteInterviewRound(Long companyId, String roundName) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		List<InterviewRoundDetails> interviewRoundDetailsList = interviewRoundDetailsRepo
				.findByCompanyId(companyInfo.getCompanyId());
		AddInterviewRoundDto addInterviewRoundDto = new AddInterviewRoundDto();
		if (interviewRoundDetailsList == null || interviewRoundDetailsList.isEmpty()) {
			log.error("Interview round information not found");
			throw new CustomExceptionForHr("Interview round information not found");
		}
		InterviewRoundDetails interviewRoundDetails = interviewRoundDetailsList.get(0);
		boolean containsValue = interviewRoundDetails.getRounds().containsValue(roundName);

		if (containsValue) {
			interviewRoundDetails.getRounds().values().removeIf(s -> s.equalsIgnoreCase(roundName));
			InterviewRoundDetails save = interviewRoundDetailsRepo.save(interviewRoundDetails);
			BeanUtils.copyProperties(save, addInterviewRoundDto);
			return addInterviewRoundDto;
		} else {
			log.info("interview round name not found in database");
			throw new CustomExceptionForHr("Interview round name not found in system");
		}
	}

	@Override
	public AddFeedbackDTO editFeedback(AddFeedbackDTO feedbackDto) {

		List<FeedbackConfiguration> findByCompanyIdAndFeedbackType = feedbackConfigurationRepo
				.findByCompanyIdAndFeedbackType(feedbackDto.getCompanyId(), feedbackDto.getFeedbackType());
		if (findByCompanyIdAndFeedbackType == null || findByCompanyIdAndFeedbackType.isEmpty()) {
			log.error("feedback factor not found");
			throw new CustomExceptionForHr("Feedback factor not found");
		}
		FeedbackConfiguration feedbackConfiguration = findByCompanyIdAndFeedbackType.get(0);
		List<String> feedbackFactor = feedbackConfiguration.getFeedbackFactor();
		int indexOf = feedbackFactor.indexOf(feedbackDto.getOldFeedbackFactor());
		for (String feedback : feedbackFactor) {
			if (feedback.equalsIgnoreCase(feedbackDto.getNewFeedbackFactor())) {
				throw new CustomExceptionForHr(
						"The Feedback factor that you are trying to add already exists in the List");
			}
		}
		if (indexOf >= 0) {
			feedbackFactor.set(indexOf, feedbackDto.getNewFeedbackFactor());
		} else {
			throw new CustomExceptionForHr("The Feedback factor that you are trying to update does not exists");
		}
		feedbackConfiguration.setFeedbackFactor(feedbackFactor);
		FeedbackConfiguration feedbackObject = feedbackConfigurationRepo.save(feedbackConfiguration);
		AddFeedbackDTO addFeedbackDTO = new AddFeedbackDTO();
		BeanUtils.copyProperties(feedbackObject, addFeedbackDTO);
		return addFeedbackDTO;
	}

	@Override
	public AddFeedbackDTO deleteFeedbackFactor(Long companyId, String feedbackFactor, String feedbackType) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		List<FeedbackConfiguration> findByCompanyIdAndFeedbackType = feedbackConfigurationRepo
				.findByCompanyIdAndFeedbackType(companyInfo.getCompanyId(), feedbackType);
		if (findByCompanyIdAndFeedbackType == null || findByCompanyIdAndFeedbackType.isEmpty()) {
			log.error("Feedback Type  not found");
			throw new CustomExceptionForHr("Feedback Type not found");
		}
		FeedbackConfiguration feedbackConfiguration = findByCompanyIdAndFeedbackType.get(0);
		boolean contains = feedbackConfiguration.getFeedbackFactor().contains(feedbackFactor);
		if (contains) {
			feedbackConfiguration.getFeedbackFactor().remove(feedbackFactor);
			FeedbackConfiguration configuration = feedbackConfigurationRepo.save(feedbackConfiguration);
			AddFeedbackDTO addFeedbackDTO = new AddFeedbackDTO();
			BeanUtils.copyProperties(configuration, addFeedbackDTO);
			log.info("feedback factors successfully deleted");
			return addFeedbackDTO;
		}
		log.error("feedback facotr not found");
		throw new CustomExceptionForHr("feedback factor not found");

	}

	@Override
	public CompanyChecklistDTO editChecklistFactor(CompanyChecklistDTO checklistFactor) {

		List<CompanyChecklistDetails> companyChecklistDetailsList = companyChecklistDetailsRepo
				.findByCompanyId(checklistFactor.getCompanyId());
		if (companyChecklistDetailsList == null || companyChecklistDetailsList.isEmpty()) {
			log.error(CHECKLIST_FACTOR_NOT_FOUND);
			throw new CustomExceptionForHr("checklist factor not found");
		}
		CompanyChecklistDetails companyChecklistDetails = companyChecklistDetailsList.get(0);
		List<String> companyChecklistFactors = companyChecklistDetails.getChecklistFactor();

		int index = companyChecklistFactors.indexOf(checklistFactor.getOldFactor());
		for (String factor : companyChecklistFactors) {
			if (factor.equalsIgnoreCase(checklistFactor.getNewFactor())) {
				throw new CustomExceptionForHr(FACTOR_ALREADY_EXISTS);
			}
		}

		if (index >= 0) {
			companyChecklistFactors.set(index, checklistFactor.getNewFactor());
		} else {
			throw new CustomExceptionForHr("The factor that you are trying to update does not exists");
		}
		companyChecklistDetails.setChecklistFactor(companyChecklistFactors);
		CompanyChecklistDetails checklistDetails = companyChecklistDetailsRepo.save(companyChecklistDetails);
		CompanyChecklistDTO companyChecklistDTO = new CompanyChecklistDTO();
		BeanUtils.copyProperties(checklistDetails, companyChecklistDTO);
		return companyChecklistDTO;

	}

	@Override
	public CompanyChecklistDTO deleteChecklistFactor(Long companyId, String checklistFactor) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));

		List<CompanyChecklistDetails> findByCompanyId = companyChecklistDetailsRepo
				.findByCompanyId(companyInfo.getCompanyId());
		if (findByCompanyId == null || findByCompanyId.isEmpty()) {
			log.error("Checklist factor not found");
			throw new CustomExceptionForHr("Checklist factor not found");
		}
		CompanyChecklistDetails companyChecklistDetails = findByCompanyId.get(0);
		boolean contains = companyChecklistDetails.getChecklistFactor().contains(checklistFactor);

		if (contains) {
			companyChecklistDetails.getChecklistFactor().remove(checklistFactor);
			CompanyChecklistDetails checklistDetails = companyChecklistDetailsRepo.save(companyChecklistDetails);
			CompanyChecklistDTO companyChecklistDTO = new CompanyChecklistDTO();
			BeanUtils.copyProperties(checklistDetails, companyChecklistDTO);
			return companyChecklistDTO;
		} else {
			log.info("checklist Factor not found");
			throw new CustomExceptionForHr("checklist factor not found");
		}
	}

	@Override
	public ConfigurationDto configuration(long companyId) {
		ConfigurationDto configurationDto = new ConfigurationDto();
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		List<InterviewRoundDetails> interviewInfo = interviewRoundDetailsRepo
				.findByCompanyId(companyInfo.getCompanyId());

		HashMap<Integer, String> hashMap = new HashMap<>();

		if (interviewInfo == null || interviewInfo.isEmpty()) {
			configurationDto.setRounds(hashMap);
		} else {
			Map<Integer, String> rounds = interviewInfo.get(0).getRounds();
			TreeMap<Integer, String> treeMap = new TreeMap<>(rounds);
			configurationDto.setRounds(treeMap);
		}

		List<String> entryFeedbackList = new ArrayList<>();
		List<FeedbackConfiguration> entryFeedbackConfiguration = feedbackConfigurationRepo
				.findByCompanyIdAndFeedbackType(companyId, "entry");
		if (entryFeedbackConfiguration.isEmpty() || entryFeedbackConfiguration.get(0) == null
				|| entryFeedbackConfiguration.get(0).getFeedbackFactor().isEmpty()) {
			configurationDto.setEntryFeedbackFactor(entryFeedbackList);
		} else {
			configurationDto.setEntryFeedbackFactor(entryFeedbackConfiguration.get(0).getFeedbackFactor());
		}

		List<FeedbackConfiguration> exitFeedbackConfiguration = feedbackConfigurationRepo
				.findByCompanyIdAndFeedbackType(companyId, "exit");
		if (exitFeedbackConfiguration.isEmpty() || exitFeedbackConfiguration.get(0) == null
				|| exitFeedbackConfiguration.get(0).getFeedbackFactor().isEmpty()) {
			configurationDto.setExitFeedbackFactor(entryFeedbackList);
		} else {
			configurationDto.setExitFeedbackFactor(exitFeedbackConfiguration.get(0).getFeedbackFactor());
		}

		List<CompanyChecklistDetails> companyChecklistDetails = companyChecklistDetailsRepo.findByCompanyId(companyId);
		if (companyChecklistDetails.isEmpty() || companyChecklistDetails.get(0) == null
				|| companyChecklistDetails.get(0).getChecklistFactor().isEmpty()) {
			configurationDto.setChecklistFactor(new ArrayList<>());
		} else {
			configurationDto.setChecklistFactor(companyChecklistDetails.get(0).getChecklistFactor());
		}

		configurationDto.setCompanyId(companyId);

		return configurationDto;
	}

	@Override
	public AddInterviewRoundDto interviewRoundList(Long companyId) {

		AddInterviewRoundDto addInterviewRoundDto = new AddInterviewRoundDto();
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_INFORMATION_NOT_PRESENT));
		List<InterviewRoundDetails> interviewInfo = interviewRoundDetailsRepo
				.findByCompanyId(companyInfo.getCompanyId());

		HashMap<Integer, String> emptyMap = new HashMap<>();
		if (interviewInfo == null || interviewInfo.isEmpty()) {
			addInterviewRoundDto.setRounds(emptyMap);
		} else {
			Map<Integer, String> rounds = interviewInfo.get(0).getRounds();	
			HashMap<Integer, String> interviewRoundList = new HashMap<>(rounds);
			addInterviewRoundDto.setRounds(interviewRoundList);
			addInterviewRoundDto.setCompanyId(companyId);
		}
		return addInterviewRoundDto;
	}

	@Override
	public AddFeedbackDTO entryFeedbackFactor(Long companyId, String feedbackType) {

		List<String> entryFeedbackList = new ArrayList<>();
		AddFeedbackDTO feedbackDTO = new AddFeedbackDTO();
		List<FeedbackConfiguration> addFeedbackDTO = feedbackConfigurationRepo.findByCompanyIdAndFeedbackType(companyId,
				"entry");
		if (addFeedbackDTO.isEmpty() || addFeedbackDTO.get(0) == null
				|| addFeedbackDTO.get(0).getFeedbackFactor().isEmpty()) {
			feedbackDTO.setFeedbackFactor(entryFeedbackList);
		} else {
			feedbackDTO.setCompanyId(companyId);
			feedbackDTO.setFeedbackFactor(addFeedbackDTO.get(0).getFeedbackFactor());
			feedbackDTO.setFeedbackType(addFeedbackDTO.get(0).getFeedbackType());
		}

		return feedbackDTO;

	}
}
