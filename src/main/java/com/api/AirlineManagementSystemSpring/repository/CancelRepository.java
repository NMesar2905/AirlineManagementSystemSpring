package com.api.AirlineManagementSystemSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.AirlineManagementSystemSpring.entities.Cancelation;

public interface CancelRepository extends JpaRepository<Cancelation, String> {

}
