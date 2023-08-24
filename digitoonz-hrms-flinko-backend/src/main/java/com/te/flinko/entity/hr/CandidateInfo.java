package com.te.flinko.entity.hr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.flinko.audit.Audit;
import com.te.flinko.entity.admin.CompanyInfo;
import com.te.flinko.entity.employee.EmployeePersonalInfo;
import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_candidate_info")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "candidateId")
public class CandidateInfo extends Audit implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ci_candidate_id", unique = true, nullable = false, precision = 19)
	private Long candidateId;
	
	@Column(name = "ci_first_name", nullable = false, length = 50)
	private String firstName;
	
	@Column(name = "ci_last_name", nullable = false, length = 25)
	private String lastName;
	
	@Column(name = "ci_email_id", unique = true, nullable = false, length = 100)
	private String emailId;
	
	@Column(name = "ci_mobile_number", unique = true, nullable = false, precision = 19)
	private Long mobileNumber;
	
	@Column(name = "ci_department_id", precision = 19)
	private Long departmentId;
	
	@Column(name = "ci_designation_name", length = 50)
	private String designationName;
	
	@Column(name = "ci_employement_status", length = 20)
	private String employementStatus;
	
	@Column(name = "ci_year_of_experience", precision = 2, scale = 2)
	private BigDecimal yearOfExperience;
	
	@Column(name = "ci_highest_degree", length = 50)
	private String highestDegree;
	
	@Column(name = "ci_is_selected", precision = 3)
	private Boolean isSelected;
	
	@Column(name = "ci_course", length = 50)
	private String course;
	
	@Column(name = "ci_institute_name", length = 50)
	private String instituteName;
	
	@Column(name = "ci_average_grade", length = 10)
	private String averageGrade;
	
	@Column(name = "ci_resume_url", length = 100)
	private String resumeUrl;
	
	@Column(name = "ci_is_document_verified", precision = 3)
	private Boolean isDocumentVerified;
	
	@Column(name = "ci_others", length = 999)
	@Convert(converter = ListToStringConverter.class)
	private List<String> others;
	
	@ManyToOne
	@JoinColumn(name = "ci_company_id")
	private CompanyInfo companyInfo;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "candidateInfo")
	private List<CandidateInterviewInfo> candidateInterviewInfoList;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "candidateInfo")
	private List<EmployeePersonalInfo> employeePersonalInfoList;

}
