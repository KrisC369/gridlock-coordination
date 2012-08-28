package be.kuleuven.cs.gridlock.coordination;

import be.kuleuven.cs.gridlock.configuration.services.ServiceFactory;
import be.kuleuven.cs.gridlock.simulation.SimulationComponent;
import be.kuleuven.cs.gridlock.simulation.SimulationContext;
import be.kuleuven.cs.gridlock.simulation.api.VehicleReference;
import be.kuleuven.cs.gridlock.simulation.events.Event;
import be.kuleuven.cs.gridlock.simulation.events.EventFilter;
import be.kuleuven.cs.gridlock.simulation.events.EventListener;
import be.kuleuven.cs.gridlock.simulation.timeframe.TimeFrameConsumer;

/**
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public class VehicleCoordination extends GenericComponentCoordination<VehicleReference, VehicleAgent, VehicleAgentFactory, VehicleAgentFactoryLoader>
    implements SimulationComponent, EventListener, EventFilter, TimeFrameConsumer {

    public VehicleCoordination() {
        super("vehicle");
    }

    @Override
    protected VehicleAgentFactory retrieveAgentFactory(SimulationContext context) {
        return ServiceFactory.Helper.load(VehicleAgentFactoryLoader.class, context.getConfiguration());
    }

    @Override
    protected VehicleReference retrieveReferenceFromEvent(Event e) {
        return e.getAttribute(CREATION_EVENT_KEY, VehicleReference.class);
    }
    
    

}