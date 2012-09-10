package be.kuleuven.cs.gridlock.coordination.infrastructure;

import be.kuleuven.cs.gridlock.simulation.timeframe.TimeFrameConsumer;

/**
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public interface InfrastructureAgent extends TimeFrameConsumer {

    public void destroy();

}