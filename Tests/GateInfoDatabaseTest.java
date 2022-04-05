package Tests;

import dev.Gate;
import dev.GateInfoDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GateInfoDatabaseTest {

    GateInfoDatabase gid;
    Gate[] gates = new Gate[3];

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 3; i++) {
            gates[i] = new Gate();
        }
        gid = new GateInfoDatabase(gates);
        gid.setStatusDebug(0, 0);
        gid.setStatusDebug(1, 1);
        gid.setStatusDebug(2, 3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getStatus() {
        assertTrue(gid.getStatus(0) >= 0 && gid.getStatus(0) < 3);
        assertTrue(gid.getStatus(1) >= 0 && gid.getStatus(1) < 3);
        assertFalse(gid.getStatus(2) >= 0 && gid.getStatus(2) < 3);
    }


    @Test
    void getStatuses() {
        int expected[] = new int[2];//an empty array with statuses set to free
        int actual[] = gid.getStatuses();// extracting all the statuses that are free
        assertArrayEquals(expected, actual);
    }

    @Test
    void getMcode() {
        gid.allocate(1, 0);//allocates management record 0 to gate 1
        assertEquals(0, gid.getManagementRecordFromGate(1)); // checking if the gate associated with the MR is correct
    }

    @Test
    void allocate() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> gid.allocate(3, 2));//trying to allocate a gate that is out of bounds throws an exception
        gid.allocate(0, 0);//allocating MR[0] to gate 0
        assertEquals(gid.getStatus(0), Gate.RESERVED);// checking if the status has changed to reserved
    }

    /**
     * Use cases for ReceivesAircraft
     *
     */


    @Test
    void docked() {
        gid.docked(0);//triggering docking from gate 0
        assertEquals(gid.getStatus(0), Gate.OCCUPIED); // checking if the gate has changed to occupied
    }

//    @Test
//    void departed() {
//        //
//        gateInfoDatabase.setStatus(0,Gate.OCCUPIED);//setting gate 0 to occupied
//        gateInfoDatabase.departed(0);// the plane has departed so the gate should be free
//        assertEquals(Gate.FREE,gateInfoDatabase.getStatus(0));//checking if the status has changed to free after the call
//    }
//
//    @Test
//    void getStatusToString() {
//        assertEquals("Free",gateInfoDatabase.getStatusToString(0));
//    }//checking if the statuses are converted correctly to strings
}
