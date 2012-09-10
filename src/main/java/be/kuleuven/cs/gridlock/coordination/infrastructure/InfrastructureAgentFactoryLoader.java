package be.kuleuven.cs.gridlock.coordination.infrastructure;

import be.kuleuven.cs.gridlock.configuration.services.ServiceFactory;

/**
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public interface InfrastructureAgentFactoryLoader extends ServiceFactory<InfrastructureAgentFactory> {

    public static final String AGENT_TYPE_CONFIGURATION_KEY = "coordination.infrastructure.agent.type";
    
}