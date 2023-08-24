package com.te.flinko.service.admin;

import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.HOLIDAY_ADD_SUCCESSFULLY;
import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.HOLIDAY_ALREADY_EXIST;
import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.HOLIDAY_DETAILS_NOT_FOUND;
import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.HOLIDAY_NOT_FOUND;
import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.HO_LIDAY_IS_SUCCESSFULLY_REMOVED;
import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.HO_LIDAY_IS_SUCCESSFULLY_UPDATE;
import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.LIST_OF_HOLIDAY_IS_UPLOAD_SUCCESSFULLY;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.core.type.TypeReference;
import com.te.flinko.beancopy.BeanCopy;
import com.te.flinko.dto.admin.CompanyHolidayDetailsDto;
import com.te.flinko.entity.admin.CompanyHolidayDetails;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.exception.admin.HolidayNotFoundException;
import com.te.flinko.repository.admin.CompanyHolidayDetailsRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class CompanyHolidayDetailsServiceImpl implements CompanyHolidayDetailsService {

	private final CompanyHolidayDetailsRepository companyHolidayDetailsRepository;

	private final CompanyInfoRepository companyInfoRepository;

	@Override
	@Transactional
	public String addCompanyHoliday(CompanyHolidayDetailsDto companyHolidayDetailsDto, Long companyId,
			LocalDate selectedDate) {

		return companyHolidayDetailsRepository.findByHolidayDateBetweenAndHolidayNameAndCompanyInfoCompanyId(
				LocalDate.of(selectedDate.getYear(), 01, 01), LocalDate.of(selectedDate.getYear(), 12, 31),
				companyHolidayDetailsDto.getHolidayName(), companyId).filter(List::isEmpty).map(holidayList -> {
					CompanyInfo companyInfo = companyInfoRepository.findById(companyId).orElseThrow();
					CompanyHolidayDetails holidayDetails = CompanyHolidayDetails.builder()
							.holidayName(companyHolidayDetailsDto.getHolidayName())
							.isOptional(companyHolidayDetailsDto.getOptional())
							.holidayDate(companyHolidayDetailsDto.getHolidayDate()).companyInfo(companyInfo).build();
					companyInfo.setCompanyHolidayDetailsList(List.of(holidayDetails));
					return HOLIDAY_ADD_SUCCESSFULLY;
				}).orElseThrow(() -> new HolidayNotFoundException(HOLIDAY_ALREADY_EXIST));
	}

	@Override
	@Transactional
	public String addListCompanyHoliday(List<CompanyHolidayDetailsDto> companyHolidayDetailsDtos, Long companyId,
			LocalDate selectedDate) {

		return companyHolidayDetailsRepository.findByHolidayDateBetweenAndHolidayNameInAndCompanyInfoCompanyId(
				LocalDate.of(selectedDate.getYear(), 01, 01), LocalDate.of(selectedDate.getYear(), 12, 31),
				companyHolidayDetailsDtos.stream().map(CompanyHolidayDetailsDto::getHolidayName)
						.collect(Collectors.toList()),
				companyId).filter(List::isEmpty).map(holidayList -> {
					CompanyInfo companyInfo = companyInfoRepository.findById(companyId).orElseThrow();
					List<CompanyHolidayDetails> companyHolidayDetails = BeanCopy
							.objectProperties(companyHolidayDetailsDtos,
									new TypeReference<List<CompanyHolidayDetails>>() {
									})
							.stream().map(holiday -> {
								holiday.setCompanyInfo(companyInfo);
								return holiday;
							}).collect(Collectors.toList());
					companyInfo.setCompanyHolidayDetailsList(companyHolidayDetails);
					return LIST_OF_HOLIDAY_IS_UPLOAD_SUCCESSFULLY;
				}).orElseThrow(() -> new HolidayNotFoundException(HOLIDAY_ALREADY_EXIST));
	}

	@Override
	public Long countHolidayPerYear(Long companyId, LocalDate selectedDate) {
		return companyHolidayDetailsRepository
				.findByHolidayDateBetweenAndCompanyInfoCompanyId(LocalDate.of(selectedDate.getYear(), 01, 01),
						LocalDate.of(selectedDate.getYear(), 12, 31), companyId)
				.filter(holidays -> !holidays.isEmpty()).map(companyHolidays -> companyHolidays.stream()
						.filter(holiday -> holiday.getHolidayName() != null).count())
				.orElseThrow(() -> new HolidayNotFoundException(HOLIDAY_DETAILS_NOT_FOUND));
	}

	@Override
	@Transactional
	public String updateCompanyHoliday(CompanyHolidayDetailsDto companyHolidayDetailsDto, Long companyId,
			LocalDate selectedDate) {
		return companyHolidayDetailsRepository
				.findByHolidayIdAndHolidayDateBetweenAndCompanyInfoCompanyId(companyHolidayDetailsDto.getHolidayId(),
						LocalDate.of(selectedDate.getYear(), 01, 01), LocalDate.of(selectedDate.getYear(), 12, 31),
						companyId)
				.map(companyHoliday -> {
					companyHoliday.setHolidayName(companyHolidayDetailsDto.getHolidayName());
					companyHoliday.setHolidayDate(companyHolidayDetailsDto.getHolidayDate());
					companyHoliday.setIsOptional(companyHolidayDetailsDto.getOptional());
					return HO_LIDAY_IS_SUCCESSFULLY_UPDATE;
				}).orElseThrow(() -> new HolidayNotFoundException(HOLIDAY_NOT_FOUND));
	}

	@Override
	public String deleteCompanyHoliday(CompanyHolidayDetailsDto companyHolidayDetailsDto, Long companyId,
			LocalDate selectedDate) {
		return companyHolidayDetailsRepository
				.findByHolidayIdAndHolidayDateBetweenAndCompanyInfoCompanyId(companyHolidayDetailsDto.getHolidayId(),
						LocalDate.of(selectedDate.getYear(), 01, 01), LocalDate.of(selectedDate.getYear(), 12, 31),
						companyId)
				.map(companyHoliday -> {
					companyHolidayDetailsRepository.delete(companyHoliday);
					return HO_LIDAY_IS_SUCCESSFULLY_REMOVED;
				}).orElseThrow(() -> new HolidayNotFoundException(HOLIDAY_NOT_FOUND));
	}

	@Override
	public List<CompanyHolidayDetailsDto> getAllHolidayPerYear(Long companyId, LocalDate selectedDate) {
		return companyHolidayDetailsRepository
				.findByHolidayDateBetweenAndCompanyInfoCompanyId(LocalDate.of(selectedDate.getYear(), 01, 01),
						LocalDate.of(selectedDate.getYear(), 12, 31), companyId)
				.filter(holidays -> !holidays.isEmpty())
				.map(companyHolidays -> companyHolidays.stream().map(holiday -> {
					CompanyHolidayDetailsDto holidayDetailsDto = CompanyHolidayDetailsDto.builder().build();
					BeanUtils.copyProperties(holiday, holidayDetailsDto);
					holidayDetailsDto.setOptional(holiday.getIsOptional());
					return holidayDetailsDto;
				}).collect(Collectors.toList())).orElseGet(Collections::emptyList);
	}

	@Override
	public CompanyHolidayDetailsDto getCompanyHoliday(Long companyId, Long holidayId) {
		return companyHolidayDetailsRepository.findByHolidayIdAndCompanyInfoCompanyId(holidayId, companyId)
				.map(companyHoliday -> {
					CompanyHolidayDetailsDto companyHolidayDetailsDto = new CompanyHolidayDetailsDto();
					BeanUtils.copyProperties(companyHoliday, companyHolidayDetailsDto);
					companyHolidayDetailsDto.setOptional(companyHoliday.getIsOptional());
					return companyHolidayDetailsDto;
				}).orElseThrow(() -> new HolidayNotFoundException(HOLIDAY_DETAILS_NOT_FOUND));
	}

}
