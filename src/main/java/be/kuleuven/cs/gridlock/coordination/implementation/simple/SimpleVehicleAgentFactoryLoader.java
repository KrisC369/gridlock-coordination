package be.kuleuven.cs.gridlock.coordination.implementation.simple;

import be.kuleuven.cs.gridlock.configuration.Configuration;
import be.kuleuven.cs.gridlock.configuration.services.TypedBasedServiceFactory;
import be.kuleuven.cs.gridlock.coordination.vehicle.VehicleAgentFactory;
import be.kuleuven.cs.gridlock.coordination.vehicle.VehicleAgentFactoryLoader;

/**
 *
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public class SimpleVehicleAgentFactoryLoader extends TypedBasedServiceFactory<VehicleAgentFactory> implements VehicleAgentFactoryLoader {

    public SimpleVehicleAgentFactoryLoader() {
        super( VehicleAgentFactoryLoader.AGENT_TYPE_CONFIGURATION_KEY, "simple" );
    }

    @Override
    public SimpleVehicleAgentFactory buildService( Configuration configuration ) {
        return new SimpleVehicleAgentFactory();
    }


}
