package com.api.AirlineManagementSystemSpring.entities;

import java.sql.Date;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "reservation")
@Entity(name = "Reservation")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "PNR")
public class Reservation {

	@Id
	@JsonProperty("pnr")
	private String PNR;
	@JsonProperty("tic")
	private String TIC;
	private Integer aadhar;
	private String name;
	private String nationality;
	private String flightname;
	private String flightcode;
	private String src;
	private String des;
	private Date ddate;

	public Reservation(Integer aadhar, String name, String nationality, String flightname, String flightcode,
			String src, String des, Date ddate) {
		Random random = new Random();
		this.PNR = "PNR-" + random.nextInt(1000000);
		this.TIC = "TIC-" + random.nextInt(10000);
		this.aadhar = aadhar;
		this.name = name;
		this.nationality = nationality; 
		this.flightname = flightname;
		this.flightcode = flightcode;
		this.src = src;
		this.des = des;
		this.ddate = ddate;
	}

}
