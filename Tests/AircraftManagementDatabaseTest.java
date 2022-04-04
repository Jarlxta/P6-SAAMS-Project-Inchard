package Tests;

import dev.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.StyleSheet;
import java.util.ArrayList;
import java.util.List;

class AircraftManagementDatabaseTest {


    AircraftManagementDatabase aircraftManagementDatabase;
    Itinerary itinerary;
    FlightDescriptor flightDescriptor;
    PassengerList passengerList;
    ManagementRecord[] MRs = new ManagementRecord[10];

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 10; i++) {
            MRs[i] = new ManagementRecord();
        }
        List<PassengerDetails> passengers = new ArrayList<>();
        aircraftManagementDatabase = new AircraftManagementDatabase(MRs);
        itinerary = new Itinerary("Canada","Tokyo","Stirling");
        passengers.add(new PassengerDetails("Hunter"));
        passengers.add(new PassengerDetails("Cameron"));
        passengers.add(new PassengerDetails("Nick"));
        passengerList = new PassengerList(passengers);
        flightDescriptor = new FlightDescriptor("FK1332",itinerary,passengerList);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getStatus() {
    }

    @Test
    void setStatus() {
    }

    @Test
    void getFlightCode() {
    }

    @Test
    void getWithStatus() {
    }

    @Test
    void radarDetect() {
    }

    @Test
    void radarLostContact() {
    }

    @Test
    void taxiTo() {
    }

    @Test
    void faultsFound() {
    }

    @Test
    void addPassenger() {
    }

    @Test
    void getPassengerList() {
    }

    @Test
    void getItinerary() {
    }

    @Test
    void findMrFromFlightCode() {
    }

    @Test
    void findMrIndex() {
    }

    @Test
    void getGate() {
    }

    @Test
    void getFaultDescription() {
    }
}