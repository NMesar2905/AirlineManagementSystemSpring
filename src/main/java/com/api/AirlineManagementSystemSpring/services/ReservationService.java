package com.api.AirlineManagementSystemSpring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.AirlineManagementSystemSpring.dto.ReservationDTO;
import com.api.AirlineManagementSystemSpring.entities.Flight;
import com.api.AirlineManagementSystemSpring.entities.Passenger;
import com.api.AirlineManagementSystemSpring.entities.Reservation;
import com.api.AirlineManagementSystemSpring.exceptions.ResourceNotFoundException;
import com.api.AirlineManagementSystemSpring.repository.FlightRepository;
import com.api.AirlineManagementSystemSpring.repository.PassengerRepository;
import com.api.AirlineManagementSystemSpring.repository.ReservationRepository;

@Service
public class ReservationService {

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private PassengerRepository passengerRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	public Reservation createReservation(ReservationDTO reservationDTO) {

		Passenger passenger = passengerRepository.findById(reservationDTO.aadhar())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not Found"));

		Flight flight = flightRepository.findBySourceAndDestination(reservationDTO.src(), reservationDTO.des())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Flight not found from " + reservationDTO.src() + " to " + reservationDTO.des()));

		Reservation reservation = new Reservation(passenger.getAadhar(), passenger.getName(),
				passenger.getNationality(), flight.getF_name(), flight.getF_code(), flight.getSource(),
				flight.getDestination(), reservationDTO.ddate());

		return reservationRepository.save(reservation); 

	}

}
