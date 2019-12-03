package service;

import event.Event;
import service.Service;
import service.Services;

import java.util.ArrayList;
import java.util.List;

public class EventProcessingService implements Service {


    public enum Events { List , Order , Execution , MarketData}

    public EventProcessingService()
    {
        Services.getInstance().register(this);
    }

    List<Topology> topologies = new ArrayList<>();

    public void register(Topology topology)
    {
        topology.setEventService();
        topologies.add(topology);
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

        topologies.parallelStream().forEach(t->t.execute());

    }

    @Override
    public void destroy() {

    }

    @Override
    public void stop() {

    }


}
