package com.api.AirlineManagementSystemSpring;

import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
//import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.api.AirlineManagementSystemSpring.dto.CancelReservationDTO;
import com.api.AirlineManagementSystemSpring.dto.FlightListInfo;
import com.api.AirlineManagementSystemSpring.dto.PassengerDTO;
import com.api.AirlineManagementSystemSpring.dto.ReservationDTO;
import com.api.AirlineManagementSystemSpring.entities.Cancelation;
import com.api.AirlineManagementSystemSpring.entities.Passenger;
import com.api.AirlineManagementSystemSpring.entities.Reservation;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
class AirlineManagementSystemSpringApplicationTests {

	RequestSpecification req;

	String pnr;
	Integer aadhar;

	@BeforeMethod
	public void createRestSpecification() {
		req = new RequestSpecBuilder().setBaseUri("http://localhost:8080/ams").setContentType(ContentType.JSON).build();

		req = given().log().all().spec(req);
	}

	@Test(priority = 1)
	public void testListFlights() {
		Response response = req.when().get("/flights");
		validateResponse(response, 200);

		List<FlightListInfo> flightListResponse = response.body().jsonPath().getList("", FlightListInfo.class);

		Assert.assertNotNull(flightListResponse);
		Assert.assertFalse(flightListResponse.isEmpty());

		boolean flightListValidation = flightListResponse.stream().allMatch(
				flight -> flight.f_code() != null && !flight.f_code().isEmpty()
				&& flight.source() != null && !flight.source().isEmpty()
				&& flight.destination() != null && !flight.destination().isEmpty()
				&& flight.f_name() != null && !flight.f_name().isEmpty());
		
		Assert.assertTrue(flightListValidation);

	}

	@Test(priority = 1)
	public void testCreatePassenger_success() {
		aadhar = (int) (Math.random() * 1000000000);

		String passengerName = generateRandomPassengerName();

		Response response = req
				.body(new PassengerDTO(passengerName, "Colombian", "3173025735", "Calle 24", aadhar, "Male")).when()
				.post("/passenger/creation");
		validateResponse(response, 201);

		Assert.assertEquals(response.body().asString(), "Customer Details Added Succesfully");

	}

	@Test(priority = 1, dependsOnMethods = "testCreatePassenger_success")
	public void testCreatePassenger_AadharAlreadyExists() {
		String passengerName = generateRandomPassengerName();

		Response response = req
				.body(new PassengerDTO(passengerName, "Brasilian", "3103942940", "Calle 90", aadhar, "Male")).when()
				.post("/passenger/creation");

		validateResponse(response, 409);
		Assert.assertEquals(response.body().asString(), "Passenger with Aadhar " + aadhar + " already exists.");

	}

	@Test
	public void testCreatePassenger_AadharOrNameEmpty() {
		Response response = req.body(new PassengerDTO("", "Mexican", "3164627495", "Carrera 23", null, "Female")).when()
				.post("/passenger/creation");
		validateResponse(response, 409);

		Assert.assertEquals(response.body().asString(), "The Aadhar or Name fields cannot be null or empty");
	}

	@Test(priority = 2, dependsOnMethods = "testCreatePassenger_success")
	public void testCreateReservation_success() {

		LocalDate startDate = LocalDate.of(2025, 2, 1);
		LocalDate endDate = LocalDate.of(2025, 12, 31);

		Date randomSqlDate = generateRandomDateBetween(startDate, endDate);

		Response response = req.body(new ReservationDTO(aadhar, "Bogota", "Medellin", randomSqlDate)).when()
				.post("/passenger/reservation");
		validateResponse(response, 201);

		Reservation reservation = response.as(Reservation.class);

		pnr = reservation.getPNR();
		Assert.assertTrue(pnr != null);
		Assert.assertEquals(reservation.getAadhar(), aadhar);

	}

	@Test
	public void testCreateReservation_AadharNotExists() {
		LocalDate startDate = LocalDate.of(2025, 2, 1);
		LocalDate endDate = LocalDate.of(2025, 12, 31);

		int aadharNotExistent = 1017270450;

		Date randomSqlDate = generateRandomDateBetween(startDate, endDate);

		Response response = req.body(new ReservationDTO(aadharNotExistent, "Medellin", "Bogota", randomSqlDate)).when()
				.post("/passenger/reservation");
		validateResponse(response, 409);

		Assert.assertEquals(response.body().asString(), "Customer not Found");
	}

