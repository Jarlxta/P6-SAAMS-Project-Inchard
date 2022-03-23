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

  FlightDescriptor fl = new FlightDescriptor("FK4",new Itinerary("A","A","A"),new PassengerList());

  AircraftManagementDatabase aircraftManagementDatabase = new AircraftManagementDatabase();
  LATC latc = new LATC(aircraftManagementDatabase);
  GOC goc = new GOC(aircraftManagementDatabase);



  System.out.println(Arrays.toString(aircraftManagementDatabase.getWithStatus(0)));
  }

}