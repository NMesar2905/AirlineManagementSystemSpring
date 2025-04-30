package com.api.AirlineManagementSystemSpring.entities;

import com.api.AirlineManagementSystemSpring.dto.PassengerDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "passenger")
@Entity(name = "Passenger")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "aadhar")
public class Passenger {

	private String name;
	private String nationality;
	private String phone;
	private String address;
	@Id
	private Integer aadhar;
	private String gender;

	public Passenger(PassengerDTO passengerDTO) {
		this.name = passengerDTO.name();
		this.nationality = passengerDTO.nationality();
		this.phone = passengerDTO.phone();
		this.address = passengerDTO.address();
		this.aadhar = passengerDTO.aadhar();
		this.gender = passengerDTO.gender();
	}

}
