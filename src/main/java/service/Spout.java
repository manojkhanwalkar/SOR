package service;

public interface Spout {

    public void process();
    public void setEventService(Topology topology);
}
