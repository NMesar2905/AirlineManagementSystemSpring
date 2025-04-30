package com.api.AirlineManagementSystemSpring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.AirlineManagementSystemSpring.entities.Flight;

public interface FlightRepository extends JpaRepository<Flight, String> {

	Optional<Flight> findBySourceAndDestination(String src, String des);

}
