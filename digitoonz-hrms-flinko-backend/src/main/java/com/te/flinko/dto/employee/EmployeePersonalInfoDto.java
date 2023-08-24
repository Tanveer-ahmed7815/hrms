package com.te.flinko.dto.employee;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

import javax.persistence.Convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.flinko.util.MapToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeePersonalInfoDto implements Serializable {

	private Long employeeInfoId;
	private String firstName;
	private String lastName;
	private String emailId;
	private long mobileNumber;
	private String pan;
	private String gender;
	private LocalDate dob;
	private String bloodGroup;
	private String guardiansName;
	private String maritalStatus;
	private String language;
	private String linkedinId;
	private long passportNumber;
	private LocalDate passportExpiryDate;
	private Boolean isActive;
	@Convert(converter = MapToStringConverter.class)
	private Map<String, String> status;
	
	private EmployeeOfficialInfoDto employeeOfficialInfo;
	
	private CompanyRegistrationDto companyInfo;

	@Override
	public String toString() {
		return "EmployeePersonalInfoDto [employeeInfoId=" + employeeInfoId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber + ", pan=" + pan + ", gender="
				+ gender + ", dob=" + dob + ", bloodGroup=" + bloodGroup + ", guardiansName=" + guardiansName
				+ ", maritalStatus=" + maritalStatus + ", language=" + language + ", linkedinId=" + linkedinId
				+ ", passportNumber=" + passportNumber + ", passportExpiryDate=" + passportExpiryDate + ", isActive="
				+ isActive + ", employeeOfficialInfo=" + employeeOfficialInfo + "]";
	}
	
	
	
}
