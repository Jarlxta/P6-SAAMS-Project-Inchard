package Tests;

import dev.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.*;

class EmbarksAircraft {

    GateInfoDatabase gid;
    Gate[] gates = new Gate[3];
    AircraftManagementDatabase amd;
    ManagementRecord[] MRs = new ManagementRecord[10];
    Itinerary itinerary;
    PassengerList passengerList1;
    PassengerList passengerList2;
    PassengerList passengerList3;

    @BeforeEach
    void setUp() {

        for (int i = 0; i < MRs.length; i++) {
            MRs[i] = new ManagementRecord();
        }
        itinerary = new Itinerary("Canada", "Tokyo", "Stirling");
        List<PassengerDetails> passengers = new ArrayList<>();
        List<PassengerDetails> passengers2 = new ArrayList<>();
        List<PassengerDetails> passengers3 = new ArrayList<>();
        passengerList1 = new PassengerList(passengers);
        passengerList2 = new PassengerList(passengers2);
        passengerList3 = new PassengerList(passengers3);
        FlightDescriptor flightDescriptor1 = new FlightDescriptor("FK1332", itinerary, passengerList1);
        FlightDescriptor flightDescriptor2 = new FlightDescriptor("FK1332", itinerary, passengerList2);
        FlightDescriptor flightDescriptor3 = new FlightDescriptor("FK1332", itinerary, passengerList3);

        amd = new AircraftManagementDatabase(MRs);

        amd.radarTestingDetect(5, flightDescriptor1);
        amd.setTestingStatus(5, ManagementRecord.WANTING_TO_LAND);

        amd.radarTestingDetect(2, flightDescriptor2);
        amd.setTestingStatus(2, ManagementRecord.AWAITING_TAKEOFF);


        amd.radarTestingDetect(8, flightDescriptor3);
        amd.setTestingStatus(8, ManagementRecord.READY_PASSENGERS);


//        amd.setTestingStatus(5, 6);
//        amd.setTestingStatus(2, 12);
//        amd.setTestingStatus(8, 3);

        for (int i = 0; i < 3; i++) {
            gates[i] = new Gate();
        }
        System.out.println(amd.getPassengerList(5).getListLength());
        gid = new GateInfoDatabase(gates);
        gid.setStatusDebug(0, 0);
        gid.setStatusDebug(1, 1);
        gid.setStatusDebug(2, 2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void receivesAircraft() {

        assertNotEquals(Gate.OCCUPIED, gid.getStatus(0));
        assertEquals(0,amd.getPassengerList(5).getListLength());
        amd.addPassenger(5, new PassengerDetails("Joe Dalton"));
        amd.addPassenger(5, new PassengerDetails("Jack Dalton"));
        amd.addPassenger(5, new PassengerDetails("William Dalton"));
        amd.addPassenger(5, new PassengerDetails("Averell Dalton"));
        assertEquals(4, amd.getPassengerList(5).getListLength());

        assertNotEquals(gid.getStatus(1), Gate.OCCUPIED);
        assertEquals(0,amd.getPassengerList(2).getListLength());
        amd.addPassenger(2, new PassengerDetails("Joe Dalton"));
        amd.addPassenger(2, new PassengerDetails("Jack Dalton"));
        amd.addPassenger(2, new PassengerDetails("William Dalton"));
        amd.addPassenger(2, new PassengerDetails("Averell Dalton"));
        assertNotEquals(4, amd.getPassengerList(2).getListLength());

        assertEquals(gid.getStatus(2), Gate.OCCUPIED);
        assertEquals(0,amd.getPassengerList(8).getListLength());
        amd.addPassenger(8, new PassengerDetails("Joe Dalton"));
        amd.addPassenger(8, new PassengerDetails("Jack Dalton"));
        amd.addPassenger(8, new PassengerDetails("William Dalton"));
        amd.addPassenger(8, new PassengerDetails("Averell Dalton"));
        assertEquals(4, amd.getPassengerList(8).getListLength());

        System.out.println("Passengers boarded...");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        amd.setStatus(5,ManagementRecord.READY_DEPART);
        assertNotEquals(ManagementRecord.READY_DEPART,amd.getStatus(5));

        amd.setStatus(2,ManagementRecord.READY_DEPART);
        assertNotEquals(ManagementRecord.READY_DEPART,amd.getStatus(2));

        assertNotEquals(ManagementRecord.UNLOADING, amd.getStatus(8));
        assertNotEquals(0, amd.getPassengerList(8).getListLength());
    }
}
