package com.te.flinko.common.hr;

import java.math.BigDecimal;

/**
 *  	
 * @author Ravindra
 *
 */
public class HrConstants {
	private HrConstants() {
		throw new IllegalStateException("utility class");
	}
	public static final String COMPANY_INFORMATION_FETCHED_SUCCESSFULLY = "Company information fetched successfully";
	public static final String INTERVIEW_DETAILS_ADDED_SUCCESSFULLY = "Interview details added successfully";
	public static final String CANDIDATE_INFORMATION_ADDED_SUCCESSFULLY = "Candidate information added successfully";
	public static final String CANDIDATE_INFORMATION_UPDATED_SUCCESSFULLY ="Candidate information updated successfully";
	public static final String CANDIDATE_RECORD_FETCH_SUCCESSFULLY = "Candidate record Fetched successfully";
	public static final String CANDIDATE_INTERVIEW_DETAILS_INSERT_SUCCESSFULLY = "Candidate interview details added successfully";
	public static final String COMPANY_INFORMATION_NOT_PRESENT = "Company information not present";
	public static final String EXCEPTION = "Exception";
	public static final String DUPLICATE_ENTRY_FOUND = "Duplicate  Record found";
	public static final String TRY_TO_FIND_INFORMATION_WHICH_IS_NOT_PRESENT = "Try to Fetch information which is not present ";
	public static final String CANDIDATE_INFORMATION_SUCCESSFULLY_DELETED = "Candidate information successfully deleted";
	public static final String REJECTED_CANDIDATE_RECORDS_FETCH_SUCCESSFULLY = "Rejected candidate records Fetched successfully";
	public static final String FEEDBACK_FACTOR_ADDED_SUCCESSFULLY = "Feedback factor added successfully";
	public static final String FEEDBACK_FACTOR_ADDED_EXCEPT_DUPLICATES = "Feedback factor added, Duplicate factors not added";
	public static final String CHECKLIST_FACTOR_ADDED_SUCCESSFULLY = "Checklist factor added successfully";
	public static final String INTERVIEWER_RECORD_NOT_FOUND_IN_DATABASE = "Interviewer record not found";
	public static final String OVERALL_FEEDBACK = "overall feedback";
	public static final String SELECTED = "selected";
	public static final String REJECTED = "rejected";
	public static final String RECORD_DOES_NOT_EXISTS_IN_REFERENCE_TABLE = "Record does not exist in reference table";
	public static final String CANDIDATE_RECORD_NOT_FOUND = "Candidate record not found";
	public static final String CHECKLISTFACTOR_FACTOR_ADDED_EXCEPT_DUPLICATES = "Checklist Feedback factor added, Duplicate factors not added";
	public static final String INTERVIEW_ROUND_DELETE_SUCCESSFULLY = "Interview round deleted successfully";
	public static final String FEEDBACK_FACTOR_DELETED_SUCCESSFULLY="Feedback deleted successfully";
	public static final String INTERVIEW_DETAILS_UPDATED_SUCCESSFULLY = "Interview round updated successfully";
	public static final String FEEDBACK_FACTOR_UPDATE_SUCCESSFULLY = "Feedback factor updated successfully";
	public static final String CHECKLIST_FACTOR_UPDATE_SUCCESSFULLY_EXCEPT_DUPLICATES = "Checklist factor updated successfully, Duplicate Checklist factors not added";
	public static final String FEEDBACK_FACTOR_UPDATE_SUCCESSFULLY_EXCEPT_DUPLICATES = "Feedback factor updated successfully, Duplicate factors not added";
	public static final String INTERVIEW_ROUND_ADDED_SUCCESSFULLY = "Interview round added successfully";
	public static final String INTERVIEW_ROUND_INFORMATION_NOT_FOUND = "Interview round information not found";
	public static final String CHECKLIST_FACTOR_UPDATE_SUCCESSFULLY = "Checklist factor updated successfully";
	public static final String CHECKLIST_FACTOR_DELETE_SUCCESSFULLY = "Checklist factor deleted successfully";
	public static final String CANDIDATE_INTERVIEW_FEEDBACK_UPDATE_SUCCESSFULLY = "Candidate interview feedback updated successfully";
	public static final String PERCENTAGE = "percentage";
	public static final String FIXED = "fixed";
	public static final String STATUS_APPROVED = "Approved";
	public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
	public static final BigDecimal ZERO = new BigDecimal(0);
	public static final String CHECKLIST_FACTOR_NOT_FOUND = "Checklist factor not found";
	public static final String FACTOR_ALREADY_EXISTS = "The factor that you are trying to add already exist in the List";
	public static final String SALARY_DETAILS_FETCHED_SUCCESSFULLY = "Salary details Fetched Successfully";
	public static final String EMPLOYEES_FETCHED_SUCCESSFULLY = "Employee Details Fetched Successfully";
	public static final String REIMBURSEMENT = "Reimbursement - ";
	public static final String REWARDS = "Referal Rewards";
	public static final String REIMBURSEMENT_PATTERN = "Reimbursement - .*";
	public static final String SALARY_CALC_SUCCESS = "Salary calculated successfully";
	public static final String SALARY_UPDATE_SUCCESS = "Salary details updated successfully";
	public static final String SALARY_CALC_FAIL = "No salary details found in company records";
	public static final String SALARY_RECORDS_NOT_FOUND = "No salary records found for given month";
	public static final String COMPANY_PAYROLL_INFORMATION_NOT_PRESENT = "Company payroll information not found";
	public static final String EMPLOYEE_SALARY_RECORD_NOT_FOUND="Employee Salary record not found";
	public static final String EMPLOYEE_REVISE_SALARY_SUCCESSFULLY_UPDATED="Employee revise salary and Annual salary successfully updated..!";
	public static final String CANDIDATE_INTERVIEW_FEEDBACK_DETAILS_FETCHED_SUCCESSFULLY=" Candidate interview feedback details fetch successfully";

}
