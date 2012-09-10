package be.kuleuven.cs.gridlock.coordination.infrastructure;

import be.kuleuven.cs.gridlock.simulation.SimulationContext;
import be.kuleuven.cs.gridlock.simulation.api.InfrastructureReference;

/**
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public interface InfrastructureAgentFactory {

    public void initialize( SimulationContext simulationContext );

    public InfrastructureAgent createAgent( InfrastructureReference component );

}
