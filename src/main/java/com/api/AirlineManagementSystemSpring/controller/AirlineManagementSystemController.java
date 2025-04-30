package com.api.AirlineManagementSystemSpring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.AirlineManagementSystemSpring.dto.FlightListInfo;
import com.api.AirlineManagementSystemSpring.dto.CancelReservationDTO;
import com.api.AirlineManagementSystemSpring.dto.PassengerDTO;
import com.api.AirlineManagementSystemSpring.dto.ReservationDTO;
import com.api.AirlineManagementSystemSpring.entities.Cancelation;
import com.api.AirlineManagementSystemSpring.entities.Passenger;
import com.api.AirlineManagementSystemSpring.entities.Reservation;
import com.api.AirlineManagementSystemSpring.exceptions.ResourceNotFoundException;
import com.api.AirlineManagementSystemSpring.repository.FlightRepository;
import com.api.AirlineManagementSystemSpring.repository.PassengerRepository;
import com.api.AirlineManagementSystemSpring.repository.ReservationRepository;
import com.api.AirlineManagementSystemSpring.services.CancelService;
import com.api.AirlineManagementSystemSpring.services.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ams")
public class AirlineManagementSystemController {

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private PassengerRepository passengerRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private CancelService cancelService;

	@Operation(summary = "Obtener la lista de vuelos disponibles", description = "Devuelve una lista de vuelos con información básica.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista de vuelos obtenida exitosamente"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@GetMapping("/flights")
	public List<FlightListInfo> getFlightList() {
		return flightRepository.findAll().stream().map(FlightListInfo::new).toList();
	}

	@Operation(summary = "Crear un nuevo pasajero", description = "Crea un nuevo pasajero si el Aadhar ID no existe en la base de datos.")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Pasajero creado exitosamente"),
			@ApiResponse(responseCode = "409", description = "Pasajero con el mismo Aadhar ID ya existe") })
	@PostMapping("/passenger/creation")
	public ResponseEntity<?> createPassenger(@RequestBody PassengerDTO passengerDTO) {

		if (passengerDTO.aadhar() == null || passengerDTO.name() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("The Aadhar or Name fields cannot be null or empty");
		} else if (passengerRepository.existsById(passengerDTO.aadhar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Passenger with Aadhar " + passengerDTO.aadhar() + " already exists.");
		} else {
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/plain");
			headers.add("Charset", "UTF-8");
			
			passengerRepository.save(new Passenger(passengerDTO));
			return ResponseEntity.status(HttpStatus.CREATED)
					.headers(headers)
					.body("Customer Details Added Succesfully");
		}

	}

	@Operation(summary = "Crear una nueva reservación de vuelo", description = "Permite al pasajero reservar un vuelo.")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Reservación creada exitosamente"),
			@ApiResponse(responseCode = "409", description = "Conflicto al crear la reservación") })
	@PostMapping("/passenger/reservation")
	public ResponseEntity<?> createReservation(@RequestBody @Valid ReservationDTO reservationDTO) {
		try {
			Reservation reservation = reservationService.createReservation(reservationDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}

	}

	@Operation(summary = "Obtener detalles de viaje", description = "Devuelve información detallada del viaje usando un PNR proporcionado.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Detalles del viaje obtenidos exitosamente"),
			@ApiResponse(responseCode = "404", description = "No se encontró la información del viaje") })
	@GetMapping("/passenger/journey-details")
	public ResponseEntity<?> journeyDetails(@RequestParam String PNR) {
		if (PNR.isBlank()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The PNR cannot be empty");
		} else {
			try {
				Reservation reservation = reservationRepository.findById(PNR)
						.orElseThrow(() -> new ResourceNotFoundException("Not information found"));
				return ResponseEntity.ok(reservation);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
			}
		}
	}

	@Operation(summary = "Cancelar una reservación", description = "Permite cancelar una reservación existente usando el PNR.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Reservación cancelada exitosamente"),
			@ApiResponse(responseCode = "409", description = "No se pudo cancelar la reservación") })
	@DeleteMapping("/passenger/cancel")
	public ResponseEntity<?> cancelReservation(@RequestBody CancelReservationDTO cancelReservationDTO) {
		if (cancelReservationDTO.pnr().isBlank()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PNR cannot be empty");
		} else {

			try {
				Cancelation cancelation = cancelService.cancelReservation(cancelReservationDTO);
				return ResponseEntity.ok(cancelation);
			} catch (ResourceNotFoundException e) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
			}
		}
	}

}
