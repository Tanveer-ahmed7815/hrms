package com.te.flinko.dto.hr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ScheduledCandidateDetailsDTO {
	private Long candidateId;
	private String firstName;
	private String lastName;
	private String fullName;
	private String emailId;
	private Long mobileNumber;
	private Long departmentId;
	private String department;
	private String designationName;
	private String interviewType;
	private String interviewDetails;
	private LocalDate interviewDate;
	private String interviewerName;
	private LocalTime startTime;
	private Integer duration;
	private Integer roundOfInterview;
	private String status;
	private Map<String, String> feedback;
	private String resumeUrl;
	private Long employeePersonalInfo;
	private Long interviewId;
	private String roundName;
	private List<String> others;

	@Override
	public String toString() {
		return "ScheduledCandidateDetails [interviewId+" + interviewId + ",departmentId+" + departmentId
				+ ",candidateId= " + candidateId + " fullName=" + fullName + ", emailId=" + emailId + ", mobileNumber="
				+ mobileNumber + ", department=" + department + ", designationName=" + designationName
				+ ", interviewType=" + interviewType + ",interviewDetails=" + interviewDetails + ", interviewDate="
				+ interviewDate + ", interviewerName=" + interviewerName + ", startTime=" + startTime + ", duration="
				+ duration + ", roundOfInterview=" + roundOfInterview + ", status=" + status + ", feedback=" + feedback
				+ ", resumeUrl=" + resumeUrl + ",employeePersonalInfo=" + employeePersonalInfo + ",roundName="
				+ roundName + "]";
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public void setInterviewType(String interviewType) {
		this.interviewType = interviewType;
	}

	public void setInterviewDate(LocalDate interviewDate) {
		this.interviewDate = interviewDate;
	}

	public void setInterviewerName(String interviewerName) {
		this.interviewerName = interviewerName;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public void setRoundOfInterview(Integer roundOfInterview) {
		this.roundOfInterview = roundOfInterview;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setFeedback(Map<String, String> feedback) {
		this.feedback = feedback;
	}

	public void setResumeUrl(String resumeUrl) {
		this.resumeUrl = resumeUrl;
	}

	public void setEmployeePersonalInfo(Long long1) {
		this.employeePersonalInfo = long1;
	}

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public String getDesignationName() {
		return designationName;
	}

	public String getInterviewType() {
		return interviewType;
	}

	public LocalDate getInterviewDate() {
		return interviewDate;
	}

	public String getInterviewerName() {
		return interviewerName;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public Integer getRoundOfInterview() {
		return roundOfInterview;
	}

	public String getStatus() {
		return status;
	}

	public Map<String, String> getFeedback() {
		return feedback;
	}

	public String getResumeUrl() {
		return resumeUrl;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

}
