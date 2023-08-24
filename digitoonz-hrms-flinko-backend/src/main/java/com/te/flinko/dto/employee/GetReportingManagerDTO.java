package com.te.flinko.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReportingManagerDTO {

	private Long employeeInfoId;
	
	private String designation;
	
	private String name;
	
	private GetReportingManagerDTO managerOf;
	
	public void setManager(GetReportingManagerDTO employee) {
		if(this.managerOf==null) {
			this.managerOf = employee;
		}
		else {
			this.managerOf.setManager(employee);
		}
	}
}
