// SN : 2836012
package Tests;

import dev.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceivesAircraft {

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
        itinerary = new Itinerary("Canada","Tokyo","Stirling");
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
        amd.radarTestingDetect(5,flightDescriptor1);
        amd.setTestingStatus(5,ManagementRecord.WANTING_TO_LAND);
        amd.addPassenger(5,new PassengerDetails("Joe Dalton"));
        amd.addPassenger(5,new PassengerDetails("Jack Dalton"));
        amd.addPassenger(5,new PassengerDetails("William Dalton"));
        amd.addPassenger(5,new PassengerDetails("Averell Dalton"));

        amd.radarTestingDetect(2,flightDescriptor2);
        amd.setTestingStatus(2,ManagementRecord.WANTING_TO_LAND);
        amd.addPassenger(2,new PassengerDetails("Joe Dalton"));
        amd.addPassenger(2,new PassengerDetails("Jack Dalton"));
        amd.addPassenger(2,new PassengerDetails("William Dalton"));
        amd.addPassenger(2,new PassengerDetails("Averell Dalton"));
        System.out.println(amd.getPassengerList(5).getListLength());
        amd.radarTestingDetect(8,flightDescriptor3);
        amd.setTestingStatus(8,ManagementRecord.WANTING_TO_LAND);
        amd.addPassenger(8,new PassengerDetails("Joe Dalton"));
        amd.addPassenger(8,new PassengerDetails("Jack Dalton"));
        amd.addPassenger(8,new PassengerDetails("William Dalton"));
        amd.addPassenger(8,new PassengerDetails("Averell Dalton"));
        System.out.println(amd.getPassengerList(5).getListLength());
        amd.setTestingStatus(5,6);
        amd.setTestingStatus(2,12);
        amd.setTestingStatus(8,3);

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
        gid.docked(1);
        gid.docked(0);
        gid.docked(2);

        assertEquals(Gate.OCCUPIED, gid.getStatus(1));
        System.out.println(amd.getPassengerList(5).getListLength());
        assertEquals(4, amd.getPassengerList(5).getListLength());

        assertNotEquals(gid.getStatus(0), Gate.OCCUPIED);
        assertEquals(4, amd.getPassengerList(2).getListLength());

        assertEquals(gid.getStatus(2), Gate.OCCUPIED);
        assertEquals(4, amd.getPassengerList(8).getListLength());

        System.out.println("Passengers are disembarking...");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        amd.getPassengerList(5).setListToEmpty();
        amd.setStatus(5,ManagementRecord.UNLOADING);

        amd.getPassengerList(2).setListToEmpty();
        amd.setStatus(2,ManagementRecord.UNLOADING);

        amd.getPassengerList(8).setListToEmpty();
        amd.setStatus(8,ManagementRecord.UNLOADING);

        assertEquals(ManagementRecord.UNLOADING, amd.getStatus(5));
        assertEquals(0,amd.getPassengerList(5).getListLength());

        assertNotEquals(ManagementRecord.UNLOADING, amd.getStatus(2));
        assertEquals(0,amd.getPassengerList(2).getListLength());

        assertNotEquals(ManagementRecord.UNLOADING, amd.getStatus(8));
        assertEquals(0,amd.getPassengerList(8).getListLength());
    }
}
