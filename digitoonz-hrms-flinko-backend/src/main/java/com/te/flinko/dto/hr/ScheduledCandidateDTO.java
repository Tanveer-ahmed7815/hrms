package com.te.flinko.dto.hr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ScheduledCandidateDTO {
	private Long candidateId;

	private String firstName;

	private String lastName;

	private String fullName;

	private String emailId;

	private Long mobileNumber;

	private String designationName;

	private String employementStatus;

	private String interviewType;

	private String interviewDetails;

	private LocalDate interviewDate;

	private LocalTime startTime;

	private Integer duration;

	private Long employeePersonalInfo;

	private Integer roundOfInterview;
	
	private String interviewerName;
	
	private String roundName;

	@Override
	public String toString() {
		return "ShowData [candidateId=" + candidateId + "fullName=" + fullName + ", emailId=" + emailId
				+ ", mobileNumber=" + mobileNumber + ", designationName=" + designationName + ", employementStatus="
				+ employementStatus + ",interviewType=" + interviewType + ",interviewDetails=" + interviewDetails
				+ ",interviewDate=" + interviewDate + ",startTime=" + startTime + ",duration=" + duration
				+ ",employeePersonalInfo=" + employeePersonalInfo + ",roundOfInterview=" + roundOfInterview + ",interviewerName="+interviewerName+",roundName="+roundName+"]";
	}

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getInterviewType() {
		return interviewType;
	}

	public void setInterviewType(String interviewType) {
		this.interviewType = interviewType;
	}

	public String getInterviewDetails() {
		return interviewDetails;
	}

	public void setInterviewDetails(String interviewDetails) {
		this.interviewDetails = interviewDetails;
	}

	public LocalDate getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(LocalDate interviewDate) {
		this.interviewDate = interviewDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Long getEmployeePersonalInfo() {
		return employeePersonalInfo;
	}

	public void setEmployeePersonalInfo(Long employeePersonalInfo) {
		this.employeePersonalInfo = employeePersonalInfo;
	}

	public Integer getRoundOfInterview() {
		return roundOfInterview;
	}

	public void setRoundOfInterview(Integer roundOfInterview) {
		this.roundOfInterview = roundOfInterview;
	}

	public String getInterviewerName() {
		return interviewerName;
	}

	public void setInterviewerName(String interviewerName) {
		this.interviewerName = interviewerName;
	}

	public String getRoundName() {
		return roundName;
	}

	public void setRoundName(String roundName) {
		this.roundName = roundName;
	}

}