	@Test(priority = 2, dependsOnMethods = "testCreatePassenger_success")
	public void testCreateReservation_IncorrectSourceOrDestination() {
		LocalDate startDate = LocalDate.of(2025, 2, 1);
		LocalDate endDate = LocalDate.of(2025, 12, 31);

		Date randomSqlDate = generateRandomDateBetween(startDate, endDate);

		String source = "Cartagena";
		String destination = "Medellin";

		Response response = req.body(new ReservationDTO(aadhar, source, destination, randomSqlDate)).when()
				.post("/passenger/reservation");
		validateResponse(response, 409);

		Assert.assertEquals(response.body().asString(), "Flight not found from " + source + " to " + destination);
	}

	@Test
	public void testCreateReservation_SourceOrDestinationOrAadharOrDateEmpty() {
		Response response = req.body(new ReservationDTO(null, "", "", null)).when().post("/passenger/reservation");
		validateResponse(response, 400);

		Map<String, String> errors = response.as(HashMap.class);
		Assert.assertTrue(errors.containsKey("aadhar"));
		Assert.assertTrue(errors.containsKey("ddate"));
		Assert.assertTrue(errors.containsKey("des"));
		Assert.assertTrue(errors.containsKey("src"));

		Assert.assertEquals(errors.get("aadhar"), "The 'Aadhar' field must be filled in");
		Assert.assertEquals(errors.get("ddate"), "The 'Date' field must be filled in");
		Assert.assertEquals(errors.get("des"), "The 'Destination' field must be filled in");
		Assert.assertEquals(errors.get("src"), "The 'Source' field must be filled in");
	}

	@Test(priority = 3, dependsOnMethods = "testCreateReservation_success")
	public void testGetJourneyDetails_success() {
		Response response = req.queryParam("PNR", pnr).when().get("/passenger/journey-details");
		validateResponse(response, 200);

		Reservation reservation = response.as(Reservation.class);

		Assert.assertEquals(reservation.getPNR(), pnr);
		Assert.assertEquals(reservation.getAadhar(), aadhar);

	}

	@Test
	public void testGetJourneyDetails_IncorrectPNR() {
		Response response = req.queryParam("PNR", "PNR-5666").when().get("/passenger/journey-details");
		validateResponse(response, 404);

		Assert.assertEquals(response.body().asString(), "Not information found");
	}

	@Test
	public void testGetJourneyDetails_PNREmpty() {
		Response response = req.queryParam("PNR", "").when().get("/passenger/journey-details");
		validateResponse(response, 400);

		Assert.assertEquals(response.body().asString(), "The PNR cannot be empty");
	}

	@Test(priority = 4, dependsOnMethods = "testCreateReservation_success")
	public void testCancelReservation_success() {

		Response response = req.body(new CancelReservationDTO(pnr)).when().delete("/passenger/cancel");
		validateResponse(response, 200);
		
		Cancelation cancelation = response.as(Cancelation.class);
		
		Assert.assertEquals(cancelation.getPnr(), pnr);
		Assert.assertNotNull(cancelation.getCancelno());
		Assert.assertNotNull(cancelation.getFcode());
		Assert.assertNotNull(cancelation.getDate());

	}

	@Test
	public void testCancelReservation_IncorrectPNR() {
		Response response = req.body(new CancelReservationDTO("PNR-116128")).when().delete("/passenger/cancel");
		validateResponse(response, 409);

		Assert.assertEquals(response.body().asString(), "Please enter correct PNR");
	}

	@Test
	public void testCancelReservation_PNREmpty() {
		Response response = req.body(new CancelReservationDTO("")).when().delete("/passenger/cancel");
		validateResponse(response, 400);

		Assert.assertEquals(response.body().asString(), "PNR cannot be empty");
	}

	private void validateResponse(Response response, int expectedStatusCode) {
		response.then().assertThat().statusCode(expectedStatusCode);
	}

	private Date generateRandomDateBetween(LocalDate startDate, LocalDate endDate) {
		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
		long randomDays = ThreadLocalRandom.current().nextLong(daysBetween + 1);

		return Date.valueOf(startDate.plusDays(randomDays));
	}

	private String generateRandomPassengerName() {
		String[] names = { "Nicolas", "Natalia", "Juan Jose", "Valentina", "Sofia", "Michael", "Dayana", "Ana",
				"Estefania", "Delia", "Juan Diego", "Jorge Ivan", "Andres", "Claudia", "Gabriela" };
		String[] lastNames = { "Mesa", "Ramirez", "Jaramillo", "De los Rios", "Araque", "Arango", "Munera", "Muñoz",
				"Contreras", "Gutierrez", "Vega", "Rendon", "Gonzales", "Contreras", "Montilla" };

		int randomNombre = ThreadLocalRandom.current().nextInt(names.length);
		int randomApellido = ThreadLocalRandom.current().nextInt(lastNames.length);

		return names[randomNombre] + " " + lastNames[randomApellido];
	}

}
