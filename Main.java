import java.util.Arrays;

/**
 * The Main class.
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

//  FlightDescriptor fl = new FlightDescriptor("FK4",new Itinerary("A","Stirling","A"),new PassengerList());

  GateInfoDatabase gateInfoDatabase = new GateInfoDatabase();
  AircraftManagementDatabase aircraftManagementDatabase = new AircraftManagementDatabase();
  RadarTransceiver radarTransceiver = new RadarTransceiver(aircraftManagementDatabase);
  LATC latc = new LATC(aircraftManagementDatabase);
  GOC goc = new GOC(aircraftManagementDatabase,gateInfoDatabase);
  GateConsole gateConsole= new GateConsole(aircraftManagementDatabase,gateInfoDatabase,1);
  GateConsole gateConsole1= new GateConsole(aircraftManagementDatabase,gateInfoDatabase,2);
  CleaningSupervisor cleaningSupervisor = new CleaningSupervisor(aircraftManagementDatabase);
  MaintenanceInspector maintenanceInspector = new MaintenanceInspector(aircraftManagementDatabase);


//  aircraftManagementDatabase.radarDetect(fl);
  System.out.println(Arrays.toString(gateInfoDatabase.getStatuses()));
  System.out.println(aircraftManagementDatabase.getWithStatus(2));
  }

}
