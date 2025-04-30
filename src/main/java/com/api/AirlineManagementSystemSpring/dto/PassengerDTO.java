package com.api.AirlineManagementSystemSpring.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PassengerDTO(
		String name, 
		String nationality, 
		String phone, 
		String address, 
		Integer aadhar,
		String gender) {
}
