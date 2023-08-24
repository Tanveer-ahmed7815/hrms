package com.te.flinko.dto.admin;

import static com.te.flinko.common.admin.LevelsOfApprovalConstants.ADVANCE_SALARY_APPROVAL_NOT_EMPTY;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.ADVANCE_SALARY_APPROVAL_NOT_NULL_OR_BLANK;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.LEAVE_APPROVAL_NOT_EMPTY;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.LEAVE_APPROVAL_NOT_NULL_OR_BLANK;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.REIMBURSEMENT_APPROVAL_NOT_EMPTY;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.REIMBURSEMENT_APPROVAL_NOT_NULL_OR_BLANK;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.TIME_SHEET_APPROVAL_NOT_EMPTY;
import static com.te.flinko.common.admin.LevelsOfApprovalConstants.TIME_SHEET_APPROVAL_NOT_NULL_OR_BLANK;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.te.flinko.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelsOfApprovalDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long levelId;

	@NotEmpty(message = LEAVE_APPROVAL_NOT_EMPTY)
	@Convert(converter = ListToStringConverter.class)
	private List<@NotBlank(message = LEAVE_APPROVAL_NOT_NULL_OR_BLANK) String> leave;

	@NotEmpty(message = TIME_SHEET_APPROVAL_NOT_EMPTY)
	@Convert(converter = ListToStringConverter.class)
	private List<@NotBlank(message = TIME_SHEET_APPROVAL_NOT_NULL_OR_BLANK) String> timeSheet;

	@NotEmpty(message = ADVANCE_SALARY_APPROVAL_NOT_EMPTY)
	@Convert(converter = ListToStringConverter.class)
	private List<@NotBlank(message = ADVANCE_SALARY_APPROVAL_NOT_NULL_OR_BLANK) String> advanceSalary;

	@NotEmpty(message = REIMBURSEMENT_APPROVAL_NOT_EMPTY)
	@Convert(converter = ListToStringConverter.class)
	private List<@NotBlank(message = REIMBURSEMENT_APPROVAL_NOT_NULL_OR_BLANK) String> reimbursement;
	
	private Boolean isSubmited;
}
