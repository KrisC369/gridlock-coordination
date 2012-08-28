package be.kuleuven.cs.gridlock.coordination;

import be.kuleuven.cs.gridlock.simulation.SimulationContext;

/**
 *
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public interface GenericAgentFactory<A,R> {

    public void initialize( SimulationContext simulationContext );

    public A createAgent( R vehicle );

}
