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
        int actualFree[] = gid.getStatuses();// extracting all the statuses that are free
        assertEquals(3, actualFree.length);
        assertEquals(actualFree[0],0);
        assertEquals(actualFree[1],1);
        assertEquals(actualFree[2],3);
    }

    @Test
    void allocate() {
        gid.allocate(0, 0);
        assertEquals(gid.getStatus(0), Gate.RESERVED);
        gid.allocate(1, 0);
        assertEquals(gid.getStatus(1), Gate.RESERVED);
        gid.allocate(2, 0);
        assertNotEquals(gid.getStatus(2), Gate.RESERVED);
    }


    @Test
    void docked() {
        gid.docked(0);
        assertNotEquals(gid.getStatus(0), Gate.OCCUPIED);
        gid.docked(1);
        assertEquals(gid.getStatus(1), Gate.OCCUPIED);
        gid.docked(2);
        assertNotEquals(gid.getStatus(2), Gate.OCCUPIED);
    }

    @Test
    void departed() {
        gid.departed(0);
        assertEquals(Gate.FREE,gid.getStatus(0));
        gid.departed(1);
        assertNotEquals(Gate.FREE,gid.getStatus(1));
        gid.departed(2);
        assertNotEquals(Gate.FREE,gid.getStatus(2));
    }
}
