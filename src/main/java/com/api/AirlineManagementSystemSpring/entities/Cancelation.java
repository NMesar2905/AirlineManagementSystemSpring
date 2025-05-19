package com.api.AirlineManagementSystemSpring.entities;

import java.sql.Date;
import java.util.Random;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cancel")
@Entity(name = "Cancelation")
@Getter
@NoArgsConstructor

@EqualsAndHashCode(of = "cancelno")
public class Cancelation {

	private String pnr;
	private String name;
	@Id
	private String cancelno;
	private String fcode;
	private Date date;

	public Cancelation(String PNR, String name, String fcode, Date ddate) {

		Random random = new Random();

		this.pnr = PNR;
		this.name = name;
		this.cancelno = Integer.toString(random.nextInt(1000000));
		this.fcode = fcode;
		this.date = ddate;

	}
}
