package be.kuleuven.cs.gridlock.coordination;

import be.kuleuven.cs.gridlock.configuration.services.ServiceFactory;
import be.kuleuven.cs.gridlock.simulation.SimulationComponent;
import be.kuleuven.cs.gridlock.simulation.SimulationContext;
import be.kuleuven.cs.gridlock.simulation.api.InfrastructureReference;
import be.kuleuven.cs.gridlock.simulation.events.Event;
import be.kuleuven.cs.gridlock.simulation.events.EventFilter;
import be.kuleuven.cs.gridlock.simulation.events.EventListener;
import be.kuleuven.cs.gridlock.simulation.api.VirtualTime;
import be.kuleuven.cs.gridlock.simulation.timeframe.TimeFrameConsumer;
import com.sun.beans.TypeResolver;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Infrastructure coordination component for creating and dispatching events to infrastructure 
 * agents.
 * This component is strongly based on the vehicle coordination component in this package.
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public class InfrastructureCoordination implements SimulationComponent, EventListener, EventFilter, TimeFrameConsumer {

    private InfrastructureAgentFactory agentFactory;

    private final Map<InfrastructureReference, InfrastructureAgent> agents;

    public InfrastructureCoordination() {
        this.agents = new HashMap<InfrastructureReference, InfrastructureAgent>();
    }

    @Override
    public void initialize( SimulationContext simulationContext ) {
        this.loadAgentFactory( simulationContext );
        simulationContext.getEventController().registerListener( this, this );
    }

    @Override
    public boolean pass( Event event ) {
        assert event != null;
        return event.getType().startsWith( "Infrastructure:creation" ) || event.getType().startsWith( "Infrastructure:removal" );
    }

    private void loadAgentFactory( SimulationContext context ) {
        this.agentFactory = ServiceFactory.Helper.load( InfrastructureAgentFactoryLoader.class, context.getConfiguration() );

        if( this.agentFactory != null ) {
            this.agentFactory.initialize( context );
        }
        else {
            Logger.getLogger( InfrastructureCoordination.class.getCanonicalName() ).log( Level.WARNING, "Could not load Infrastructure agent factory" );
        }
    }

    @Override
    public void notifyOf( Event event ) {
        //TODO review based on event driver that doesn't exist yet.
        if( event.getType().startsWith( "Infrastructure:creation" ) ) {
            InfrastructureReference Infrastructure = event.getAttribute( "Infrastructure", InfrastructureReference.class );
            if( Infrastructure != null ) {
                this.startAgent( Infrastructure );
            } else {
                Logger.getLogger( InfrastructureCoordination.class.getCanonicalName() ).log( Level.SEVERE, "Faulty event got through: {0}", event.type );
            }
        } else if( event.getType().startsWith( "Infrastructure:removal" ) ) {
            InfrastructureReference Infrastructure = event.getAttribute( "Infrastructure", InfrastructureReference.class );
            InfrastructureAgent agent = this.agents.get( Infrastructure );
            if( agent != null ) {
                agent.destroy();
                this.agents.remove( Infrastructure );
            }
        }
    }

    private void startAgent( InfrastructureReference component ) {
        synchronized( this.agents ) {
            if( this.agents.containsKey( component ) ) {
                throw new IllegalStateException( "Infrastructure already has an agent" );
            }
            if( this.agentFactory == null ) {
                throw new IllegalStateException( "Agent Factory is null" );
            }
            this.agents.put( component, this.agentFactory.InfrastructureAgentFactory( component ) );
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
            for( InfrastructureReference Infrastructure : this.agents.keySet() ) {
                this.agents.get( Infrastructure ).consume( currentTime, timeFrameDuration );
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
