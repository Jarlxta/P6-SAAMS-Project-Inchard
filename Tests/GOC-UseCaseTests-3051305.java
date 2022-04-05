package Tests;

import dev.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A set of JUnit tests to validate the functionality of the GateInfoDatabase.
 * Specifically, these tests will deal with the use cases of the GOC, which accesses and observes the GateInfoDatabase.
 * The UML diagram being used as reference is the GroundOperCUC diagram; there are a number of smaller use cases
 * within this diagram contained inside the main use case, but for ones that interact with the GateInfoDatabase,
 * there are only two, one in each listed use case.
 *
 * Because of the nature of how inputs work for the interfaces, certain validation is done
 * outside the Gate Info Database before the Gate Info Database is called.
 * For that reason, replication of that validation done in the GOC will be done within some tests.
 *
 * It could be said that this validation is outside the scope of the functionality of the GateInfoDatabase; that is
 * to say, that we should not be including that validation here within these tests. However, I would disagree; the
 * GateInfoDatabase lacks any sort of association to the AircraftManagementDatabase, which it would need to validate
 * certain things like the status of ManagementRecords. As such, the validation is done through boundary classes.
 * The GateInfoDatabase does not see any use outside of these boundary classes, all interactions are done
 * through this validation, and it is difficult to directly call that functionality without the interfaces to
 * interact with, so that is why I am including the validation here.
 * */
class GOCTest {

    GateInfoDatabase GIDB;
    AircraftManagementDatabase AMDB;
    GOC goc;

    PassengerDetails Hunter;
    PassengerDetails Nick;
    PassengerDetails Cameron;
    PassengerDetails John;
    PassengerDetails Jane;
    PassengerDetails Joe;

    PassengerList pList1;
    PassengerList pList2;
    PassengerList pList3;

    FlightDescriptor RU23;
    FlightDescriptor DE44;
    FlightDescriptor UK96;
    FlightDescriptor FR67;
    FlightDescriptor US88;
    FlightDescriptor GR40;

    ManagementRecord FlightRU23;
    ManagementRecord FlightDE44;
    ManagementRecord FlightUK96;
    ManagementRecord FlightFR67;
    ManagementRecord FlightUS88;
    ManagementRecord FlightGR40;

    @BeforeEach
    void setUp() {
        GIDB = new GateInfoDatabase();
        AMDB = new AircraftManagementDatabase();
        goc = new GOC(AMDB,GIDB);

        Hunter = new PassengerDetails("Hunter Lowery");
        Nick = new PassengerDetails("Nikolaos Filippopoulos");
        Cameron = new PassengerDetails("Cameron Morrison");
        John = new PassengerDetails("John Doe");
        Jane = new PassengerDetails("Jane Doe");
        Joe = new PassengerDetails("Joe Bloggs");

        pList1 = new PassengerList();
        pList1.addPassenger(Hunter, 14);
        pList1.addPassenger(John, 14);

        pList2 = new PassengerList();
        pList2.addPassenger(Nick, 14);
        pList2.addPassenger(Jane, 14);

        pList3 = new PassengerList();
        pList3.addPassenger(Cameron, 14);
        pList3.addPassenger(Joe, 14);

        RU23 = new FlightDescriptor("RU23", new Itinerary("Moscow", "Washington DC", "Stirling"), pList1);
        DE44 = new FlightDescriptor("DE44", new Itinerary("Berlin", "Glasgow", "Stirling"), pList2);
        UK96 = new FlightDescriptor("UK96", new Itinerary("London", "Edinburgh", "Stirling"), pList3);
        FR67 = new FlightDescriptor("FR67", new Itinerary("Stirling", "Paris", "London"), new PassengerList());
        US88 = new FlightDescriptor("US88", new Itinerary("Stirling", "Chicago", "Boston"), new PassengerList());
        GR40 = new FlightDescriptor("GR40", new Itinerary("Stirling", "Athens", "Rome"), new PassengerList());

        AMDB.radarDetect(RU23);
        AMDB.radarDetect(DE44);
        AMDB.radarDetect(UK96);
        AMDB.radarDetect(FR67);
        AMDB.radarDetect(US88);
        AMDB.radarDetect(GR40);

        AMDB.findMrFromFlightCode(RU23.getFlightCode()).setStatusDebugOverride(5); // Landed - Normal
        AMDB.findMrFromFlightCode(DE44.getFlightCode()).setStatusDebugOverride(4); // Landing - Boundary
        AMDB.findMrFromFlightCode(UK96.getFlightCode()).setStatusDebugOverride(6); // Taxiing - Boundary

        AMDB.findMrFromFlightCode(GR40.getFlightCode()).setStatusDebugOverride(16); // Awaiting Taxi - Normal
        AMDB.findMrFromFlightCode(FR67.getFlightCode()).setStatusDebugOverride(17); // Awaiting Takeoff - Boundary
        AMDB.findMrFromFlightCode(US88.getFlightCode()).setStatusDebugOverride(15); // Ready Depart - Boundary


        FlightRU23 = AMDB.findMrFromFlightCode(RU23.getFlightCode());
        FlightDE44 = AMDB.findMrFromFlightCode(DE44.getFlightCode());
        FlightUK96 = AMDB.findMrFromFlightCode(UK96.getFlightCode());
        FlightFR67 = AMDB.findMrFromFlightCode(FR67.getFlightCode());
        FlightUS88 = AMDB.findMrFromFlightCode(US88.getFlightCode());
        FlightGR40 = AMDB.findMrFromFlightCode(GR40.getFlightCode());
    }

