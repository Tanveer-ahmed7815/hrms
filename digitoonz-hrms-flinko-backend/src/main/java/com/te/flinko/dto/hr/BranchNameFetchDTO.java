package com.te.flinko.dto.hr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchNameFetchDTO {
	private Long branchId;
	
	private String branchName;

}
