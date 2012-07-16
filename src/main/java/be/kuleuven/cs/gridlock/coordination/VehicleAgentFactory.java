package be.kuleuven.cs.gridlock.coordination;

import be.kuleuven.cs.gridlock.simulation.SimulationContext;
import be.kuleuven.cs.gridlock.simulation.api.VehicleReference;

/**
 *
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public interface VehicleAgentFactory {

    public void initialize( SimulationContext simulationContext );

    public VehicleAgent createAgent( VehicleReference vehicle );

}
