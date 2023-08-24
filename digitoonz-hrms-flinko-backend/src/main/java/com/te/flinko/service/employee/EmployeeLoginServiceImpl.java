package com.te.flinko.service.employee;

import static com.te.flinko.common.employee.EmployeeLoginConstants.CURRENT_PASSWORD_AND_CONFIRM_PASSWORD_DOES_NOT_MATCHED;
import static com.te.flinko.common.employee.EmployeeLoginConstants.EMPLYOEE_DOES_NOT_EXIST;
import static com.te.flinko.common.employee.EmployeeLoginConstants.INVAILED_EMPLYOEE_ID_OR_PASSWORD;
import static com.te.flinko.common.employee.EmployeeLoginConstants.INVALID_OTP;
import static com.te.flinko.common.employee.EmployeeLoginConstants.OLD_PASSWORD_AND_CURRENT_PASSWORD_DOES_NOT_MATCHED;
import static com.te.flinko.common.employee.EmployeeLoginConstants.OLD_PASSWORD_AND_NEW_PASSWORD_IS_SAME;
import static com.te.flinko.common.employee.EmployeeLoginConstants.OTP_SEND_TO_YOUR_RESPECTIVE_EMAIL;
import static com.te.flinko.common.employee.EmployeeLoginConstants.OTP_SEND_TO_YOUR_RESPECTIVE_EMAIL_AND_MOBILE_NUMBER;
import static com.te.flinko.common.employee.EmployeeLoginConstants.OTP_SEND_TO_YOUR_RESPECTIVE_MOBILE_NUMBER;
import static com.te.flinko.common.employee.EmployeeLoginConstants.RESET_PASSWORD_WITH_EMPLOYEE_ID;
import static com.te.flinko.common.employee.EmployeeLoginConstants.SOMETHING_WENT_WRONG;
import static com.te.flinko.common.employee.EmployeeLoginConstants.SUCCESSFULLY_UPDATE_PASSWORD;
import static com.te.flinko.common.employee.EmployeeLoginConstants.VALID_OTP;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.SESSION_EXPIRED_FORGOT;
import static com.te.flinko.common.employee.EmployeeRegistrationConstants.SESSION_TIME_EXPIRED;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.te.flinko.dto.employee.EmployeeIdDto;
import com.te.flinko.dto.employee.EmployeeLoginDto;
import com.te.flinko.dto.employee.EmployeeLoginResponseDto;
import com.te.flinko.dto.employee.MailDto;
import com.te.flinko.dto.employee.NewConfirmPasswordDto;
import com.te.flinko.dto.employee.ResetPasswordDto;
import com.te.flinko.dto.employee.VerifyOTPDto;
import com.te.flinko.entity.employee.EmployeeLoginInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.exception.employee.DataNotFoundException;
import com.te.flinko.exception.employee.EmployeeLoginException;
import com.te.flinko.exception.employee.EmployeeNotFoundException;
import com.te.flinko.exception.employee.EmployeeNotRegisteredException;
import com.te.flinko.repository.employee.EmployeeLoginInfoRepository;
import com.te.flinko.service.mail.employee.EmailService;
import com.te.flinko.service.sms.employee.SmsService;
import com.te.flinko.util.CacheStore;

import lombok.RequiredArgsConstructor;

/**
 * @author Sahid
 *
 */

@Service
@RequiredArgsConstructor
public class EmployeeLoginServiceImpl implements EmployeeLoginService {

	private static final String YOUR_ACCOUNT_IS_NOT_ACTIVATE_PLEASE_CONTACT_ADMIN_OR_HR = "Your Account Is Inactive Please Contact Admin Or HR!!!";

	private final EmployeeLoginInfoRepository employeeLoginRepository;

	private final CacheStore<EmployeeLoginInfo> cacheStoreEmployeeLogin;

	private final CacheStore<Long> cacheStoreOTP;

	private final CacheStore<Boolean> cacheStoreValidOTP;

	private final EmailService emailService;

	private final SmsService smsService;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public EmployeeLoginResponseDto login(EmployeeLoginDto employeeLoginDto) {

		if (employeeLoginRepository.findByEmployeeIdAndCurrentPasswordAndEmployeePersonalInfoIsActiveFalse(
				employeeLoginDto.getEmployeeId(), employeeLoginDto.getPassword()) != null)
			throw new DataNotFoundException(YOUR_ACCOUNT_IS_NOT_ACTIVATE_PLEASE_CONTACT_ADMIN_OR_HR);

		return Optional
				.ofNullable(employeeLoginRepository
						.findByEmployeeIdAndCurrentPasswordAndEmployeePersonalInfoIsActive(
								employeeLoginDto.getEmployeeId(), employeeLoginDto.getPassword(), Boolean.TRUE)
						.orElseThrow(() -> new EmployeeLoginException(INVAILED_EMPLYOEE_ID_OR_PASSWORD)))
				.map(employeeLogedin -> {
					EmployeePersonalInfo info = employeeLogedin.getEmployeePersonalInfo();
					return EmployeeLoginResponseDto.builder().companyId(info.getCompanyInfo().getCompanyId())
							.employeeId(employeeLoginDto.getEmployeeId()).employeeInfoId(info.getEmployeeInfoId())
							.build();
				}).orElseThrow(() -> new EmployeeLoginException(EMPLYOEE_DOES_NOT_EXIST));

	}

