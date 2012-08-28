package be.kuleuven.cs.gridlock.coordination;

import be.kuleuven.cs.gridlock.configuration.services.ServiceFactory;
import be.kuleuven.cs.gridlock.simulation.SimulationComponent;
import be.kuleuven.cs.gridlock.simulation.SimulationContext;
import be.kuleuven.cs.gridlock.simulation.api.InfrastructureReference;
import be.kuleuven.cs.gridlock.simulation.events.Event;
import be.kuleuven.cs.gridlock.simulation.events.EventFilter;
import be.kuleuven.cs.gridlock.simulation.events.EventListener;
import be.kuleuven.cs.gridlock.simulation.timeframe.TimeFrameConsumer;

/**
 * Infrastructure coordination component for creating and dispatching events to infrastructure 
 * agents.
 * This component is strongly based on the vehicle coordination component in this package.
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public class InfrastructureCoordination extends GenericComponentCoordination<InfrastructureReference, InfrastructureAgent, InfrastructureAgentFactory, InfrastructureAgentFactoryLoader>
        implements SimulationComponent, EventListener, EventFilter, TimeFrameConsumer {

    public InfrastructureCoordination() {
        super("infrastructure");
    }

    @Override
    protected InfrastructureAgentFactory retrieveAgentFactory(SimulationContext context) {
        return ServiceFactory.Helper.load(InfrastructureAgentFactoryLoader.class, context.getConfiguration());
    }

    @Override
    protected InfrastructureReference retrieveReferenceFromEvent(Event e) {
        return e.getAttribute(CREATION_EVENT_KEY, InfrastructureReference.class);
    }
}