package service;

import event.Event;

public abstract class AbstractSpout implements Spout  {

   Topology topology;

    @Override
    public void setEventService(Topology topology) {
        this.topology = topology;
    }



    protected void emit(String name, Event event)
    {
            topology.forward(name,event);
    }
}
