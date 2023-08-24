package com.te.flinko.service.employee;

import static com.te.flinko.common.employee.EmployeeRegistrationConstants.ADMIN_WILL_VERIFY_YOUR_ACCOUNT;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.ALREADY_REGISTERED;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.DATA_NOT_FOUND;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.EMPLOYEE_ALREADY_REGISTERED;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.EMPLOYEE_ID_ALREADY_EXIST;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.INVALID_OTP;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.REGISTRATION_SUCCESSFULLY_DONE;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.SESSION_EXPIRED;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.SESSION_TIME_EXPIRED;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.VALID_OTP;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.te.flinko.beancopy.BeanCopy;
import com.te.flinko.dto.admin.CompanyDesignationNamesDto;
import com.te.flinko.dto.admin.CompanyInfoNamesDto;
import com.te.flinko.dto.employee.EmployeeIdDto;
import com.te.flinko.dto.employee.EmployeeLoginDto;
import com.te.flinko.dto.employee.EmployeeOfficialInfoDto;
import com.te.flinko.dto.employee.EmployeePersonalInfoDto;
import com.te.flinko.dto.employee.MailDto;
import com.te.flinko.dto.employee.NewConfirmPasswordDto;
import com.te.flinko.dto.employee.Registration;
import com.te.flinko.dto.employee.VerifyOTPDto;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeeLoginInfo;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.exception.employee.EmployeeNotRegisteredException;
import com.te.flinko.repository.admin.CompanyDesignationInfoRepository;
import com.te.flinko.repository.admin.CompanyInfoRepository;
import com.te.flinko.repository.employee.EmployeeLoginInfoRepository;
import com.te.flinko.repository.employee.EmployeePersonalInfoRepository;
import com.te.flinko.service.mail.employee.EmailService;
import com.te.flinko.util.CacheStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeRegistrationServiceImpl implements EmployeeRegistrationService {

	private static final String MOBILE_NUMBER_OR_EMAIL_ID_ALREADY_EXIST = "Mobile Number Or Email Id Already Exist!!!";

	private final CompanyInfoRepository companyInfoRepository;

	private final CompanyDesignationInfoRepository companyDesignationInfoRepository;

	private final EmployeeLoginInfoRepository employeeLoginInfoRepository;

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final CacheStore<Registration> cacheStoreEmployeeRegistrationDto;

	private final CacheStore<Long> cacheStoreOTP;

	private final EmailService emailService;

	private final CacheStore<Boolean> cacheStoreValiedOTP;

	private final EmployeeLoginServiceImpl employeeLoginServiceImpl;

	@Override
	public List<CompanyInfoNamesDto> getAllCompany() {

		return Stream.of(companyInfoRepository.getAllCompany()).map(company -> {
			CompanyInfoNamesDto dto = new CompanyInfoNamesDto();
			Object[] companyInfoNamesDto = (Object[]) company;
			return Stream.of(companyInfoNamesDto).map(x -> Optional.of(x).filter(Long.class::isInstance).map(y -> {
				dto.setCompanyId((Long) x);
				return dto;
			}).orElseGet(() -> {
				dto.setCompanyName((String) x);
				return dto;
			})).collect(Collectors.toSet());
		}).flatMap(Collection::stream).collect(Collectors.toList());

	}

	@Override
	public List<CompanyDesignationNamesDto> getAllDesignation(Long companyId) {
		return companyDesignationInfoRepository.findByCompanyInfoCompanyId(companyId).stream().map(x -> {
			CompanyDesignationNamesDto companyDesignationNamesDto = new CompanyDesignationNamesDto();
			BeanUtils.copyProperties(x, companyDesignationNamesDto);
			return companyDesignationNamesDto;
		}).collect(Collectors.toList());
	}

	@Override
	public String registration(NewConfirmPasswordDto newConfirmPasswordDto) {

		if (employeeLoginInfoRepository.findByEmployeeIdAndEmployeePersonalInfoCompanyInfoCompanyId(
				newConfirmPasswordDto.getEmployeeId(), newConfirmPasswordDto.getCompanyId()).isPresent())
			return EMPLOYEE_ALREADY_REGISTERED;

		return Optional.ofNullable(newConfirmPasswordDto)
				.filter(registration -> Optional.ofNullable(cacheStoreValiedOTP.get(registration.getEmployeeId()))
						.orElse(Boolean.FALSE)
						&& registration.getNewPassword().equals(registration.getConfirmPassword()))
				.map(companyRegistration -> {
					Registration employeeRegistrationDto2 = cacheStoreEmployeeRegistrationDto
							.get(newConfirmPasswordDto.getEmployeeId());
					employeeRegistrationDto2.setIsActive(Boolean.TRUE);
					CompanyInfo companyInfo2 = companyInfoRepository
							.findByCompanyName(employeeRegistrationDto2.getCompanyName()).filter(Objects::nonNull)
							.map(companyInfo -> {
								employeeRegistrationDto2.setIsActive(Boolean.FALSE);
								return companyInfo;
							}).orElse(null);

					employeeRegistrationDto2.setPassword(newConfirmPasswordDto.getNewPassword());

					CompanyInfo companyInfo3 = null;
					if (companyInfo2 == null) {
						CompanyInfo companyInfo = BeanCopy.objectProperties(employeeRegistrationDto2,
								CompanyInfo.class);
						companyInfo3 = companyInfoRepository.save(companyInfo);
					} else if (companyInfo2 != null && !companyInfo2.getEmployeePersonalInfoList().isEmpty()) {
						emailService.sendMail(MailDto.builder().subject("Verification Employee Account")
								.body("Dear Admin," + "\r\n" + "\r\n"
										+ "The Employee Wants To Register In Your Company Please Verify The Account With EmployeeId :"
										+ employeeRegistrationDto2.getEmployeeId() + "\r\n" + "\r\n" + "\r\n"
										+ "Thanks and Regards," + "\r\n" + "Team FLINKO")
								.to(companyInfo2.getEmployeePersonalInfoList().get(0).getEmployeeOfficialInfo()
										.getOfficialEmailId())
								.body(ALREADY_REGISTERED).build());
					}

					EmployeeLoginDto employeeLoginDto = BeanCopy.objectProperties(employeeRegistrationDto2,
							EmployeeLoginDto.class);
					employeeLoginDto.setCurrentPassword(employeeRegistrationDto2.getPassword());

					EmployeePersonalInfoDto employeePersonalInfoDto = BeanCopy
							.objectProperties(employeeRegistrationDto2, EmployeePersonalInfoDto.class);

					EmployeeOfficialInfoDto employeeOfficialInfoDto = BeanCopy
							.objectProperties(employeeRegistrationDto2, EmployeeOfficialInfoDto.class);

					employeePersonalInfoDto.setEmployeeOfficialInfo(employeeOfficialInfoDto);
					employeeLoginDto.setEmployeePersonalInfo(employeePersonalInfoDto);
					EmployeeLoginInfo objectProperties = BeanCopy.objectProperties(employeeLoginDto,
							EmployeeLoginInfo.class);

					if (companyInfo2 != null && !companyInfo2.getEmployeePersonalInfoList().isEmpty()) {
						objectProperties.getEmployeePersonalInfo().setStatus(Map.of("RequestedTo", companyInfo2
								.getEmployeePersonalInfoList().get(0).getEmployeeOfficialInfo().getOfficialEmailId()));
					}

					objectProperties.getEmployeePersonalInfo()
							.setCompanyInfo(companyInfo2 == null ? companyInfo3 : companyInfo2);
					objectProperties.getEmployeePersonalInfo().getEmployeeOfficialInfo()
							.setDesignation(employeePersonalInfoDto.getEmployeeOfficialInfo().getDesignationName());
					employeeLoginInfoRepository.save(objectProperties);
					cleanUp(newConfirmPasswordDto.getEmployeeId());
					return companyInfo2 == null ? REGISTRATION_SUCCESSFULLY_DONE : ADMIN_WILL_VERIFY_YOUR_ACCOUNT;
				}).orElseThrow(() -> new DataNotFoundException(SESSION_EXPIRED));
	}

	@Override
	public String varifyEmployee(Registration employeeRegistrationDto, Long companyId) {

		if (!employeePersonalInfoRepository.findByMobileNumberOrEmployeeOfficialInfoOfficialEmailId(
				employeeRegistrationDto.getMobileNumber(), employeeRegistrationDto.getOfficialEmailId()).isEmpty()) {
			throw new EmployeeNotRegisteredException(MOBILE_NUMBER_OR_EMAIL_ID_ALREADY_EXIST);
		}

		if (cacheStoreEmployeeRegistrationDto.get(employeeRegistrationDto.getEmployeeId()) != null)
			cacheStoreEmployeeRegistrationDto.invalidate(employeeRegistrationDto.getEmployeeId());

		return employeePersonalInfoRepository.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(companyId,
				employeeRegistrationDto.getEmployeeId()).filter(List::isEmpty).map(x -> {
					cacheStoreEmployeeRegistrationDto.add(employeeRegistrationDto.getEmployeeId(),
							employeeRegistrationDto);
					return employeeLoginServiceImpl.sendOtp(employeeRegistrationDto.getEmployeeId(),
							employeeRegistrationDto.getOfficialEmailId(), employeeRegistrationDto.getMobileNumber(),
							employeeRegistrationDto.getFirstName() + " " + employeeRegistrationDto.getLastName());
				}).orElseThrow(() -> new EmployeeNotRegisteredException(EMPLOYEE_ID_ALREADY_EXIST));

	}

	@Override
	public String validateOTP(VerifyOTPDto verifyOTPDto) {
		Boolean valiedEmployee = Objects.nonNull(cacheStoreEmployeeRegistrationDto.get(verifyOTPDto.getEmployeeId()))
				? Boolean.TRUE
				: Boolean.FALSE;
		Boolean valiedOTP = Optional.ofNullable(cacheStoreOTP.get(verifyOTPDto.getEmployeeId()))
				.orElseThrow(() -> new DataNotFoundException(SESSION_EXPIRED)).equals(verifyOTPDto.getOtp())
						? valiedEmployee
						: Boolean.FALSE;
		if (cacheStoreValiedOTP.get(verifyOTPDto.getEmployeeId()) != null)
			cacheStoreValiedOTP.invalidate(verifyOTPDto.getEmployeeId());
		cacheStoreValiedOTP.add(verifyOTPDto.getEmployeeId(), valiedOTP);
		return Optional.of(valiedOTP).filter(Boolean::booleanValue).map(y -> VALID_OTP)
				.orElseThrow(() -> new DataNotFoundException(INVALID_OTP));
	}

	@Override
	public String resendOTP(EmployeeIdDto employeeIdDto) {
		Registration registration2 = cacheStoreEmployeeRegistrationDto.get(employeeIdDto.getEmployeeId());
		Registration registration = Optional.ofNullable(registration2)
				.orElseThrow(() -> new DataNotFoundException(SESSION_EXPIRED));
		return Objects.nonNull(registration) ? employeeLoginServiceImpl.sendOtp(registration.getEmployeeId(),
				registration.getOfficialEmailId(), registration.getMobileNumber(),
				registration2.getFirstName() + " " + registration2.getLastName()) : DATA_NOT_FOUND;
	}

	private void cleanUp(String employeeId) {
		try {
			cacheStoreValiedOTP.invalidate(employeeId);
			cacheStoreEmployeeRegistrationDto.invalidate(employeeId);
			cacheStoreOTP.invalidate(employeeId);
		} catch (Exception e) {
			throw new DataNotFoundException(SESSION_TIME_EXPIRED);
		}
	}

}
