package com.te.flinko.service.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.flinko.constants.admin.AdminConstants;
import com.te.flinko.dto.admin.CompanyDesignationInfoDto;
import com.te.flinko.dto.admin.DeleteCompanyDesignationDto;
import com.te.flinko.entity.admin.CompanyDesignationInfo;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.exception.CompanyNotFoundException;
import com.te.flinko.exception.DuplicateDesignationException;
import com.te.flinko.exception.admin.DesignationCannotUpdate;
import com.te.flinko.exception.admin.DesignationIdNotFoundException;
import com.te.flinko.repository.admin.CompanyDesignationRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;

@Service
public class CompanyDesignationServiceImpl implements CompanyDesignationService {

	private static final String DESIGNATION_CANNOT_BE_DELETED = "Designation Cannot Be Deleted";

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private CompanyDesignationRepository companyDesignationRepository;

	@Override
	public CompanyDesignationInfoDto addCompanyDesignation(long companyId, long parentDesignationId,
			CompanyDesignationInfoDto companyDesignationInfoDto) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		if (Boolean.TRUE.equals(companyInfo.getIsSubmited())) {
			throw new DesignationCannotUpdate("Designation Cannot Be Updated");
		}

		if (companyDesignationRepository
				.findByDepartmentAndDesignationNameAndCompanyInfoCompanyId(companyDesignationInfoDto.getDepartment(),
						companyDesignationInfoDto.getDesignationName(), companyId)
				.isPresent()) {
			throw new DuplicateDesignationException("Designation Name Already Exist!!");
		}
		CompanyDesignationInfo companyDesignationInfo = new CompanyDesignationInfo();
		BeanUtils.copyProperties(companyDesignationInfoDto, companyDesignationInfo);
		companyDesignationInfo.setCompanyInfo(companyInfo);

		CompanyDesignationInfo designationInfo = companyDesignationRepository.findById(parentDesignationId)
				.orElse(null);

		if (designationInfo != null) {
			companyDesignationInfo.setParentDesignationInfo(designationInfo);
		} else {
			companyDesignationInfo.setParentDesignationInfo(companyDesignationInfo);
		}

		companyDesignationRepository.save(companyDesignationInfo);
		BeanUtils.copyProperties(companyDesignationInfo, companyDesignationInfoDto);

