package com.te.flinko.dto.hr;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.hr.CandidateInterviewInfo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class FollowUpDetailsDTO {
	private String firstName;
	private String lastName;
	private String fullName;
	private String emailId;
	private Long mobileNumber;
	private Long departmentId;
	private String department;
	private String designationName;
	private String employementStatus;
	private BigDecimal yearOfExperience;
	private String referencePersonName;
	private String highestDegree;
	private String averageGrade;
	private String highestDegreeAndAverageGrade;
	private String previousInterviewername;
	private int roundNumber;
	private String roundName;
	private String resumeUrl;
	private Long interviewId;
	private List<String> others;

	private Map<String, String> feedback;

	private List<EmployeePersonalInfo> employeePersonalInfoList;
	private List<CandidateInterviewInfo> candidateInterviewInfoList;

	@Override
	public String toString() {
		return "FollowUpDetailsDto [fullName=" + fullName + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber
				+ ", department=" + department + ", designationName=" + designationName + ", employementStatus="
				+ employementStatus + ", yearOfExperience=" + yearOfExperience + ", referencePersonName="
				+ referencePersonName + ", highestDegreeAndAverageGrade=" + highestDegreeAndAverageGrade
				+ ", previousInterviewername=" + previousInterviewername + ", roundNumber=" + roundNumber
				+ ", roundName=" + roundName + ", feedback=" + feedback + ", employeePersonalInfoList="
				+ employeePersonalInfoList + ", candidateInterviewInfoList=" + candidateInterviewInfoList
				+ ", resumeUrl=" + resumeUrl + ",interviewId=" + interviewId + "]";
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

	public void setEmployementStatus(String employementStatus) {
		this.employementStatus = employementStatus;
	}

	public void setYearOfExperience(BigDecimal yearOfExperience) {
		this.yearOfExperience = yearOfExperience;
	}

	public void setReferencePersonName(String referencePersonName) {
		this.referencePersonName = referencePersonName;
	}

	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}

	public void setAverageGrade(String averageGrade) {
		this.averageGrade = averageGrade;
	}

	public void setHighestDegreeAndAverageGrade(String highestDegreeAndAverageGrade) {
		this.highestDegreeAndAverageGrade = highestDegreeAndAverageGrade;
	}

	public void setPreviousInterviewername(String previousInterviewername) {
		this.previousInterviewername = previousInterviewername;
	}

	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}

	public void setRoundName(String roundName) {
		this.roundName = roundName;
	}

	public void setResumeUrl(String resumeUrl) {
		this.resumeUrl = resumeUrl;
	}

	public void setFeedback(Map<String, String> feedback) {
		this.feedback = feedback;
	}

	public void setEmployeePersonalInfoList(List<EmployeePersonalInfo> employeePersonalInfoList) {
		this.employeePersonalInfoList = employeePersonalInfoList;
	}

	public void setCandidateInterviewInfoList(List<CandidateInterviewInfo> candidateInterviewInfoList) {
		this.candidateInterviewInfoList = candidateInterviewInfoList;
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

	public Long getDepartmentId() {
		return departmentId;
	}

	public String getDesignationName() {
		return designationName;
	}

	public String getEmployementStatus() {
		return employementStatus;
	}

	public BigDecimal getYearOfExperience() {
		return yearOfExperience;
	}

	public String getReferencePersonName() {
		return referencePersonName;
	}

	public String getHighestDegreeAndAverageGrade() {
		return this.highestDegree + " " + this.averageGrade;
	}

	public String getPreviousInterviewername() {
		return previousInterviewername;
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public String getRoundName() {
		return roundName;
	}

	public String getResumeUrl() {
		return resumeUrl;
	}

	public Map<String, String> getFeedback() {
		return feedback;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Long getInterviewId() {
		return interviewId;
	}

	public void setInterviewId(Long interviewId) {
		this.interviewId = interviewId;
	}

	public List<String> getOthers() {
		return others;
	}

	public void setOthers(List<String> others) {
		this.others = others;
	}

}
