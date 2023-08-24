package com.te.flinko.dto.hr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventManagementAddEventDTO {
	
	
	private Long eventId;
	
	private LocalDate eventDate;
	
	private LocalTime startTime;
	
	private LocalTime endTime;
	
	private List<String> winners;
	
	private String location;
	
	private String photoUrl;
	
	private String eventTitle;
	
	private String eventDescription;
	
	private List<String> employees;
	
	private List<String> departments;
	
	private Boolean isMailRequired;
	
	private BigDecimal eventVisibleDuration;
	
	
	
	
	
	
}
	