		return companyDesignationInfoDto;
	}

	@Transactional
	@Override
	public CompanyDesignationInfoDto updateCompanyDesignation(long companyId,
			CompanyDesignationInfoDto companyDesignationInfoDto) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		if (Boolean.TRUE.equals(companyInfo.getIsSubmited())) {
			throw new DesignationCannotUpdate("Designation Cannot Be Updated");
		}

		CompanyDesignationInfo companyDesignationInfo = companyDesignationRepository
				.findByCompanyInfoCompanyIdAndDesignationId(companyId, companyDesignationInfoDto.getDesignationId())
				.orElseThrow(() -> new DesignationIdNotFoundException("No Designation Found To Update!!"));
		Optional<CompanyDesignationInfo> companyDesignationInfos = companyDesignationRepository
				.findByDepartmentAndDesignationNameAndCompanyInfoCompanyId(companyDesignationInfoDto.getDepartment(),
						companyDesignationInfoDto.getDesignationName(), companyId);
		if (companyDesignationInfos.isPresent() && !companyDesignationInfoDto.getDesignationId()
				.equals(companyDesignationInfos.get().getDesignationId())) {
			throw new DuplicateDesignationException("Designation Name Already Exist!!");
		}

		companyDesignationInfo.setDesignationName(companyDesignationInfoDto.getDesignationName());
		companyDesignationInfo.setRoles(companyDesignationInfoDto.getRoles());

		BeanUtils.copyProperties(companyDesignationInfo, companyDesignationInfoDto);
		companyDesignationInfoDto.setIsSubmited(companyInfo.getIsSubmited());
		return companyDesignationInfoDto;
	}

	private CompanyDesignationInfoDto getCompanyDesignationInfoDto(List<CompanyDesignationInfo> companyDesignationInfos,
			CompanyDesignationInfo companyDesignationInfo) {
		CompanyDesignationInfoDto companyDesignationInfoDto = new CompanyDesignationInfoDto();
		BeanUtils.copyProperties(companyDesignationInfo, companyDesignationInfoDto);
		Set<CompanyDesignationInfoDto> childcompanyDesignationInfoDto = new LinkedHashSet<>();
		for (CompanyDesignationInfo companyDesignationInfo2 : companyDesignationInfos) {
			if (companyDesignationInfo2.getParentDesignationInfo().getDesignationId()
					.equals(companyDesignationInfo.getDesignationId())
					&& !companyDesignationInfo2.getParentDesignationInfo().getDesignationId()
							.equals(companyDesignationInfo2.getDesignationId())) {
				childcompanyDesignationInfoDto
						.add(getCompanyDesignationInfoDto(companyDesignationInfos, companyDesignationInfo2));
			}
		}
		companyDesignationInfoDto.setChildcompanyDesignationInfoDto(childcompanyDesignationInfoDto);
		return companyDesignationInfoDto;
	}

	@Override
	public List<CompanyDesignationInfoDto> getAllDepartmentDesignation(long companyId, String departmentName) {

		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));
		List<CompanyDesignationInfo> entityDesignationList = companyDesignationRepository
				.findByCompanyInfoCompanyIdAndDepartment(companyId, departmentName);
		List<CompanyDesignationInfoDto> companyDesignationInfoDtos = new ArrayList<>();
		if (!entityDesignationList.isEmpty() && companyInfo != null) {
			for (CompanyDesignationInfo companyDesignationInfo : entityDesignationList) {
				if (Objects.equals(companyDesignationInfo.getDesignationId(),
						companyDesignationInfo.getParentDesignationInfo().getDesignationId())) {
					companyDesignationInfoDtos
							.add(getCompanyDesignationInfoDto(entityDesignationList, companyDesignationInfo));
				}
			}
		} else {
			return Collections.emptyList();
		}
		companyDesignationInfoDtos.forEach(x -> x.setIsSubmited(companyInfo.getIsSubmited()));
		return companyDesignationInfoDtos;
	}

	private List<Long> ids;

	@Transactional
	@Override
	public String deleteCompanyDesignation(DeleteCompanyDesignationDto deleteCompanyDesignationDto) {
		ids = new ArrayList<>();
		CompanyInfo companyInfo = companyInfoRepository.findById(deleteCompanyDesignationDto.getCompanyId())
				.orElseThrow(() -> new CompanyNotFoundException(AdminConstants.COMPANY_NOT_FOUND));

		if (Boolean.FALSE.equals(companyInfo.getIsSubmited()) || companyInfo.getIsSubmited() == null) {
			CompanyDesignationInfo designationInfo = companyDesignationRepository
					.findById(deleteCompanyDesignationDto.getDesignationId())
					.orElseThrow(() -> new DesignationCannotUpdate(DESIGNATION_CANNOT_BE_DELETED));
			List<CompanyDesignationInfo> designationList = companyDesignationRepository
					.findByCompanyInfoCompanyIdAndParentDesignationInfoDesignationId(
							deleteCompanyDesignationDto.getCompanyId(), deleteCompanyDesignationDto.getDesignationId())
					.filter(x -> !x.isEmpty() || designationInfo != null)
					.orElseThrow(() -> new DesignationCannotUpdate(DESIGNATION_CANNOT_BE_DELETED));
			if (!designationList.isEmpty()) {
				List<CompanyDesignationInfo> entityDesignationList = companyDesignationRepository
						.findByCompanyInfoCompanyIdAndDepartment(deleteCompanyDesignationDto.getCompanyId(),
								designationList.get(0).getDepartment());
				for (CompanyDesignationInfo companyDesignationInfo : designationList) {
					deleteCompanyDesignationD(companyDesignationInfo, entityDesignationList);
				}
			}
			ids.add(deleteCompanyDesignationDto.getDesignationId());
			companyDesignationRepository.deleteAllById(ids);
		} else {
			throw new DesignationCannotUpdate(DESIGNATION_CANNOT_BE_DELETED);
		}
		return "Designation Deleted Successfully";
	}

	private void deleteCompanyDesignationD(CompanyDesignationInfo companyDesignationInfo,
			List<CompanyDesignationInfo> entityDesignationList) {
		if (companyDesignationInfo.getParentDesignationInfo() != null && !companyDesignationInfo.getDesignationId()
				.equals(companyDesignationInfo.getParentDesignationInfo().getDesignationId())) {
			for (CompanyDesignationInfo companyDesignationInfo2 : entityDesignationList) {
				if (companyDesignationInfo.getDesignationId()
						.equals(companyDesignationInfo2.getParentDesignationInfo().getDesignationId())) {
					deleteCompanyDesignationD(companyDesignationInfo2, entityDesignationList);
				}
			}
			ids.add(companyDesignationInfo.getDesignationId());
		}
	}

}
