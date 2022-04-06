package dev;

import dev.*;

/**
 * The dev.Main class.
 *
 * The principal component is the usual main method required by Java application to launch the application.
 *
 * Instantiates the databases.
 * Instantiates and shows all the system interfaces as Frames.
 * @stereotype control
 */
public class Main {

/**
 * Launch SAAMS.
 */

public static void main(String[] args) {
    // Instantiate databases
    // Instantiate and show all interfaces as Frames

//  dev.FlightDescriptor fl = new dev.FlightDescriptor("FK4",new dev.Itinerary("A","Stirling","A"),new dev.PassengerList());

  GateInfoDatabase gateInfoDatabase = new GateInfoDatabase();
  AircraftManagementDatabase aircraftManagementDatabase = new AircraftManagementDatabase();
  RadarTransceiver radarTransceiver = new RadarTransceiver(aircraftManagementDatabase);
  LATC latc = new LATC(aircraftManagementDatabase);
  GOC goc = new GOC(aircraftManagementDatabase,gateInfoDatabase);
  GateConsole gateConsole= new GateConsole(aircraftManagementDatabase,gateInfoDatabase,1);
  GateConsole gateConsole1= new GateConsole(aircraftManagementDatabase,gateInfoDatabase,2);
  GateConsole gateConsole2= new GateConsole(aircraftManagementDatabase,gateInfoDatabase,3);

  MaintenanceInspector maintenanceInspector = new MaintenanceInspector(aircraftManagementDatabase);
  CleaningSupervisor cleaningSupervisor = new CleaningSupervisor(aircraftManagementDatabase);
  RefuellingSupervisor refuellingSupervisor = new RefuellingSupervisor(aircraftManagementDatabase);
  PublicInfo publicInfo = new PublicInfo(aircraftManagementDatabase);
  PublicInfo publicInfo1 = new PublicInfo(aircraftManagementDatabase);
//  aircraftManagementDatabase.radarDetect(fl);
  }

}