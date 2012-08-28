package be.kuleuven.cs.gridlock.coordination;

import be.kuleuven.cs.gridlock.simulation.timeframe.TimeFrameConsumer;

/**
 *
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public interface GenericAgent extends TimeFrameConsumer {

    public void destroy();

}