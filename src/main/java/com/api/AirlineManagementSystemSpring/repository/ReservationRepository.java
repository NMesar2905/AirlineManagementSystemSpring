package com.api.AirlineManagementSystemSpring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.AirlineManagementSystemSpring.dto.CancelReservationDTO;
import com.api.AirlineManagementSystemSpring.entities.Flight;
import com.api.AirlineManagementSystemSpring.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

	Optional<Reservation> findByPNR(String PNR);
}
