package be.kuleuven.cs.gridlock.coordination.vehicle;

import be.kuleuven.cs.gridlock.configuration.services.ServiceFactory;

/**
 *
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public interface VehicleAgentFactoryLoader extends ServiceFactory<VehicleAgentFactory> {

    public static final String AGENT_TYPE_CONFIGURATION_KEY = "coordination.vehicle.agent.type";
    
}