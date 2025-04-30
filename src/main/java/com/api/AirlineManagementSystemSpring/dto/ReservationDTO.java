package com.api.AirlineManagementSystemSpring.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationDTO(
		
		@NotNull (message = "The 'Aadhar' field must be filled in")
		Integer aadhar, 
		
		@NotBlank (message = "The 'Source' field must be filled in")
		String src, 
		
		@NotBlank (message = "The 'Destination' field must be filled in")
		String des, 
		
		@NotNull (message = "The 'Date' field must be filled in")
		Date ddate) {
}
