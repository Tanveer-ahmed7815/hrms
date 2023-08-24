package com.te.flinko.dto.hr;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.entity.hr.CandidateInterviewInfo;

public class RejectedCandidatedetailsDTO {
	private Long candidateId;
	private String firstName;
	private String lastName;
	private String fullName;
	private String emailId;
	private Long mobileNumber;
	private String department;
	private String designationName;
	private String employementStatus;
	private BigDecimal yearOfExperience;
	private String referencePersonName;
	private String highestDegree;
	private String averageGrade;
	private String highestDegreeAndAverageGrade;
	private int roundNumber;
	private Map<String, String> feedback;
	private String resumeUrl;
	private List<CandidateInterviewInfo> candidateInterviewInfoList;
	private List<String> others;
	
	@Override
	public String toString() {
		return "RejectedCandidatedetails [candidateId= "+ candidateId +" fullName=" + fullName + ", emailId=" + emailId + ", mobileNumber="
				+ mobileNumber + ", department=" + department + ", designationName=" + designationName
				+ ", employementStatus=" + employementStatus + ", yearOfExperience=" + yearOfExperience
				+ ", referencePersonName=" + referencePersonName + ", highestDegreeAndAverageGrade="
				+ highestDegreeAndAverageGrade + ", roundNumber=" + roundNumber + ", feedback=" + feedback
				+ ", resumeUrl=" + resumeUrl + ", candidateInterviewInfoList=" + candidateInterviewInfoList + "]";
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
	public void setDepartment(String department) {
		this.department = department;
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
	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}
	public void setFeedback(Map<String, String> feedback) {
		this.feedback = feedback;
	}
	public void setResumeUrl(String resumeUrl) {
		this.resumeUrl = resumeUrl;
	}
	public void setCandidateInterviewInfoList(List<CandidateInterviewInfo> candidateInterviewInfoList) {
		this.candidateInterviewInfoList = candidateInterviewInfoList;
	}
	public String getFullName() {
		return this.firstName+" "+this.lastName;
	}
	public String getEmailId() {
		return emailId;
	}
	public Long getMobileNumber() {
		return mobileNumber;
	}
	public String getDepartment() {
		return department;
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
		return this.highestDegree+" "+this.averageGrade;
	}
	
	public int getRoundNumber() {
		return roundNumber;
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
	public List<String> getOthers() {
		return others;
	}
	public void setOthers(List<String> others) {
		this.others = others;
	}
	
}