    @AfterEach
    void tearDown() {
    }

    // Normal Test - Testing the Use Case of Allocating a FREE gate to a LANDED aircraft.
    // Making sure the normal use case functions as intended, with a FREE gate and LANDED aircraft.
    @Test
    void normalGateReservation() {

        // From GOC class, Method actionPerformed(ActionEvent e), for the taxiToGate button
        if(AMDB.getStatus(AMDB.findMrIndex(RU23.getFlightCode())) == ManagementRecord.LANDED
                && GIDB.getStatus(1)== Gate.FREE) {
            GIDB.allocate(1, AMDB.findMrIndex(RU23.getFlightCode()));
        }

        assertEquals(1, GIDB.getStatus(1), "The FREE gate should now be RESERVED.");
    }

    // Boundary Test - Testing the Use case of Allocating a FREE gate to a LANDED aircraft
    // The aircraft status is valid; it is LANDED, but the gate status is not; it is already DOCKED.
    @Test
    void gateAlreadyReserved() {
        GIDB.allocate(1, AMDB.findMrIndex(UK96.getFlightCode())); // 1st gate starts RESERVED
        GIDB.docked(1); // and then DOCKED

        // From GOC class, Method actionPerformed(ActionEvent e), for the taxiToGate button
        if(AMDB.getStatus(AMDB.findMrIndex(RU23.getFlightCode())) == ManagementRecord.LANDED
                && GIDB.getStatus(1)== Gate.FREE) {
            GIDB.allocate(1, AMDB.findMrIndex(RU23.getFlightCode()));
        }

        assertEquals(2, GIDB.getStatus(1), "The DOCKED gate should remain DOCKED.");
    }

    // Boundary Test - Testing the Use case of Allocating a FREE gate to a LANDED aircraft
    // The aircraft status is invalid; it is LANDING, but the gate is FREE.
    @Test
    void invalidStatusReservationLanding() {

        // From GOC class, Method actionPerformed(ActionEvent e), for the taxiToGate button
        if(AMDB.getStatus(AMDB.findMrIndex(DE44.getFlightCode())) == ManagementRecord.LANDED
                && GIDB.getStatus(1)== Gate.FREE) {
            GIDB.allocate(1, AMDB.findMrIndex(DE44.getFlightCode()));
        }

        assertEquals(0, GIDB.getStatus(1), "The FREE gate should remain FREE");
    }

    // Boundary Test - Testing the Use case of Allocating a FREE gate to a LANDED aircraft
    // The aircraft status is invalid; it is TAXIING, but the gate is FREE.
    @Test
    void invalidStatusReservationTaxiing() {

        // From GOC class, Method actionPerformed(ActionEvent e), for the taxiToGate button
        if(AMDB.getStatus(AMDB.findMrIndex(UK96.getFlightCode())) == ManagementRecord.LANDED
                && GIDB.getStatus(1)== Gate.FREE) {
            GIDB.allocate(1, AMDB.findMrIndex(UK96.getFlightCode()));
        }

        assertEquals(0, GIDB.getStatus(1), "The FREE gate should remain FREE");
    }

