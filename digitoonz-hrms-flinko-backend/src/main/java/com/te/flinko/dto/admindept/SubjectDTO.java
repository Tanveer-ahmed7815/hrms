package com.te.flinko.dto.admindept;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(value =  Include.NON_DEFAULT)
public class SubjectDTO implements Serializable {
	private Long salesOrderId;
	private Long subjectId;
	private String subject;
	private Long purchaseOrderId;
}
