package com.api.AirlineManagementSystemSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.AirlineManagementSystemSpring.entities.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
}
