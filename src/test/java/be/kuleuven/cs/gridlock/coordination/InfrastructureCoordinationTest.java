package be.kuleuven.cs.gridlock.coordination;

import be.kuleuven.cs.gridlock.simulation.api.VirtualTime;
import be.kuleuven.cs.gridlock.simulation.events.Event;
import be.kuleuven.cs.gridlock.simulation.events.EventFactoryImplementation;
import be.kuleuven.cs.gridlock.simulation.events.EventManager;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public class InfrastructureCoordinationTest {

    public InfrastructureCoordinationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void testInitialize() {
//       fail("Not yet implemented.");
//    }
    @Test
    public void testPass() {
        {
            Event event = new EventFactoryImplementation().build("infrastructure:creation", new EventManager());
            InfrastructureCoordination instance = new InfrastructureCoordination();
            boolean expResult = true;
            boolean result = instance.pass(event);
            assertEquals(expResult, result);
        }
        {
            Event event = new EventFactoryImplementation().build("infrastructure:removal", new EventManager());
            InfrastructureCoordination instance = new InfrastructureCoordination();
            boolean expResult = true;
            boolean result = instance.pass(event);
            assertEquals(expResult, result);
        }
        {
            Event event = new EventFactoryImplementation().build("randomOther", new EventManager());
            InfrastructureCoordination instance = new InfrastructureCoordination();
            boolean expResult = false;
            boolean result = instance.pass(event);
            assertEquals(expResult, result);
        }
        {
            try {
                Event event = null;
                InfrastructureCoordination instance = new InfrastructureCoordination();
                boolean expResult = false;
                boolean result = instance.pass(event);
                assertEquals(expResult, result);
                fail("Assertion error should have occured on null event.");
            } catch (AssertionError ae) {
                //Succeed
            }
        }
    }

//    @Test
//    public void testNotifyOf() {
//        fail("Not yet implemented.");
//    }

    @Test
    public void testGetConsumers() {
        InfrastructureCoordination instance = new InfrastructureCoordination();
        Collection result = instance.getConsumers();
        assertTrue(result != null);
        assertTrue(result.contains(instance));
    }

    @Test
    public void testGetSubComponents() {
        InfrastructureCoordination instance = new InfrastructureCoordination();
        Collection result = instance.getSubComponents();
        assertTrue(result != null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testConsume() {
        VirtualTime currentTime = null;
        double timeFrameDuration = 0.0;
        InfrastructureCoordination instance = new InfrastructureCoordination();
        instance.consume(currentTime, timeFrameDuration);
    }

    @Test
    public void testContinueSimulation() {
        InfrastructureCoordination instance = new InfrastructureCoordination();
        boolean expResult = false;
        boolean result = instance.continueSimulation();
        assertEquals(expResult, result);
    }
}
