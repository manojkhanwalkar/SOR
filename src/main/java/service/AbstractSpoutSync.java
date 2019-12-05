package service;

import event.Event;

public abstract class AbstractSpoutSync implements Spout  {

   Topology topology;

    @Override
    public void setEventService(Topology topology) {
        this.topology = topology;
    }



    protected void emit(String name, Event event)
    {
            topology.forwardSync(name,event);
    }
}
