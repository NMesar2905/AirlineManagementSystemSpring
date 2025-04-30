package com.api.AirlineManagementSystemSpring.dto;

import com.api.AirlineManagementSystemSpring.entities.Flight;

public record FlightListInfo(String f_code, String f_name, String source, String destination) {

	public FlightListInfo(Flight flight) {
		this(flight.getF_code(), flight.getF_name(), flight.getSource(), flight.getDestination());
	}

}
