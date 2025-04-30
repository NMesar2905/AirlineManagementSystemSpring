package com.api.AirlineManagementSystemSpring.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "flight")
@Entity(name = "Flight")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "f_code")
public class Flight {

	@Id
	private String f_code;
	private String f_name;
	private String source;
	private String destination;

}
