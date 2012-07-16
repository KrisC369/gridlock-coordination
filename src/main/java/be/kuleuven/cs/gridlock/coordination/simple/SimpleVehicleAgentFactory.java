package be.kuleuven.cs.gridlock.coordination.simple;

import be.kuleuven.cs.gridlock.configuration.services.ServiceFactory;
import be.kuleuven.cs.gridlock.coordination.VehicleAgent;
import be.kuleuven.cs.gridlock.coordination.VehicleAgentFactory;
import be.kuleuven.cs.gridlock.routing.RoutingService;
import be.kuleuven.cs.gridlock.routing.RoutingServiceLoader;
import be.kuleuven.cs.gridlock.simulation.SimulationContext;
import be.kuleuven.cs.gridlock.simulation.api.VehicleReference;
import java.util.ServiceConfigurationError;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public class SimpleVehicleAgentFactory implements VehicleAgentFactory {

    private RoutingService routingService;
    private SimulationContext context;

    @Override
    public void initialize( SimulationContext simulationContext ) {
        this.context = simulationContext;
        this.routingService = ServiceFactory.Helper.load( RoutingServiceLoader.class, simulationContext.getConfiguration() );

        if( this.routingService == null ) {
            Logger.getLogger( SimpleVehicleAgentFactory.class.getCanonicalName() ).log( Level.SEVERE, "Could not instantiate a routing service" );
            throw new ServiceConfigurationError( "Could not instantiate a routing service" );
        }
    }

    @Override
    public VehicleAgent createAgent( VehicleReference vehicle ) {
        return new SimpleVehicleAgent( vehicle, context, routingService );
    }
}