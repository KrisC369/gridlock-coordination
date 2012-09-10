package be.kuleuven.cs.gridlock.coordination.vehicle;

import be.kuleuven.cs.gridlock.simulation.timeframe.TimeFrameConsumer;

/**
 *
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public interface VehicleAgent extends TimeFrameConsumer {

    public void destroy();

}