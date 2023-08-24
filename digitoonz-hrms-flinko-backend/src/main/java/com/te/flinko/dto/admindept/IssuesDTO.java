package com.te.flinko.dto.admindept;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.flinko.dto.helpandsupport.mongo.TicketHistroy;
import com.te.flinko.dto.helpandsupport.mongo.TicketHistroyDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(value = Include.NON_DEFAULT)

public class IssuesDTO implements Serializable {

	private String description;
	private String resolvedBy;
	private String feedback;
	private Integer rating;
	private List<TicketHistroyDto> ticketHistroys;
}