	@Override
	public String forgotPassword(EmployeeIdDto employeeIdDto) {

		if (employeeLoginRepository
				.findByEmployeePersonalInfoEmployeeOfficialInfoOfficialEmailIdAndEmployeePersonalInfoIsActive(
						employeeIdDto.getEmailId(), Boolean.FALSE)
				.isPresent())
			throw new DataNotFoundException(YOUR_ACCOUNT_IS_NOT_ACTIVATE_PLEASE_CONTACT_ADMIN_OR_HR);

		EmployeeLoginInfo employeeLoginInfo = employeeLoginRepository
				.findByEmployeePersonalInfoEmployeeOfficialInfoOfficialEmailIdAndEmployeePersonalInfoIsActive(
						employeeIdDto.getEmailId(), Boolean.TRUE)
				.orElseThrow(() -> new EmployeeNotRegisteredException(EMPLYOEE_DOES_NOT_EXIST));
		if (cacheStoreEmployeeLogin.get(employeeIdDto.getEmailId()) != null)
			cacheStoreEmployeeLogin.invalidate(employeeIdDto.getEmailId());
		cacheStoreEmployeeLogin.add(employeeIdDto.getEmailId(), employeeLoginInfo);
		return sendOtp(employeeIdDto.getEmailId(),
				employeeLoginInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId(),
				employeeLoginInfo.getEmployeePersonalInfo().getMobileNumber(),
				employeeLoginInfo.getEmployeePersonalInfo().getFirstName() + " "
						+ employeeLoginInfo.getEmployeePersonalInfo().getLastName());

	}

	@Override
	public String resendOTP(EmployeeIdDto employeeIdDto) {
		EmployeeLoginInfo employeeLoginInfo = Optional
				.ofNullable(cacheStoreEmployeeLogin.get(employeeIdDto.getEmailId()))
				.orElseThrow(() -> new DataNotFoundException(SESSION_TIME_EXPIRED));
		return Objects.nonNull(employeeLoginInfo)
				? sendOtp(employeeIdDto.getEmailId(),
						employeeLoginInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId(),
						employeeLoginInfo.getEmployeePersonalInfo().getMobileNumber(),
						employeeLoginInfo.getEmployeePersonalInfo().getFirstName() + " "
								+ employeeLoginInfo.getEmployeePersonalInfo().getLastName())
				: SESSION_TIME_EXPIRED;
	}

	@Override
	public String validateOTP(VerifyOTPDto verifyOTPDto) {
		EmployeeLoginInfo employeeLoginInfo = cacheStoreEmployeeLogin.get(verifyOTPDto.getEmailId());
		Boolean valiedEmployee = Objects.nonNull(employeeLoginInfo) ? Boolean.TRUE : Boolean.FALSE;
		Boolean valiedOTP = Optional.ofNullable(cacheStoreOTP.get(verifyOTPDto.getEmailId()))
				.orElseThrow(() -> new DataNotFoundException(SESSION_TIME_EXPIRED)).equals(verifyOTPDto.getOtp())
						? valiedEmployee
						: Boolean.FALSE;
		if (cacheStoreValidOTP.get(verifyOTPDto.getEmailId()) != null)
			cacheStoreValidOTP.invalidate(verifyOTPDto.getEmailId());
		cacheStoreValidOTP.add(verifyOTPDto.getEmailId(), valiedOTP);
		return Optional.of(valiedOTP).filter(Boolean::booleanValue).map(y -> VALID_OTP)
				.orElseThrow(() -> new DataNotFoundException(INVALID_OTP));
	}

	String sendOtp(String emailId, String email, Long mobileNumber, String userName) {
		Long otp = ThreadLocalRandom.current().nextLong(1000, 10000);
		Integer emailStatus = emailService.sendMail(new MailDto(email, "Your OTP For Verification",
				"Dear " + userName + ",\r\n" + "\r\n" + "One Time Password for Password Verification is :" + otp
						+ "\r\n" + "Please use this password to complete the verification." + "\r\n" + "\r\n" + "\r\n"
						+ "Do not share OTP with anyone." + "\r\n" + "Thanks and Regards," + "\r\n" + "Team FLINKO"));
		Integer smsStatus = smsService.sendSms("Dear " + userName + ",\r\n" + "\r\n" + "Your OTP For Verification :"
				+ otp + "\r\n" + "\r\n" + "Thanks and Regards," + "\r\n" + "Team FLINKO", "" + mobileNumber);
		if (cacheStoreOTP.get(emailId) != null)
			cacheStoreOTP.invalidate(emailId);
		cacheStoreOTP.add(emailId, otp);

		return optional.filter(emailSuccess -> emailStatus == 200)
				.map(emialRespone -> optional.filter(smsSuccess -> smsStatus == 200)
						.map(emialSmsRespone -> OTP_SEND_TO_YOUR_RESPECTIVE_EMAIL_AND_MOBILE_NUMBER)
						.orElseGet(() -> OTP_SEND_TO_YOUR_RESPECTIVE_EMAIL))
				.orElseGet(() -> optional.filter(smsSuccess -> smsStatus == 200)
						.map(smsRespone -> OTP_SEND_TO_YOUR_RESPECTIVE_MOBILE_NUMBER)
						.orElseThrow(() -> new DataNotFoundException(SOMETHING_WENT_WRONG)));
	}

