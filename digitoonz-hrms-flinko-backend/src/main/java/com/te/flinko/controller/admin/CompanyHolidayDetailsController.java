package com.te.flinko.controller.admin;

import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.COUNTS_OF_HOLIDAY_IN_THE_YEAR;
import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.FETCH_ALL_HOLIDAYS_OF_THE_YEAR;
import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.FETCH_HOLIDAY_DETAIL_SUCCESSFULLY;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.flinko.audit.BaseConfigController;
import com.te.flinko.dto.admin.CompanyHolidayDetailsDto;
import com.te.flinko.response.SuccessResponse;
import com.te.flinko.service.admin.CompanyHolidayDetailsService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class CompanyHolidayDetailsController extends BaseConfigController {

	private final CompanyHolidayDetailsService companyHolidayDetailsService;

	@PostMapping("company-holiday/{slectedDate}")
	public ResponseEntity<SuccessResponse> addCompanyHoliday(
			@RequestBody @Valid CompanyHolidayDetailsDto companyHolidayDetailsDto, @PathVariable Integer slectedDate) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(companyHolidayDetailsService
						.addCompanyHoliday(companyHolidayDetailsDto, getCompanyId(), LocalDate.of(slectedDate, 01, 01)))
						.build());
	}

	@PostMapping("company-holidays/{slectedDate}")
	public ResponseEntity<SuccessResponse> addListCompanyHoliday(
			@RequestBody List<@Valid CompanyHolidayDetailsDto> companyHolidayDetailsDtos,
			@PathVariable Integer slectedDate) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(companyHolidayDetailsService.addListCompanyHoliday(companyHolidayDetailsDtos,
								getCompanyId(), LocalDate.of(slectedDate, 01, 01)))
						.build());
	}

	@GetMapping("company-holidays/{slectedDate}")
	public ResponseEntity<SuccessResponse> getAllHolidayPerYear(@PathVariable Integer slectedDate) {

		return ResponseEntity
				.status(HttpStatus.OK).body(
						SuccessResponse
								.builder().error(Boolean.FALSE).message(FETCH_ALL_HOLIDAYS_OF_THE_YEAR+slectedDate).data(companyHolidayDetailsService
										.getAllHolidayPerYear(getCompanyId(), LocalDate.of(slectedDate, 01, 01)))
								.build());
	}

	@GetMapping("company-holidays/count/{slectedDate}")
	public ResponseEntity<SuccessResponse> countCompanyHoliday(@PathVariable Integer slectedDate) {
		return ResponseEntity
				.status(HttpStatus.OK).body(
						SuccessResponse
								.builder().error(Boolean.FALSE).message(COUNTS_OF_HOLIDAY_IN_THE_YEAR+slectedDate).data(companyHolidayDetailsService
										.countHolidayPerYear(getCompanyId(), LocalDate.of(slectedDate, 01, 01)))
								.build());
	}
	
	@GetMapping("company-holiday/{holidayId}")
	public ResponseEntity<SuccessResponse> getCompanyHoliday(@PathVariable Long holidayId) {
		return ResponseEntity
				.status(HttpStatus.OK).body(
						SuccessResponse
								.builder().error(Boolean.FALSE).message(FETCH_HOLIDAY_DETAIL_SUCCESSFULLY).data(companyHolidayDetailsService
										.getCompanyHoliday(getCompanyId(), holidayId))
								.build());
	}

	@PutMapping("company-holiday/{slectedDate}")
	public ResponseEntity<SuccessResponse> updateCompanyHoliday(
			@RequestBody @Valid CompanyHolidayDetailsDto companyHolidayDetailsDto, @PathVariable Integer slectedDate) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(companyHolidayDetailsService.updateCompanyHoliday(companyHolidayDetailsDto,
								getCompanyId(), LocalDate.of(slectedDate, 01, 01)))
						.build());
	}

	@DeleteMapping("company-holiday/{slectedDate}")
	public ResponseEntity<SuccessResponse> deleteCompanyHoliday(
			@RequestBody @Valid CompanyHolidayDetailsDto companyHolidayDetailsDto, @PathVariable Integer slectedDate) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(companyHolidayDetailsService.deleteCompanyHoliday(companyHolidayDetailsDto,
								getCompanyId(), LocalDate.of(slectedDate, 01, 01)))
						.build());
	}

}
