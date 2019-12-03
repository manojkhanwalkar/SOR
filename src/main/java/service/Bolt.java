package service;

import event.Event;

public interface Bolt {

    public void process(Event event);
    public void setEventService(Topology topology);

}