	@Override
	public String resetPassword(NewConfirmPasswordDto newConfirmPasswordDto) {
		EmployeeLoginInfo employeeLoginInfo = Optional
				.ofNullable(cacheStoreEmployeeLogin.get(newConfirmPasswordDto.getEmailId()))
				.orElseThrow(() -> new DataNotFoundException(SESSION_EXPIRED_FORGOT));

		return optional
				.filter(checkOtp -> cacheStoreValidOTP.get(newConfirmPasswordDto.getEmailId())).map(
						q -> optional
								.filter(newConfirmPassword -> newConfirmPasswordDto.getNewPassword()
										.equals(newConfirmPasswordDto.getConfirmPassword()))
								.map(newOldPassword -> optional
										.filter(newOldPassword1 -> !newConfirmPasswordDto.getNewPassword()
												.equals(employeeLoginInfo.getOldPassword()))
										.map(password1 -> optional
												.filter(e -> !employeeLoginInfo.getCurrentPassword()
														.equals(newConfirmPasswordDto.getConfirmPassword()))
												.map(resetPassword -> {
													employeeLoginInfo
															.setOldPassword(employeeLoginInfo.getCurrentPassword());
													employeeLoginInfo
															.setCurrentPassword(newConfirmPasswordDto.getNewPassword());
													employeeLoginRepository.save(employeeLoginInfo);
													cleanUp(newConfirmPasswordDto.getEmailId());
													return RESET_PASSWORD_WITH_EMPLOYEE_ID
															+ employeeLoginInfo.getEmployeeId();
												})
												.orElseThrow(() -> new DataNotFoundException(
														"Current Password And New Password Can Not Be Same!!!")))
										.orElseThrow(
												() -> new DataNotFoundException(OLD_PASSWORD_AND_NEW_PASSWORD_IS_SAME)))
								.orElseThrow(() -> new DataNotFoundException(
										CURRENT_PASSWORD_AND_CONFIRM_PASSWORD_DOES_NOT_MATCHED)))
				.orElseThrow(() -> new DataNotFoundException(SESSION_EXPIRED_FORGOT));
	}

	@Transactional
	@Override
	public String updatePassword(ResetPasswordDto resetPasswordDto, String logedInUser) {
		EmployeeLoginInfo employeeLoginInfo = employeeLoginRepository.findByEmployeeId(logedInUser)
				.filter(y -> !y.isEmpty()).map(x -> x.get(0))
				.orElseThrow(() -> new EmployeeNotFoundException(EMPLYOEE_DOES_NOT_EXIST));

		return optional
				.filter(newOldPassword -> !resetPasswordDto.getNewPassword()
						.equals(employeeLoginInfo.getOldPassword()))
				.map(newOldPasswordSuccess -> optional
						.filter(currentOldPassword -> employeeLoginInfo.getCurrentPassword()
								.equals(resetPasswordDto.getOldPassword()))
						.map(currentOldPasswordSuccess -> optional.filter(newConfirmPassword -> resetPasswordDto
								.getNewPassword().equals(resetPasswordDto.getConfirmPassword())).map(updatePassword -> {
									employeeLoginInfo.setOldPassword(employeeLoginInfo.getCurrentPassword());
									employeeLoginInfo.setCurrentPassword(resetPasswordDto.getNewPassword());
									return SUCCESSFULLY_UPDATE_PASSWORD;
								}).orElseGet(() -> CURRENT_PASSWORD_AND_CONFIRM_PASSWORD_DOES_NOT_MATCHED))
						.orElseGet(() -> OLD_PASSWORD_AND_CURRENT_PASSWORD_DOES_NOT_MATCHED))
				.orElseGet(() -> OLD_PASSWORD_AND_NEW_PASSWORD_IS_SAME);
	}

	private void cleanUp(String emailId) {
		try {
			cacheStoreOTP.invalidate(emailId);
			cacheStoreEmployeeLogin.invalidate(emailId);
			cacheStoreValidOTP.invalidate(emailId);
		} catch (Exception e) {
			throw new DataNotFoundException(SESSION_TIME_EXPIRED);
		}
	}
}
