package com.te.flinko.dto.admindept;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author Manish kumar
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySoftwareDetailsDto implements Serializable {
	private static final long serialVersionUID = 1L;
	

	private Long softwareId;
	private String softwareName;
	private LocalDate expirationDate;
	private BigDecimal amount;

}
