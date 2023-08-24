package com.te.flinko.dto.hr;

import java.math.BigDecimal;

public class CandidateListDTO {
	
	private Long candidateId;
	
	private String firstName;

	private String lastName;

	private String fullName;

	private String emailId;

	private Long mobileNumber;

	private String designationName;

	private BigDecimal yearOfExperience;

	private String employementStatus;

	@Override
	public String toString() {
		return "CandidateListDto [candidateId= "+ candidateId +"fullName=" + fullName + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber
				+ ", designationName=" + designationName + ", yearOfExperience=" + yearOfExperience
				+ ", employementStatus=" + employementStatus + "]";
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public BigDecimal getYearOfExperience() {
		return yearOfExperience;
	}

	public void setYearOfExperience(BigDecimal yearOfExperience) {
		this.yearOfExperience = yearOfExperience;
	}

	public String getEmployementStatus() {
		return employementStatus;
	}

	public void setEmployementStatus(String employementStatus) {
		this.employementStatus = employementStatus;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

}
