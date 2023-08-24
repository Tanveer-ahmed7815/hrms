package com.te.flinko.service.admin;

import static com.te.flinko.common.admin.LevelsOfApprovalConstants.COMPANY_NOT_FOUND;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.COMPANY_NOT_FOUND_OR_COMPANY_CAN_NOT_UPDATE_THE_RECORD;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.LEVELS_OF_APPROVAL_ADDED_SUCCESSFULLY;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.LEVELS_OF_APPROVAL_ALREADY_EXIST;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.THE_ADD_LEVELS_OF_APPROVAL_METHOD_BEGINS;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.THE_ADD_LEVELS_OF_APPROVAL_METHOD_END;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.THE_GET_LEVELS_OF_APPROVAL_METHOD_BEGINS;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.THE_GET_LEVELS_OF_APPROVAL_METHOD_END;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.te.flinko.dto.admin.LevelsOfApprovalDto;
import com.te.flinko.entity.admin.LevelsOfApproval;
import com.te.flinko.exception.admin.CompanyNotExistException;
import com.te.flinko.exception.admin.LevelsOfApprovalAlreadyExistException;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.admin.LevelsOfApprovalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class LevelsOfApprovalServiceImpl implements LevelsOfApprovalService {

	private final LevelsOfApprovalRepository approvalRepository;

	private final CompanyInfoRepository companyInfoRepository;

	@Override
	public String addLevelsOfApproval(LevelsOfApprovalDto approvalDto, Long companyId) {
		log.info(THE_ADD_LEVELS_OF_APPROVAL_METHOD_BEGINS, approvalDto, " And Company Id", companyId);
		return companyInfoRepository.findByCompanyIdAndIsSubmited(companyId, false).map(companyInfo -> Optional
				.of(companyInfo).filter(company -> companyInfo.getLevelsOfApproval() == null).map(companydetails -> {
					LevelsOfApproval levelsOfApproval = new LevelsOfApproval();
					BeanUtils.copyProperties(approvalDto, levelsOfApproval);
					companyInfo.setIsSubmited(Boolean.TRUE);
					levelsOfApproval.setCompanyInfo(companyInfo);
					approvalRepository.save(levelsOfApproval);
					log.info(THE_ADD_LEVELS_OF_APPROVAL_METHOD_END + LEVELS_OF_APPROVAL_ADDED_SUCCESSFULLY);
					return LEVELS_OF_APPROVAL_ADDED_SUCCESSFULLY;
				}).orElseThrow(() -> {
					log.error(LEVELS_OF_APPROVAL_ALREADY_EXIST);
					return new LevelsOfApprovalAlreadyExistException(LEVELS_OF_APPROVAL_ALREADY_EXIST);
				})).orElseThrow(() -> {
					log.error(COMPANY_NOT_FOUND_OR_COMPANY_CAN_NOT_UPDATE_THE_RECORD);
					return new CompanyNotExistException(COMPANY_NOT_FOUND_OR_COMPANY_CAN_NOT_UPDATE_THE_RECORD);
				});
	}

	@Override
	public LevelsOfApprovalDto getLevelsOfApproval(Long companyId) {
		log.info(THE_GET_LEVELS_OF_APPROVAL_METHOD_BEGINS + companyId);
		return companyInfoRepository.findById(companyId).map(companydetails -> {
			LevelsOfApprovalDto approvalDto = new LevelsOfApprovalDto();
			if (companydetails.getLevelsOfApproval() == null) 
				return approvalDto;
			BeanUtils.copyProperties(companydetails.getLevelsOfApproval(), approvalDto);
			log.info(THE_GET_LEVELS_OF_APPROVAL_METHOD_END, approvalDto);
			approvalDto.setIsSubmited(companydetails.getIsSubmited());
			return approvalDto;
		}).orElseThrow(() -> {
			log.error(COMPANY_NOT_FOUND);
			return new CompanyNotExistException(COMPANY_NOT_FOUND);
		});
	}

}
