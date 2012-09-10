package be.kuleuven.cs.gridlock.coordination;

import be.kuleuven.cs.gridlock.coordination.vehicle.VehicleAgentFactory;
import be.kuleuven.cs.gridlock.coordination.vehicle.VehicleAgent;
import be.kuleuven.cs.gridlock.coordination.vehicle.VehicleAgentFactoryLoader;
import be.kuleuven.cs.gridlock.configuration.services.ServiceFactory;
import be.kuleuven.cs.gridlock.simulation.SimulationComponent;
import be.kuleuven.cs.gridlock.simulation.SimulationContext;
import be.kuleuven.cs.gridlock.simulation.api.VehicleReference;
import be.kuleuven.cs.gridlock.simulation.events.Event;
import be.kuleuven.cs.gridlock.simulation.events.EventFilter;
import be.kuleuven.cs.gridlock.simulation.events.EventListener;
import be.kuleuven.cs.gridlock.simulation.api.VirtualTime;
import be.kuleuven.cs.gridlock.simulation.timeframe.TimeFrameConsumer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public class VehicleCoordination implements SimulationComponent, EventListener, EventFilter, TimeFrameConsumer {

    private VehicleAgentFactory agentFactory;

    private final Map<VehicleReference, VehicleAgent> agents;

    public VehicleCoordination() {
        this.agents = new HashMap<VehicleReference, VehicleAgent>();
    }

    @Override
    public void initialize( SimulationContext simulationContext ) {
        this.loadAgentFactory( simulationContext );
        simulationContext.getEventController().registerListener( this, this );
    }

    @Override
    public boolean pass( Event event ) {
        assert event != null;
        return event.getType().startsWith( "vehicle:creation" ) || event.getType().startsWith( "vehicle:removal" );
    }

    private void loadAgentFactory( SimulationContext context ) {
        this.agentFactory = ServiceFactory.Helper.load( VehicleAgentFactoryLoader.class, context.getConfiguration() );

        if( this.agentFactory != null ) {
            this.agentFactory.initialize( context );
        }
        else {
            Logger.getLogger( VehicleCoordination.class.getCanonicalName() ).log( Level.WARNING, "Could not load vehicle agent factory" );
        }
    }

    @Override
    public void notifyOf( Event event ) {
        if( event.getType().startsWith( "vehicle:creation" ) ) {
            VehicleReference vehicle = event.getAttribute( "vehicle", VehicleReference.class );
            if( vehicle != null ) {
                this.startAgent( vehicle );
            } else {
                Logger.getLogger( VehicleCoordination.class.getCanonicalName() ).log( Level.SEVERE, "Faulty event got through: {0}", event.type );
            }
        } else if( event.getType().startsWith( "vehicle:removal" ) ) {
            VehicleReference vehicle = event.getAttribute( "vehicle", VehicleReference.class );
            VehicleAgent agent = this.agents.get( vehicle );
            if( agent != null ) {
                agent.destroy();
                this.agents.remove( vehicle );
            }
        }
    }

    private void startAgent( VehicleReference vehicle ) {
        synchronized( this.agents ) {
            if( this.agents.containsKey( vehicle ) ) {
                throw new IllegalStateException( "Vehicle already has an agent" );
            }
            if( this.agentFactory == null ) {
                throw new IllegalStateException( "Agent Factory is null" );
            }
            this.agents.put( vehicle, this.agentFactory.createAgent( vehicle ) );
        }
    }

    @Override
    public Collection<? extends TimeFrameConsumer> getConsumers() {
        return Collections.singleton( this );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<? extends SimulationComponent> getSubComponents() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void consume( VirtualTime currentTime, double timeFrameDuration ) {
        synchronized( this.agents ) {
            for( VehicleReference vehicle : this.agents.keySet() ) {
                this.agents.get( vehicle ).consume( currentTime, timeFrameDuration );
            }
        }
    }

    @Override
    public boolean continueSimulation() {
        synchronized( this.agents ) {
            return !this.agents.isEmpty();
        }
    }
}