    // Normal Test - Testing the Use case of FREEing a DOCKED gate when an aircraft is TAXIIED to the runway.
    // Making sure the use case functions as intended, with an aircraft awaiting takeoff at a docked gate.
    @Test
    void normalGateFreeing() {

        GIDB.allocate(0, AMDB.findMrIndex(GR40.getFlightCode()));
        GIDB.docked(0);

        // From GOC class, Method actionPerformed(ActionEvent e), for the grantTaxiRunwayClearance button
        if(AMDB.getStatus(AMDB.findMrIndex(GR40.getFlightCode())) == ManagementRecord.AWAITING_TAXI) {
            // Normally this would check the gate associated with GR40 (or whatever flight is departing), but
            // these tests are to test the functionality of the gate info database, not that of the AMDatabase or the
            // management records. We know the gate we're working with within this test.
            GIDB.departed(0);
        }

        assertEquals(0, GIDB.getStatus(0), "The DOCKED gate should now be FREE");
    }

    // Boundary Test - Testing the Use case of FREEing a DOCKED gate when an aircraft is TAXIIED to the runway.
    // The aircraft will be the valid status, but the gate will not be DOCKED; it will be RESERVED for landing.
    @Test
    void gateNotDocked() {
        GIDB.allocate(0, AMDB.findMrIndex(GR40.getFlightCode()));

        // From GOC class, Method actionPerformed(ActionEvent e), for the grantTaxiRunwayClearance button
        if(AMDB.getStatus(AMDB.findMrIndex(GR40.getFlightCode())) == ManagementRecord.AWAITING_TAXI) {
            // Normally this would check the gate associated with GR40 (or whatever flight is departing), but
            // these tests are to test the functionality of the gate info database, not that of the AMDatabase or the
            // management records. We know the gate we're working with within this test.
            GIDB.departed(0);
        }

        assertEquals(1, GIDB.getStatus(0), "The RESERVED gate should remain RESERVED");
    }

    // Boundary Test - Testing the Use case of FREEing a DOCKED gate when an aircraft is TAXXIED to the runway.
    // The aircraft will have an invalid status; AWAITING_TAKEOFF, while the gate is DOCKED.
    @Test
    void invalidStatusUndockAwaitTakeoff() {
        GIDB.allocate(0, AMDB.findMrIndex(GR40.getFlightCode()));
        GIDB.docked(0);

        // From GOC class, Method actionPerformed(ActionEvent e), for the grantTaxiRunwayClearance button
        if(AMDB.getStatus(AMDB.findMrIndex(FR67.getFlightCode())) == ManagementRecord.AWAITING_TAXI) {
            // Normally this would check the gate associated with GR40 (or whatever flight is departing), but
            // these tests are to test the functionality of the gate info database, not that of the AMDatabase or the
            // management records. We know the gate we're working with within this test.
            GIDB.departed(0);
        }

        assertEquals(2, GIDB.getStatus(0), "The DOCKED gate should remain DOCKED");
    }

    // Boundary Test - Testing the Use case of FREEing a DOCKED gate when an aircraft is TAXXIED to the runway.
    // The aircraft will have an invalid status; READY_DEPART, while the gate is DOCKED.
    @Test
    void invalidStatusUndockReadyDepart() {
        GIDB.allocate(0, AMDB.findMrIndex(GR40.getFlightCode()));
        GIDB.docked(0);

        // From GOC class, Method actionPerformed(ActionEvent e), for the grantTaxiRunwayClearance button
        if(AMDB.getStatus(AMDB.findMrIndex(US88.getFlightCode())) == ManagementRecord.AWAITING_TAXI) {
            // Normally this would check the gate associated with GR40 (or whatever flight is departing), but
            // these tests are to test the functionality of the gate info database, not that of the AMDatabase or the
            // management records. We know the gate we're working with within this test.
            GIDB.departed(0);
        }

        assertEquals(2, GIDB.getStatus(0), "The DOCKED gate should remain DOCKED");
    }
}