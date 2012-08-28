package be.kuleuven.cs.gridlock.coordination;

import be.kuleuven.cs.gridlock.configuration.services.ServiceFactory;
import be.kuleuven.cs.gridlock.simulation.SimulationComponent;
import be.kuleuven.cs.gridlock.simulation.SimulationContext;
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
 * Infrastructure coordination component for creating and dispatching events to infrastructure 
 * agents.
 * This component is strongly based on the vehicle coordination component in this package.
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public abstract class GenericComponentCoordination<R, A extends GenericAgent, F extends GenericAgentFactory<A, R>, L extends ServiceFactory<F>> 
    implements SimulationComponent, EventListener, EventFilter, TimeFrameConsumer {

    protected F agentFactory;
    protected final Map<R, A> agents;

   
protected final String CREATION_EVENT_KEY;
    
    public GenericComponentCoordination() {
        this.agents = new HashMap<R, A>();
        CREATION_EVENT_KEY = "";
    }

    protected GenericComponentCoordination(String key){
        this.agents = new HashMap<R, A>();
        CREATION_EVENT_KEY = key;
    }
    @Override
    public void initialize(SimulationContext simulationContext) {
        this.loadAgentFactory(simulationContext);
        simulationContext.getEventController().registerListener(this, this);
    }

    @Override
    public boolean pass(Event event) {
        assert event != null;
        return event.getType().startsWith(CREATION_EVENT_KEY+":creation") || event.getType().startsWith(CREATION_EVENT_KEY+":removal");
    }

    protected void startAgent(R component) {
        synchronized (this.agents) {
            if (this.agents.containsKey(component)) {
                throw new IllegalStateException(CREATION_EVENT_KEY +" already has an agent");
            }
            if (this.agentFactory == null) {
                throw new IllegalStateException("Agent Factory is null");
            }
            this.agents.put(component, this.agentFactory.createAgent(component));
        }
    } 
    
     private void loadAgentFactory(SimulationContext context) {
        this.agentFactory = retrieveAgentFactory(context);
        if (this.agentFactory != null) {
            this.agentFactory.initialize(context);
        } else {
            Logger.getLogger(GenericComponentCoordination.class.getCanonicalName()).log(Level.WARNING, "Could not load{0} agent factory", CREATION_EVENT_KEY);
        }
    }

     protected abstract F retrieveAgentFactory(SimulationContext context);
    
    @Override
    public void notifyOf(Event event) {
        //TODO review based on event driver that doesn't exist yet.
        if (event.getType().startsWith(CREATION_EVENT_KEY + ":creation")) {
            Class<R> r = null;
            R infrastructure = retrieveReferenceFromEvent(event);
            if (infrastructure != null) {
                this.startAgent(infrastructure);
            } else {
                Logger.getLogger(GenericComponentCoordination.class.getCanonicalName()).log(Level.SEVERE, "Faulty event got through: {0}", event.type);
            }
        } else if (event.getType().startsWith(CREATION_EVENT_KEY + ":removal")) {
            Class<R> r = null;
            R infrastructure = retrieveReferenceFromEvent(event);
            A agent = this.agents.get(infrastructure);
            if (agent != null) {
                agent.destroy();
                this.agents.remove(infrastructure);
            }
        }
    }
     
     protected abstract R retrieveReferenceFromEvent(Event e);

    @Override
    public Collection<? extends TimeFrameConsumer> getConsumers() {
        return Collections.singleton(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends SimulationComponent> getSubComponents() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void consume(VirtualTime currentTime, double timeFrameDuration) {
        synchronized (this.agents) {
            for (R component : this.agents.keySet()) {
                this.agents.get(component).consume(currentTime, timeFrameDuration);
            }
        }
    }

    @Override
    public boolean continueSimulation() {
        synchronized (this.agents) {
            return !this.agents.isEmpty();
        }
    }

}