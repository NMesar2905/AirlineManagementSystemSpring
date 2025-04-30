package com.api.AirlineManagementSystemSpring.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.AirlineManagementSystemSpring.dto.CancelReservationDTO;
import com.api.AirlineManagementSystemSpring.entities.Cancelation;
import com.api.AirlineManagementSystemSpring.entities.Reservation;
import com.api.AirlineManagementSystemSpring.exceptions.ResourceNotFoundException;
import com.api.AirlineManagementSystemSpring.repository.CancelRepository;
import com.api.AirlineManagementSystemSpring.repository.ReservationRepository;

@Service
public class CancelService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private CancelRepository cancelRepository;

	public Cancelation cancelReservation(CancelReservationDTO cancelReservationDTO) {

		Reservation reservation = reservationRepository.findById(cancelReservationDTO.pnr())
				.orElseThrow(() -> new ResourceNotFoundException("Please enter correct PNR"));

		Cancelation cancelation = new Cancelation(reservation.getPNR(), reservation.getName(),
				reservation.getFlightcode(), reservation.getDdate());

		reservationRepository.delete(reservation);

		return cancelRepository.save(cancelation);
	}

}
