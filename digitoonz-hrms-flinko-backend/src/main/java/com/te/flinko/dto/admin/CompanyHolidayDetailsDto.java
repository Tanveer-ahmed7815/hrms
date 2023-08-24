package com.te.flinko.dto.admin;

import static com.te.flinko.common.admin.CompanyHolidayDetailsConstants.HOLIDAY_NAME_CAN_NOT_BE_NULL_OF_BLANK;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyHolidayDetailsDto implements Serializable {

	private Long holidayId;

	@NotBlank(message = HOLIDAY_NAME_CAN_NOT_BE_NULL_OF_BLANK)
	private String holidayName;

	@JsonFormat(shape = Shape.STRING, pattern = "MM-dd-yyyy")
	private LocalDate holidayDate;

	private Boolean optional;
	
	private Boolean isOptional;

}
