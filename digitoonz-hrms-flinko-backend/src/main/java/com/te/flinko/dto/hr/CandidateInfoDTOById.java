package com.te.flinko.dto.hr;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateInfoDTOById {
	private String firstName;
	private String lastName;
	private String emailId;
	private Long mobileNumber;
	private Long departmentId;
	private String department;
	private Long designationId;
	private String designationName;
	private String employementStatus;
	private BigDecimal yearOfExperience;
	private String employeeId;
	private String employeeName;
	private String highestDegree;
	private String course;
	private String instituteName;
	private String averageGrade;
	private String resumeUrl;
	private List<String> others;
	
}
