package be.kuleuven.cs.gridlock.coordination.implementation.simple;

import be.kuleuven.cs.gridlock.coordination.vehicle.VehicleAgent;
import be.kuleuven.cs.gridlock.routing.Path;
import be.kuleuven.cs.gridlock.routing.RoutingService;
import be.kuleuven.cs.gridlock.simulation.SimulationContext;
import be.kuleuven.cs.gridlock.simulation.api.LinkReference;
import be.kuleuven.cs.gridlock.simulation.api.NodeReference;
import be.kuleuven.cs.gridlock.simulation.api.VehicleReference;
import be.kuleuven.cs.gridlock.simulation.api.VirtualTime;
import be.kuleuven.cs.gridlock.simulation.model.SimulationModel;
import be.kuleuven.cs.gridlock.simulation.model.vehicle.PathRoutingDelegate;
import be.kuleuven.cs.gridlock.simulation.model.vehicle.RoutingDelegate;
import be.kuleuven.cs.gridlock.simulation.model.vehicle.Vehicle;
import be.kuleuven.cs.gridlock.simulation.model.vehicle.VehicleManager;
import be.kuleuven.cs.gridlock.utilities.graph.Graph;
import java.util.Collection;

/**
 *
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public class SimpleVehicleAgent implements VehicleAgent, RoutingDelegate {

    private final RoutingService routing;
    private RoutingDelegate delegate;
    private final SimulationContext context;
    private final VehicleReference reference;

    public SimpleVehicleAgent(VehicleReference reference, SimulationContext context, RoutingService routing) {
        this.context = context;
        this.reference = reference;
        this.routing = routing;

        this.getVehicle().setRoutingDelegate(this);
    }

    protected Vehicle getVehicle() {
        return this.context.getSimulationComponent(VehicleManager.class).getVehicle(reference);
    }

    protected Graph<NodeReference, LinkReference> getGraph() {
        return this.context.getSimulationComponent(SimulationModel.class).getGraph();
    }

    @Override
    public void consume(VirtualTime currentTime, double timeFrameDuration) {
        // NO-OP
    }

    @Override
    public boolean continueSimulation() {
        return false;
    }

    @Override
    public LinkReference choose(NodeReference position, Collection<LinkReference> possibleLinks) {
        if (this.delegate == null) {
            this.delegate = new PathRoutingDelegate(this.calculateRoute(position));
        }

        return this.delegate.choose(position, possibleLinks);
    }

    protected Path calculateRoute(NodeReference position) {
        return this.routing.route(position, this.getVehicle().getDestination(), this.getGraph());
    }

    @Override
    public void updatePosition(NodeReference nodeReference) {
        //this.updatePosition(nodeReference);
    }

    @Override
    // TODO it should not be the vehicle agents responsibility to purge the vehicle information
    public void destroy() {
        this.delegate = null;
        this.context.getSimulationComponent(VehicleManager.class).purge(this.reference);
    }

    protected SimulationContext getContext() {
        return context;
    }

    protected RoutingDelegate getDelegate() {
        return delegate;
    }
    
    protected void setDelegate(RoutingDelegate delegate) {
        this.delegate = delegate;
    }

    protected VehicleReference getReference() {
        return reference;
    }

    protected RoutingService getRouting() {
        return routing;
    }
}
