package com.te.flinko.dto.hr;

import lombok.Data;

@Data
public class BankInformationDTO {
	
	private Long bankId;
	private String bankName;
	private String bankBranch;
	private String accountType;
	private Long accountNumber;
	private String ifscCode;
	private String accountHolderName;
	
}
